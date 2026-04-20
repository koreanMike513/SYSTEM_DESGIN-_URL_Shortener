terraform {
  required_providers {
    aws    = { source = "hashicorp/aws", version = "~> 5.0" }
    random = { source = "hashicorp/random", version = "~> 3.0" }
  }
}

provider "aws" { region = "ap-northeast-2" }

# ── Default VPC 사용 (신규 VPC 생성 생략 → 빠른 구성) ──
data "aws_vpc" "default" { default = true }

data "aws_subnets" "default" {
  filter {
    name   = "vpc-id"
    values = [data.aws_vpc.default.id]
  }
  filter {
    name   = "map-public-ip-on-launch"
    values = ["true"]
  }
}

# ── S3: JAR 업로드 ──────────────────────────────────────
resource "random_id" "suffix" { byte_length = 4 }

resource "aws_s3_bucket" "jar" {
  bucket        = "url-shortener-quicktest-${random_id.suffix.hex}"
  force_destroy = true
}

resource "aws_s3_bucket_public_access_block" "jar" {
  bucket                  = aws_s3_bucket.jar.id
  block_public_acls       = true
  block_public_policy     = true
  ignore_public_acls      = true
  restrict_public_buckets = true
}

resource "aws_s3_object" "app_jar" {
  bucket = aws_s3_bucket.jar.bucket
  key    = "app.jar"
  source = "../../build/libs/uri-shortener-0.0.1-SNAPSHOT.jar"
  etag   = filemd5("../../build/libs/uri-shortener-0.0.1-SNAPSHOT.jar")
}

# ── IAM: EC2 → S3 접근 ──────────────────────────────────
resource "aws_iam_role" "ec2" {
  name = "url-shortener-quicktest-role"
  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{ Effect = "Allow", Principal = { Service = "ec2.amazonaws.com" }, Action = "sts:AssumeRole" }]
  })
}

resource "aws_iam_role_policy" "s3_read" {
  role = aws_iam_role.ec2.id
  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{ Effect = "Allow", Action = ["s3:GetObject"], Resource = "${aws_s3_bucket.jar.arn}/*" }]
  })
}

resource "aws_iam_instance_profile" "ec2" {
  name = "url-shortener-quicktest-profile"
  role = aws_iam_role.ec2.name
}

# ── Security Group ──────────────────────────────────────
resource "aws_security_group" "app" {
  name   = "url-shortener-quicktest-sg"
  vpc_id = data.aws_vpc.default.id

  ingress {
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# ── AMI ─────────────────────────────────────────────────
data "aws_ami" "al2023" {
  most_recent = true
  owners      = ["amazon"]
  filter {
    name   = "name"
    values = ["al2023-ami-*-x86_64"]
  }
  filter {
    name   = "architecture"
    values = ["x86_64"]
  }
}

# ── EC2: MySQL + Redis + Spring Boot ────────────────────
resource "aws_instance" "app" {
  ami                         = data.aws_ami.al2023.id
  instance_type               = "t3.xlarge"   # 4 vCPU, 16 GB (MySQL + Redis + App)
  subnet_id                   = data.aws_subnets.default.ids[0]
  vpc_security_group_ids      = [aws_security_group.app.id]
  iam_instance_profile        = aws_iam_instance_profile.ec2.name
  associate_public_ip_address = true

  user_data = templatefile("${path.module}/userdata.sh", {
    bucket = aws_s3_bucket.jar.bucket
  })

  depends_on = [aws_s3_object.app_jar]
  tags       = { Name = "url-shortener-quicktest" }
}

output "app_public_ip"  { value = aws_instance.app.public_ip }
output "s3_bucket_name" { value = aws_s3_bucket.jar.bucket }
output "base_url"       { value = "http://${aws_instance.app.public_ip}:8080" }
