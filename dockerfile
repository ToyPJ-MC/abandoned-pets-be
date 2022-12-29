FROM openjdk:11
ARG JAR_FILE=*.jar
ADD /build/libs/pets-0.0.1-SNAPSHOT-plain.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]