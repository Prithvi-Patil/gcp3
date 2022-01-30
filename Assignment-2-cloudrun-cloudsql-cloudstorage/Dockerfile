FROM maven:3.8-jdk-11 as builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn package -DskipTests

FROM adoptopenjdk/openjdk8:alpine-jre
COPY --from=builder /app/target/employeeApp.jar /employeeApp.jar
CMD ["java", "-jar", "/employeeApp.jar"]
