plugins {
    id("org.springframework.boot") version "3.1.4"
    id("io.spring.dependency-management") version "1.1.3"
    id("com.google.cloud.tools.jib") version "3.4.0"
}

group = "com.yellowsunn"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jib {
    setAllowInsecureRegistries(true)
    from {
        image = "10.10.10.6:80/archive/eclipse-temurin:17-jre-alpine"
    }
    container {
        ports = listOf("8080")
    }
}
