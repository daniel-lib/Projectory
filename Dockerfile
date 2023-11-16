FROM maven:3.8.3-openjdk-11 AS build
COPY  . .
RUN mvn clean package -DskipTests

FROM openjdk:11-jdk-slim
COPY --from=build /target/Projectory-0.0.1-SNAPSHOT.jar Projectory.jar
EXPOSE 8080
ENTRYPOINT [ "java","-jar","Projectory.jar" ]