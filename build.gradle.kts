import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


project.description = "Kotlin version of the Spring Petclinic application"
// Align with Spring Version
project.version = "2.1.6"

plugins {
    val kotlinVersion = "1.3.21"
    id("org.springframework.boot") version "2.1.6.RELEASE"
    id("org.jetbrains.kotlin.jvm") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.spring") version kotlinVersion
    id("io.spring.dependency-management") version "1.0.7.RELEASE"
    id("com.google.cloud.tools.jib") version "1.3.0"
}

val boostrapVersion = "3.3.6"
val jQueryVersion = "2.2.4"
val jQueryUIVersion = "1.11.4"

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xjsr305=strict")
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("javax.cache:cache-api")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.webjars:webjars-locator-core")
    implementation("org.webjars:jquery:$jQueryVersion")
    implementation("org.webjars:jquery-ui:$jQueryUIVersion")
    implementation("org.webjars:bootstrap:$boostrapVersion")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "junit")
    }
    testImplementation("org.springframework.boot:spring-boot-starter-webflux")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    runtimeOnly("org.hsqldb:hsqldb")
    runtimeOnly("mysql:mysql-connector-java")
}


jib {
    to {
        image = "springcommunity/spring-petclinic-kotlin"
        tags = setOf(project.version.toString(), "latest")
    }
}

