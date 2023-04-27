

plugins {
	id("org.springframework.boot") version "2.7.6"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
}

group = "Winter"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}
dependencies {
	implementation("io.springfox:springfox-boot-starter:3.0.0")
	implementation("org.springframework:spring-jdbc:5.3.24")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.javassist:javassist:3.29.2-GA")
	implementation("org.json:json:20220924")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.security:spring-security-oauth2-client:5.7.5")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.7.6")
	implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.4") //yml 암호화
	implementation("mysql:mysql-connector-java:8.0.32")
	compileOnly("org.projectlombok:lombok")
	runtimeOnly("mysql:mysql-connector-java")

	implementation("io.jsonwebtoken:jjwt-api:0.11.2")
	runtimeOnly ("io.jsonwebtoken:jjwt-impl:0.11.2")
	runtimeOnly ("io.jsonwebtoken:jjwt-jackson:0.11.2")

	implementation("com.googlecode.json-simple:json-simple:1.1.1")
	implementation("org.thymeleaf:thymeleaf:3.0.15.RELEASE")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")

}

tasks.withType<Test> {
	useJUnitPlatform()
}

