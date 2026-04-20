terraform {
  required_version = ">= 1.6"
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region = var.aws_region
}

module "vpc" {
  source             = "../../modules/vpc"
  name               = var.name
  vpc_cidr           = var.vpc_cidr
  availability_zones = var.availability_zones
}

module "alb" {
  source            = "../../modules/alb"
  name              = var.name
  vpc_id            = module.vpc.vpc_id
  public_subnet_ids = module.vpc.public_subnet_ids
}

module "rds" {
  source              = "../../modules/rds"
  name                = var.name
  vpc_id              = module.vpc.vpc_id
  private_subnet_ids  = module.vpc.private_subnet_ids
  app_security_group_id = module.ecs.app_security_group_id
  db_instance_class   = var.db_instance_class
  db_password         = var.db_password
}

module "elasticache" {
  source                = "../../modules/elasticache"
  name                  = var.name
  vpc_id                = module.vpc.vpc_id
  private_subnet_ids    = module.vpc.private_subnet_ids
  app_security_group_id = module.ecs.app_security_group_id
  node_type             = var.redis_node_type
}

module "ecs" {
  source              = "../../modules/ecs"
  name                = var.name
  vpc_id              = module.vpc.vpc_id
  private_subnet_ids  = module.vpc.private_subnet_ids
  alb_security_group_id = module.alb.security_group_id
  target_group_arn    = module.alb.target_group_arn
  ecr_image_uri       = var.ecr_image_uri
  db_url              = "jdbc:mysql://${module.rds.endpoint}/url_shortener?useSSL=true"
  db_password         = var.db_password
  redis_host          = module.elasticache.primary_endpoint
  app_host            = "http://${module.alb.dns_name}"
  task_cpu            = var.task_cpu
  task_memory         = var.task_memory
  min_capacity        = var.ecs_min_capacity
  max_capacity        = var.ecs_max_capacity
}

module "k6" {
  source            = "../../modules/k6"
  name              = var.name
  vpc_id            = module.vpc.vpc_id
  public_subnet_id  = module.vpc.public_subnet_ids[0]
  alb_dns_name      = module.alb.dns_name
  instance_type     = var.k6_instance_type
}
