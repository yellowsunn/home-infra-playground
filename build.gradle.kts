plugins {
    java
    id("org.sonarqube") version "4.3.1.3277"
    id("jacoco")
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "org.sonarqube")
    apply(plugin = "jacoco")

    repositories {
        mavenCentral()
    }

    java.sourceCompatibility = JavaVersion.VERSION_17

    sonar {
        properties {
            property("sonar.java.binaries", "$buildDir/classes")
            property("sonar.coverage.jacoco.xmlReportPaths", "$buildDir/reports/jacoco.xml")
        }
    }

    jacoco {
        toolVersion = "0.8.9"
    }
    tasks.jacocoTestReport {
        dependsOn(tasks.test)
        reports {
            html.required = true
            xml.required = true
            xml.outputLocation = file("$buildDir/reports/jacoco.xml")
        }
    }
    tasks.test {
        finalizedBy(tasks.jacocoTestReport)
    }
}

sonar {
    properties {
        val exclusions = listOf(
            "**/test/**",
            "**/*Application*.java",
            "**/*Application*.kt",
            "**/*Config*.java",
            "**/*Config*.kt",
        )
        property("sonar.projectKey", "yellowsunn_home-infra-playground_AYsO44GeSRfkzjl51dyo")
        property("sonar.sources", "src")
        property("sonar.sourceEncoding", "UTF-8")
        property("sonar.test.inclusions", "**/*Test.java, **/*Test.kt")
        property("sonar.exclusions", exclusions.joinToString(","))
        property("sonar.java.coveragePlugin", "jacoco")
    }
    isSkipProject = false
}
