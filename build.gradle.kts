import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


description = "Kotlin version of the Spring Petclinic application"
group = "org.springframework.samples"
// Align with Spring Version
version = "3.2.2"

java.sourceCompatibility = JavaVersion.VERSION_17

plugins {
    val kotlinVersion = "1.9.20"
    id("org.springframework.boot") version "3.2.2"
    id("io.spring.dependency-management") version "1.1.4"
    id("com.google.cloud.tools.jib") version "3.1.4"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    id("org.jetbrains.kotlinx.kover") version "0.7.2"
}

// WebJars versions are also referenced in src/main/resources/templates/fragments/layout.html for resource URLs
val boostrapVersion = "5.1.3"
val fontAwesomeVersion = "4.7.0"

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs = listOf("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy(tasks.koverXmlReport)
}

koverReport {
    filters {
        includes {
            classes("org.springframework.samples.*")
        }
        excludes {
            classes("org.springframework.samples.petclinic.system.TransactionalRepository")
        }
    }
    defaults {
        xml {
            onCheck = false
            setReportFile(layout.projectDirectory.file(".qodana/code-coverage/result.xml").asFile)
        }
    }
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/snapshot") }
    maven { url = uri("https://repo.spring.io/milestone") }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.glassfish.jaxb:jaxb-runtime")
    implementation("javax.cache:cache-api")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.webjars.npm:bootstrap:$boostrapVersion")
    implementation("org.webjars.npm:font-awesome:$fontAwesomeVersion")
    implementation("io.github.resilience4j:resilience4j-spring-boot3:2.2.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webflux")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation(kotlin("test"))
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")


    runtimeOnly("com.h2database:h2")
    runtimeOnly("com.mysql:mysql-connector-j")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
    implementation(kotlin("stdlib-jdk8"))
}

jib {
    to {
        image = "springcommunity/spring-petclinic-kotlin"
        tags = setOf(project.version.toString(), "latest")
    }
}

