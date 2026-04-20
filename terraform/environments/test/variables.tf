variable "aws_region"         { default = "ap-northeast-2" }
variable "name"               { default = "url-shortener-test" }
variable "vpc_cidr"           { default = "10.0.0.0/16" }
variable "availability_zones" { default = ["ap-northeast-2a", "ap-northeast-2c"] }

# RDS
variable "db_instance_class"  { default = "db.t3.medium" }
variable "db_password"        { sensitive = true }

# ElastiCache
variable "redis_node_type"    { default = "cache.t3.medium" }

# ECS
variable "ecr_image_uri"      {}
variable "task_cpu"           { default = 2048 }  # 2 vCPU
variable "task_memory"        { default = 4096 }  # 4 GB
variable "ecs_min_capacity"   { default = 2 }
variable "ecs_max_capacity"   { default = 20 }

# k6
variable "k6_instance_type"   { default = "c5.xlarge" }
