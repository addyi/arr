import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.detekt)
}

group = "com.example"
version = "0.0.1"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.host.common)
    implementation(libs.ktor.server.netty)

    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)

    implementation(libs.h2)
    implementation(libs.postgresql)

    implementation(libs.ktor.serialization.server)

    implementation(libs.logback.classic)

    testImplementation(libs.kotlin.test.junit)
    testImplementation(libs.ktor.server.test.host)
}

configure<KtlintExtension> {
    version.set("1.5.0")

    android.set(false)
    outputToConsole.set(true)
    ignoreFailures.set(false)
    enableExperimentalRules.set(false)

    reporters { reporter(ReporterType.PLAIN_GROUP_BY_FILE) }

    verbose.set(false)
    debug.set(false)
}

detekt {
    source.setFrom(".") // The directories where detekt looks for source files.

    buildUponDefaultConfig = true
    config.setFrom("${rootProject.rootDir}/detekt.yml")

    parallel = true // Builds the AST in parallel.

    baseline = file("${rootProject.rootDir}/detekt-baseline.xml") // TODO get rid of baseline as fast as possible

    basePath = projectDir.absolutePath

    allRules = true // Turns on all the rules. Default: false
}

tasks.withType<Detekt>().configureEach {
    exclude("**/build/**")
}

tasks.withType<DetektCreateBaselineTask>().configureEach {
    exclude("**/build/**")
}
