
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jooq.meta.jaxb.ForcedType
import org.jooq.meta.jaxb.Logging
import org.jooq.meta.jaxb.Property

plugins {
	id("org.springframework.boot") version "3.0.0-SNAPSHOT"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
	groovy
	id("nu.studer.jooq") version "7.1.1"
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

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
	implementation("org.jooq:jooq")

	jooqGenerator("org.postgresql:postgresql:42.3.2")

	runtimeOnly("com.h2database:h2")
	runtimeOnly("io.r2dbc:r2dbc-h2")
	implementation("io.r2dbc:r2dbc-postgresql:0.8.12.RELEASE")
	implementation("org.postgresql:postgresql")

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

jooq {
	configurations {
		create("main") {  // name of the jOOQ configuration
			generateSchemaSourceOnCompilation.set(true)  // default (can be omitted)

			jooqConfiguration.apply {
				logging = Logging.WARN
				jdbc.apply {
					driver = "org.postgresql.Driver"
					url = "jdbc:postgresql://localhost:5432/sample"
					user = "some_user"
					password = "some_secret"
					properties.add(Property().apply {
						key = "ssl"
						value = "true"
					})
				}
				generator.apply {
					name = "org.jooq.codegen.DefaultGenerator"
					database.apply {
						name = "org.jooq.meta.postgres.PostgresDatabase"
						inputSchema = "public"
						forcedTypes.addAll(listOf(
							ForcedType().apply {
								name = "varchar"
								includeExpression = ".*"
								includeTypes = "JSONB?"
							},
							ForcedType().apply {
								name = "varchar"
								includeExpression = ".*"
								includeTypes = "INET"
							}
						))
					}
					generate.apply {
						isDeprecated = false
						isRecords = true
						isImmutablePojos = true
						isFluentSetters = true
					}
					target.apply {
						packageName = "nu.studer.sample"
						directory = "build/generated-src/jooq/main"  // default (can be omitted)
					}
					strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
				}
			}
		}
	}
}

ext["jooq.version"] = jooq.version.get()

tasks.named<nu.studer.gradle.jooq.JooqGenerate>("generateJooq") { allInputsDeclared.set(true) }
