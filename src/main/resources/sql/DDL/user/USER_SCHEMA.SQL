-- 사용자 기본 정보 테이블 생성
create or replace table travel_agency.user
(
    user_email             varchar(50)                                  not null comment '사용자 ID(이메일 ID)' primary key,
    user_password          varchar(88)                                  not null comment '사용자 비밀번호',
    user_join_type         varchar(40)      default 'form'              not null comment '사용자 가입유형',
    user_name              varchar(40)                                  not null comment '사용자 이름',
    user_nationality       varchar(40)                                  null comment '사용자 국적',
    user_birthday          varchar(40)                                  null comment '사용자 생년월일',
    user_gender            varchar(5)                                   null comment '사용자 성별',
    user_mobile            varchar(15)                                  null comment '사용자 휴대폰번호',
    user_tel               varchar(15)                                  null comment '사용자 연락처',
    user_addr_zipcode      varchar(15)                                  null comment '사용자 주소 우편번호',
    user_addr_extra        varchar(200)                                 null comment '사용자 주소 참고사항',
    user_addr_jibun        varchar(400)                                 null comment '사용자 지번 주소',
    user_addr_road         varchar(400)                                 null comment '사용자 도로명 주소',
    user_addr_detail       varchar(400)                                 null comment '사용자 상세주소',
    user_role              varchar(12)      default 'USER'              not null comment '사용자 권한',
    user_class_name        varchar(100)                                 null comment '사용자 소속명(업체명)',
    user_class_code        varchar(100)                                 null comment '사용자 소속 구분',
    user_group_code        varchar(10)                                  null comment '사용자 그룹 코드',
    user_token_id          varchar(50)                                  null comment '사용자 고유 토큰값',
    user_grade_id          int                                          null comment '사용자 등급 산정 ID',
    user_grade             varchar(50)                                  null comment '사용자 등급',
    user_grade_start       datetime         default current_timestamp() null comment '사용자 등급 시작일',
    user_grade_end         datetime                                     null comment '사용자 등급 종료일',
    user_membership_id     int                                          null comment '사용자 유료 멤버십 등급 산정 ID',
    user_membership_grade  varchar(50)                                  null comment '사용자 유료 멤버십 등급',
    user_membership_start  datetime                                     null comment '사용자 유료 멤버십 시작일',
    user_membership_end    datetime                                     null comment '사용자 유료 멤버십 종료일',
    user_verified_email    enum ('Y', 'N')  default 'N'                 not null comment '사용자 이메일 인증 여부',
    user_verified_mobile   enum ('Y', 'N')  default 'N'                 not null comment '사용자 휴대폰번호 인증 여부',
    user_ci                varchar(88)                                  null comment '사용자 CI 정보',
    user_di                varchar(64)                                  null comment '사용자 DI 정보',
    user_di_corp           varchar(13)                                  null comment '사용자 DI 연계 키 정보',
    user_join_date         datetime         default current_timestamp() not null comment '가입일자',
    user_modify_date       datetime         default current_timestamp() not null comment '사용자정보 변경일자',
    last_login_date        datetime                                     null comment '최종 로그인일자',
    last_password_date     datetime         default current_timestamp() not null comment '최종 비밀번호 변경일자',
    last_login_fail_count  tinyint unsigned default 0                   not null comment '마지막 로그인 시도 실패 횟수',
    account_status         varchar(10)      default 'active'            not null comment '계정 상태 코드(active: 활성화, lock: 비밀번호 오류 잠금, inactive: 휴면)',
    account_type         varchar(10)        default null                null comment '계정 유형 (M: SNS-폼 통합계정, F: 일반단독계정, S: SNS 단독계정)',
    naver_token            varchar(64)                                  null comment '네이버 토큰 ID',
    naver_email            varchar(50)                                  null comment '네이버 이메일 값',
    naver_join_date        datetime                                     null comment '네이버 아이디 연동일자',
    kakao_token            varchar(64)                                  null comment '카카오 토큰 ID',
    kakao_email            varchar(50)                                  null comment '카카오 이메일 값',
    kakao_join_date        datetime                                     null comment '카카오 아이디 연동일자',
    google_token           varchar(64)                                  null comment '구글 토큰 ID',
    google_email           varchar(50)                                  null comment '구글 이메일 값',
    google_join_date       datetime                                     null comment '구글 아이디 연동일자',
    facebook_token         varchar(64)                                  null comment '페이스북 토큰 ID',
    facebook_email         varchar(50)                                  null comment '페이스북 이메일 값',
    facebook_join_date     datetime                                     null comment '페이스북 아이디 연동일자',
    secondary_email        varchar(50)                                  null comment '복구용 2차 이메일',
    privacy_retention_days int                                          null comment '개인정보 보관기간',
    mailing_yn             enum ('Y', 'N')  default 'N'                 not null comment '이메일 수신 여부',
    constraint user_id_token
        unique (user_token_id)
)
    comment '사용자 정보';


-- 탈퇴 사용자 분리 테이블 생성
create or replace table travel_agency.user_stored_withdrawal
(
    user_email             varchar(50)                                  not null comment '사용자 ID(이메일 ID)' primary key,
    user_password          varchar(88)                                  not null comment '사용자 비밀번호',
    user_join_type         varchar(40)      default 'form'              not null comment '사용자 가입유형',
    user_name              varchar(40)                                  not null comment '사용자 이름',
    user_nationality       varchar(40)                                  null comment '사용자 국적',
    user_birthday          varchar(40)                                  null comment '사용자 생년월일',
    user_gender            varchar(5)                                   null comment '사용자 성별',
    user_mobile            varchar(15)                                  null comment '사용자 휴대폰번호',
    user_tel               varchar(15)                                  null comment '사용자 연락처',
    user_addr_zipcode      varchar(15)                                  null comment '사용자 주소 우편번호',
    user_addr_extra        varchar(200)                                 null comment '사용자 주소 참고사항',
    user_addr_jibun        varchar(400)                                 null comment '사용자 지번 주소',
    user_addr_road         varchar(400)                                 null comment '사용자 도로명 주소',
    user_addr_detail       varchar(400)                                 null comment '사용자 상세주소',
    user_role              varchar(12)      default 'USER'              not null comment '사용자 권한',
    user_class_name        varchar(100)                                 null comment '사용자 소속명(업체명)',
    user_class_code        varchar(100)                                 null comment '사용자 소속 구분',
    user_group_code        varchar(10)                                  null comment '사용자 그룹 코드',
    user_token_id          varchar(50)                                  null comment '사용자 고유 토큰값',
    user_grade_id          int                                          null comment '사용자 등급 산정 ID',
    user_grade             varchar(50)                                  null comment '사용자 등급',
    user_grade_start       datetime         default current_timestamp() null comment '사용자 등급 시작일',
    user_grade_end         datetime                                     null comment '사용자 등급 종료일',
    user_membership_id     int                                          null comment '사용자 유료 멤버십 등급 산정 ID',
    user_membership_grade  varchar(50)                                  null comment '사용자 유료 멤버십 등급',
    user_membership_start  datetime                                     null comment '사용자 유료 멤버십 시작일',
    user_membership_end    datetime                                     null comment '사용자 유료 멤버십 종료일',
    user_verified_email    enum ('Y', 'N')  default 'N'                 not null comment '사용자 이메일 인증 여부',
    user_verified_mobile   enum ('Y', 'N')  default 'N'                 not null comment '사용자 휴대폰번호 인증 여부',
    user_ci                varchar(88)                                  null comment '사용자 CI 정보',
    user_di                varchar(64)                                  null comment '사용자 DI 정보',
    user_di_corp           varchar(13)                                  null comment '사용자 DI 연계 키 정보',
    user_join_date         datetime         default current_timestamp() not null comment '가입일자',
    user_modify_date       datetime         default current_timestamp() not null comment '사용자정보 변경일자',
    last_login_date        datetime                                     null comment '최종 로그인일자',
    last_password_date     datetime         default current_timestamp() not null comment '최종 비밀번호 변경일자',
    last_login_fail_count  tinyint unsigned default 0                   not null comment '마지막 로그인 시도 실패 횟수',
    account_status         varchar(10)      default 'active'            not null comment '계정 상태 코드(active: 활성화, lock: 비밀번호 오류 잠금, inactive: 휴면)',
    account_type         varchar(10)        default null                null comment '계정 유형 (M: SNS-폼 통합계정, F: 일반단독계정, S: SNS 단독계정)',
    naver_token            varchar(64)                                  null comment '네이버 토큰 ID',
    naver_email            varchar(50)                                  null comment '네이버 이메일 값',
    naver_join_date        datetime                                     null comment '네이버 아이디 연동일자',
    kakao_token            varchar(64)                                  null comment '카카오 토큰 ID',
    kakao_email            varchar(50)                                  null comment '카카오 이메일 값',
    kakao_join_date        datetime                                     null comment '카카오 아이디 연동일자',
    google_token           varchar(64)                                  null comment '구글 토큰 ID',
    google_email           varchar(50)                                  null comment '구글 이메일 값',
    google_join_date       datetime                                     null comment '구글 아이디 연동일자',
    facebook_token         varchar(64)                                  null comment '페이스북 토큰 ID',
    facebook_email         varchar(50)                                  null comment '페이스북 이메일 값',
    facebook_join_date     datetime                                     null comment '페이스북 아이디 연동일자',
    secondary_email        varchar(50)                                  null comment '복구용 2차 이메일',
    privacy_retention_days int                                          null comment '개인정보 보관기간',
    mailing_yn             enum ('Y', 'N')  default 'N'                 not null comment '이메일 수신 여부',
    delete_date DATETIME NULL DEFAULT NULL COMMENT '삭제일시',
    delete_id VARCHAR(50) NULL DEFAULT NULL COMMENT '삭제자ID' COLLATE 'utf8mb4_unicode_ci'
)
    comment '사용자 탈퇴 정보';


-- 사용자 로그인 시도 이력 테이블 생성
create or replace table travel_agency.user_login_attempt_log
(
    log_id          bigint unsigned auto_increment comment '로그 번호'
        primary key,
    user_email      varchar(50)                    not null comment '로그인 시도 아이디',
    login_type      varchar(50) default 'password' not null comment '로그인 인증 방법',
    attempt_ip      varchar(32)                    not null comment '로그인 시도 IP',
    attempt_agent   varchar(500)                   null comment '로그인 시도 USER AGENT',
    attempt_referer varchar(500)                   null comment '로그인 시도 REFERER',
    attempt_time    datetime                       not null comment '로그인 시도 시간',
    error_code      varchar(10)                    not null comment '로그인 에러 코드',
    error_message   varchar(500)                   not null comment '로그인 에러 메시지'
);


-- 사용자 로그인 이력 테이블 생성
create table travel_agency.user_login_session
(
    seq           int unsigned auto_increment comment '고유번호'
        primary key,
    user_email    varchar(50)                    null comment '로그인 아이디',
    login_type    varchar(50) default 'password' not null comment '로그인 인증방법',
    login_session varchar(100)                   not null comment '로그인 세션아이디',
    login_ip      varchar(32)                    not null comment '로그인 아이피',
    login_agent   varchar(500)                   null comment '로그인 에이전트',
    login_referer varchar(500)                   null comment '접근페이지',
    login_time    datetime                       not null comment '로그인 시간',
    logout_time   datetime                       null comment '로그아웃 시간',
    logout_type   varchar(20)                    null comment '로그아웃 방법',
    constraint user_login_session_fk
        foreign key (user_email) references travel_agency.user (user_email)
            on update cascade on delete set null
)
    comment '사용자 로그인 기록';

