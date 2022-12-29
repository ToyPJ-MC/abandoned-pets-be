FROM openjdk:11
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8080/tcp
ENTRYPOINT ["java","-jar","/app.jar"]