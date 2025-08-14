plugins {
    id("java")
}

group = "de.greensurvivors"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.jetbrains", "annotations", "26.0.2")
    implementation("com.google.code.gson", "gson", "2.13.1")
    implementation("org.bouncycastle", "bcprov-jdk18on", "1.81")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}