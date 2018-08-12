FROM maven:3.5.2-jdk-8-alpine AS MAVEN_TOOL_CHAIN
COPY pom.xml .
COPY src ./src/
RUN mvn package
ARG JAR_FILE=target/invoiceapi-2.0.jar
COPY ${JAR_FILE} invoiceapi.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/invoiceapi.jar"]