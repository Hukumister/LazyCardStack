import java.util.Properties

plugins {
    `maven-publish`
    signing
}

// Stub secrets to let the project sync and build without the publication values set up
ext["signing.keyId"] = null
ext["signing.password"] = null
ext["signing.secretKeyRingFile"] = null
ext["ossrhUsername"] = null
ext["ossrhPassword"] = null

// Grabbing secrets from local.properties file or from environment variables, which could be used on CI
val secretPropsFile = project.rootProject.file("local.properties")
if (secretPropsFile.exists()) {
    secretPropsFile.reader()
        .use { Properties().apply { load(it) } }
        .onEach { (name, value) -> ext[name.toString()] = value }
} else {
    ext["signing.keyId"] = System.getenv("SIGNING_KEY_ID")
    ext["signing.password"] = System.getenv("SIGNING_PASSWORD")
    ext["signing.secretKeyRingFile"] = System.getenv("SIGNING_SECRET_KEY_RING_FILE")
    ext["ossrhUsername"] = System.getenv("OSSRH_USERNAME")
    ext["ossrhPassword"] = System.getenv("OSSRH_PASSWORD")
}

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

fun getExtraString(name: String) = ext[name]?.toString()

publishing {
    // Configure maven central repository
    repositories {
        maven {
            name = "Maven"
            setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = getExtraString("ossrhUsername")
                password = getExtraString("ossrhPassword")
            }
        }
    }

    publications.withType<MavenPublication>().configureEach {

        // Provide artifacts information requited by Maven Central
        pom {
            name.set("LazyCardStack")
            description.set("Jetpack Compose library that allows to implement tinder like card stack.")
            url.set("https://github.com/Hukumister/LazyCardStack")

            licenses {
                license {
                    name.set("MIT")
                    url.set("https://opensource.org/licenses/MIT")
                }
            }
            developers {
                developer {
                    id.set("zaltsman.nikita")
                    name.set("Zaltsman Nikita")
                    email.set("nikitazaltsman@gmail.com")
                }
            }
            scm {
                developerConnection.set("scm:git@github.com:Hukumister/LazyCardStack.git")
                connection.set("scm:git:git://github.com/Hukumister/LazyCardStack")
                url.set("https://github.com/Hukumister/LazyCardStack")
            }
        }
    }
}

// Signing artifacts. Signing.* extra properties values will be used
signing {
    sign(publishing.publications)
}
