-- 유저 데이터 삽입
INSERT INTO users (id, user_uuid, email, password, nick_name)
VALUES (1, UNHEX(REPLACE('e7b92e30-bd61-42f3-8e17-1a4c59b8a837', '-', '')), 'user1@a.com',
        '$2a$10$KJKrxz3GsgCt0cAiSpz/dOENvDhPp76uN4j4s6YF6Lq92yma9JplS', 'User1Nickname'),
       (2, UNHEX(REPLACE('f16b37b7-0f8d-4564-82a6-5edbe24433fc', '-', '')), 'user2@a.com',
        '$2a$10$KJKrxz3GsgCt0cAiSpz/dOENvDhPp76uN4j4s6YF6Lq92yma9JplS', 'User2Nickname'),
       (3, UNHEX(REPLACE('8f7597b9-6dd4-4f86-ae8b-19b1913569be', '-', '')), 'user3@a.com',
        '$2a$10$KJKrxz3GsgCt0cAiSpz/dOENvDhPp76uN4j4s6YF6Lq92yma9JplS', 'User3Nickname'),
       (4, UNHEX(REPLACE('21ac7aef-6323-4930-bdb1-7456a8d75d8e', '-', '')), 'admin1@a.com',
        '$2a$10$KJKrxz3GsgCt0cAiSpz/dOENvDhPp76uN4j4s6YF6Lq92yma9JplS', 'Admin1Nickname'),
       (5, UNHEX(REPLACE('f3b6b9c5-ccad-46b4-b2a2-1a66723a7ac7', '-', '')), 'admin2@a.com',
        '$2a$10$KJKrxz3GsgCt0cAiSpz/dOENvDhPp76uN4j4s6YF6Lq92yma9JplS', 'Admin2Nickname'),
       (6, UNHEX(REPLACE('bd7b7bff-160d-44a3-95f1-7f6ac437fa7b', '-', '')), 'admin3@a.com',
        '$2a$10$KJKrxz3GsgCt0cAiSpz/dOENvDhPp76uN4j4s6YF6Lq92yma9JplS', 'Admin3Nickname');

INSERT INTO user_roles (user_id, roles)
VALUES (1, 'ROLE_USER'),
       (2, 'ROLE_USER'),
       (3, 'ROLE_USER');

-- 관리자에게 ROLE_ADMIN 부여
INSERT INTO user_roles (user_id, roles)
VALUES (4, 'ROLE_ADMIN'),
       (5, 'ROLE_ADMIN'),
       (6, 'ROLE_ADMIN');


-- 지역 카테고리 (영어 이름 컬럼 추가)
-- 부모가 없는 최상위 지역 (대한민국 시도)
INSERT INTO region_category (id, region_name, region_name_eng, region_code, parent_id)
VALUES (1, '서울특별시', 'Seoul', '11', NULL),
       (2, '부산광역시', 'Busan', '26', NULL),
       (3, '대구광역시', 'Daegu', '27', NULL),
       (4, '인천광역시', 'Incheon', '28', NULL),
       (5, '광주광역시', 'Gwangju', '29', NULL),
       (6, '대전광역시', 'Daejeon', '30', NULL),
       (7, '울산광역시', 'Ulsan', '31', NULL),
       (8, '세종특별자치시', 'Sejong', '36', NULL),
       (9, '경기도', 'Gyeonggi', '41', NULL),
       (10, '강원도', 'Gangwon', '42', NULL),
       (11, '충청북도', 'Chungbuk', '43', NULL),
       (12, '충청남도', 'Chungnam', '44', NULL),
       (13, '전라북도', 'Jeonbuk', '45', NULL),
       (14, '전라남도', 'Jeonnam', '46', NULL),
       (15, '경상북도', 'Gyeongbuk', '47', NULL),
       (16, '경상남도', 'Gyeongnam', '48', NULL),
       (17, '제주특별자치도', 'Jeju', '50', NULL);

-- 서울특별시 하위 구 데이터 (영어 이름 추가)
INSERT INTO region_category (id, region_name, region_name_eng, region_code, parent_id)
VALUES (101, '강남구', 'Gangnam-gu', '11-680', 1),
       (102, '강동구', 'Gangdong-gu', '11-740', 1),
       (103, '강북구', 'Gangbuk-gu', '11-305', 1),
       (104, '강서구', 'Gangseo-gu', '11-500', 1),
       (105, '관악구', 'Gwanak-gu', '11-620', 1),
       (106, '광진구', 'Gwangjin-gu', '11-215', 1),
       (107, '구로구', 'Guro-gu', '11-530', 1),
       (108, '금천구', 'Geumcheon-gu', '11-545', 1),
       (109, '노원구', 'Nowon-gu', '11-350', 1),
       (110, '도봉구', 'Dobong-gu', '11-320', 1),
       (111, '동대문구', 'Dongdaemun-gu', '11-230', 1),
       (112, '동작구', 'Dongjak-gu', '11-590', 1),
       (113, '마포구', 'Mapo-gu', '11-440', 1),
       (114, '서대문구', 'Seodaemun-gu', '11-410', 1),
       (115, '서초구', 'Seocho-gu', '11-650', 1),
       (116, '성동구', 'Seongdong-gu', '11-200', 1),
       (117, '성북구', 'Seongbuk-gu', '11-290', 1),
       (118, '송파구', 'Songpa-gu', '11-710', 1),
       (119, '양천구', 'Yangcheon-gu', '11-470', 1),
       (120, '영등포구', 'Yeongdeungpo-gu', '11-560', 1),
       (121, '용산구', 'Yongsan-gu', '11-170', 1),
       (122, '은평구', 'Eunpyeong-gu', '11-380', 1),
       (123, '종로구', 'Jongno-gu', '11-010', 1),
       (124, '중구', 'Jung-gu', '11-140', 1),
       (125, '중랑구', 'Jungnang-gu', '11-260', 1);

-- 경기도 하위 시군구 (31개 시군, 영어 이름 추가)
INSERT INTO region_category (id, region_name, region_name_eng, region_code, parent_id)
VALUES (201, '수원시', 'Suwon-si', '41-110', 9),
       (202, '고양시', 'Goyang-si', '41-280', 9),
       (203, '용인시', 'Yongin-si', '41-460', 9),
       (204, '성남시', 'Seongnam-si', '41-130', 9),
       (205, '부천시', 'Bucheon-si', '41-190', 9),
       (206, '안산시', 'Ansan-si', '41-270', 9),
       (207, '안양시', 'Anyang-si', '41-210', 9),
       (208, '남양주시', 'Namyangju-si', '41-250', 9),
       (209, '화성시', 'Hwaseong-si', '41-200', 9),
       (210, '평택시', 'Pyeongtaek-si', '41-150', 9),
       (211, '의정부시', 'Uijeongbu-si', '41-170', 9),
       (212, '시흥시', 'Siheung-si', '41-250', 9),
       (213, '파주시', 'Paju-si', '41-240', 9),
       (214, '김포시', 'Gimpo-si', '41-220', 9),
       (215, '광명시', 'Gwangmyeong-si', '41-160', 9),
       (216, '광주시', 'Gwangju-si', '41-190', 9),
       (217, '군포시', 'Gunpo-si', '41-230', 9),
       (218, '이천시', 'Icheon-si', '41-180', 9),
       (219, '오산시', 'Osan-si', '41-210', 9),
       (220, '하남시', 'Hanam-si', '41-300', 9),
       (221, '안성시', 'Anseong-si', '41-310', 9),
       (222, '의왕시', 'Uiwang-si', '41-320', 9),
       (223, '여주시', 'Yeoju-si', '41-330', 9),
       (224, '양평군', 'Yangpyeong-gun', '41-340', 9),
       (225, '동두천시', 'Dongducheon-si', '41-350', 9),
       (226, '구리시', 'Guri-si', '41-360', 9),
       (227, '과천시', 'Gwacheon-si', '41-370', 9),
       (228, '가평군', 'Gapyeong-gun', '41-380', 9),
       (229, '연천군', 'Yeoncheon-gun', '41-390', 9),
       (230, '포천시', 'Pocheon-si', '41-400', 9),
       (231, '양주시', 'Yangju-si', '41-410', 9);

-- 강서구 하위 읍면동 데이터 (영어 이름 추가)
INSERT INTO region_category (id, region_name, region_name_eng, region_code, parent_id)
VALUES (1001, '가양동', 'Gayang-dong', '11-500-5200', 104),
       (1002, '등촌동', 'Deungchon-dong', '11-500-6300', 104),
       (1003, '화곡동', 'Hwagok-dong', '11-500-6500', 104),
       (1004, '발산동', 'Balsan-dong', '11-500-6150', 104),
       (1005, '방화동', 'Banghwa-dong', '11-500-6450', 104),
       (1006, '마곡동', 'Magok-dong', '11-500-6710', 104),
       (1007, '염창동', 'Yeomchang-dong', '11-500-6300', 104),
       (1008, '내발산동', 'Naebalsan-dong', '11-500-6510', 104),
       (1009, '외발산동', 'Oebalsan-dong', '11-500-6520', 104),
       (1010, '공항동', 'Gonghang-dong', '11-500-6200', 104);

-- 가양동 (1001)
INSERT INTO region_post (id, title, content, region_category_id)
VALUES (1, '가양동 포스트 1', '가양동 포스트 내용 1', 1001),
       (2, '가양동 포스트 2', '가양동 포스트 내용 2', 1001);

-- 등촌동 (1002)
INSERT INTO region_post (id, title, content, region_category_id)
VALUES (3, '등촌동 포스트 1', '등촌동 포스트 내용 1', 1002),
       (4, '등촌동 포스트 2', '등촌동 포스트 내용 2', 1002);

-- 화곡동 (1003)
INSERT INTO region_post (id, title, content, region_category_id)
VALUES (5, '화곡동 포스트 1', '화곡동 포스트 내용 1', 1003),
       (6, '화곡동 포스트 2', '화곡동 포스트 내용 2', 1003);

-- 발산동 (1004)
INSERT INTO region_post (id, title, content, region_category_id)
VALUES (7, '발산동 포스트 1', '발산동 포스트 내용 1', 1004),
       (8, '발산동 포스트 2', '발산동 포스트 내용 2', 1004);

-- 방화동 (1005)
INSERT INTO region_post (id, title, content, region_category_id)
VALUES (9, '방화동 포스트 1', '방화동 포스트 내용 1', 1005),
       (10, '방화동 포스트 2', '방화동 포스트 내용 2', 1005);

-- 마곡동 (1006)
INSERT INTO region_post (id, title, content, region_category_id)
VALUES (11, '마곡동 포스트 1', '마곡동 포스트 내용 1', 1006),
       (12, '마곡동 포스트 2', '마곡동 포스트 내용 2', 1006);

-- 염창동 (1007)
INSERT INTO region_post (id, title, content, region_category_id)
VALUES (13, '염창동 포스트 1', '염창동 포스트 내용 1', 1007),
       (14, '염창동 포스트 2', '염창동 포스트 내용 2', 1007);

-- 내발산동 (1008)
INSERT INTO region_post (id, title, content, region_category_id)
VALUES (15, '내발산동 포스트 1', '내발산동 포스트 내용 1', 1008),
       (16, '내발산동 포스트 2', '내발산동 포스트 내용 2', 1008);

-- 외발산동 (1009)
INSERT INTO region_post (id, title, content, region_category_id)
VALUES (17, '외발산동 포스트 1', '외발산동 포스트 내용 1', 1009),
       (18, '외발산동 포스트 2', '외발산동 포스트 내용 2', 1009);

-- 공항동 (1010)
INSERT INTO region_post (id, title, content, region_category_id)
VALUES (19, '공항동 포스트 1', '공항동 포스트 내용 1', 1010),
       (20, '공항동 포스트 2', '공항동 포스트 내용 2', 1010);
