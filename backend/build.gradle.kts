val logback_version: String by project
val ktor_version: String by project
val kotlin_version: String by project
val skrapeit_version: String by project
val koin_version: String by project
val mongo_version: String by project
val kotlix_serialization_version: String by project
val redisson_version: String by project
val quartz_version: String by project

val nonLocalEnv: String? = System.getenv("DEV")

plugins {
    application
    kotlin("jvm") version "1.4.32"
    kotlin("plugin.serialization") version "1.4.32"
    id("jacoco")
    id("idea")
    id("io.gitlab.arturbosch.detekt") version "1.15.0"
    id("org.jlleitschuh.gradle.ktlint") version "9.4.1"
    id("org.sonarqube") version "3.2.0"
}

detekt {
    autoCorrect = true
}

jacoco {
    toolVersion = "0.8.7"
}

val jacocoTestReport = tasks.named("jacocoTestReport")
val excludeList = emptyList<String>()

val test by tasks.getting(Test::class) {
    configure<JacocoTaskExtension> {
        isEnabled = true
        excludes
    }
    finalizedBy(jacocoTestReport)
    useJUnitPlatform { }
}

tasks.jacocoTestReport {
    classDirectories.setFrom(
        sourceSets.main.get().output.asFileTree.matching {
            exclude(excludeList)
        }
    )

    reports {
        html.isEnabled = true
        html.destination = File("$buildDir/reports/jacoco/report.html")
        xml.isEnabled = true
        xml.destination = File("$buildDir/reports/jacoco/report.xml")
    }
}

tasks.jacocoTestCoverageVerification {
    afterEvaluate {
        classDirectories.setFrom(
            files(
                classDirectories.files.map {
                    fileTree(it).apply {
                        exclude(excludeList)
                    }
                }
            )
        )
    }
    dependsOn(jacocoTestReport)
    violationRules {
        rule {
            limit {
                minimum = BigDecimal("0.8")
            }
        }
    }
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
    implementation("io.ktor:ktor-jackson:$ktor_version")
    implementation("io.ktor:ktor-auth:$ktor_version")
    implementation("io.ktor:ktor-client-okhttp:$ktor_version")
    implementation("io.ktor:ktor-auth-jwt:$ktor_version")
    implementation("org.koin:koin-ktor:$koin_version")
    implementation("org.koin:koin-core:$koin_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlix_serialization_version")
    implementation("it.skrape:skrapeit:$skrapeit_version")
    implementation("org.redisson:redisson:$redisson_version")
    implementation("org.mongodb:mongo-java-driver:$mongo_version")
    implementation("software.amazon.awssdk:ses:2.17.4")
    implementation("org.quartz-scheduler:quartz:$quartz_version")
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.15.0")
    implementation("org.jacoco:org.jacoco.core:0.8.7")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("io.mockk:mockk:1.12.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.0-M1")

}

kotlin.sourceSets["main"].kotlin.srcDirs("src")
kotlin.sourceSets["test"].kotlin.srcDirs("test")

sourceSets["main"].resources.srcDirs("resources")
sourceSets["test"].resources.srcDirs("testresources")

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

tasks.withType<Test> {
    useJUnitPlatform()
}