# Private Docker Registry 오픈소스인 Habor에 이미지 업로드하기

Docker hub에 private image를 올리는 것은 제한이 있기 때문에, 오픈소스인 Harbor로 내부망에서 사용할 수 있는 private docker registry를 구성하였다.

Docker hub에서 Image를 pull하는 경우 6시간 동안 최대 100회(로그인한 경우 200회)의 횟수 제한이 있기 때문에, 사내에서 Docker hub에서 직접 이미지를 Pull 하고 있었다면 제한 문제가 종종 발생하기도 한다. 

그렇기 때문에 JDK와 같은 베이스 이미지를 Habor에 미리 올려놓고 사용하면 제한없이 다운로드할 수 있는 장점도 있다.

## 1. JIB 컨테이너로 JAVA 기반 이미지 빌드 및 배포하기
구글에서 제공하는 오픈소스인 [JIB 컨테이너](https://github.com/GoogleContainerTools/jib) 를 사용하면 이미지를 DockerFile 작성 없이 쉽게 배포할 수 있다.

* build.gradle.kts
    ```kotlin
    plugins {
        id("com.google.cloud.tools.jib") version "3.4.0"
    }
    
    jib {
        from {
            image = "harbor.example.com/archive/eclipse-temurin:17-jre-alpine"
            auth {
                username = "username"
                password = "password"
            }
        }
        to {
            image = "harboar.example.com/test/test-image"
            tags = setOf("latest")
            auth {
                username = "username"
                password = "password"
            }
        }
        container {
            jvmFlags = listOf("-Xms512")
            ports = listOf("8080")
        }
    }
    ```
Gradle 환경에서는 위와 같은 예시로 작성할 수 있으며 `./gradlew jib` 명령 실행시 이미지를 빌드하고 이미지 저장소에 배포할 수 있다. 
```bash
./gradlew jib \
  -Djib.from.auth.username="myuser" \
  -Djib.from.auth.password="password"
```
또 jvm 옵션으로도 추가적인 jib 속성을 정의 할 수도 있다.

## 2. Github Action Job 예시
```yml
on:
  workflow_dispatch:
name: Harbor에 도커 이미지 배포하기
jobs:
  build:
    name: push-image-on-harbor
    runs-on: self-hosted
    steps:
      - name: Checkout
        uses: actions/checkout@v4
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: 17
          distribution: temurin
      - name: Get current time
        uses: josStorer/get-current-time@v2
        id: current-time
        with:
          format: yyyyMMDDHHmmss
          utcOffset: "+09:00"
      - name: Get deploy version
        run: echo "deployment-version=${{ steps.current-time.outputs.formattedTime }}" >> $GITHUB_ENV
      - name: Grant execute permission for gradlew
        run: chmod +x gradlew
      - name: Build and Deploy api server image
        uses: gradle/gradle-build-action@v2
        with:
          gradle-version: wrapper
          cache-read-only: true
          arguments: |
            :${{ github.event.inputs.module }}:jib
            -DsendCredentialsOverHttp=true
            -Djib.from.auth.username=${{ secrets.HARBOR_USERNAME }}
            -Djib.from.auth.password=${{ secrets.HARBOR_PASSWORD }}
            -Djib.to.image=harboar.example.com/test/test-image
            -Djib.to.tags=${{ env.deployment-version }},latest
            -Djib.to.auth.username=${{ secrets.HARBOR_USERNAME }}
            -Djib.to.auth.password=${{ secrets.HARBOR_PASSWORD }}
```

## 3. 실제 Habor에 올라간 도커 이미지 결과
![스크린샷 2023-10-11 오후 10 08 51](https://github.com/yellowsunn/home-infra-playground/assets/43487002/347f220e-0347-40d3-bbbe-4c9e1a2d485a)
![스크린샷 2023-10-11 오후 10 08 59](https://github.com/yellowsunn/home-infra-playground/assets/43487002/5e7acb27-ab4a-446d-b6a1-ac002c456dfb)
현재 위치한 `private-docker-registry-example` 모듈에 있는 자바 어플리케이션을 이미지로 올린 결과다.

