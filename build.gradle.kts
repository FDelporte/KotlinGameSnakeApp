plugins {
    kotlin("jvm") version "2.2.0"
}
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.13.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.1.20")
    }
}
//dependencies {
//    implementation(kotlin("stdlib-jdk8"))
//}
repositories {
    mavenCentral()
}
//kotlin {
//    jvmToolchain(8)
//}