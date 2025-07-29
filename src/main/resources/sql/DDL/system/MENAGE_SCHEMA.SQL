-- travel_agency.manage_menu definition

create or replace TABLE travel_agency.`manage_menu` (
  `menu_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '메뉴ID',
  `upper_menu_id` bigint(20) unsigned DEFAULT NULL COMMENT '상위 메뉴ID',
  `menu_name` varchar(200) NOT NULL COMMENT '메뉴 이름',
  `menu_icon` varchar(100) DEFAULT NULL COMMENT '메뉴 사용 아이콘(font athum',
  `menu_sort` int(4) unsigned DEFAULT NULL COMMENT '메뉴순서',
  `menu_desc` text DEFAULT NULL COMMENT '메뉴 설명',
  `create_id` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '생성자',
  `create_date` datetime DEFAULT NULL COMMENT '생성일시',
  `last_update_id` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '최종수정자',
  `last_update_date` datetime DEFAULT NULL COMMENT '최종수정일시',
  `delete_yn` enum('Y','N') CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT 'N' COMMENT '삭제여부',
  `hide_yn` enum('Y','N') CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT 'N' COMMENT '숨김여부',
  PRIMARY KEY (`menu_id`),
  KEY `manage_menu_upper_menu_id_fk` (`upper_menu_id`),
  CONSTRAINT `manage_menu_upper_menu_id_fk` FOREIGN KEY (`upper_menu_id`) REFERENCES `manage_menu` (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='관리자 메뉴';