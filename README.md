# 🎬 Movie Review Platform

React와 Spring Boot를 활용한 영화 리뷰 플랫폼입니다.
사용자는 영화를 검색하고, 리뷰 및 별점을 작성할 수 있으며
개인 프로필을 통해 작성한 리뷰를 관리할 수 있습니다.

---

## 🔧 기술 스택

### 🖥️ 프론트엔드

* React (JavaScript)
* Vite
* Axios
* HTML/CSS
* Figma (디자인)

> 개발 환경:
>
> * Node.js v18 이상
> * npm 또는 yarn

### ⚙️ 백엔드

* Java 22 (Amazon Corretto 22)
* Spring Boot 3.x
* Spring Web (REST API)
* Spring Data JPA
* MySQL / MariaDB
* Gradle

> 개발 환경:
>
> * IntelliJ IDEA
> * JDK 22
> * MySQL 8.x / MariaDB

> **참고:** 런타임으로 Amazon Corretto 22를 사용합니다.

---

---

## 🛠️ 개발 환경 설정

1. **Java SDK (Amazon Corretto 22, JDK 22)**

2. **백엔드 초기 설정**

   * 프로젝트 루트에서 Gradle Wrapper가 설정되어 있는지 확인하세요.
   * Gradle 빌드 및 실행:

     ```bash
     ./gradlew clean build
     ./gradlew bootRun
     ```

3. **프론트엔드 초기 설정**

   * `frontend/` 디렉터리로 이동 후 의존성 설치:

     ```bash
     npm install
     ```
   * 개발 서버 실행:

     ```bash
     npm run dev
     ```
   * 개발 서버가 실행된 포트(기본: 5173)를 확인 후,
     `backend` 프로젝트의 `Webconfig.java` 파일 내 CORS 설정(Allowed Origins)을 해당 포트로 맞춰주세요.

---

## 👨‍👩‍👧‍👦 팀 구성

| 파트           | 이름 (GitHub ID)       |
| ------------ | -------------------- |
| Frontend     | namisu009, namisu000 |
| Frontend     | NASEEL4013           |
| Backend (팀장) | JOONG-BA             |
| Backend      | jeonseohee9          |

---

## 📁 프로젝트 구조

```bash
movie-review-project/
├── backend/          # Spring Boot 프로젝트
├── frontend/         # React 프로젝트
└── README.md         # 협업 문서
```

---

## 🌿 GitHub Flow 기반 브랜치 전략

* `master`: 항상 배포 가능한 상태를 유지합니다.
* 모든 작업은 `master` 브랜치로부터 새 브랜치를 생성해 작업합니다.
* 작업 완료 후 GitHub에서 **Pull Request(PR)** 를 생성해 리뷰 후 병합합니다.
* 병합된 브랜치는 **삭제하지 않고**, 보존된 상태로 유지하며 이후 더 이상 사용하지 않습니다.

### 📂 브랜치 네이밍 규칙

| 접두어         | 용도              |
| ----------- | --------------- |
| `feature/`  | 새로운 기능 개발       |
| `fix/`      | 버그 수정, 간단한 수정   |
| `refactor/` | 코드 리팩토링         |
| `style/`    | UI 스타일 및 포맷팅    |
| `docs/`     | 문서 작성 및 수정      |
| `test/`     | 테스트 코드 추가       |
| `chore/`    | 환경설정, 빌드 스크립트 등 |

**예시:**

* `feature/login-api`
* `fix/movie-detail-bug`
* `refactor/user-service`
* `docs/update-readme`

---

### 📝 커밋 메시지 컨벤션

> 참고: [velog 커밋 메시지 규칙](https://velog.io/@chojs28/Git-%EC%BB%A4%EB%B0%8B-%EB%A9%94%EC%8B%9C%EC%A7%80-%EA%B7%9C%EC%B9%99)

커밋 메시지는 아래와 같은 형식을 사용합니다:

| 타입         | 설명                                 |
| ---------- | ---------------------------------- |
| `feat`     | 새로운 기능 추가                          |
| `fix`      | 버그 수정                              |
| `refactor` | 코드 리팩토링                            |
| `style`    | 코드 포맷팅, 세미콜론 누락 등 비즈니스 로직 변경 없는 경우 |
| `docs`     | 문서 작성 및 수정                         |
| `test`     | 테스트 코드 작성 및 수정                     |
| `chore`    | 빌드, 패키지 매니저 설정 등 기타 자잘한 작업         |

#### ✅ 커밋 메시지 예시

```bash
feat: 영화 리뷰 등록 API 구현
fix: 리뷰 수정 시 평점 반영되지 않는 오류 해결
refactor: MovieService 로직 개선
style: 들여쓰기 및 코드 정렬
docs: README 사용법 설명 추가
test: MovieController 단위 테스트 추가
chore: .gitignore 파일 정리
```

---

## 🤝 협업 규칙

* 모든 작업은 브랜치를 나눠서 진행하며, main에 직접 커밋하지 않습니다.
* PR을 통해 코드 리뷰 및 병합을 진행합니다.
* 병합된 브랜치는 삭제하지 않고 보존하며, 이후에는 더 이상 작업하지 않습니다.
* 커밋 메시지는 명확하고 일관되게 작성합니다.
* 한 커밋에는 하나의 목적만 담는 것을 원칙으로 합니다.
* PR에는 반드시 작업 내용 / 테스트 여부 / 관련 이슈를 명시합니다.
* 브랜치 충돌이 예상되거나 병합 순서가 필요할 경우, 팀원 간 커뮤니케이션(Notion, 카카오톡, Discord 등)으로 사전 조율합니다.

---

## 📖 사용 설명서 및 기능 설명

아래는 일반 사용자를 위한 기능 안내입니다.

### 0. 초기 세팅
 - GitHub 저장소를 로컬에 클론합니다 (예: master 브랜치 기준):
  ```
   git clone -b master https://github.com/JOONG-BA/movie-review.git .
  ```
 - IDE(IntelliJ 추천)로 프로젝트 디렉토리를 엽니다.

 * 프로젝트 루트에서 Gradle Wrapper가 설정되어 있는지 확인하세요.
   * Gradle 빌드 및 실행:

	**백엔드 초기 설정**
     ```bash
     ./gradlew clean build
     ./gradlew bootRun
     ```

	 **프론트엔드 초기 설정**

   * `frontend/` 디렉터리로 이동 후 의존성 설치:

     ```bash
     npm install
     ```
   * 개발 서버 실행:

     ```bash
     npm run dev
     ```
   * 개발 서버가 실행된 포트(기본: 5173)를 확인 후,
     `backend` 프로젝트의 `Webconfig.java` 파일 내 CORS 설정(Allowed Origins)을 해당 포트로 맞춰주세요.

 ### 1. 홈페이지 접속

* 브라우저 주소창에 서비스 도메인 또는 `http://localhost:5173`을 입력해 접속합니다.
* 메인 페이지에는 인기 영화 목록이 표시됩니다.

### 2. 영화 검색

* 상단의 검색창에 원하는 영화 제목이나 키워드를 입력한 뒤 돋보기 아이콘을 클릭합니다.
* 검색 결과 목록에서 스크롤하여 더 많은 영화를 확인할 수 있습니다.

### 3. 영화 상세 보기

* 영화 목록 또는 검색 결과에서 포스터를 클릭하면 상세 페이지로 이동합니다.
* 상세 페이지에는 영화 개요, 개봉일, 평점, 출연 배우 정보 등이 제공됩니다.

### 4. 리뷰 작성 및 별점

* 상세 페이지 상단의 ‘리뷰 작성’ 섹션에서 별점(0.5\~5점)을 선택하고, 자유롭게 한 줄 이상의 리뷰 내용을 입력합니다.
* 작성 완료 후 ‘제출’ 버튼을 누르면 나의 리뷰로 등록됩니다.

### 5. 리뷰 관리 (내 리뷰 확인, 수정, 삭제)

* 우측 상단의 '마이페이지' 버튼 클릭하여 ‘마이 페이지’로 이동합니다.
* 마이 페이지에서 내가 작성한 모든 리뷰 목록을 확인할 수 있습니다.


### 6. 로그아웃

* 상단 네비게이션 바의 ‘로그아웃’ 버튼을 클릭하면 안전하게 로그아웃됩니다.

---

앱 사용 중 문제가 발생하거나 개선 의견이 있을 경우, 팀 메일 혹은 깃허브 이슈를 통해 알려주세요.
