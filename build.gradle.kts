import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.builtins.StandardNames.FqNames.annotation

plugins {
	id("org.springframework.boot") version "2.7.2"
	id("io.spring.dependency-management") version "1.0.13.RELEASE"
	id("com.palantir.docker") version "0.26.0"
	id("com.palantir.docker-run") version "0.26.0"
	id("com.palantir.docker-compose") version "0.26.0"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
	kotlin("plugin.jpa") version "1.6.21"
	kotlin("plugin.allopen") version "1.3.61"
}

group = "net.keithyw"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-mustache")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("junit:junit:4.13.2")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("com.h2database:h2")
	runtimeOnly("mysql:mysql-connector-java")
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(module = "junit")
		exclude(module = "mockito-core")
	}
	testImplementation("org.junit.jupiter:junit-jupiter-api")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
	testImplementation("org.junit.jupiter:junit-jupiter-params")
	testImplementation("com.ninja-squad:springmockk:3.1.1")
	implementation("org.springframework.boot:spring-boot-starter-security:2.7.2")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")

	testImplementation("org.springframework.security:spring-security-test:5.7.3")

	testImplementation("org.testcontainers:testcontainers:1.15.2")
	testImplementation("org.testcontainers:junit-jupiter:1.15.2")
	implementation(platform("org.testcontainers:testcontainers-bom:1.15.2"))
	testImplementation("org.testcontainers:mysql:1.15.2")

	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

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

allOpen {
	annotation("javax.persistence.Entity")
	annotation("javax.persistence.Embeddable")
	annotation("javax.persistence.MappedSuperclass")
}

docker {
	name = "net.keithyw.game/game:".plus(version)
	tag("name", "keithyw.game")
	copySpec.from("build").into("build")
	buildArgs(com.google.common.collect.ImmutableMap.of("name", "keithyw.game"))
	pull(true)
	setDockerfile(file("Dockerfile"))
}

dockerCompose {
	setTemplate("docker-compose.yml.template")
	setDockerComposeFile("docker-compose.yml")
}

dockerRun {
	name = "keithyw.game"
	image = "net.keithyw.game/game:".plus(version)
	ports("8080:8080")
}