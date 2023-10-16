FROM maven:3.6.3-openjdk-17-slim as stage1
ENV MAVEN_OPTS="-XX:+TieredCompilation -XX:TieredStopAtLevel=1"
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY ./src ./src
RUN mvn clean install -Dmaven.test.skip=true

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=stage1 /app/target/pokemon-0.0.1-SNAPSHOT.jar /app
CMD java -jar /app/pokemon-0.0.1-SNAPSHOT.jar