FROM maven:3.9.9-eclipse-temurin-8

RUN sed -i 's|http://archive.ubuntu.com|http://mirrors.aliyun.com|g' /etc/apt/sources.list.d/*.sources 2>/dev/null || true \
    && sed -i 's|http://security.ubuntu.com|http://mirrors.aliyun.com|g' /etc/apt/sources.list.d/*.sources 2>/dev/null || true \
    && apt-get update \
    && apt-get install -y --no-install-recommends \
    git \
    mysql-server \
    redis-server \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY repo/crmeb/ .

RUN mkdir -p /root/.m2 \
    && echo '<?xml version="1.0" encoding="UTF-8"?><settings><mirrors><mirror><id>aliyun</id><mirrorOf>central</mirrorOf><name>Aliyun Maven</name><url>https://maven.aliyun.com/repository/public</url></mirror></mirrors></settings>' > /root/.m2/settings.xml \
    && mvn install -DskipTests -B -q

RUN git init && git config user.email "dev@example.com" && git config user.name "Developer" && git add -A && git commit -m "Initial commit"

COPY docker-entrypoint.sh /usr/local/bin/
RUN chmod +x /usr/local/bin/docker-entrypoint.sh

EXPOSE 8080 8081

ENTRYPOINT ["docker-entrypoint.sh"]
