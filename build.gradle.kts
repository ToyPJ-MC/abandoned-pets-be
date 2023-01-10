import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.json:json:20220924")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.7.6")
	implementation("mysql:mysql-connector-java:8.0.31")
	compileOnly("org.projectlombok:lombok")
	implementation("org.keycloak:keycloak-spring-security-adapter:19.0.3")
	implementation("com.googlecode.json-simple:json-simple:1.1.1")
	implementation("org.thymeleaf:thymeleaf:3.0.15.RELEASE")
	//implementation("org.keycloak:keycloak-admin-client:19.0.3") //키클락
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
