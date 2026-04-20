variable "name"                  {}
variable "vpc_id"                {}
variable "private_subnet_ids"    { type = list(string) }
variable "alb_security_group_id" {}
variable "target_group_arn"      {}
variable "ecr_image_uri"         {}
variable "db_url"                {}
variable "db_password"           { sensitive = true }
variable "redis_host"            {}
variable "app_host"              {}
variable "task_cpu"              { type = number }
variable "task_memory"           { type = number }
variable "min_capacity"          { type = number }
variable "max_capacity"          { type = number }
