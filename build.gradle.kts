import kotlinx.kover.gradle.plugin.dsl.AggregationType
import kotlinx.kover.gradle.plugin.dsl.MetricType

group = "ru.otus"
version = "1.0-SNAPSHOT"

plugins {
    kotlin("jvm") version "2.1.20"
    kotlin("plugin.spring") version "2.1.20"
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.jetbrains.kotlinx.kover") version "0.7.6"
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.security:spring-security-config")
    implementation("org.springframework.security:spring-security-oauth2-resource-server")
    implementation("org.springframework.security:spring-security-oauth2-jose")

    implementation("io.ktor:ktor-client-core:2.3.7")
    implementation("io.ktor:ktor-client-cio:2.3.7")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.7")
    implementation("io.ktor:ktor-serialization-jackson:2.3.7")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.assertj:assertj-core:3.27.3")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.0")
    testImplementation("org.mockito:mockito-core")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1")
    testImplementation("com.ninja-squad:springmockk:4.0.2")
    testImplementation("io.ktor:ktor-client-mock:2.3.7")
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

kotlin {
    jvmToolchain(17)
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

//koverReport {
//    verify {
//        rule("Min 90% lines coverage") {
//            isEnabled = true
//            bound {
//                minValue = 90
//                metric = MetricType.LINE
//                aggregation = AggregationType.COVERED_PERCENTAGE
//            }
//        }
//        rule("Min 90% instructions coverage") {
//            isEnabled = true
//            bound {
//                minValue = 90
//                metric = MetricType.INSTRUCTION
//                aggregation = AggregationType.COVERED_PERCENTAGE
//            }
//        }
//        rule("Min 90% branches coverage") {
//            isEnabled = true
//            bound {
//                minValue = 90
//                metric = MetricType.BRANCH
//                aggregation = AggregationType.COVERED_PERCENTAGE
//            }
//        }
//    }
//}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}
