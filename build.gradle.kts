plugins {
    kotlin("jvm") version "2.0.0"
}

group = "org.example"
version = "1.0"

repositories {
    mavenCentral()
}


dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.0.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")

    implementation("org.xerial:sqlite-jdbc:3.34.0")
    implementation("org.jetbrains.kotlinx:kotlinx-cli-jvm:0.3.5")
}


tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(22)
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "MainKt"
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
