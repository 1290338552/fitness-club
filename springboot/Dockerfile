# 构建阶段：Maven 3.9 + JDK 21（兼容你的项目）
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# 运行阶段：JDK 21
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# 你的端口 9999
EXPOSE 9999
ENTRYPOINT ["java", "-jar", "app.jar"]
