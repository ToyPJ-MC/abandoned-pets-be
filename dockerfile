FROM openjdk:11
VOLUME /tmp
ARG JAR_FILE=./build/libs/*.jar
COPY ${JAR_FILE} SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/SNAPSHOT.jar"]