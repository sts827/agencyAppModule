-- 웹서비스 접속 로그 테이블 생성
create or replace table travel_agency.webservice_log
(
    log_id          bigint unsigned auto_increment comment '로그 번호'
        primary key,
    user_token      varchar(50)                 not null comment '사용자 아이디 token',
    user_email      varchar(50)                 null comment '사용자 이메일ID',
    referer         varchar(500)                null comment '이전 페이지',
    request_uri     varchar(200)                not null comment '요청 페이지',
    request_time    datetime                    not null comment '요청 시간',
    request_host    varchar(32)                 not null comment '요청 호스트',
    request_agent   varchar(500)                null comment '요청 Agent',
    response_status smallint                    null comment 'HTTP 응답코드',
    session_id      varchar(32)                 not null comment '접속 세션아이디',
    tracking        enum ('Y', 'N') default 'N' not null
)
    comment '웹서비스 접속 로그';

