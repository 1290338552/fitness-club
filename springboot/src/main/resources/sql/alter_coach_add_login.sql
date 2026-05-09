-- 为 coach 表添加登录相关字段
ALTER TABLE `coach` 
ADD COLUMN `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '登录账号' AFTER `num`,
ADD COLUMN `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '123456' COMMENT '登录密码' AFTER `username`,
ADD COLUMN `phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机号' AFTER `password`,
ADD COLUMN `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱' AFTER `phone`,
ADD COLUMN `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'COACH' COMMENT '角色' AFTER `email`,
ADD COLUMN `avatar` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像' AFTER `role`,
ADD UNIQUE INDEX `idx_username`(`username`) USING BTREE COMMENT '用户名唯一索引';

-- 为现有教练添加登录账号
UPDATE `coach` SET `username` = 'coach_mbg', `password` = '123456', `phone` = '13800000001', `email` = 'mbg@coach.com' WHERE `id` = 1;
UPDATE `coach` SET `username` = 'coach_cat', `password` = '123456', `phone` = '13800000002', `email` = 'cat@coach.com' WHERE `id` = 2;
UPDATE `coach` SET `username` = 'coach_dd', `password` = '123456', `phone` = '13800000003', `email` = 'dd@coach.com' WHERE `id` = 3;
UPDATE `coach` SET `username` = 'coach_asl', `password` = '123456', `phone` = '13800000004', `email` = 'asl@coach.com' WHERE `id` = 4;
UPDATE `coach` SET `username` = 'coach_wl', `password` = '123456', `phone` = '13800000005', `email` = 'wl@coach.com' WHERE `id` = 5;
UPDATE `coach` SET `username` = 'coach_ln', `password` = '123456', `phone` = '13800000006', `email` = 'ln@coach.com' WHERE `id` = 6;

-- 验证数据
SELECT id, coach, name, username, phone, email, role FROM coach;
