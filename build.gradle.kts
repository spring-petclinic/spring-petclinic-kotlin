import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


buildscript {

    extra["springBootVersion"] = "2.0.0.RELEASE"
    extra["boostrapVersion"] = "3.3.6"
    extra["jQueryVersion"] = "2.2.4"
    extra["jQueryUIVersion"] = "1.11.4"

    val springBootVersion: String by extra

    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:$springBootVersion")
    }
}


plugins {
    val kotlinVersion = "1.2.21"

    id("org.jetbrains.kotlin.jvm") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.spring") version kotlinVersion
    id("io.spring.dependency-management") version "1.0.4.RELEASE"
}

apply {
    plugin("org.springframework.boot")
}


val kotlinVersion: String by extra
val springBootVersion: String by extra
val jUnitVersion: String by extra
val boostrapVersion: String by extra
val jQueryVersion: String by extra
val jQueryUIVersion: String by extra

version = springBootVersion

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xjsr305=strict")
        }
    }
}

val test by tasks.getting(Test::class) {
    useJUnitPlatform()
}

repositories {
    mavenCentral()
}

dependencies {
    compile("org.springframework.boot:spring-boot-starter-actuator")
    compile("org.springframework.boot:spring-boot-starter-cache")
    compile("org.springframework.boot:spring-boot-starter-data-jpa")
    compile("org.springframework.boot:spring-boot-starter-web")
    compile("org.springframework.boot:spring-boot-starter-thymeleaf")
    compile("javax.cache:cache-api")
    compile("org.jetbrains.kotlin:kotlin-stdlib")
    compile("org.jetbrains.kotlin:kotlin-reflect")
    compile("org.webjars:webjars-locator-core")
    compile("org.webjars:jquery:$jQueryVersion")
    compile("org.webjars:jquery-ui:$jQueryUIVersion")
    compile("org.webjars:bootstrap:$boostrapVersion")

    testCompile("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "junit")
    }
    testCompile("org.springframework.boot:spring-boot-starter-webflux")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    runtime("org.hsqldb:hsqldb")
    runtime("mysql:mysql-connector-java")
    runtime("com.fasterxml.jackson.module:jackson-module-kotlin")
}

