plugins {
    java
    id("org.sonarqube") version "4.3.1.3277"
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "org.sonarqube")

    repositories {
        mavenCentral()
    }

    java.sourceCompatibility = JavaVersion.VERSION_17

    sonar {
        properties {
            property("sonar.projectKey", "github.pr.project")
        }
        isSkipProject = false
    }
}
