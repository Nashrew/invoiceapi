FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=target/invoiceapi-2.0.jar
COPY ${JAR_FILE} invoiceapi.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/invoiceapi.jar"]
