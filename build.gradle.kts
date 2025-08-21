plugins {
    id("java")
    id("maven-publish")
}

val javaVersion = 21
group = "de.greensurvivors"
version = "1.0-SNAPSHOT"
description = "A nifty java api for https://pastefy.app"

java {
    // Configure the java toolchain. This allows gradle to auto-provision JDK 17 on systems that only have JDK 8 installed for example.
    toolchain.languageVersion.set(JavaLanguageVersion.of(javaVersion))
}

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

tasks {
    test {
        useJUnitPlatform()
    }
    compileJava {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything

        // Set the release flag. This configures what version bytecode the compiler will emit, as well as what JDK APIs are usable.
        // See https://openjdk.java.net/jeps/247 for more information.
        options.release.set(javaVersion)
    }
}

publishing {
    publications.create<MavenPublication>("PastefyAPI") {
        from(components["java"])
        pom {
            name.set("PastefyAPI")
            description.set("$description")
            url.set("https://github.com/GreenSurvivors/PastefyAPI")
            licenses {
                license {
                    name.set("GNU General Public License v3.0")
                    url.set("https://www.gnu.org/licenses/gpl-3.0.html")
                }
            }
            developers {
                developer {
                    name.set("GreenSurvivors Team")
                }
            }
        }
        repositories { // todo make this dynamic
            maven {
                name = "greensurvivorsMaven"
                url = uri("https://maven.greensurvivors.de/releases")
                credentials(PasswordCredentials::class)
                authentication {
                    create<BasicAuthentication>("basic")
                }
            }
        }
    }
}