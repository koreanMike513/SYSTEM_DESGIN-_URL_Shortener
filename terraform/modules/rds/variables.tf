variable "name"                  {}
variable "vpc_id"                {}
variable "private_subnet_ids"    { type = list(string) }
variable "app_security_group_id" {}
variable "db_instance_class"     {}
variable "db_password"           { sensitive = true }
