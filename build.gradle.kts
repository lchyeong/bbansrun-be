plugins {
	java
	id("org.springframework.boot") version "3.3.2"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "com.lchproject3"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-websocket")
	compileOnly("org.projectlombok:lombok")
	runtimeOnly("com.mysql:mysql-connector-j")
	implementation ("com.h2database:h2")

	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	implementation("org.apache.httpcomponents:httpclient:4.5.13")

	implementation("org.springframework.boot:spring-boot-starter-actuator") // 상태 모니터링 및 관리 기능
	implementation("org.springframework.boot:spring-boot-starter-validation") // MVC 유효성 검증 기능
	implementation("com.fasterxml.jackson.core:jackson-databind") // JSON 데이터 바인딩
	implementation("org.springdoc:springdoc-openapi-ui:1.7.0") // OpenAPI 3.0 문서 생성

}

tasks.withType<Test> {
	useJUnitPlatform()
}
