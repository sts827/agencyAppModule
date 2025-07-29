# 🏗️ 개선된 TravelAgency 아키텍처 설계

## 📁 새로운 패키지 구조

```
src/main/java/kr/co/wayplus/travel/
├── TravelAgencyApplication.java
├── config/                          # 설정 클래스
│   ├── security/
│   ├── database/
│   └── web/
├── shared/                          # 공통 모듈
│   ├── exception/
│   ├── util/
│   └── constant/
├── domain/                          # 도메인별 모듈
│   ├── user/                        # 사용자 도메인
│   │   ├── domain/                  # 도메인 모델
│   │   ├── application/             # 애플리케이션 서비스
│   │   ├── infrastructure/          # 인프라 (Repository, Mapper)
│   │   └── presentation/            # 프레젠테이션 (Controller, DTO)
│   ├── product/                     # 상품 도메인
│   │   ├── domain/
│   │   ├── application/
│   │   ├── infrastructure/
│   │   └── presentation/
│   ├── reservation/                 # 예약 도메인
│   │   ├── domain/
│   │   ├── application/
│   │   ├── infrastructure/
│   │   └── presentation/
│   ├── board/                       # 게시판 도메인
│   │   ├── domain/
│   │   ├── application/
│   │   ├── infrastructure/
│   │   └── presentation/
│   └── management/                  # 관리 도메인
│       ├── domain/
│       ├── application/
│       ├── infrastructure/
│       └── presentation/
└── api/                            # API 계층
    ├── v1/
    └── admin/
```

## 🎯 핵심 설계 원칙

### 1. 헥사고날 아키텍처 (Hexagonal Architecture)
- **도메인 중심 설계**: 비즈니스 로직을 도메인 계층에 집중
- **의존성 역전**: 외부 의존성을 인터페이스로 추상화
- **포트와 어댑터**: 외부 시스템과의 연결점을 명확히 분리

### 2. 도메인 주도 설계 (DDD)
- **바운디드 컨텍스트**: 각 도메인을 독립적으로 관리
- **애그리게이트**: 관련된 엔티티들을 하나의 단위로 관리
- **도메인 서비스**: 복잡한 비즈니스 로직을 도메인 서비스로 분리

### 3. CQRS (Command Query Responsibility Segregation)
- **명령과 조회 분리**: 데이터 변경과 조회를 분리하여 성능 최적화
- **읽기 전용 모델**: 조회에 최적화된 별도 모델 사용