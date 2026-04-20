data "aws_ami" "amazon_linux" {
  most_recent = true
  owners      = ["amazon"]
  filter {
    name   = "name"
    values = ["al2023-ami-*-x86_64"]
  }
}

resource "aws_security_group" "k6" {
  name   = "${var.name}-k6-sg"
  vpc_id = var.vpc_id

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]   # 실제 운영 시 자신의 IP로 제한
  }
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_instance" "k6" {
  ami                         = data.aws_ami.amazon_linux.id
  instance_type               = var.instance_type
  subnet_id                   = var.public_subnet_id
  vpc_security_group_ids      = [aws_security_group.k6.id]
  associate_public_ip_address = true

  user_data = <<-EOF
    #!/bin/bash
    # k6 설치
    dnf install -y https://dl.k6.io/rpm/repo.rpm
    dnf install -y k6
    # 테스트 스크립트 위치
    mkdir -p /opt/k6
    echo "ALB_URL=http://${var.alb_dns_name}" > /opt/k6/.env
  EOF

  tags = { Name = "${var.name}-k6-runner" }
}
