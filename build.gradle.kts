plugins {
    kotlin("jvm") version "1.8.21"
    application
    wrapper
}

group = "me.user"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnit()
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("BoardImpl")
}

tasks {
    "wrapper"(Wrapper::class) {
        gradleVersion = "8.0"
        distributionType = Wrapper.DistributionType.BIN
    }
}