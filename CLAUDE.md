# CLAUDE.md

## Project Overview

Loopers Commerce Platform — Spring Boot 기반 멀티모듈 커머스 프로젝트 템플릿.
Hexagonal(Ports & Adapters) 아키텍처를 채택하며, 3개의 실행 가능한 애플리케이션과 6개의 공유 모듈로 구성된다.

## Tech Stack & Versions

| Category | Technology | Version |
|----------|-----------|---------|
| Language | Java | 21 |
| Framework | Spring Boot | 3.4.4 |
| Cloud | Spring Cloud Dependencies | 2024.0.1 |
| Build | Gradle (Kotlin DSL) | - |
| ORM | Spring Data JPA + QueryDSL | (managed by BOM) |
| Cache | Spring Data Redis (Lettuce) | (managed by BOM) |
| Messaging | Spring Kafka | (managed by BOM) |
| DB | MySQL | 8.0 |
| Cache Store | Redis | 7.0 (master-replica) |
| Broker | Kafka | 3.5.1 (KRaft mode) |
| API Docs | SpringDoc OpenAPI | 2.7.0 |
| Monitoring | Micrometer + Prometheus + Grafana | (managed by BOM) |
| Logging | Logback + Slack Appender | 1.6.1 |
| Serialization | Jackson (JSR-310, Kotlin module) | (managed by BOM) |
| Code Gen | Lombok | (managed by BOM) |
| Test | JUnit 5, Mockito 5.14.0, SpringMockK 4.0.2, Instancio 5.0.2 | - |
| Test Infra | Testcontainers (MySQL, Redis, Kafka) | (managed by BOM) |
| Coverage | JaCoCo | - |
| Lint | ktlint 1.0.1 (plugin 12.1.2), EditorConfig | - |
| Review | CodeRabbit (AI code review, Korean) | - |

## Module Structure

```
commerce-platform/
├── apps/                          # 실행 가능한 Spring Boot 애플리케이션
│   ├── commerce-api/              # REST API 서버 (port: 8080, mgmt: 8081)
│   ├── commerce-batch/            # Spring Batch 배치 처리
│   └── commerce-streamer/         # Kafka 컨슈머 기반 스트리밍 서버
│
├── modules/                       # 재사용 가능한 인프라 모듈 (java-library)
│   ├── jpa/                       # JPA + QueryDSL + MySQL + HikariCP 설정
│   ├── redis/                     # Redis master-replica 설정 (Lettuce)
│   └── kafka/                     # Kafka producer/consumer/batch listener 설정
│
├── supports/                      # 횡단 관심사 지원 모듈
│   ├── jackson/                   # Jackson ObjectMapper 커스터마이징
│   ├── logging/                   # Logback 프로파일별 설정 + Slack 알림
│   └── monitoring/                # Prometheus 메트릭 + health probes (port: 8081)
│
├── docker/
│   ├── infra-compose.yml          # MySQL, Redis, Kafka, Kafka UI
│   └── monitoring-compose.yml     # Prometheus, Grafana
│
└── http/                          # IntelliJ HTTP Client 테스트 파일
```

### Module Dependency Map

```
commerce-api      → jpa, redis, jackson, logging, monitoring
commerce-batch    → jpa, redis, jackson, logging, monitoring
commerce-streamer → jpa, redis, kafka, jackson, logging, monitoring
```

## Architecture & Package Convention

Hexagonal Architecture 레이어 구조를 따른다. Base package: `com.loopers`

```
com.loopers
├── interfaces/                    # Inbound Adapters
│   ├── api/{domain}/              # REST Controllers (*V1Controller)
│   └── consumer/                  # Kafka Consumers
├── application/{domain}/          # Use Cases (*Facade)
├── domain/{domain}/               # Core Business Logic (*Service, *Model, *Repository 인터페이스)
├── infrastructure/{domain}/       # Outbound Adapters (*RepositoryImpl, *JpaRepository)
├── support/error/                 # 공통 에러 처리 (CoreException, ErrorType)
├── batch/                         # Batch 전용 (job/, listener/)
└── config/                        # 모듈별 설정 (jpa, redis, kafka, jackson)
```

### Naming Conventions

- Controller: `*V1Controller`, `*V2Controller` (API 버전 포함)
- Facade: `*Facade` (application 레이어 오케스트레이션)
- Service: `*Service` (도메인 비즈니스 로직)
- Repository (Port): `*Repository` (인터페이스)
- Repository (Adapter): `*RepositoryImpl` / `*JpaRepository` (구현체)
- Entity: `*Model` (JPA 엔티티, `BaseEntity` 상속)
- Config: `*Config` (@Configuration 클래스)

## Build & Run Commands

```bash
# 로컬 인프라 실행
docker-compose -f ./docker/infra-compose.yml up -d

# 모니터링 스택 실행
docker-compose -f ./docker/monitoring-compose.yml up -d

# 전체 빌드
./gradlew build

# 특정 모듈 빌드
./gradlew :apps:commerce-api:build

# 테스트 실행 (timezone: Asia/Seoul, profile: test)
./gradlew test

# 특정 앱 실행
./gradlew :apps:commerce-api:bootRun
./gradlew :apps:commerce-batch:bootRun -Djob.name=demoJob
./gradlew :apps:commerce-streamer:bootRun
```

## Test Configuration

- 테스트 프레임워크: JUnit 5 + Mockito + SpringMockK + Instancio
- 통합 테스트: Testcontainers (MySQL, Redis, Kafka) — 별도 Docker 불필요
- 테스트 시 timezone: `Asia/Seoul`, profile: `test`
- `maxParallelForks = 1` (순차 실행)
- 모듈별 `testFixtures` 제공 (DatabaseCleanUp, RedisCleanUp, TestContainers 설정)
- JaCoCo 커버리지 리포트: XML 형식

## Environment Profiles

| Profile | 용도 | Hibernate DDL | Log Level |
|---------|------|--------------|-----------|
| local | 로컬 개발 | create | DEBUG (com.loopers) |
| test | 테스트 | create | DEBUG (com.loopers) |
| dev | 개발 서버 | none | DEBUG (com.loopers) + Slack |
| qa | QA 서버 | none | INFO + Slack |
| prd | 운영 서버 | none | INFO + Slack |

## Key Patterns & Conventions

- **Soft Delete**: `BaseEntity`의 `deletedAt` 필드 활용 (delete/restore 메서드)
- **에러 처리**: `CoreException` + `ErrorType` enum (INTERNAL_ERROR, BAD_REQUEST, NOT_FOUND, CONFLICT)
- **Redis**: master-replica 구조, 읽기는 replica-preferred, 쓰기는 master-only RedisTemplate 분리
- **Kafka**: batch listener (최대 3000건), manual ACK, concurrency 3
- **Batch**: `job.name` 시스템 프로퍼티로 실행할 Job 지정
- **Monitoring**: 애플리케이션과 별도 포트(8081)에서 health/prometheus 엔드포인트 노출
- **Logging**: local/test는 plain console, dev/qa/prd는 JSON + Slack 알림
- **Line Length**: 130자 (테스트 파일은 제한 없음, `.editorconfig`)
- **Config Import**: 각 모듈의 yml 파일을 앱의 `application.yml`에서 import

## TDD 연습 세션 (회원 도메인)

### 진행 방식
- **페어 프로그래밍**: Claude가 테스트 작성 → 사용자가 구현 (번갈아가며)
- **Top-Down TDD**: E2E → 통합 → 단위 테스트 순서
- **3단계 루프**: 실패하는 테스트 작성 (RED) → 통과하는 최소 코드 (GREEN) → 리팩토링
- **기술**: JUnit5 + Mockito + TestRestTemplate

### 요구사항

**회원가입** (`POST /api/v1/members/signup`)
- 필요 정보: loginId, password, name, birthDate, email
- 중복 loginId 가입 불가 → 409 CONFLICT
- 비밀번호 규칙:
  - 8~16자, 영문 대소문자/숫자/특수문자만 허용
  - 생년월일 포함 불가
- 비밀번호는 암호화 저장

**내 정보 조회** (`GET /api/v1/members/me`)
- 인증 헤더: `X-Loopers-LoginId`, `X-Loopers-LoginPw`
- 반환: loginId, name(마지막 글자 `*` 마스킹), birthDate, email
- loginId는 영문+숫자만 허용

**비밀번호 수정** (`PATCH /api/v1/members/me/password`)
- 인증 헤더 필요
- 필요 정보: currentPassword, newPassword
- 현재 비밀번호와 동일 불가 → 400 BAD_REQUEST
- 비밀번호 규칙 동일 적용

### 현재 진행 상태

| 단계 | 상태 | 파일 |
|------|------|------|
| E2E 테스트 작성 | ✅ 완료 (RED) | `src/test/java/.../interfaces/api/member/MemberV1ApiE2ETest.java` |
| E2E 구현 (GREEN) | ⏳ 사용자 작업 중 | 회원가입 API 흐름 연결 중 |
| 통합 테스트 작성 | ❌ 대기 | - |
| 단위 테스트 작성 | ❌ 대기 | - |

### 완료된 파일들 (commerce-api 기준)

| 파일 | 상태 | 설명 |
|------|------|------|
| `interfaces/api/member/MemberV1ApiSpec.java` | ✅ | Swagger API 스펙 인터페이스 |
| `interfaces/api/member/MemberV1Dto.java` | ✅ | SignupRequest(`toCommand()` 포함), SignupResponse, MeResponse, ChangePasswordRequest |
| `interfaces/api/member/MemberV1Controller.java` | ⏳ | `toCommand()` 변환까지 완료, Service 주입 + 응답 변환 남음 |
| `domain/member/MemberCommand.java` | ✅ | `CreateMember` record (순수 데이터, 메서드 없음) |
| `domain/member/MemberEntity.java` | ✅ | BaseEntity 상속, `create(CreateMember)` 정적 팩토리 메서드 |
| `domain/member/MemberService.java` | ✅ | `signUp(CreateMember)` — Entity 생성 + Repository 저장 |
| `domain/member/MemberRepository.java` | ✅ | 도메인 인터페이스 (`exists`, `save`) |

### 남은 파일들

| 파일 | 상태 | 설명 |
|------|------|------|
| `infrastructure/member/MemberJpaRepository.java` | ❌ | Spring Data JPA 인터페이스 |
| `infrastructure/member/MemberRepositoryImpl.java` | ❌ | MemberRepository 구현체 |
| Controller 완성 | ❌ | MemberService 주입 + signUp 호출 + SignupResponse 변환 리턴 |

### 설계 결정 사항

- **Facade 미사용**: 회원가입은 단일 도메인 기능이므로 Controller → Service 직접 호출 (YAGNI 원칙)
- **레이어 의존 방향**: interfaces → domain 단방향. DTO→Command 변환은 `SignupRequest.toCommand()`에서 담당
- **엔티티 생성 책임**: `MemberEntity.create(command)` 정적 팩토리 메서드 사용

**참고**: ErrorType에 `UNAUTHORIZED(HttpStatus.UNAUTHORIZED, ...)` 추가 필요
