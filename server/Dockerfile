FROM amazoncorretto:21.0.4-alpine3.20
LABEL maintainer="Eduardo Guiraldelli Schelive"
COPY target/xlr8server-0.0.2.jar /opt/xlr8server/xlr8server.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/opt/xlr8server/xlr8server.jar"]