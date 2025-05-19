import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.targets.native.tasks.PodGenTask

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinCocoapods)
    alias(libs.plugins.mavenPublish)
}

group = "uk.co.lidbit"
version = "0.1.3"

kotlin {
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "PusherWebsocketKmp"
            isStatic = false
        }
    }

    cocoapods {
        version = "1.0"
        summary = "KMP integration with PusherSwift"
        homepage = "https://github.com/samliddleg/pusher-websocket-kmp"
        ios.deploymentTarget = "13.0"
        pod("PusherSwift") {
            version = "~> 10.1.5"
            extraOpts += listOf("-compiler-option", "-fmodules")
        }
        framework {
            baseName = "PusherWebsocketKmp"
            isStatic = false
        }
    }

    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
    }

    jvmToolchain(17)
    
    sourceSets {
        jvmMain.dependencies {
            implementation("com.pusher:pusher-java-client:2.4.4")
        }
    }
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()

    coordinates(group.toString(), "pusher-websocket-kmp", version.toString())

    pom {
        name = "KMP integration of Pusher"
        description = "Android and desktop through pusher-websocket-java, iOS through pusher-websocket-swift."
        inceptionYear = "2025"
        url = "https://github.com/samliddleg/pusher-websocket-kmp/"
        licenses {
            license {
                name = "MIT License"
                url = "https://opensource.org/licenses/MIT"
                distribution = "https://opensource.org/licenses/MIT"
            }
        }
        developers {
            developer {
                id = "samliddleg"
                name = "Sam Liddle"
                url = "https://github.com/samliddleg/"
            }
        }
        scm {
            url = "https://github.com/samliddleg/pusher-websocket-kmp/"
            connection = "scm:git:git://github.com/samliddleg/pusher-websocket-kmp.git"
            developerConnection = "scm:git:ssh://git@github.com/samliddleg/pusher-websocket-kmp.git"
        }
    }
}

tasks.withType<PodGenTask>().configureEach {
    doLast {
        podfile.get().appendText("\nENV['SWIFT_VERSION'] = '5'")
    }
}
