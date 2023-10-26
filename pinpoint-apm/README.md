# Pinpoint-APM

오픈소스 [Pinpoint APM](https://github.com/pinpoint-apm/pinpoint) 을 통한 모니터링 시스템 구축

## 0. 실제 구축한 Pinpoint APM 예시
![스크린샷 2023-10-26 오후 9 47 39](https://github.com/yellowsunn/home-infra-playground/assets/43487002/c224f332-a6bb-43b8-a1af-b658ef85dbb0)

![스크린샷 2023-10-26 오후 9 48 11](https://github.com/yellowsunn/home-infra-playground/assets/43487002/b125d7b8-f67c-4d78-a457-cae1530823a8)


## 1. Pinpoint APM 서버 구축하기
Pinpoint APM 서버를 구축하기 위해서는 다음과 같은 요소들이 구성되어야 한다.
* HBase (for storage)
* Pinpoint Collector (deployed on a web container)
* Pinpoint Web (deployed on a web container)
* Pinpoint Agent (attached to a java application for profiling)
  
![4-1](https://github.com/yellowsunn/home-infra-playground/assets/43487002/eef1cdae-0037-4c1c-a513-0c054a2833bf)


## 2. 쿠버네티스 Pod에 Pinpoint Agent 연동하기
자바 어플리케이션을 쿠버네티스 Pod 에 띄우기 위해서 다음과 같은 방법을 진행하였다.
1. Pinpoint Agent는 [Pinpoint APM Releases](https://github.com/pinpoint-apm/pinpoint/releases) 페이지에서 다운로드 받는다.
2. Dockerfile 로 pinpint-agent 파일들이 포함된 JDK를 빌드하여 개인 Docker Registry (Harbor) 저장소에 배포한다.
   * ./Dockerfile 참고
3. Poinpoint-Agent 파일이 포함된 JDK를 Base-image로 하여 Java 컨테이너를 구성한다.
4. 쿠버네티스 Pod 설정에 아래 예시처럼 JAVA_TOOL_OPTION 옵션을 추가한다.
    ```yml
    spec:
      containers:
      - name: spring-boot-example
        image: harbor.yellowsunn.com:80/test/spring-boot-example
        env:
        - name: JAVA_TOOL_OPTIONS
          value: >-
            -javaagent:/pinpoint-agent/pinpoint-bootstrap.jar
            -Dpinpoint.applicationName=SPRING-BOOT-EXAMPLE
            -Dpinpoint.agentId=springbootexample
            -Dpinpoint.profiler.profiles.active=release
    ```
5. Pod 구동시 Pinpoint Agent가 연동되었는지 확인할 수 있다.

    [ArgoCD 로 확인한 예시]

    ![스크린샷 2023-10-26 오후 10 09 52](https://github.com/yellowsunn/home-infra-playground/assets/43487002/a89f0ddb-c9fc-4cb6-aea6-e1d634174ee3)
