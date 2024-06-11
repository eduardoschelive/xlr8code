FROM amazoncorretto:21-alpine-jdk
LABEL maintainer="Eduardo Guiraldelli Schelive"
COPY target/xlr8server-0.0.1.jar /opt/xlr8server/xlr8server.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/opt/xlr8server/xlr8server.jar"]