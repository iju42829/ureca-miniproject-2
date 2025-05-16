# 유레카 미니 프로젝트 2 - 마피아 게임 

## 프로젝트 개요

이 프로젝트는 유레카 미니 프로젝트 2로 진행된 협업 기반 실습 프로젝트입니다.  
프로젝트의 완성도보다는 지금까지 학습한 기술 스택을 실전에서 적용해보고, 팀원 간 협업과 역할 분담 경험을 쌓는 것을 주요 목표로 삼았습니다.  
게임 로직과 실시간 상태 관리를 구현하며 백엔드 구조 설계와 API 설계에 대한 경험을 쌓을 수 있었습니다.

## 팀원 소개

| 이름   | 역할           | 주요 구현 내용            | GitHub                                             |
|--------|----------------|-------------------|----------------------------------------------------|
| 이재윤 | 백엔드 (팀장)   | 게임 비즈니스 로직        | [iju42829](https://github.com/iju42829)         |
| 김정민 | 백엔드 (팀원)   | 회원가입/로그인, 친구 비즈니스 로직, 사용자 상호작용| [jmk445](https://github.com/jmk445)        |
| 손민혁 | 백엔드 (팀원)   | webSocket 기반 채팅/STATE_MACHINE   | [Sonminhyeok](https://github.com/Sonminhyeok)               |

## 기술 스택

| 분류 | 기술                     |
|------|------------------------|
| Language | Java 17                |
| Framework | Spring Boot 3.4.5      |
| Build Tool | Gradle                 |
| DB | MySQL, H2              |
| ORM | JPA        |
| Auth | Spring Security        |
| Infra | Docker |
| Test | JUnit5, Mockito        |

## 구현 내용

- 회원가입 / 로그인
- 게임 방 생성 / 입장 / 퇴장
- 게임 시작 시 역할 랜덤 배정
- 투표 기능 및 사망 처리
- 게임 종료 조건 처리
- WebSocket 기반 실시간 통신

## ERD (Entity Relationship Diagram)

DB 관계도는 다음과 같습니다.
![ERD](https://github.com/user-attachments/assets/8fdae029-077c-4c43-b89a-611cf941381e)
## 프로젝트 실행 방법

### 1. `.env` 파일 생성

루트 디렉토리에 `.env` 파일을 생성하고 아래 내용을 입력합니다:

```env
SERVER_PORT=
DB_USERNAME=
DB_PASSWORD=
DB_PORT=
DB_NAME=
DB_URL=
```
### 2. Docker Compose로 실행
```
docker-compose up --build
```


## 실행화면


![화면 캡처 2025-05-16 140512](https://github.com/user-attachments/assets/def5494f-c87b-4c37-8e2d-4435f86a5ad8)
![화면 캡처 2025-05-16 140549](https://github.com/user-attachments/assets/0272909c-7e65-4aef-aeac-a9049aae1370)
![화면 캡처 2025-05-16 141016](https://github.com/user-attachments/assets/dff6e28e-8c6d-4853-9493-a069c2bb3af5)
![화면 캡처 2025-05-16 141028](https://github.com/user-attachments/assets/322ae9e5-9b7f-43e6-abf4-88d2a91e64c5)
![화면 캡처 2025-05-16 141104](https://github.com/user-attachments/assets/d8bcf36b-fec1-4e71-899d-a6fa745a808a)
![화면 캡처 2025-05-16 141152](https://github.com/user-attachments/assets/1c079875-7b95-448f-a80b-ee03d84e07e4)
![화면 캡처 2025-05-16 141227](https://github.com/user-attachments/assets/a1305aa9-3054-4f23-bdd7-4201c923cac8)
![화면 캡처 2025-05-16 141244](https://github.com/user-attachments/assets/b6845bf5-bed2-45d8-ab2a-f8c257323318)
![화면 캡처 2025-05-16 141308](https://github.com/user-attachments/assets/7267345a-12dc-4d23-9b07-e378e356a464)
![화면 캡처 2025-05-16 141426](https://github.com/user-attachments/assets/9ec2cf54-751b-490a-8719-3c045ff0e4e1)
![화면 캡처 2025-05-16 141459](https://github.com/user-attachments/assets/d13fa73d-d7c9-43bb-9796-91cdc9958ab4)
![화면 캡처 2025-05-16 141511](https://github.com/user-attachments/assets/1de8d9d5-ce7a-463a-a91c-0852c27bc833)

