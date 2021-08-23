import com.adarshr.gradle.testlogger.theme.ThemeType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val logback_version: String by project
val ktor_version: String by project
val kotlin_version: String by project
val skrapeit_version: String by project
val koin_version: String by project
val kmongo_version: String by project
val kotlix_serialization_version: String by project
val redisson_version: String by project
val quartz_version: String by project
val jacoco_version: String by project
val easy_random_version: String by project
val detekt_version: String by project
val mockk_version: String by project
val kotest_version: String by project
val wiremock_version: String by project
val prometheus_version: String by project

val nonLocalEnv: String? = System.getenv("DEV")

plugins {
    application
    kotlin("jvm") version "1.4.32"
    kotlin("plugin.serialization") version "1.4.32"
    id("com.adarshr.test-logger") version "3.0.0"
    id("jacoco")
    id("idea")
    id("io.gitlab.arturbosch.detekt") version "1.15.0"
    id("org.jlleitschuh.gradle.ktlint") version "9.4.1"
    id("org.sonarqube") version "3.2.0"
}

detekt {
    autoCorrect = true
}

group = "com.duberton"
version = "0.0.1-SNAPSHOT"

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-metrics-micrometer:$ktor_version")
    implementation("io.ktor:ktor-jackson:$ktor_version")
    implementation("io.ktor:ktor-auth:$ktor_version")
    implementation("io.ktor:ktor-client-okhttp:$ktor_version")
    implementation("io.ktor:ktor-auth-jwt:$ktor_version")
    implementation("org.koin:koin-ktor:$koin_version")
    implementation("org.koin:koin-core:$koin_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlix_serialization_version")
    implementation("it.skrape:skrapeit-http-fetcher:$skrapeit_version")
    implementation("it.skrape:skrapeit-html-parser:$skrapeit_version")
    implementation("org.redisson:redisson:$redisson_version")
    implementation("org.litote.kmongo:kmongo:$kmongo_version")
    implementation("org.litote.kmongo:kmongo-async:$kmongo_version")
    implementation("org.litote.kmongo:kmongo-coroutine:$kmongo_version")
    implementation("org.litote.kmongo:kmongo-id:$kmongo_version")
    implementation("software.amazon.awssdk:ses:2.17.4")
    implementation("org.quartz-scheduler:quartz:$quartz_version")
    implementation("io.micrometer:micrometer-registry-prometheus:$prometheus_version")
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:$detekt_version")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("org.jeasy:easy-random-core:$easy_random_version")
    testImplementation("io.mockk:mockk:$mockk_version")
    testImplementation("org.koin:koin-test:$koin_version")
    testImplementation("io.kotest:kotest-assertions-core:$kotest_version")
    testImplementation("io.kotest:kotest-assertions-ktor:$kotest_version")
    testImplementation("com.github.tomakehurst:wiremock-standalone:$wiremock_version") {
        exclude(group = "junit")
    }
}

kotlin.sourceSets["main"].kotlin.srcDirs("src")
kotlin.sourceSets["test"].kotlin.srcDirs("test")

sourceSets["main"].resources.srcDirs("resources")
sourceSets["test"].resources.srcDirs("testresources")

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

tasks.test {
    filter {
        if (project.hasProperty("excludeItTests")) {
            excludeTestsMatching("*ItTest")
        }
    }
    useJUnit()
    finalizedBy(tasks.jacocoTestReport)
}

jacoco {
    toolVersion = jacoco_version
}

tasks.jacocoTestReport {
    reports {
        xml.isEnabled = true
        csv.isEnabled = true
        html.isEnabled = true
    }
}

testlogger {
    theme = ThemeType.STANDARD
    slowThreshold = 5000
}