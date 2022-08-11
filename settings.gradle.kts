rootProject.name = "kollchap"
// Plugins can be loaded here in settings to allow versions to be loaded from gradle.properties
// https://docs.gradle.org/current/userguide/plugins.html#sec:plugin_management
pluginManagement {
    // load from gradle.properties
    val kotlinPluginVersion: String by settings
    val springDependencyPluginVersion: String by settings
    val springBootPluginVersion: String by settings
    val asciiDocPluginVersion: String by settings
    val asciiDocGemsVersion: String by settings
    val detektPluginVersion: String by settings

    repositories {
        mavenCentral()
        gradlePluginPortal() // required to load plugins in settings
    }

    plugins {
        kotlin("jvm") version kotlinPluginVersion
        kotlin("plugin.spring") version kotlinPluginVersion
        kotlin("plugin.jpa") version kotlinPluginVersion
        id("io.spring.dependency-management") version springDependencyPluginVersion
        id("org.springframework.boot") version springBootPluginVersion
        id("org.asciidoctor.jvm.convert") version asciiDocPluginVersion
        id("org.asciidoctor.jvm.gems") version asciiDocGemsVersion
        id("io.gitlab.arturbosch.detekt") version detektPluginVersion
    }
}
