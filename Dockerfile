FROM maven:3.9.9-eclipse-temurin-8

RUN apt-get update && apt-get install -y --no-install-recommends \
    git \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY repo/crmeb/ .

RUN mvn install -DskipTests -B -q

RUN git init && git config user.email "dev@example.com" && git config user.name "Developer" && git add -A && git commit -m "Initial commit"

CMD ["/bin/bash"]
