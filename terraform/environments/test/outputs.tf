output "alb_dns_name"       { value = module.alb.dns_name }
output "k6_public_ip"       { value = module.k6.public_ip }
output "rds_endpoint"       { value = module.rds.endpoint }
output "redis_endpoint"     { value = module.elasticache.primary_endpoint }
