#!/bin/bash
set -e
exec > /var/log/userdata.log 2>&1

# Java 21 (Corretto)
dnf install -y java-21-amazon-corretto

# Docker
dnf install -y docker
systemctl start docker
systemctl enable docker

# Docker Compose v2
mkdir -p /usr/local/lib/docker/cli-plugins
curl -fsSL https://github.com/docker/compose/releases/download/v2.24.1/docker-compose-linux-x86_64 \
  -o /usr/local/lib/docker/cli-plugins/docker-compose
chmod +x /usr/local/lib/docker/cli-plugins/docker-compose

# MySQL + Redis 컨테이너
mkdir -p /opt/app
cat > /opt/app/docker-compose.yml << 'COMPOSE'
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: url_shortener
      MYSQL_USER: app
      MYSQL_PASSWORD: app1234
    ports:
      - "3306:3306"
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost", "-u", "root", "-proot"]
      interval: 5s
      retries: 12
  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"
COMPOSE

cd /opt/app && docker compose up -d

# MySQL 준비 대기
echo "Waiting for MySQL..."
until docker compose -f /opt/app/docker-compose.yml exec -T mysql mysqladmin ping -h localhost -u root -proot --silent 2>/dev/null; do
  sleep 3
done
echo "MySQL ready."

# JAR 다운로드
aws s3 cp s3://${bucket}/app.jar /opt/app/app.jar

# Spring Boot 실행 (ddl-auto=create → 테이블 자동 생성)
nohup java \
  -XX:+UseContainerSupport \
  -Xms512m -Xmx2g \
  -jar /opt/app/app.jar \
  --spring.profiles.active=local \
  --spring.datasource.url="jdbc:mysql://localhost:3306/url_shortener?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC" \
  --spring.datasource.username=app \
  --spring.datasource.password=app1234 \
  --spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver \
  --spring.data.redis.host=localhost \
  --spring.data.redis.port=6379 \
  --spring.jpa.hibernate.ddl-auto=create \
  --spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect \
  --app.host=http://localhost:8080 \
  > /opt/app/app.log 2>&1 &

echo "App started. PID=$!"

# 헬스체크 대기 (최대 2분)
echo "Waiting for app to start..."
for i in $(seq 1 24); do
  if curl -sf http://localhost:8080/actuator/health > /dev/null 2>&1; then
    echo "App is UP after $${i}x5s"
    break
  fi
  sleep 5
done

echo "Setup complete."
