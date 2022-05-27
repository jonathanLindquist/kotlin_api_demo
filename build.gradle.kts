
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
  id("org.springframework.boot") version "3.0.0-SNAPSHOT"
  id("io.spring.dependency-management") version "1.0.11.RELEASE"
  kotlin("jvm") version "1.6.21"
  kotlin("plugin.spring") version "1.6.21"
  groovy
}

group = "com.lindquist"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
  mavenLocal()
  mavenCentral()
  maven { url = uri("https://repo.spring.io/milestone") }
  maven { url = uri("https://repo.spring.io/snapshot") }
}

val localH2Implementation: Configuration by configurations.creating {
	extendsFrom(configurations["implementation"])
	exclude(group = "org.postgresql", module = "postgresql")
}

val localH2RuntimeOnly: Configuration by configurations.creating {
	extendsFrom(configurations["runtimeOnly"])
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
  runtimeOnly("io.r2dbc:r2dbc-postgresql")
  runtimeOnly("org.postgresql:postgresql")
  localH2RuntimeOnly("com.h2database:h2")
  localH2RuntimeOnly("io.r2dbc:r2dbc-h2")

  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")

  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

  testImplementation("org.spockframework:spock-core:2.1-groovy-3.0")
  testImplementation("org.codehaus.groovy:groovy-all:3.0.10")
  testImplementation("io.projectreactor:reactor-test")
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs = listOf("-Xjsr305=strict")
    jvmTarget = "17"
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
  testLogging {
    events(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
  }
}

sourceSets {
  create("localH2") {
    compileClasspath += sourceSets.main.get().output
    runtimeClasspath += sourceSets.main.get().output
  }
}

val newTask = task<BootRun>("localH2") {
  mainClass.set("com.lindquist.api.ApiApplication")
  classpath = sourceSets["localH2"].runtimeClasspath
}
