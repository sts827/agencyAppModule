-- travel_agency.log_board_view definition

CREATE TABLE `log_board_view` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '고유번호',
  `board_id` int(11) unsigned NOT NULL COMMENT '게시판 아이디',
  `content_id` bigint(20) unsigned DEFAULT NULL COMMENT '게시물 번호',
  `user_token` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '사용자 아이디 token',
  `user_email` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '사용자 아이디',
  `request_host` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '사용자 호스트',
  `request_time` datetime NOT NULL COMMENT '조회 일시',
  `request_agent` varchar(300) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '요청 기기',
  PRIMARY KEY (`id`),
  KEY `board_view_board_fk` (`board_id`) USING BTREE,
  KEY `board_view_content_fk` (`content_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='게시글 조회 로그';