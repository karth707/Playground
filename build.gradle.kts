plugins {
    id("java")
    id("com.diffplug.spotless") version "6.22.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

spotless {
    java {
        googleJavaFormat("1.17.0") // Use Google Java Format
        target("src/**/*.java")
    }
}

tasks.test {
    useJUnitPlatform()
}