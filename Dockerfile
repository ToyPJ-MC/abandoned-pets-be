FROM openjdk:11
WORKDIR app
EXPOSE 8080
COPY build.gradle.kts settings.gradle.kts ./
COPY ./build/libs/*.jar app.jar
COPY src ./src
CMD ["java","-jar","app.jar"]