import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// versioning is moved to settings.gradle.kts
plugins {
    id("io.spring.dependency-management")
    id("org.springframework.boot")
    id("org.asciidoctor.jvm.convert")
    id("io.gitlab.arturbosch.detekt")
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
}

repositories {
    mavenCentral()
}

group = "griffio.kollchap"
version = "0.0.1-SNAPSHOT"

detekt {
    val detektPluginVersion: String by project
    toolVersion = detektPluginVersion
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
    implementation("org.hibernate.validator:hibernate-validator")
    // configures asciidoctor plugin with restdocs settings
    asciidoctorExt("org.springframework.restdocs:spring-restdocs-asciidoctor")
    runtimeOnly("com.h2database:h2")
    // install hal client http://localhost:8080/webjars/hal-explorer/1.1.0/index.html#uri=/
    runtimeOnly("org.webjars:hal-explorer:1.1.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
}

kotlin {
    // https://blog.jetbrains.com/kotlin/2021/11/gradle-jvm-toolchain-support-in-the-kotlin-plugin/
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of("17"))
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
