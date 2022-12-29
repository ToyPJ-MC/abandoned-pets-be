FROM openjdk:11
CMD ["./mvnw", "clean", "package"]
COPY ./build/libs/*.version: '3'
services:
  abandoned-pets-be:
    image: abandoned-pets:0.0.1
    ports:
      - "18082:8080"
    restart: always
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://203.241.228.50:13307/pets?useSSL=false&serverTimezone=Asia/Seoul&useLegacyDatetimeCode=false
      SPRING_DATASOURCE_USERNAME: "root"
      SPRING_DATASOURCE_PASSWORD: "mega123"



~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
"docker-compose.yml" 14L, 368B                                                                        12,0-1       모두
Last login: Thu Dec 29 16:01:48 on ttys004
> ssh 203.241.228.50 -p 10001 -l mega
mega@203.241.228.50's password:
Welcome to Ubuntu 22.04.1 LTS (GNU/Linux 5.15.0-56-generic x86_64)

 * Documentation:  https://help.ubuntu.com
 * Management:     https://landscape.canonical.com
 * Support:        https://ubuntu.com/advantage

130 updates can be applied immediately.
To see these additional updates run: apt list --upgradable

Last login: Thu Dec 29 16:01:52 2022 from 203.241.228.51
mega@mega-cluster-1:~$ ls
Inje-Discord-Bot-v2    db                        nginx              production       yarn.lock  사진
Mega-Kubernetes        dev                       node_modules       psql             공개       음악
Mega-Waka-Board-be-v2  docker-compose-mysql.yml  nohup.out          q                다운로드   템플릿
MegaTime               kubeadm-conf.yaml         outsourcing        smartfarm        문서
abandoned-pets-be      mariadb                   package-lock.json  snap             바탕화면
data                   mega-waka-board-fe        package.json       trend-in-one-be  비디오
mega@mega-cluster-1:~$ cd abandoned-pets-be
mega@mega-cluster-1:~/abandoned-pets-be$ ls
build  build.gradle.kts  docker-compose.yml  dockerfile  gradle  gradlew  gradlew.bat  settings.gradle.kts  src
mega@mega-cluster-1:~/abandoned-pets-be$ vi docker-compose.yml
mega@mega-cluster-1:~/abandoned-pets-be$ docker build -t abandoned-pets:0.0.1 .
Sending build context to Docker daemon  50.65MB
Step 1/5 : FROM openjdk:11
 ---> 47a932d998b7
Step 2/5 : CMD ["./mvnw", "clean", "package"]
 ---> Using cache
 ---> 0ada79ac5266
Step 3/5 : ARG JAR_FILE_PATH= ./build/libs/*.jar
 ---> Running in f151be5fbbd9
Removing intermediate container f151be5fbbd9
FROM openjdk:11
CMD ["./mvnw", "clean", "package"]
ARG JAR_FILE_PATH= ./build/libs/*.jar
COPY ${JAR_FILE_PATH} app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
"dockerfile" 5L, 158B                                                                                                                               3,21         모두
FROM openjdk:11
 ---> 8852d471b565
Step 4/5 : COPY ${JAR_FILE_PATH} app.jar
 ---> 1f48aa87ebfc
Step 5/5 : ENTRYPOINT ["java", "-jar", "app.jar"]
 ---> Running in 60ccd1c3a90d
Removing intermediate container 60ccd1c3a90d
 ---> 2de16f179e84
Successfully built 2de16f179e84
Successfully tagged abandoned-pets:0.0.1
mega@mega-cluster-1:~/abandoned-pets-be$ docker-compose up -d
Creating abandoned-pets-be_abandoned-pets-be_1 ... done
mega@mega-cluster-1:~/abandoned-pets-be$ docker ps
CONTAINER ID   IMAGE                           COMMAND                  CREATED         STATUS                        PORTS                                                    NAMES
a189a9ca350b   abandoned-pets:0.0.1            "java -jar app.jar .…"   5 seconds ago   Restarting (1) 1 second ago                                                            abandoned-pets-be_abandoned-pets-be_1
20deb85f2a6b   postgres                        "docker-entrypoint.s…"   2 days ago      Up 17 minutes                 0.0.0.0:15432->5432/tcp, :::15432->5432/tcp              psql_postgresql_1
06d97bdbadaf   jadru/smartfarm-dashboard:1.4   "docker-entrypoint.s…"   5 weeks ago     Up 17 minutes                 0.0.0.0:3001->3000/tcp, :::3001->3000/tcp                backend_smartfarm-frontend_1
68ab19c9c0df   smartfarm-backend:0.1.2         "/bin/sh -c 'exec ja…"   5 weeks ago     Up 12 minutes                 0.0.0.0:18081->8080/tcp, :::18081->8080/tcp              backend_smartfarm-backend_1
309e64d91998   mysql:5.7                       "docker-entrypoint.s…"   5 weeks ago     Up 12 minutes                 33060/tcp, 0.0.0.0:13307->3306/tcp, :::13307->3306/tcp   mega_mega-mysql_1
e1f90e87fe39   influxdb:2.2-alpine             "/entrypoint.sh infl…"   5 weeks ago     Up 17 minutes                 0.0.0.0:8086->8086/tcp, :::8086->8086/tcp                influxdb2
FROM openjdk:11
CMD ["./mvnw", "clean", "package"]
ARG JAR_FILE_PATH=./build/libs/*.jar
COPY ${JAR_FILE_PATH} app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
"dockerfile" 5L, 157B                                                                                                                               3,18         모두
FROM openjdk:11
mega@mega-cluster-1:~/abandoned-pets-be$ docker stop a189a9ca350b
a189a9ca350b
mega@mega-cluster-1:~/abandoned-pets-be$ docker rm a189a9ca350b
a189a9ca350b
mega@mega-cluster-1:~/abandoned-pets-be$ vi dockerfile
mega@mega-cluster-1:~/abandoned-pets-be$ docker build -t abandoned-pets:0.0.1 .
Sending build context to Docker daemon  50.65MB
Step 1/5 : FROM openjdk:11
 ---> 47a932d998b7
Step 2/5 : CMD ["./mvnw", "clean", "package"]
 ---> Using cache
 ---> 0ada79ac5266
Step 3/5 : ARG JAR_FILE_PATH=./build/libs/*.jar
 ---> Running in e6a96e319f0a
Removing intermediate container e6a96e319f0a
 ---> 2157436ae6b6
Step 4/5 : COPY ${JAR_FILE_PATH} app.jar
When using COPY with more than one source file, the destination must be a directory and end with a /
mega@mega-cluster-1:~/abandoned-pets-be$ docker build -t abandoned-pets:0.0.2 .
Sending build context to Docker daemon  50.65MB
Step 1/5 : FROM openjdk:11
 ---> 47a932d998b7
Step 2/5 : CMD ["./mvnw", "clean", "package"]
 ---> Using cache
 ---> 0ada79ac5266
Step 3/5 : ARG JAR_FILE_PATH=./build/libs/*.jar
 ---> Using cache
 ---> 2157436ae6b6
Step 4/5 : COPY ${JAR_FILE_PATH} app.jar
When using COPY with more than one source file, the destination must be a directory and end with a /
mega@mega-cluster-1:~/abandoned-pets-be$ vi dockerfile
mega@mega-cluster-1:~/abandoned-pets-be$ ./gradlew clean

BUILD SUCCESSFUL in 833ms
1 actionable task: 1 executed
mega@mega-cluster-1:~/abandoned-pets-be$ ./gradlew build

> Task :compileKotlin
w: /home/mega/abandoned-pets-be/src/main/kotlin/Winter/pets/controller/TestController.kt: (27, 85): The lambda expression is unused. If you mean a block, you can use 'run { ... }'
w: /home/mega/abandoned-pets-be/src/main/kotlin/Winter/pets/service/PetServiceImpl.kt: (24, 5): Lateinit is unnecessary: definitely initialized in constructors
w: /home/mega/abandoned-pets-be/src/main/kotlin/Winter/pets/service/PetServiceImpl.kt: (26, 5): Lateinit is unnecessary: definitely initialized in constructors
w: /home/mega/abandoned-pets-be/src/main/kotlin/Winter/pets/service/PetServiceImpl.kt: (28, 5): Lateinit is unnecessary: definitely initialized in constructors
w: /home/mega/abandoned-pets-be/src/main/kotlin/Winter/pets/service/PetServiceImpl.kt: (30, 5): Lateinit is unnecessary: definitely initialized in constructors
w: /home/mega/abandoned-pets-be/src/main/kotlin/Winter/pets/service/PetServiceImpl.kt: (32, 5): Lateinit is unnecessary: definitely initialized in constructors
w: /home/mega/abandoned-pets-be/src/main/kotlin/Winter/pets/service/PetServiceImpl.kt: (55, 28): Variable 'str' initializer is redundant
FROM openjdk:11
CMD ["./mvnw", "clean", "package"]
COPY ./build/libs/*,jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
"dockerfile" 4L, 122B                                                                                                                               3,31         모두
FROM openjdk:11
w: /home/mega/abandoned-pets-be/src/main/kotlin/Winter/pets/service/PetServiceImpl.kt: (93, 28): Variable 'str' initializer is redundant
w: /home/mega/abandoned-pets-be/src/main/kotlin/Winter/pets/service/PetServiceImpl.kt: (139, 28): Variable 'str' initializer is redundant
w: /home/mega/abandoned-pets-be/src/main/kotlin/Winter/pets/service/PetServiceImpl.kt: (184, 28): Variable 'str' initializer is redundant
w: /home/mega/abandoned-pets-be/src/main/kotlin/Winter/pets/service/PetServiceImpl.kt: (270, 28): Variable 'str' initializer is redundant

> Task :test
2022-12-29 16:43:38.738  INFO 949258 --- [ionShutdownHook] j.LocalContainerEntityManagerFactoryBean : Closing JPA EntityManagerFactory for persistence unit 'default'
2022-12-29 16:43:38.741  INFO 949258 --- [ionShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown initiated...
2022-12-29 16:43:38.752  INFO 949258 --- [ionShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown completed.

BUILD SUCCESSFUL in 11s
8 actionable tasks: 8 executed
mega@mega-cluster-1:~/abandoned-pets-be$ docker build -t abandoned-pets:0.0.1 .
Sending build context to Docker daemon  50.65MB
Step 1/4 : FROM openjdk:11
 ---> 47a932d998b7
Step 2/4 : CMD ["./mvnw", "clean", "package"]
 ---> Using cache
 ---> 0ada79ac5266
FROM openjdk:11
CMD ["./mvnw", "clean", "package"]
COPY ./build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
"dockerfile" 4L, 122B                                                                                                                               3,21         모두
FROM openjdk:11
CMD ["./mvnw", "clean", "package"]
COPY ./build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
~
"dockerfile" 4L, 122B                                                                                                                               3,20         모두jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
