resource "aws_security_group" "redis" {
  name   = "${var.name}-redis-sg"
  vpc_id = var.vpc_id

  ingress {
    from_port       = 6379
    to_port         = 6379
    protocol        = "tcp"
    security_groups = [var.app_security_group_id]
  }
}

resource "aws_elasticache_subnet_group" "this" {
  name       = "${var.name}-redis-subnet"
  subnet_ids = var.private_subnet_ids
}

resource "aws_elasticache_replication_group" "this" {
  replication_group_id = "${var.name}-redis"
  description          = "Redis for url shortener"
  node_type            = var.node_type
  num_cache_clusters   = 2   # primary 1 + replica 1
  port                 = 6379
  subnet_group_name    = aws_elasticache_subnet_group.this.name
  security_group_ids   = [aws_security_group.redis.id]
  automatic_failover_enabled = true

  # 9000 read TPS 대응: replica에서 읽기 분산 가능
  # Spring app에서 read-from-replica 설정 시 활용
}
