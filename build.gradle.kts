plugins {
    kotlin("jvm") version "1.7.10"
}

group = "com.github.lamba92"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    target {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }
    sourceSets {
        all {
            languageSettings {
                optIn("kotlinx.coroutines.FlowPreview")
            }
        }
    }
}

dependencies {
    implementation("io.reactivex.rxjava3:rxjava:3.1.5")
    val coroutinesVersion = "1.6.4"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-rx3:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
}

tasks.test {
    useJUnitPlatform()
}