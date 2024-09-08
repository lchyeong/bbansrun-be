-- 유저 데이터 삽입
INSERT INTO users (id, user_uuid, email, password)
VALUES (1, UNHEX(REPLACE('e7b92e30-bd61-42f3-8e17-1a4c59b8a837', '-', '')), 'user1@a.com',
        '$2a$10$KJKrxz3GsgCt0cAiSpz/dOENvDhPp76uN4j4s6YF6Lq92yma9JplS'),
       (2, UNHEX(REPLACE('f16b37b7-0f8d-4564-82a6-5edbe24433fc', '-', '')), 'user2@a.com',
        '$2a$10$KJKrxz3GsgCt0cAiSpz/dOENvDhPp76uN4j4s6YF6Lq92yma9JplS'),
       (3, UNHEX(REPLACE('8f7597b9-6dd4-4f86-ae8b-19b1913569be', '-', '')), 'user3@a.com',
        '$2a$10$KJKrxz3GsgCt0cAiSpz/dOENvDhPp76uN4j4s6YF6Lq92yma9JplS'),
       (4, UNHEX(REPLACE('21ac7aef-6323-4930-bdb1-7456a8d75d8e', '-', '')), 'admin1@a.com',
        '$2a$10$KJKrxz3GsgCt0cAiSpz/dOENvDhPp76uN4j4s6YF6Lq92yma9JplS'),
       (5, UNHEX(REPLACE('f3b6b9c5-ccad-46b4-b2a2-1a66723a7ac7', '-', '')), 'admin2@a.com',
        '$2a$10$KJKrxz3GsgCt0cAiSpz/dOENvDhPp76uN4j4s6YF6Lq92yma9JplS'),
       (6, UNHEX(REPLACE('bd7b7bff-160d-44a3-95f1-7f6ac437fa7b', '-', '')), 'admin3@a.com',
        '$2a$10$KJKrxz3GsgCt0cAiSpz/dOENvDhPp76uN4j4s6YF6Lq92yma9JplS');


-- 역할 데이터 삽입
-- 일반 유저에게 ROLE_USER 부여
INSERT INTO user_roles (user_id, roles)
VALUES (1, 'ROLE_USER'),
       (2, 'ROLE_USER'),
       (3, 'ROLE_USER');

-- 관리자에게 ROLE_ADMIN 부여
INSERT INTO user_roles (user_id, roles)
VALUES (4, 'ROLE_ADMIN'),
       (5, 'ROLE_ADMIN'),
       (6, 'ROLE_ADMIN');
