# Guider

**Guider**은 플레이어가 서버 내에서 네비게이션(길 찾기) 기능을 제공해주는 페이퍼 플러그인입니다. Java와 Kotlin으로 개발되어 안정적이고 뛰어난 성능을 자랑합니다.

[![Minecraft Version](https://img.shields.io/badge/Minecraft-1.21.x%20~%201.21.x-brightgreen.svg)](https://www.minecraft.net/)
[![License](https://img.shields.io/badge/License-MIT-lightgrey.svg)](https://opensource.org/licenses/MIT)

## ✨ 주요 기능

* **뛰어난 성능**: 다익스트라 알고리즘을 사용하여 빠른 길 찾기를 제공합니다.
* **다양한 포인트 설정**: 좌표, 포인트의 종류, 그 포인트에서 이동할 수 있는 곳 등등 다양한 설정을 제공합니다.
* **커스텀 가능한 안내 방식**: 서버 관리자는 안내 화살표와 포인트 등의 세부 사항을 자유롭게 설정할 수 있습니다.
* **다중 월드 지원**: 여러 월드를 넘나드는 경로 탐색 및 안내가 가능합니다.
* **다중 서버 지원**: 여러 서버 간의 경로 탐색을 지원합니다.

## 💾 설치 방법

1.  [릴리즈 페이지](https://github.com/bindglam/Guider/releases)에서 플러그인의 최신 버전을 다운로드합니다.
2.  다운로드한 `.jar` 파일을 서버의 `plugins` 폴더에 넣어주세요.
3.  서버를 재시작하여 플러그인을 활성화합니다.

## 🎮 사용법

플러그인의 주요 기능은 `/guider` 명령어를 통해 사용할 수 있습니다.

### 🔑 명령어 및 권한

| 명령어                             | 설명                          | 권한                           |
|:--------------------------------|:----------------------------|:-----------------------------|
| `/guider navi add <플레이어> <목적지>` | 해당 플레이어에게 길 찾기를 시작합니다.      | `guider.command.navi.add`    |
| `/guider navi remove <플레이어>`    | 해당 플레이어의 진행 중인 길 찾기를 취소합니다. | `guider.command.navi.remove` |
| `/guider reload`                | 플러그인 설정을 다시 불러옵니다.          | `guider.command.reload`      |

## ⚙️ 설정 (config.yml)

`plugins/Guider/config.yml` 파일에서 플러그인의 세부 동작을 설정할 수 있습니다.

## 🔗 의존성

*   [BetterModel](https://github.com/toxicity188/BetterModel) (선택 사항): 커스텀 모델을 적용하여 더욱 멋진 비주얼을 만들 수 있습니다.

## 👨‍💻 개발자를 위한 정보 (API)

다른 플러그인에서 **Guider**의 기능을 사용하고 싶다면 API를 활용할 수 있습니다.

**Gradle (Kotlin DSL)**
```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("com.github.bindglam:Guider:VERSION")
}
```

## 🛠️ 소스 코드로 빌드하기

이 프로젝트는 Java와 Kotlin으로 작성되었으며, Gradle을 사용하여 빌드할 수 있습니다.

1.  저장소를 복제합니다: `git clone https://github.com/bindglam/Guider.git`
2.  프로젝트 디렉토리로 이동합니다: `cd Guider`
3.  다음 명령어를 실행하여 빌드합니다:
    *   **Windows**: `gradlew build`
    *   **Linux/macOS**: `./gradlew build`
4.  빌드가 완료되면 `build/libs/` 디렉토리에서 `.jar` 파일을 찾을 수 있습니다.

## 📜 라이선스

이 플러그인은 [MIT 라이선스](https://opensource.org/licenses/MIT)에 따라 배포됩니다.

## 🤝 기여하기

버그 리포트, 기능 제안 등 모든 종류의 기여를 환영합니다! [이슈 트래커](https://github.com/bindglam/Guider/issues)에 이슈를 남겨주세요.

## 📞 문의

플러그인 관련 문의나 지원이 필요한 경우, [디스코드](https://discord.gg/your-discord) 서버에 방문해주세요.