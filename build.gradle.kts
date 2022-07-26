import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
plugins {
    val kotlinPluginVersion = "1.7.10"
    id("org.springframework.boot") version "2.7.1"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("org.asciidoctor.jvm.convert") version "3.3.2"
    id("io.gitlab.arturbosch.detekt").version("1.21.0")
    kotlin("jvm") version kotlinPluginVersion
    kotlin("plugin.spring") version kotlinPluginVersion
    kotlin("plugin.jpa") version kotlinPluginVersion
}
repositories {
    mavenCentral()
}
group = "griffio.kollchap"
version = "0.0.1-SNAPSHOT"

detekt {
    toolVersion = "1.21.0"
    buildUponDefaultConfig = true
    config = files("${projectDir}/detekt.yml")
}
// custom classpath config as per https://github.com/spring-projects/spring-restdocs/blob/main/samples/rest-notes-spring-hateoas/build.gradle
val asciidoctorExt: Configuration by configurations.creating

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-hateoas")
    implementation("org.springframework.boot:spring-boot-starter-data-rest")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.hibernate.validator:hibernate-validator")
    // configures asciidoctor plugin with restdocs settings
    asciidoctorExt("org.springframework.restdocs:spring-restdocs-asciidoctor")
    runtimeOnly("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
}

kotlin {
    // https://blog.jetbrains.com/kotlin/2021/11/gradle-jvm-toolchain-support-in-the-kotlin-plugin/
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of("11")) // 1.7.0 kotlin plugin
    }
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions.allWarningsAsErrors = true

tasks.withType<Test> {
    useJUnitPlatform()
}

// https://github.com/spring-io/initializr/issues/922
val snippetsDir by extra { file("build/generated-snippets") }

tasks.test {
    outputs.dir(snippetsDir)
}
// https://github.com/spring-io/initializr/issues/922
tasks.asciidoctor {
    configurations(asciidoctorExt.name) // set as per docs
    inputs.dir(snippetsDir)
    dependsOn(tasks.test)
}
// copy index.html 
// https://docs.spring.io/spring-boot/docs/2.1.x/reference/html/boot-features-developing-web-applications.html#boot-features-spring-mvc-static-content
tasks.bootJar {
    dependsOn(tasks.asciidoctor)
    from(tasks.asciidoctor.get().outputDir) {
        into("static/docs")
    }
}
