# SonarQube Pr Decoration을 사용하여 코드 품질 분석하기

SonarQube 유료 버전에는 깃허브 PR, 깃랩 MR 등에 대해 정적 분석 코멘트를 남겨주는 **Pull Request decoration** 기능이 있다.

하지만 무료 버전에도 [한 플러그인](https://github.com/mc1arke/sonarqube-community-branch-plugin)을 설치하면 1개 브랜치에 한해서 동일한 기능을 사용할 수 있다.

### 적용 결과

실제 해당 프로젝트에 적용된 결과를 보면 SonarQube 의 PR Decoration 결과가 [Github APP](https://github.com/apps/y-sonarbot) 에 의해 실행되어 코멘트를 남겨주는 것을 확인할 수 있다.

* 성공: [[NONE-CODE-SMELL] 코드 스멜이 없는 코드 추가 #8](https://github.com/yellowsunn/home-infra-playground/pull/8)   
* 실패: [[CODE-SMELL] 코드 스멜 로직 추가 #7](https://github.com/yellowsunn/home-infra-playground/pull/7)

이 기능을 활용하여 SonarQube의 분석결과가 기준치를 통과하지 않으면 실패 코멘트와 함께 브랜치를 머지하지 못하도록 강제하는 설정을 연계할 수도 있다.

<img width="948" alt="스크린샷 2023-10-09 오후 6 58 25" src="https://github.com/yellowsunn/home-infra-playground/assets/43487002/cc8f05f7-7e85-423c-8656-38cabc158df4">

<img width="939" alt="스크린샷 2023-10-09 오후 6 52 16" src="https://github.com/yellowsunn/home-infra-playground/assets/43487002/201ce6d8-990c-424e-9a4b-53e6c086c86a">

### Github Action 워크플로우
PR을 올리거나 수정이 발생하면 GithubAction에서 빌드 및 SonarQube PR Decoration 기능을 수행하는 Job을 실행시킨다.
1. 프로젝트 Checkout
2. JDK 설치
3. (SonarQube/Gradle 데이터를 캐시하여 불필요하연 패키지 재설치 방지)
4. Gradle 빌드 및 SonarQube에 코드 분석 요청을 보낸다.
   * 4.1. SonarQube에서는 코드를 분석한다.
   * 4.2. PR Decoration 결과를 연동한 Github App에 알려준다.
   * 4.3. GitHub App이 해당 PR에 분석한 결과 코멘트를 남긴다.
5. SonarQube Quality Gate에서 최소 품질 기준을 충족 하였는지 확인한다. (만족하지 못하면 실패)
```yml
on:
  pull_request:
    types: [opened, synchronize, reopened]
    branches:
      - main
name: SonarQube pull request decoration
jobs:
  build:
    name: Build
    runs-on: ubuntu-latest
    steps:
      - name: Checkout
        uses: actions/checkout@v3
        with:
          fetch-depth: 0  # Shallow clones should be disabled for a better relevancy of analysis
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'
      - name: Cache SonarQube packages
        uses: actions/cache@v3
        with:
          path: ~/.sonar/cache
          key: ${{ runner.os }}-sonar
          restore-keys: ${{ runner.os }}-sonar
      - name: Cache Gradle packages
        uses: actions/cache@v3
        with:
          path: ~/.gradle/caches
          key: ${{ runner.os }}-gradle-${{ hashFiles('**/*.gradle') }}
          restore-keys: ${{ runner.os }}-gradle
      - name: Build and analyze
        env:
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}  # Needed to get PR information, if any
          SONAR_TOKEN: ${{ secrets.SONAR_TOKEN }}
          SONAR_HOST_URL: ${{ secrets.SONAR_HOST_URL }}
        run: ./gradlew build sonar --info

      # Check the Quality Gate status.
      - name: SonarQube Quality Gate check
        id: sonarqube-quality-gate-check
        uses: sonarsource/sonarqube-quality-gate-action@master
        with:
          scanMetadataReportFile: build/sonar/report-task.txt
        # Force to fail step after specific time.
        timeout-minutes: 5
        env:
          SONAR_TOKEN: ${{ secrets.SONAR_TOKEN }}
          SONAR_HOST_URL: ${{ secrets.SONAR_HOST_URL }} #OPTIONAL

      # Optionally you can use the output from the Quality Gate in another step.
      # The possible outputs of the `quality-gate-status` variable are `PASSED`, `WARN` or `FAILED`.
      - name: "Example show SonarQube Quality Gate Status value"
        run: echo "The Quality Gate status is ${{ steps.sonarqube-quality-gate-check.outputs.quality-gate-status }}"
```
