# 이게뭐예요? 백엔드 레포지토리 입니다

## 서비스 소개
생활 속 헷갈리는 사용법과 도움이 되는 제품들을 소개하는 웹사이트입니다 <br>
[서비스 바로가기](https://whatisthis-fe.vercel.app/)

## 팀원 소개
| 윤영석 | 천성호 | 이정준 | 남성현 |
|:------:|:------:|:------:|:------:|
| [<img src="https://avatars.githubusercontent.com/u/149371593?v=4" alt="윤영석" width="150">](https://github.com/noonbbara) | [<img src="https://avatars.githubusercontent.com/u/70241022?v=4" alt="천성호" width="150">](https://github.com/c5ln)| [<img src="https://avatars.githubusercontent.com/u/202734335?v=4" alt="이정준" width="150">](https://github.com/ljj5616) | [<img src="https://avatars.githubusercontent.com/u/179928955?v=4" alt="남성" width="150">](https://github.com/nsh0919) |
| BE | BE | BE | BE |

## 기술 스택
| Category             | Stack                                                                                                                                                                                                                                                                       |
| -------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Framework            | ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
| Programming Language | ![Java](https://img.shields.io/badge/Java%2017-007396?style=for-the-badge&logo=java&logoColor=white) |
| Database   | ![Amazon RDS](https://img.shields.io/badge/Amazon%20RDS-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white) ![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white) ![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white)
| Vector DB       | Pinecone |
| Infrastructure       | ![AWS EC2](https://img.shields.io/badge/AWS%20EC2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white) ![AWS S3](https://img.shields.io/badge/AWS%20S3-569A31?style=for-the-badge&logo=amazons3&logoColor=white) ![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white) |
| API   | ![Google Gemini](https://img.shields.io/badge/google%20gemini-8E75B2?style=for-the-badge&logo=google%20gemini&logoColor=white)
| Authentication       | ![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white) |
| CI/CD                | ![GitHub Actions](https://img.shields.io/badge/GitHub%20Actions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white) |
| Version Control      | ![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white) ![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white) |
| Collaboration Tool     | ![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white) ![Notion](https://img.shields.io/badge/Notion-%23000000.svg?style=for-the-badge&logo=notion&logoColor=white)|

## 아키텍처 다이어그램
<img src="https://github.com/user-attachments/assets/f1f0d9f6-82af-41e0-831f-8ec0296dd6a0" width="580" />

## API 문서 (Swagger)
[Swagger 바로가기](https://api.whatisthis.co.kr/swagger-ui/index.html)

## Commit Convention
| Tag        | 설명                      |
|------------|---------------------------|
| [Feat]     | 새로운 기능 추가           |
| [Fix]      | 버그 수정                  |
| [Docs]     | 문서 추가, 수정, 삭제      |
| [Test]     | 테스트 코드 추가, 수정, 삭제 |
| [Style]    | 코드 형식 변경             |
| [Refactor] | 코드 리팩토링              |
| [Perf]     | 성능 개선                  |
| [Build]    | 빌드 관련 변경사항         |
| [Ci]       | CI 관련 설정 수정          |
| [Chore]    | 기타 변경사항              |
| [Revert]    | git revert              |

## Branch
github-flow<br>
main : 배포되는 브랜치 입니다<br>
feat : 기능 개발을 위한 브랜치 입니다<br>
fix : 버그나 코드 수정을 위한 브랜치 입니다

## 프로젝트 구조
```
whatisthis_BE
├─ .github/
│  ├─ ISSUE_TEMPLATE/
│  └─ workflows/            # CI/CD 파이프라인
├─ gradle/
├─ src/
│  ├─ main/
│  │ ├─ java/umc/demoday/whatisthis/
│  │ │ ├─ domain/           # 도메인형 구조 사용
│  │ │ │ ├─ admin/
│  │ │ │ │  ├─ controller/  # controller
│  │ │ │ │  ├─ dto/         # dto
│  │ │ │ │  ├─ repository/  # 리포지토리
│  │ │ │ │  ├─ service/     # 서비스
│  │ │ │ │  └─ Admin.java   # 엔티티
│  │ │ │ │  └─ ...          # 기타 패키지
│  │ │ │ └─ ...             # 기타 도메인/패키지
│  │ │ ├─ global/           # 전역 설정
│  │ │ │  ├─ config/
│  │ │ │  ├─ security/
│  │ │ │  ├─ apiPayload/    # 응답통일
│  │ │ │  ├─ service/
│  │ │ │  └─ ...
│  │ │ └─ WhatisthisApplication.java
│  │ └─ resources/
│  └─ test/
├─ Dockerfile
├─ build.gradle
├─ gradlew*
└─ README.md
```

