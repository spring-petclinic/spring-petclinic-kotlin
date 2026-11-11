description = "Kotlin version of the Spring Petclinic application"
group = "org.springframework.samples"
// Align with Spring Version
version = "4.0.2"

plugins {
    val kotlinVersion = "2.4.21"
    id("org.springframework.boot") version "4.1.2"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.google.cloud.tools.jib") version "3.5.4"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion
    id("org.jlleitschuh.gradle.ktlint") version "14.2.0"
    id("io.gitlab.arturbosch.detekt") version "1.23.8"
}

val bootstrapVersion = "5.3.8"
val fontAwesomeVersion = "4.7.0"
val webjarsLocatorLiteVersion = "1.1.5"
val ktlintVersion = "1.8.0"

kotlin {
    jvmToolchain(17)
    compilerOptions {
        allWarningsAsErrors.set(true)
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

ktlint {
    version.set(ktlintVersion)
}

detekt {
    buildUponDefaultConfig = true
    config.setFrom(files("config/detekt/detekt.yml"))
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    jvmTarget = "17"
}

// Detekt and ktlint are compiled against their own Kotlin version: prevent the Spring dependency
// management plugin from aligning their classpaths with the project's Kotlin version.
configurations.matching { it.name == "detekt" || it.name.startsWith("ktlint") }.configureEach {
    resolutionStrategy.eachDependency {
        if (requested.group == "org.jetbrains.kotlin") {
            val targetVersion = if (name == "detekt") {
                io.gitlab.arturbosch.detekt.getSupportedKotlinVersion()
            } else {
                "2.3.10"
            }
            useVersion(targetVersion)
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("tools.jackson.module:jackson-module-kotlin")
    implementation("org.glassfish.jaxb:jaxb-runtime")
    implementation("javax.cache:cache-api")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.webjars.npm:bootstrap:$bootstrapVersion")
    implementation("org.webjars.npm:font-awesome:$fontAwesomeVersion")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webflux")

    runtimeOnly("com.h2database:h2")
    runtimeOnly("com.mysql:mysql-connector-j")
    runtimeOnly("org.webjars:webjars-locator-lite:$webjarsLocatorLiteVersion")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
}

jib {
    to {
        image = "springcommunity/spring-petclinic-kotlin"
        tags = setOf(project.version.toString(), "latest")
    }
}
