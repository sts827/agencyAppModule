-- travel_agency.board_attach_file definition

CREATE TABLE `board_attach_file` (
  `file_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `board_id` int(10) unsigned NOT NULL COMMENT '게시판 ID',
  `content_id` int(10) unsigned NOT NULL COMMENT '게시글 ID',
  `service_type` varchar(20) NOT NULL DEFAULT '' COMMENT '서비스 구분',
  `upload_path` varchar(100) NOT NULL COMMENT '저장 경로',
  `upload_filename` varchar(50) NOT NULL COMMENT '저장 파일명',
  `file_extension` varchar(50) DEFAULT NULL COMMENT '파일 확장자명',
  `file_size` int(10) unsigned NOT NULL DEFAULT 0 COMMENT '파일 크기(Byte)',
  `file_mimetype` varchar(50) DEFAULT NULL COMMENT '파일 마임타입',
  `origin_filename` varchar(200) NOT NULL COMMENT '원본 파일명',
  `upload_id` varchar(50) NOT NULL COMMENT '저장ID',
  `upload_date` datetime NOT NULL COMMENT '저장일시',
  PRIMARY KEY (`file_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- travel_agency.board_category definition

CREATE TABLE `board_category` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '고유번호',
  `board_id` int(11) unsigned DEFAULT NULL COMMENT '게시판 아이디',
  `title` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '카테고리명',
  `note` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '카테고리 설명',
  `order_num` tinyint(4) DEFAULT 10 COMMENT '정렬순서',
  `use_yn` enum('Y','N') CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT 'Y' COMMENT '사용여부',
  `start_date` datetime DEFAULT NULL COMMENT '사용 시작일시',
  `expire_date` datetime DEFAULT NULL COMMENT '사용 종료일시',
  `create_id` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '생성자',
  `create_date` datetime DEFAULT NULL COMMENT '생성일시',
  `last_update_id` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '최종수정자',
  `last_update_date` datetime DEFAULT NULL COMMENT '최종수정일시',
  PRIMARY KEY (`id`),
  KEY `board_category_fk` (`board_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='게시판 카테고리 설정';


-- travel_agency.board_comment definition

CREATE TABLE `board_comment` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '고유번호',
  `board_id` int(11) unsigned NOT NULL COMMENT '게시판 아이디',
  `content_id` int(11) unsigned NOT NULL COMMENT '게시글 아이디',
  `tab_index` tinyint(4) NOT NULL DEFAULT 0 COMMENT '댓글 뎁스',
  `upper_id` int(11) DEFAULT NULL COMMENT '상위댓글 번호',
  `note` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '댓글내용',
  `warning_id` tinyint(11) DEFAULT NULL COMMENT '댓글 경고 설정',
  `blind_yn` enum('Y','N') CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT 'N' COMMENT '관리자 숨김여부',
  `blind_id` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '숨김처리자',
  `blind_date` datetime DEFAULT NULL COMMENT '숨김처리일시',
  `create_id` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '생성자',
  `create_date` datetime DEFAULT NULL COMMENT '생성일시',
  `last_update_id` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '최종수정자',
  `last_update_date` datetime DEFAULT NULL COMMENT '최종수정일시',
  `delete_yn` enum('Y','N') CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT 'N' COMMENT '삭제여부',
  PRIMARY KEY (`id`),
  KEY `board_comment_fk` (`board_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='게시판 댓글 ';


-- travel_agency.board_contents definition

CREATE TABLE `board_contents` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '고유번호',
  `product_tour_id` int(10) unsigned DEFAULT 0 COMMENT '상품 ID (필수아님)',
  `board_id` int(10) unsigned NOT NULL COMMENT '게시판 아이디',
  `category_id` int(11) unsigned DEFAULT NULL COMMENT '카테고리 아이디',
  `title` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '게시글 제목',
  `content` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '게시글 내용',
  `apply_code` varchar(4) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '접수상태코드',
  `series_id` int(4) unsigned DEFAULT NULL COMMENT '시리즈 게시물 번호',
  `tags` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '관련 태그',
  `thumbnail_url` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '썸네일 이미지 경로',
  `favorite_count` smallint(6) unsigned NOT NULL DEFAULT 0 COMMENT '추천 숫자',
  `scrap_count` smallint(6) unsigned NOT NULL DEFAULT 0 COMMENT '스크랩 숫자',
  `comment_count` smallint(6) unsigned NOT NULL DEFAULT 0 COMMENT '코멘트 숫자',
  `view_count` int(11) unsigned NOT NULL DEFAULT 0 COMMENT '조회 수',
  `attachment_count` tinyint(3) unsigned NOT NULL DEFAULT 0,
  `use_yn` enum('Y','N') CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT 'Y' COMMENT '게시글 사용여부',
  `fix_yn` enum('Y','N') DEFAULT 'N' COMMENT '게시글 고정 여부',
  `guest_name` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '게스트 이름',
  `guest_pass` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '게스트 비밀번호',
  `start_date` datetime DEFAULT NULL COMMENT '게시글 시작일시',
  `expire_date` datetime DEFAULT NULL COMMENT '게시글 만료일시',
  `delete_yn` enum('Y','N') CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT 'N' COMMENT '삭제여부',
  `delete_id` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '삭제자',
  `delete_date` datetime DEFAULT NULL COMMENT '삭제일시',
  `create_id` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '생성자',
  `create_date` datetime DEFAULT current_timestamp() COMMENT '생성일시',
  `last_update_id` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '최종수정자',
  `last_update_date` datetime DEFAULT current_timestamp() COMMENT '최종수정일시',
  PRIMARY KEY (`id`),
  KEY `board_contents_category_fk` (`category_id`) USING BTREE,
  KEY `board_contents_id_IDX` (`id`,`board_id`,`category_id`,`series_id`) USING BTREE,
  KEY `board_contents_series_fk` (`series_id`) USING BTREE,
  KEY `board_contents_setting_fk` (`board_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='게시판 내용';


-- travel_agency.board_setting definition

CREATE TABLE `board_setting` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '게시판 아이디',
  `url` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '게시판 접속 URL',
  `type_code` varchar(6) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '게시판 유형\nboard / faq / album / event',
  `title` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '게시판명',
  `subtitle` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '게시판 부제',
  `thumbnail_yn` enum('Y','N') CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT 'N' COMMENT '썸네일 사용여부 ',
  `banner_large_yn` enum('Y','N') CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT 'N' COMMENT '큰 배너 사용 여부 ',
  `banner_small_yn` enum('Y','N') CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT 'N' COMMENT '작은 배너 사용 여부 ',
  `category_yn` enum('Y','N') CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT 'Y' COMMENT '카테고리 사용 여부',
  `apply_code_yn` enum('Y','N') CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT 'N' COMMENT '접수코드 사용여부',
  `secret_yn` enum('Y','N') CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT 'N' COMMENT '비밀글 사용여부',
  `guest_yn` enum('Y','N') CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT 'N' COMMENT '비회원 사용여부',
  `comment_yn` enum('Y','N') CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT 'N' COMMENT '댓글 기능 사용 여부 ',
  `tag_yn` enum('Y','N') CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT 'N' COMMENT '태그 기능 사용 여부 ',
  `favorite_yn` enum('Y','N') CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT 'N' COMMENT '게시글 추천 수 기능 사용 여부',
  `scrap_yn` enum('Y','N') CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT 'N' COMMENT '게시물 스크랩 기능 사용 여부 ',
  `series_yn` enum('Y','N') CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT 'N' COMMENT '엮인글 기능 사용 여부',
  `default_page_size` tinyint(4) unsigned NOT NULL DEFAULT 20 COMMENT '기본 페이지 사이즈',
  `content_store_days` smallint(6) unsigned NOT NULL DEFAULT 0 COMMENT '콘텐츠 저장 일수 (0:무제한)',
  `use_yn` enum('Y','N') CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT 'Y' COMMENT '게시판 사용 여부',
  `fix_yn` enum('Y','N') DEFAULT 'N' COMMENT '고정 기능 사용 여부',
  `attach_file_size` int(2) unsigned DEFAULT 0 COMMENT '0 무제한, 1이상 개수만큼 제한',
  `start_date` datetime DEFAULT NULL COMMENT '게시판 사용 시작일',
  `expire_date` datetime DEFAULT NULL COMMENT '게시판 사용 만료일',
  `create_id` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '생성자',
  `create_date` datetime DEFAULT NULL COMMENT '생성일시',
  `last_update_id` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '최종수정자',
  `last_update_date` datetime DEFAULT NULL COMMENT '최종수정일시',
  `delete_yn` enum('Y','N') CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT 'N' COMMENT '삭제 여부',
  `delete_id` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '삭제자',
  `delete_date` datetime DEFAULT NULL COMMENT '삭제일시',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='게시판 환경설정';