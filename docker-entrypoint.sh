#!/bin/bash
set -e

echo "=== Initializing MySQL ==="
if [ ! -d /var/lib/mysql/mysql ]; then
    mysqld --initialize-insecure --user=mysql --datadir=/var/lib/mysql --default-authentication-plugin=mysql_native_password
fi
mysqld --user=mysql --datadir=/var/lib/mysql --bind-address=127.0.0.1 --default-authentication-plugin=mysql_native_password &
MYSQL_PID=$!

echo "=== Starting Redis ==="
redis-server --daemonize yes --requirepass 111111 --bind 127.0.0.1

echo "=== Waiting for MySQL ==="
for i in $(seq 1 30); do
    if mysql -u root -e "SELECT 1" --silent 2>/dev/null; then
        break
    fi
    sleep 1
done

echo "=== Setting up database ==="
mysql -u root -e "CREATE DATABASE IF NOT EXISTS single_open DEFAULT CHARACTER SET utf8mb4;"
mysql -u root -e "ALTER USER 'root'@'localhost' IDENTIFIED BY ''; FLUSH PRIVILEGES;" 2>/dev/null || true
mysql -u root -e "CREATE USER IF NOT EXISTS 'single_open'@'127.0.0.1' IDENTIFIED BY '111111';" 2>/dev/null || true
mysql -u root -e "CREATE USER IF NOT EXISTS 'single_open'@'localhost' IDENTIFIED BY '111111';" 2>/dev/null || true
mysql -u root -e "GRANT ALL PRIVILEGES ON single_open.* TO 'single_open'@'127.0.0.1';" 2>/dev/null || true
mysql -u root -e "GRANT ALL PRIVILEGES ON single_open.* TO 'single_open'@'localhost'; FLUSH PRIVILEGES;" 2>/dev/null || true
echo "=== Importing schema ==="
mysql -u root --init-command="SET autocommit=0; SET unique_checks=0; SET foreign_key_checks=0;" single_open < /app/sql/Crmeb_v1.4.sql 2>/tmp/import.err || true
mysql -u root -e "COMMIT;"
echo "=== Schema imported ($(mysql -u root -e 'SELECT COUNT(*) FROM information_schema.tables WHERE table_schema=\"single_open\";' -sN) tables) ==="

echo "=== Starting Admin (port 8080) ==="
cd /app/crmeb-admin
java -jar target/Crmeb-admin.jar &
ADMIN_PID=$!

echo "=== Starting Front (port 8081) ==="
cd /app/crmeb-front
java -jar target/Crmeb-front.jar &
FRONT_PID=$!

echo ""
echo "========================================"
echo "  Admin API: http://localhost:8080"
echo "  Front API: http://localhost:8081"
echo "  Admin login: admin / 123456"
echo "========================================"
echo ""

wait
