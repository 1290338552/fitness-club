/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : localhost:3306
 Source Schema         : quan

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 03/05/2026 13:01:48
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '账号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '名称',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '电话',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'ADMIN' COMMENT '角色',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username_index`(`username`) USING BTREE COMMENT '账号'
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '管理员信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (1, 'admin', 'admin', '管理员4', '19978018409', '1876754046@qq.com', 'ADMIN', 'http://localhost:9999/files/download/1776675868349_1750395286572_shop.jpg');
INSERT INTO `admin` VALUES (6, 'admin22', 'admin', '管理员22', '13454456765', '123@qq.com', 'ADMIN', 'http://localhost:9999/files/download/1776675999396_1750679667700-peach.jpg');
INSERT INTO `admin` VALUES (7, 'test', 'admin', '管理员t22', '13465546765', '235@qq.com', 'ADMIN', 'http://localhost:9999/files/download/1776675119579_1750402736180_woshishangjia.jpg');

-- ----------------------------
-- Table structure for apply
-- ----------------------------
DROP TABLE IF EXISTS `apply`;
CREATE TABLE `apply`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '会员id',
  `course_id` int(11) NULL DEFAULT NULL COMMENT '课程ID',
  `course_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'CUSTOM' COMMENT '预约类型：COURSE(课程预约), CUSTOM(自定义课程)',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '课程标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '说明',
  `time` datetime NULL DEFAULT NULL COMMENT '提交时间',
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '审核状态',
  `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '审核说明',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_course_id`(`course_id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 56 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of apply
-- ----------------------------
INSERT INTO `apply` VALUES (47, 2, 3, 'COURSE', '力量训练进阶', '达到教练的力量训练进阶课程，提升肌肉力量', '2026-03-31 14:40:44', '预约成功', NULL);
INSERT INTO `apply` VALUES (45, 2, 4, 'COURSE', '艾萨拉专业指导', '艾萨拉教练的专业健身指导课程', '2026-03-18 18:14:04', '预约成功', NULL);
INSERT INTO `apply` VALUES (46, 2, 2, 'COURSE', '小猫瑜伽课', '猫大人教练的温和瑜伽课程，适合放松身心', '2026-03-18 21:26:29', '预约成功', NULL);
INSERT INTO `apply` VALUES (44, 2, 1, 'COURSE', '五连鞭基础课程', '马保国教练亲自指导的五连鞭基础训练课程，适合初学者', '2026-03-18 17:48:12', '预约成功', NULL);
INSERT INTO `apply` VALUES (43, 2, 6, 'COURSE', '小狗锻炼法', '预约课程：小狗锻炼法，教练：威龙', '2026-03-18 17:23:39', '预约成功', NULL);
INSERT INTO `apply` VALUES (42, 2, 5, 'COURSE', '威龙综合训练', '威龙教练的综合体能训练课程', '2026-01-09 09:16:56', '预约成功', NULL);
INSERT INTO `apply` VALUES (41, 2, 2, 'COURSE', '小猫瑜伽课', '猫大人教练的温和瑜伽课程，适合放松身心', '2026-01-09 08:31:41', '预约成功', NULL);
INSERT INTO `apply` VALUES (40, 2, 3, 'COURSE', '力量训练进阶', '达到教练的力量训练进阶课程，提升肌肉力量', '2026-01-09 08:26:00', '预约成功', NULL);
INSERT INTO `apply` VALUES (32, 2, NULL, 'CUSTOM', 'wuyang', '无萨达', '2026-01-09 02:07:52', '预约成功', NULL);
INSERT INTO `apply` VALUES (48, 2, 6, 'COURSE', '小狗锻炼法', '小狗速跑', '2026-03-31 14:44:45', '预约成功', NULL);
INSERT INTO `apply` VALUES (49, 2, 6, 'COURSE', '小狗锻炼法', '小狗速跑', '2026-03-31 15:04:15', '预约成功', NULL);
INSERT INTO `apply` VALUES (50, 2, 6, 'COURSE', '小狗锻炼法', '小狗速跑', '2026-03-31 15:19:19', '预约成功', NULL);
INSERT INTO `apply` VALUES (51, 2, 6, 'COURSE', '小狗锻炼法', '小狗速跑', '2026-03-31 20:37:21', '预约成功', NULL);
INSERT INTO `apply` VALUES (52, 2, 4, 'COURSE', '艾萨拉专业指导', '艾萨拉教练的专业健身指导课程', '2026-03-31 20:58:40', '预约成功', NULL);
INSERT INTO `apply` VALUES (53, 2, 6, 'COURSE', '小狗锻炼法', '小狗速跑', '2026-03-31 20:59:52', '预约失败', NULL);
INSERT INTO `apply` VALUES (54, 2, 6, 'COURSE', '小狗锻炼法', '小狗速跑', '2026-04-20 17:02:38', '预约成功', NULL);
INSERT INTO `apply` VALUES (55, 2, 6, 'COURSE', '小狗锻炼法', '小狗速跑', '2026-04-20 17:07:20', '预约成功', NULL);

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '匪类标题',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '健身' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES (1, '有氧');
INSERT INTO `category` VALUES (2, '练胸');
INSERT INTO `category` VALUES (3, '练杯');
INSERT INTO `category` VALUES (4, '无氧');

-- ----------------------------
-- Table structure for coach
-- ----------------------------
DROP TABLE IF EXISTS `coach`;
CREATE TABLE `coach`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '教练图片',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '教练职位',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '教练价格',
  `coach` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '教练名字',
  `num` int(11) NULL DEFAULT NULL,
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '登录账号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '123456' COMMENT '登录密码',
  `phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'COACH' COMMENT '角色',
  `avatar` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_username`(`username`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of coach
-- ----------------------------
INSERT INTO `coach` VALUES (1, 'http://localhost:9999/files/download/1750868212517_mbg1.jpg', '五连鞭', 123.00, '马保国', 10, 'coach_mbg', '123456', '13800000001', 'mbg@coach.com', 'COACH', NULL);
INSERT INTO `coach` VALUES (2, 'http://localhost:9999/files/download/1750868517575_peach.jpg', '小猫11', 123.00, '猫大人', 1, 'coach_cat', '123456', '13800000002', 'cat@coach.com', 'COACH', NULL);
INSERT INTO `coach` VALUES (3, 'http://localhost:9999/files/download/1767896665307_1767886391712_1750395292768_tutou.png', '阿萨大大', 120.00, '达到', 1, 'coach_dd', '123456', '13800000003', 'dd@coach.com', 'COACH', NULL);
INSERT INTO `coach` VALUES (4, 'http://localhost:9999/files/download/1766048794181_1750395273839_peach.jpg', 'zhiye', 100.00, '艾萨拉', 1, 'coach_asl', '123456', '13800000004', 'asl@coach.com', 'COACH', NULL);
INSERT INTO `coach` VALUES (5, 'http://localhost:9999/files/download/1776675876648_1750758617055_mbg1.jpg', 'zhiweiii', 120.00, '威龙', 1, 'coach_wl', '123456', '13800000005', 'wl@coach.com', 'COACH', NULL);
INSERT INTO `coach` VALUES (6, 'http://localhost:9999/files/download/1776675092455_1750402708865_shop.jpg', '小子', 120.00, '露娜', 10, 'coach_ln', '123456', '13800000006', 'ln@coach.com', 'COACH', NULL);

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '课程名称',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '课程描述',
  `price` decimal(10, 2) NOT NULL COMMENT '课程价格',
  `duration` int(11) NOT NULL COMMENT '课程时长(分钟)',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ACTIVE' COMMENT '课程状态：ACTIVE(启用), DISABLED(禁用)',
  `category_id` int(11) NULL DEFAULT NULL COMMENT '课程分类ID',
  `coach_id` int(11) NULL DEFAULT NULL COMMENT '教练ID',
  `max_participants` int(11) NULL DEFAULT 1 COMMENT '最大参与人数',
  `image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '课程图片',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE,
  INDEX `idx_category_id`(`category_id`) USING BTREE,
  INDEX `idx_coach_id`(`coach_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '课程信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of course
-- ----------------------------
INSERT INTO `course` VALUES (6, '小狗锻炼法', '小狗速跑', 100.00, 2, 'ACTIVE', 1, 5, 5, 'http://localhost:9999/files/download/1776674805928_1750395286572_shop.jpg', '2026-01-08 12:24:36', '2026-04-20 16:46:47');

-- ----------------------------
-- Table structure for introduction
-- ----------------------------
DROP TABLE IF EXISTS `introduction`;
CREATE TABLE `introduction`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '健身标准图',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '健身标题',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '健身内容',
  `time` datetime NULL DEFAULT NULL COMMENT '发布时间',
  `category_id` int(11) NULL DEFAULT NULL COMMENT '分类ID',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '健身攻略' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of introduction
-- ----------------------------
INSERT INTO `introduction` VALUES (7, 'http://localhost:9999/files/download/1776675073839_1750668860592_movie.jpg', '无氧运动', '<p>对对对对对对的点点滴滴哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒</p>', '2026-01-07 21:08:39', 2, 2);
INSERT INTO `introduction` VALUES (8, 'http://localhost:9999/files/download/1776675067432_1750679588434-peach.jpg', '游泳运动', '<p>大大大大萨达阿达撒旦王企鹅企鹅请问企鹅请问请问热确认氛围染发v我</p>', '2026-01-08 22:56:15', 1, 2);

-- ----------------------------
-- Table structure for logs
-- ----------------------------
DROP TABLE IF EXISTS `logs`;
CREATE TABLE `logs`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `operation` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作名称',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作类型',
  `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作人',
  `time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 86 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '会员日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of logs
-- ----------------------------
INSERT INTO `logs` VALUES (2, '登录', '登录', '127.0.0.1', 'zhuo', '2025-12-07 12:35:33');
INSERT INTO `logs` VALUES (3, '登录', '登录', '127.0.0.1', 'admin', '2025-12-07 12:36:24');
INSERT INTO `logs` VALUES (4, '登录', '登录', '127.0.0.1', 'ncc', '2025-12-08 11:59:45');
INSERT INTO `logs` VALUES (5, '登录', '登录', '127.0.0.1', 'zhuo', '2025-12-08 12:00:21');
INSERT INTO `logs` VALUES (6, '注册', '注册', '127.0.0.1', 'lbb', '2025-12-08 12:29:02');
INSERT INTO `logs` VALUES (7, '登录', '登录', '127.0.0.1', 'zhuo', '2025-12-08 12:35:37');
INSERT INTO `logs` VALUES (8, '登录', '登录', '127.0.0.1', 'zhuo', '2025-12-08 12:36:34');
INSERT INTO `logs` VALUES (9, '登录', '登录', '127.0.0.1', 'zhuo', '2025-12-18 17:07:11');
INSERT INTO `logs` VALUES (10, '登录', '登录', '127.0.0.1', 'zhuo', '2025-12-18 17:14:42');
INSERT INTO `logs` VALUES (11, '登录', '登录', '127.0.0.1', 'zhuo', '2025-12-19 13:52:56');
INSERT INTO `logs` VALUES (12, '登录', '登录', '127.0.0.1', 'zhuo', '2025-12-19 14:00:05');
INSERT INTO `logs` VALUES (13, '登录', '登录', '127.0.0.1', 'zhuo', '2025-12-19 14:23:05');
INSERT INTO `logs` VALUES (14, '登录', '登录', '127.0.0.1', 'zhuo', '2025-12-19 14:33:57');
INSERT INTO `logs` VALUES (15, '登录', '登录', '127.0.0.1', 'zhuo', '2025-12-19 14:36:32');
INSERT INTO `logs` VALUES (16, '登录', '登录', '127.0.0.1', 'zhuo', '2025-12-19 14:40:34');
INSERT INTO `logs` VALUES (17, '登录', '登录', '127.0.0.1', 'ncc', '2025-12-19 14:46:02');
INSERT INTO `logs` VALUES (18, '登录', '登录', '127.0.0.1', 'zhuo', '2025-12-23 16:20:20');
INSERT INTO `logs` VALUES (19, '登录', '登录', '127.0.0.1', 'zhuo', '2025-12-23 16:24:03');
INSERT INTO `logs` VALUES (20, '登录', '登录', '127.0.0.1', 'zhuo', '2025-12-23 16:32:15');
INSERT INTO `logs` VALUES (21, '登录', '登录', '127.0.0.1', 'zhuo', '2025-12-23 16:35:23');
INSERT INTO `logs` VALUES (22, '登录', '登录', '127.0.0.1', 'zhuo', '2025-12-23 16:36:30');
INSERT INTO `logs` VALUES (23, '登录', '登录', '127.0.0.1', 'zhuo', '2025-12-23 16:41:10');
INSERT INTO `logs` VALUES (24, '登录', '登录', '127.0.0.1', 'zhuo', '2025-12-23 16:43:21');
INSERT INTO `logs` VALUES (25, '登录', '登录', '127.0.0.1', 'zhuo', '2025-12-23 16:45:41');
INSERT INTO `logs` VALUES (26, '登录', '登录', '127.0.0.1', 'zhuo', '2025-12-23 16:51:55');
INSERT INTO `logs` VALUES (27, '登录', '登录', '127.0.0.1', 'zhuo', '2025-12-23 16:52:58');
INSERT INTO `logs` VALUES (28, '登录', '登录', '127.0.0.1', 'zhuo', '2025-12-23 17:16:49');
INSERT INTO `logs` VALUES (29, '登录', '登录', '127.0.0.1', 'zhuo', '2025-12-23 17:19:39');
INSERT INTO `logs` VALUES (30, '登录', '登录', '127.0.0.1', 'zhuo', '2026-01-07 21:06:30');
INSERT INTO `logs` VALUES (31, '登录', '登录', '127.0.0.1', 'zhuo', '2026-01-08 13:15:24');
INSERT INTO `logs` VALUES (32, '登录', '登录', '127.0.0.1', 'zhuo', '2026-01-08 13:17:57');
INSERT INTO `logs` VALUES (33, '登录', '登录', '127.0.0.1', 'zhuo', '2026-01-08 13:18:53');
INSERT INTO `logs` VALUES (34, '登录', '登录', '127.0.0.1', 'zhuo', '2026-01-08 13:38:00');
INSERT INTO `logs` VALUES (35, '登录', '登录', '127.0.0.1', 'zhuo', '2026-01-08 22:16:01');
INSERT INTO `logs` VALUES (36, '登录', '登录', '127.0.0.1', 'zhuo', '2026-01-08 22:55:38');
INSERT INTO `logs` VALUES (37, '登录', '登录', '127.0.0.1', 'zhuo', '2026-01-08 23:04:44');
INSERT INTO `logs` VALUES (38, '登录', '登录', '127.0.0.1', 'zhuo', '2026-01-08 23:05:02');
INSERT INTO `logs` VALUES (39, '登录', '登录', '127.0.0.1', 'zhuo', '2026-01-08 23:40:36');
INSERT INTO `logs` VALUES (40, '登录', '登录', '127.0.0.1', 'zhuo', '2026-01-08 23:51:32');
INSERT INTO `logs` VALUES (41, '登录', '登录', '127.0.0.1', 'zhuo', '2026-01-08 23:52:32');
INSERT INTO `logs` VALUES (42, '登录', '登录', '127.0.0.1', 'zhuo', '2026-01-09 01:13:15');
INSERT INTO `logs` VALUES (43, '登录', '登录', '127.0.0.1', 'admin', '2026-01-09 02:26:33');
INSERT INTO `logs` VALUES (44, '登录', '登录', '127.0.0.1', 'zhuo', '2026-01-09 02:43:06');
INSERT INTO `logs` VALUES (45, '登录', '登录', '127.0.0.1', 'admin', '2026-01-09 02:45:50');
INSERT INTO `logs` VALUES (46, '登录', '登录', '127.0.0.1', 'zhuo', '2026-01-09 08:25:49');
INSERT INTO `logs` VALUES (47, '登录', '登录', '127.0.0.1', 'zhuo', '2026-01-09 09:16:40');
INSERT INTO `logs` VALUES (48, '登录', '登录', '127.0.0.1', 'zhuo', '2026-01-14 22:22:44');
INSERT INTO `logs` VALUES (49, '登录', '登录', '127.0.0.1', 'coach_mbg', '2026-01-14 22:26:12');
INSERT INTO `logs` VALUES (50, '登录', '登录', '127.0.0.1', 'coach_mbg', '2026-01-14 22:26:52');
INSERT INTO `logs` VALUES (51, '登录', '登录', '127.0.0.1', 'coach_mbg', '2026-01-14 22:27:08');
INSERT INTO `logs` VALUES (52, '登录', '登录', '127.0.0.1', 'coach_mbg', '2026-01-14 22:28:43');
INSERT INTO `logs` VALUES (53, '登录', '登录', '127.0.0.1', 'coach_mbg', '2026-03-18 17:21:38');
INSERT INTO `logs` VALUES (54, '登录', '登录', '127.0.0.1', 'zhuo', '2026-03-18 17:22:19');
INSERT INTO `logs` VALUES (55, '登录', '登录', '127.0.0.1', 'zhuo', '2026-03-18 17:23:04');
INSERT INTO `logs` VALUES (56, '登录', '登录', '127.0.0.1', 'coach_mbg', '2026-03-18 17:42:59');
INSERT INTO `logs` VALUES (57, '登录', '登录', '127.0.0.1', 'zhuo', '2026-03-18 17:48:00');
INSERT INTO `logs` VALUES (58, '登录', '登录', '127.0.0.1', 'coach_mbg', '2026-03-18 17:48:46');
INSERT INTO `logs` VALUES (59, '登录', '登录', '127.0.0.1', 'coach_wl', '2026-03-18 17:59:03');
INSERT INTO `logs` VALUES (60, '登录', '登录', '127.0.0.1', 'zhuo', '2026-03-18 18:12:31');
INSERT INTO `logs` VALUES (61, '登录', '登录', '127.0.0.1', 'zhuo', '2026-03-18 18:14:54');
INSERT INTO `logs` VALUES (62, '登录', '登录', '127.0.0.1', 'zhuo', '2026-03-18 21:27:02');
INSERT INTO `logs` VALUES (63, '登录', '登录', '127.0.0.1', 'coach_mbg', '2026-03-18 21:33:54');
INSERT INTO `logs` VALUES (64, '登录', '登录', '127.0.0.1', 'zhuo', '2026-03-18 21:34:21');
INSERT INTO `logs` VALUES (65, '登录', '登录', '127.0.0.1', 'zhuo', '2026-03-20 20:14:03');
INSERT INTO `logs` VALUES (66, '登录', '登录', '127.0.0.1', 'coach_mbg', '2026-03-31 14:41:35');
INSERT INTO `logs` VALUES (67, '登录', '登录', '127.0.0.1', 'zhuo', '2026-03-31 14:41:57');
INSERT INTO `logs` VALUES (68, '登录', '登录', '127.0.0.1', 'zhuo', '2026-03-31 14:44:32');
INSERT INTO `logs` VALUES (69, '登录', '登录', '127.0.0.1', 'zhuo', '2026-03-31 14:45:44');
INSERT INTO `logs` VALUES (70, '登录', '登录', '127.0.0.1', 'coach_mbg', '2026-03-31 14:48:33');
INSERT INTO `logs` VALUES (71, '登录', '登录', '127.0.0.1', 'zhuo', '2026-03-31 14:55:49');
INSERT INTO `logs` VALUES (72, '登录', '登录', '127.0.0.1', 'zhuo', '2026-03-31 15:04:01');
INSERT INTO `logs` VALUES (73, '登录', '登录', '127.0.0.1', 'zhuo', '2026-03-31 15:05:26');
INSERT INTO `logs` VALUES (74, '登录', '登录', '127.0.0.1', 'coach_mbg', '2026-03-31 15:06:59');
INSERT INTO `logs` VALUES (75, '登录', '登录', '127.0.0.1', 'zhuo', '2026-03-31 15:19:11');
INSERT INTO `logs` VALUES (76, '登录', '登录', '127.0.0.1', 'zhuo', '2026-03-31 15:20:27');
INSERT INTO `logs` VALUES (77, '登录', '登录', '127.0.0.1', 'zhuo', '2026-03-31 20:40:39');
INSERT INTO `logs` VALUES (78, '登录', '登录', '127.0.0.1', 'zhuo', '2026-03-31 20:45:43');
INSERT INTO `logs` VALUES (79, '登录', '登录', '127.0.0.1', 'coach_mbg', '2026-03-31 20:47:18');
INSERT INTO `logs` VALUES (80, '登录', '登录', '127.0.0.1', 'zhuo', '2026-03-31 20:58:34');
INSERT INTO `logs` VALUES (81, '登录', '登录', '127.0.0.1', 'zhuo', '2026-03-31 20:59:32');
INSERT INTO `logs` VALUES (82, '登录', '登录', '127.0.0.1', 'zhuo', '2026-04-20 16:52:42');
INSERT INTO `logs` VALUES (83, '登录', '登录', '127.0.0.1', 'zhuo', '2026-04-20 17:02:21');
INSERT INTO `logs` VALUES (84, '登录', '登录', '127.0.0.1', 'zhuo', '2026-04-20 17:06:58');
INSERT INTO `logs` VALUES (85, '登录', '登录', '127.0.0.1', 'zhuo', '2026-04-20 17:08:58');

-- ----------------------------
-- Table structure for mail_template
-- ----------------------------
DROP TABLE IF EXISTS `mail_template`;
CREATE TABLE `mail_template`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `template_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '模板名称',
  `subject` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮件主题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '邮件内容模板',
  `template_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '模板类型：REMINDER(提醒), NOTICE(通知)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_template_type`(`template_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '邮件模板表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of mail_template
-- ----------------------------
INSERT INTO `mail_template` VALUES (1, '课程提醒模板', '【健身房】课程提醒 - {courseName}', '亲爱的{userName}，\r\n\r\n您好！这是来自健身房的课程提醒。\r\n\r\n您预约的课程信息如下：\r\n课程名称：{courseName}\r\n课程描述：{courseDescription}\r\n教练：{coachName}（{coachPosition}）\r\n课程费用：￥{price}\r\n课程时长：{duration}分钟\r\n预约时间：{appointmentTime}\r\n\r\n请您准时参加课程，如有任何问题请及时联系我们。\r\n\r\n祝您健身愉快！\r\n健身房管理团队', 'REMINDER', '2025-12-18 17:12:54', '2025-12-19 14:22:00');

-- ----------------------------
-- Table structure for notice
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '公告标题',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '公告内容',
  `time` datetime NULL DEFAULT NULL COMMENT '发布时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of notice
-- ----------------------------
INSERT INTO `notice` VALUES (3, '体会到卡的', '啊大大时间啊快点回家卡仕达洒基看哈登记卡核打击萨卡核打击看哈打撒的撒旦撒旦撒记得会撒娇扩大花洒金卡很大萨哈打撒金卡很大洒基很大萨卡几哈打撒金卡很大洒基看哈打撒金卡', '2025-06-23 11:22:24');
INSERT INTO `notice` VALUES (4, '体能训练', '跑步', '2025-06-23 11:30:33');
INSERT INTO `notice` VALUES (5, '体能训练2', '攀爬', '2025-06-23 11:30:45');
INSERT INTO `notice` VALUES (6, '体能3', '跳绳', '2025-06-23 11:50:21');
INSERT INTO `notice` VALUES (7, 'dadsada', 'adasda', '2025-06-23 14:06:49');

-- ----------------------------
-- Table structure for recharge
-- ----------------------------
DROP TABLE IF EXISTS `recharge`;
CREATE TABLE `recharge`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '会员ID',
  `amount` decimal(10, 2) NOT NULL COMMENT '充值金额',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'RECHARGE' COMMENT 'RECHARGE充值/CONSUME消费',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of recharge
-- ----------------------------
INSERT INTO `recharge` VALUES (1, 2, 1000.00, 'RECHARGE', '账户充值', '2026-03-18 18:13:49');
INSERT INTO `recharge` VALUES (2, 2, 299.00, 'CONSUME', '预约课程：艾萨拉专业指导', '2026-03-18 18:14:38');
INSERT INTO `recharge` VALUES (3, 2, 1.00, 'RECHARGE', '账户充值', '2026-03-18 21:05:40');
INSERT INTO `recharge` VALUES (4, 2, 89.00, 'CONSUME', '预约课程：小猫瑜伽课', '2026-03-18 21:26:47');
INSERT INTO `recharge` VALUES (5, 2, 159.00, 'CONSUME', '预约课程：力量训练进阶', '2026-03-31 14:41:12');
INSERT INTO `recharge` VALUES (6, 2, 100.00, 'CONSUME', '预约课程：小狗锻炼法', '2026-03-31 14:45:08');
INSERT INTO `recharge` VALUES (7, 2, 100.00, 'CONSUME', '预约课程：小狗锻炼法', '2026-03-31 15:04:56');
INSERT INTO `recharge` VALUES (8, 2, 100.00, 'RECHARGE', '账户充值', '2026-03-31 15:06:29');
INSERT INTO `recharge` VALUES (9, 2, 100.00, 'CONSUME', '预约课程：小狗锻炼法', '2026-03-31 15:19:44');
INSERT INTO `recharge` VALUES (10, 2, 100.00, 'RECHARGE', '账户充值', '2026-03-31 15:21:02');
INSERT INTO `recharge` VALUES (11, 2, 100.00, 'CONSUME', '预约课程：小狗锻炼法', '2026-03-31 20:39:33');
INSERT INTO `recharge` VALUES (12, 2, 100.00, 'RECHARGE', '账户充值', '2026-03-31 20:44:40');
INSERT INTO `recharge` VALUES (13, 2, 299.00, 'CONSUME', '预约课程：艾萨拉专业指导', '2026-03-31 20:59:08');

-- ----------------------------
-- Table structure for record
-- ----------------------------
DROP TABLE IF EXISTS `record`;
CREATE TABLE `record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `coach_id` int(11) NULL DEFAULT NULL COMMENT '教练id',
  `course_id` int(11) NULL DEFAULT NULL COMMENT '课程ID',
  `time` datetime NULL DEFAULT NULL,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_course_id`(`course_id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of record
-- ----------------------------
INSERT INTO `record` VALUES (23, 2, 3, NULL, '2026-01-09 02:09:03', '预约成功', NULL);

-- ----------------------------
-- Table structure for scheduled_mail
-- ----------------------------
DROP TABLE IF EXISTS `scheduled_mail`;
CREATE TABLE `scheduled_mail`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户ID',
  `record_id` int(11) NULL DEFAULT NULL COMMENT '预约记录ID',
  `to_email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '收件人邮箱',
  `subject` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮件主题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '邮件内容',
  `scheduled_time` datetime NULL DEFAULT NULL COMMENT '计划发送时间',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'PENDING' COMMENT '状态：PENDING(待发送), SENT(已发送), FAILED(发送失败), COMPLETED(已完成)',
  `sent_count` int(11) NULL DEFAULT 0 COMMENT '已发送次数',
  `max_send_count` int(11) NULL DEFAULT 2 COMMENT '最大发送次数',
  `last_sent_time` datetime NULL DEFAULT NULL COMMENT '最后发送时间',
  `next_send_time` datetime NULL DEFAULT NULL COMMENT '下次发送时间',
  `error_message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '错误信息',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_status_next_send_time`(`status`, `next_send_time`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_record_id`(`record_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 51 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '定时邮件任务表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of scheduled_mail
-- ----------------------------
INSERT INTO `scheduled_mail` VALUES (1, 2, 15, '1290338552@qq.com', '【健身房】课程提醒 - 威龙教练课程', '亲爱的做做嘛~，\r\n\r\n您好！这是来自健身房的课程提醒。\r\n\r\n您预约的课程信息如下：\r\n教练：威龙（zhiweiii）\r\n课程费用：￥12.00\r\n预约时间：2025-12-18 17:14:48\r\n\r\n请您准时参加课程，如有任何问题请及时联系我们。\r\n\r\n祝您健身愉快！\r\n健身房管理团队', '2025-12-17 17:14:48', 'COMPLETED', 2, 2, '2025-12-19 12:35:06', NULL, NULL, '2025-12-18 17:15:20');
INSERT INTO `scheduled_mail` VALUES (2, 10, NULL, '1290338552@qq.com', '【健身房】课程预约成功 - 威龙综合训练', '亲爱的大哥哥~，\n\n您好！您的课程预约已审核通过。\n\n预约信息如下：\n课程名称：威龙综合训练\n课程描述：威龙教练的综合体能训练课程\n教练：威龙\n课程费用：￥179.00\n课程时长：75分钟\n预约时间：2025-12-19 14:46:27\n\n请您准时参加课程，如有任何问题请及时联系我们。\n\n祝您健身愉快！\n健身房管理团队', '2025-12-19 14:52:01', 'COMPLETED', 2, 2, '2025-12-19 15:05:02', NULL, NULL, '2025-12-19 14:47:01');
INSERT INTO `scheduled_mail` VALUES (3, 2, NULL, '1290338552@qq.com', '【健身房】课程预约成功 - 力量训练进阶', '亲爱的做做嘛~，\n\n您好！您的课程预约已审核通过。\n\n预约信息如下：\n课程名称：力量训练进阶\n课程描述：达到教练的力量训练进阶课程，提升肌肉力量\n教练：达到\n课程费用：￥159.00\n课程时长：90分钟\n预约时间：2025-12-23 17:14:01\n\n请您准时参加课程，如有任何问题请及时联系我们。\n\n祝您健身愉快！\n健身房管理团队', '2025-12-23 17:20:03', 'COMPLETED', 2, 2, '2026-01-07 21:20:04', NULL, NULL, '2025-12-23 17:15:03');
INSERT INTO `scheduled_mail` VALUES (4, 2, NULL, '1290338552@qq.com', '【健身房】课程预约成功 - 小狗锻炼法', '亲爱的卓卓嘛~，\n\n您好！您的课程预约已审核通过。\n\n预约信息如下：\n课程名称：小狗锻炼法\n课程描述：小狗速跑\n教练：威龙\n课程费用：￥100.00\n课程时长：2分钟\n预约时间：2026-01-08 13:19:04\n\n请您准时参加课程，如有任何问题请及时联系我们。\n\n祝您健身愉快！\n健身房管理团队', '2026-01-08 13:24:23', 'COMPLETED', 2, 2, '2026-01-08 13:30:06', NULL, NULL, '2026-01-08 13:19:23');
INSERT INTO `scheduled_mail` VALUES (5, 2, NULL, '1290338552@qq.com', '【健身房】课程预约成功 - 五连鞭基础课程', '亲爱的卓卓嘛~，\n\n您好！您的课程预约已审核通过。\n\n预约信息如下：\n课程名称：五连鞭基础课程\n课程描述：马保国教练亲自指导的五连鞭基础训练课程，适合初学者\n教练：马保国\n课程费用：￥199.00\n课程时长：60分钟\n预约时间：2026-01-08 13:24:25\n\n请您准时参加课程，如有任何问题请及时联系我们。\n\n祝您健身愉快！\n健身房管理团队', '2026-01-08 13:29:33', 'COMPLETED', 2, 2, '2026-01-08 13:31:02', NULL, NULL, '2026-01-08 13:24:33');
INSERT INTO `scheduled_mail` VALUES (6, 2, NULL, '1290338552@qq.com', '【健身房】课程预约成功 - 小猫瑜伽课', '亲爱的卓卓嘛~，\n\n您好！您的课程预约已审核通过。\n\n预约信息如下：\n课程名称：小猫瑜伽课\n课程描述：猫大人教练的温和瑜伽课程，适合放松身心\n教练：猫大人\n课程费用：￥89.00\n课程时长：45分钟\n预约时间：2026-01-08 13:29:41\n\n请您准时参加课程，如有任何问题请及时联系我们。\n\n祝您健身愉快！\n健身房管理团队', '2026-01-08 13:34:48', 'COMPLETED', 2, 2, '2026-01-08 13:36:04', NULL, NULL, '2026-01-08 13:29:48');
INSERT INTO `scheduled_mail` VALUES (7, 2, NULL, '1290338552@qq.com', '【健身房】课程预约成功 - 艾萨拉专业指导', '亲爱的卓卓嘛~，\n\n您好！您的课程预约已审核通过。\n\n预约信息如下：\n课程名称：艾萨拉专业指导\n课程描述：艾萨拉教练的专业健身指导课程\n教练：艾萨拉\n课程费用：￥299.00\n课程时长：120分钟\n预约时间：2026-01-08 13:38:40\n\n请您准时参加课程，如有任何问题请及时联系我们。\n\n祝您健身愉快！\n健身房管理团队', '2026-01-08 13:43:50', 'COMPLETED', 2, 2, '2026-01-08 13:45:02', NULL, NULL, '2026-01-08 13:38:50');
INSERT INTO `scheduled_mail` VALUES (8, 2, NULL, '1290338552@qq.com', '【健身房】课程预约成功 - 小猫瑜伽课', '亲爱的卓卓嘛~，\n\n您好！您的课程预约已审核通过。\n\n预约信息如下：\n课程名称：小猫瑜伽课\n课程描述：猫大人教练的温和瑜伽课程，适合放松身心\n教练：猫大人\n课程费用：￥89.00\n课程时长：45分钟\n预约时间：2026-01-08 14:24:15\n\n请您准时参加课程，如有任何问题请及时联系我们。\n\n祝您健身愉快！\n健身房管理团队', '2026-01-08 14:29:27', 'COMPLETED', 2, 2, '2026-01-08 14:31:02', NULL, NULL, '2026-01-08 14:24:27');
INSERT INTO `scheduled_mail` VALUES (9, 2, NULL, '1290338552@qq.com', '【健身房】课程预约成功 - 威龙综合训练', '亲爱的卓卓嘛~，\n\n您好！您的课程预约已审核通过。\n\n预约信息如下：\n课程名称：威龙综合训练\n课程描述：威龙教练的综合体能训练课程\n教练：威龙\n课程费用：￥179.00\n课程时长：75分钟\n预约时间：2026-01-08 14:31:33\n\n请您准时参加课程，如有任何问题请及时联系我们。\n\n祝您健身愉快！\n健身房管理团队', '2026-01-08 14:36:40', 'COMPLETED', 2, 2, '2026-01-08 14:38:02', NULL, NULL, '2026-01-08 14:31:40');
INSERT INTO `scheduled_mail` VALUES (10, 2, NULL, '1290338552@qq.com', '【健身房】课程预约成功 - 威龙综合训练', '亲爱的卓卓嘛~，\n\n您好！您的课程预约已审核通过。\n\n预约信息如下：\n课程名称：威龙综合训练\n课程描述：威龙教练的综合体能训练课程\n教练：威龙\n课程费用：￥179.00\n课程时长：75分钟\n预约时间：2026-01-08 14:35:46\n\n请您准时参加课程，如有任何问题请及时联系我们。\n\n祝您健身愉快！\n健身房管理团队', '2026-01-08 14:40:52', 'COMPLETED', 2, 2, '2026-01-08 14:41:32', NULL, NULL, '2026-01-08 14:35:52');
INSERT INTO `scheduled_mail` VALUES (11, 2, NULL, '1290338552@qq.com', '【健身房】课程预约成功 - 力量训练进阶', '亲爱的卓卓嘛~，\n\n您好！您的课程预约已审核通过。\n\n预约信息如下：\n课程名称：力量训练进阶\n课程描述：达到教练的力量训练进阶课程，提升肌肉力量\n教练：达到\n课程费用：￥159.00\n课程时长：90分钟\n预约时间：2026-01-08 14:39:51\n\n请您准时参加课程，如有任何问题请及时联系我们。\n\n祝您健身愉快！\n健身房管理团队', '2026-01-08 14:44:58', 'COMPLETED', 2, 2, '2026-01-08 14:45:37', NULL, NULL, '2026-01-08 14:39:58');
INSERT INTO `scheduled_mail` VALUES (12, 2, NULL, '1290338552@qq.com', '【健身房】课程预约成功 - 小狗锻炼法', '亲爱的卓卓嘛~，\n\n您好！您的课程预约已审核通过。\n\n预约信息如下：\n课程名称：小狗锻炼法\n课程描述：小狗速跑\n教练：威龙\n课程费用：￥100.00\n课程时长：2分钟\n预约时间：2026-01-08 14:45:45\n\n请您准时参加课程，如有任何问题请及时联系我们。\n\n祝您健身愉快！\n健身房管理团队', '2026-01-08 14:50:51', 'COMPLETED', 2, 2, '2026-01-08 14:51:31', NULL, NULL, '2026-01-08 14:45:51');
INSERT INTO `scheduled_mail` VALUES (13, 2, NULL, '1290338552@qq.com', '【健身房】课程预约成功 - 威龙综合训练', '亲爱的卓卓嘛~，\n\n您好！您的课程预约已审核通过。\n\n预约信息如下：\n课程名称：威龙综合训练\n课程描述：威龙教练的综合体能训练课程\n教练：威龙\n课程费用：￥179.00\n课程时长：75分钟\n预约时间：2026-01-08 14:54:54\n\n请您准时参加课程，如有任何问题请及时联系我们。\n\n祝您健身愉快！\n健身房管理团队', '2026-01-08 14:55:00', 'COMPLETED', 2, 2, '2026-01-08 14:55:37', NULL, NULL, '2026-01-08 14:55:00');
INSERT INTO `scheduled_mail` VALUES (14, 2, NULL, '1290338552@qq.com', '【健身房】课程预约成功 - 小狗锻炼法', '亲爱的卓卓嘛~，\n\n您好！您的课程预约已审核通过。\n\n预约信息如下：\n课程名称：小狗锻炼法\n课程描述：小狗速跑\n教练：威龙\n课程费用：￥100.00\n课程时长：2分钟\n预约时间：2026-01-08 14:55:48\n\n请您准时参加课程，如有任何问题请及时联系我们。\n\n祝您健身愉快！\n健身房管理团队', '2026-01-08 14:56:02', 'COMPLETED', 2, 2, '2026-01-08 14:56:41', NULL, NULL, '2026-01-08 14:56:02');
INSERT INTO `scheduled_mail` VALUES (15, 2, NULL, '1290338552@qq.com', '【健身房】课程预约成功 - 五连鞭基础课程', '亲爱的卓卓嘛~，\n\n您好！您的课程预约已审核通过。\n\n预约信息如下：\n课程名称：五连鞭基础课程\n课程描述：马保国教练亲自指导的五连鞭基础训练课程，适合初学者\n教练：马保国\n课程费用：￥199.00\n课程时长：60分钟\n预约时间：2026-01-08 14:59:27\n\n请您准时参加课程，如有任何问题请及时联系我们。\n\n祝您健身愉快！\n健身房管理团队', '2026-01-08 14:59:35', 'COMPLETED', 2, 2, '2026-01-08 15:00:31', NULL, NULL, '2026-01-08 14:59:35');
INSERT INTO `scheduled_mail` VALUES (16, 2, NULL, '1290338552@qq.com', '【健身房】课程预约成功 - 健身哑铃', '亲爱的卓卓嘛~，\n\n您好！您的课程预约已审核通过。\n\n预约信息如下：\n课程标题：健身哑铃\n课程内容：温度计啊忘记打卡大家啊\n预约时间：2026-01-08 15:00:10\n\n请您准时参加课程，如有任何问题请及时联系我们。\n\n祝您健身愉快！\n健身房管理团队', '2026-01-08 15:00:28', 'FAILED', 1, 2, NULL, '2026-01-09 00:43:05', 'Authentication failed', '2026-01-08 15:00:28');
INSERT INTO `scheduled_mail` VALUES (17, 2, 21, '1290338552@qq.com', '【健身房】课程提醒 - {courseName}', '亲爱的卓卓嘛~，\r\n\r\n您好！这是来自健身房的课程提醒。\r\n\r\n您预约的课程信息如下：\r\n课程名称：{courseName}\r\n课程描述：{courseDescription}\r\n教练：露娜（小子）\r\n课程费用：￥22.00\r\n课程时长：{duration}分钟\r\n预约时间：2026-01-09 00:42:21\r\n\r\n请您准时参加课程，如有任何问题请及时联系我们。\r\n\r\n祝您健身愉快！\r\n健身房管理团队', '2026-01-09 00:42:33', 'FAILED', 0, 2, NULL, '2026-01-09 00:43:06', 'Authentication failed', '2026-01-09 00:42:33');
INSERT INTO `scheduled_mail` VALUES (18, 2, 21, '1290338552@qq.com', '【健身房】课程提醒 - {courseName}', '亲爱的卓卓嘛~，\r\n\r\n您好！这是来自健身房的课程提醒。\r\n\r\n您预约的课程信息如下：\r\n课程名称：{courseName}\r\n课程描述：{courseDescription}\r\n教练：露娜（小子）\r\n课程费用：￥22.00\r\n课程时长：{duration}分钟\r\n预约时间：2026-01-09 00:42:21\r\n\r\n请您准时参加课程，如有任何问题请及时联系我们。\r\n\r\n祝您健身愉快！\r\n健身房管理团队', '2026-01-09 00:42:35', 'FAILED', 0, 2, NULL, '2026-01-09 00:49:35', 'Authentication failed', '2026-01-09 00:42:35');
INSERT INTO `scheduled_mail` VALUES (19, 2, NULL, '1290338552@qq.com', '【健身房】课程预约成功 - 小狗锻炼法', '亲爱的卓卓嘛~，\n\n您好！您的课程预约已审核通过。\n\n预约信息如下：\n课程名称：小狗锻炼法\n课程描述：小狗速跑\n教练：威龙\n课程费用：￥100.00\n课程时长：2分钟\n预约时间：2026-01-09 00:48:57\n\n请您准时参加课程，如有任何问题请及时联系我们。\n\n祝您健身愉快！\n健身房管理团队', '2026-01-09 00:49:05', 'FAILED', 0, 2, NULL, '2026-01-09 00:58:08', 'Authentication failed', '2026-01-09 00:49:05');
INSERT INTO `scheduled_mail` VALUES (20, 2, 22, '1290338552@qq.com', '【健身房】课程提醒 - {courseName}', '亲爱的卓卓嘛~，\r\n\r\n您好！这是来自健身房的课程提醒。\r\n\r\n您预约的课程信息如下：\r\n课程名称：{courseName}\r\n课程描述：{courseDescription}\r\n教练：艾萨拉（zhiye）\r\n课程费用：￥1.00\r\n课程时长：{duration}分钟\r\n预约时间：2026-01-09 00:57:29\r\n\r\n请您准时参加课程，如有任何问题请及时联系我们。\r\n\r\n祝您健身愉快！\r\n健身房管理团队', '2026-01-09 00:57:37', 'FAILED', 0, 2, NULL, '2026-01-09 01:00:58', 'Authentication failed', '2026-01-09 00:57:37');
INSERT INTO `scheduled_mail` VALUES (21, 2, NULL, '1290338552@qq.com', '【健身房】课程预约成功 - 小猫瑜伽课', '亲爱的卓卓嘛~，\n\n您好！您的课程预约已审核通过。\n\n预约信息如下：\n课程名称：小猫瑜伽课\n课程描述：猫大人教练的温和瑜伽课程，适合放松身心\n教练：猫大人\n课程费用：￥89.00\n课程时长：45分钟\n预约时间：2026-01-09 01:00:18\n\n请您准时参加课程，如有任何问题请及时联系我们。\n\n祝您健身愉快！\n健身房管理团队', '2026-01-09 01:00:26', 'FAILED', 0, 2, NULL, '2026-01-09 01:20:02', 'Authentication failed', '2026-01-09 01:00:26');
INSERT INTO `scheduled_mail` VALUES (22, 2, NULL, '1290338552@qq.com', '【健身房】课程预约成功 - 威龙综合训练', '亲爱的卓卓嘛~，\n\n您好！您的课程预约已审核通过。\n\n预约信息如下：\n课程名称：威龙综合训练\n课程描述：威龙教练的综合体能训练课程\n教练：威龙\n课程费用：￥179.00\n课程时长：75分钟\n预约时间：2026-01-09 01:26:26\n\n请您准时参加课程，如有任何问题请及时联系我们。\n\n祝您健身愉快！\n健身房管理团队', '2026-01-09 01:31:35', 'FAILED', 0, 2, NULL, '2026-01-09 01:32:32', 'Authentication failed', '2026-01-09 01:26:35');
INSERT INTO `scheduled_mail` VALUES (23, 2, NULL, '1290338552@qq.com', '【健身房】课程预约成功 - 威龙综合训练', '亲爱的卓卓嘛~，\n\n您好！您的课程预约已审核通过。\n\n预约信息如下：\n课程名称：威龙综合训练\n课程描述：威龙教练的综合体能训练课程\n教练：威龙\n课程费用：￥179.00\n课程时长：75分钟\n预约时间：2026-01-09 01:28:18\n\n请您准时参加课程，如有任何问题请及时联系我们。\n\n祝您健身愉快！\n健身房管理团队', '2026-01-09 01:33:29', 'FAILED', 0, 2, NULL, '2026-01-09 01:34:01', 'Authentication failed', '2026-01-09 01:28:29');
INSERT INTO `scheduled_mail` VALUES (24, 2, NULL, '1290338552@qq.com', '【健身房】课程预约成功 - 小狗锻炼法', '亲爱的卓卓嘛~，\n\n您好！您的课程预约已审核通过。\n\n预约信息如下：\n课程名称：小狗锻炼法\n课程描述：小狗速跑\n教练：威龙\n课程费用：￥100.00\n课程时长：2分钟\n预约时间：2026-01-09 01:37:16\n\n请您准时参加课程，如有任何问题请及时联系我们。\n\n祝您健身愉快！\n健身房管理团队', '2026-01-09 01:42:22', 'FAILED', 0, 2, NULL, '2026-01-09 01:43:01', 'Authentication failed', '2026-01-09 01:37:22');
INSERT INTO `scheduled_mail` VALUES (25, 2, NULL, '1290338552@qq.com', '【健身房】课程预约成功 - 五连鞭基础课程', '亲爱的卓卓嘛~，\n\n您好！您的课程预约已审核通过。\n\n预约信息如下：\n课程名称：五连鞭基础课程\n课程描述：马保国教练亲自指导的五连鞭基础训练课程，适合初学者\n教练：马保国\n课程费用：￥199.00\n课程时长：60分钟\n预约时间：2026-01-09 01:41:18\n\n请您准时参加课程，如有任何问题请及时联系我们。\n\n祝您健身愉快！\n健身房管理团队', '2026-01-09 01:41:24', 'FAILED', 0, 2, NULL, '2026-01-09 01:42:02', 'Authentication failed', '2026-01-09 01:41:24');
INSERT INTO `scheduled_mail` VALUES (26, 2, NULL, '1290338552@qq.com', '【第2次提醒】【健身房】课程预约成功 - 小猫瑜伽课', '【最后一次提醒】\n\n亲爱的卓卓嘛~，\n\n您好！您的课程预约已审核通过。\n\n预约信息如下：\n课程名称：小猫瑜伽课\n课程描述：猫大人教练的温和瑜伽课程，适合放松身心\n教练：猫大人\n课程费用：￥89.00\n课程时长：45分钟\n预约时间：2026-01-09 02:06:22\n\n请您准时参加课程，如有任何问题请及时联系我们。\n\n祝您健身愉快！\n健身房管理团队\n\n这是最后一次提醒，请务必准时参加！', '2026-01-09 02:06:32', 'COMPLETED', 2, 2, '2026-01-09 02:08:03', NULL, NULL, '2026-01-09 02:06:32');
INSERT INTO `scheduled_mail` VALUES (27, 2, NULL, '1290338552@qq.com', '【第2次提醒】【健身房】课程预约成功 - 威龙综合训练', '【最后一次提醒】\n\n亲爱的卓卓嘛~，\n\n您好！您的课程预约已审核通过。\n\n预约信息如下：\n课程名称：威龙综合训练\n课程描述：威龙教练的综合体能训练课程\n教练：威龙\n课程费用：￥179.00\n课程时长：75分钟\n预约时间：2026-01-09 02:07:24\n\n请您准时参加课程，如有任何问题请及时联系我们。\n\n祝您健身愉快！\n健身房管理团队\n\n这是最后一次提醒，请务必准时参加！', '2026-01-09 02:07:32', 'COMPLETED', 2, 2, '2026-01-09 02:09:02', NULL, NULL, '2026-01-09 02:07:32');
INSERT INTO `scheduled_mail` VALUES (28, 2, NULL, '1290338552@qq.com', '【第2次提醒】【健身房】课程预约成功 - wuyang', '【最后一次提醒】\n\n亲爱的卓卓嘛~，\n\n您好！您的课程预约已审核通过。\n\n预约信息如下：\n课程标题：wuyang\n课程内容：无萨达\n预约时间：2026-01-09 02:07:52\n\n请您准时参加课程，如有任何问题请及时联系我们。\n\n祝您健身愉快！\n健身房管理团队\n\n这是最后一次提醒，请务必准时参加！', '2026-01-09 02:08:00', 'COMPLETED', 2, 2, '2026-01-09 02:09:04', NULL, NULL, '2026-01-09 02:08:00');
INSERT INTO `scheduled_mail` VALUES (29, 2, 23, '1290338552@qq.com', '【第2次提醒】【健身房】预约成功通知 - 达到教练', '【最后一次提醒】\n\n亲爱的卓卓嘛~，\n\n您好！您的教练预约已审核通过。\n\n预约信息如下：\n教练：达到（阿萨大大）\n课程费用：￥121.00\n预约时间：2026-01-09 02:09:03\n\n请您准时参加课程，如有任何问题请及时联系我们。\n\n祝您健身愉快！\n健身房管理团队\n\n这是最后一次提醒，请务必准时参加！', '2026-01-09 02:09:18', 'COMPLETED', 2, 2, '2026-01-09 02:10:32', NULL, NULL, '2026-01-09 02:09:18');
INSERT INTO `scheduled_mail` VALUES (30, 7, NULL, '3166349382@aa.com', '【第2次提醒】【健身房】课程预约成功 - 小狗锻炼法', '【最后一次提醒】\n\n亲爱的admin，\n\n您好！您的课程预约已审核通过。\n\n预约信息如下：\n课程名称：小狗锻炼法\n课程描述：小狗速跑\n教练：威龙\n课程费用：￥100.00\n课程时长：2分钟\n预约时间：2026-01-09 02:26:42\n\n请您准时参加课程，如有任何问题请及时联系我们。\n\n祝您健身愉快！\n健身房管理团队\n\n这是最后一次提醒，请务必准时参加！', '2026-01-09 02:26:52', 'COMPLETED', 2, 2, '2026-01-09 02:28:03', NULL, NULL, '2026-01-09 02:26:52');
INSERT INTO `scheduled_mail` VALUES (31, 7, NULL, '3166349382@aa.com', '【第2次提醒】【健身房】课程预约成功 - 威龙综合训练', '【最后一次提醒】\n\n亲爱的admin，\n\n您好！您的课程预约已审核通过。\n\n预约信息如下：\n课程名称：威龙综合训练\n课程描述：威龙教练的综合体能训练课程\n教练：威龙\n课程费用：￥179.00\n课程时长：75分钟\n预约时间：2026-01-09 02:33:02\n\n请您准时参加课程，如有任何问题请及时联系我们。\n\n祝您健身愉快！\n健身房管理团队\n\n这是最后一次提醒，请务必准时参加！', '2026-01-09 02:33:16', 'COMPLETED', 2, 2, '2026-01-09 02:34:33', NULL, NULL, '2026-01-09 02:33:16');
INSERT INTO `scheduled_mail` VALUES (32, 7, NULL, '1290338552@aa.com', '【第2次提醒】【健身房】课程预约成功 - 艾萨拉专业指导', '【最后一次提醒】\n\n亲爱的admin，\n\n您好！您的课程预约已审核通过。\n\n预约信息如下：\n课程名称：艾萨拉专业指导\n课程描述：艾萨拉教练的专业健身指导课程\n教练：艾萨拉\n课程费用：￥299.00\n课程时长：120分钟\n预约时间：2026-01-09 02:34:24\n\n请您准时参加课程，如有任何问题请及时联系我们。\n\n祝您健身愉快！\n健身房管理团队\n\n这是最后一次提醒，请务必准时参加！', '2026-01-09 02:34:33', 'COMPLETED', 2, 2, '2026-01-09 02:36:02', NULL, NULL, '2026-01-09 02:34:33');
INSERT INTO `scheduled_mail` VALUES (33, 7, NULL, '3166349382@aa.com', '【第2次提醒】【健身房】课程预约成功 - 威龙综合训练', '【最后一次提醒】\n\n亲爱的admin，\n\n您好！您的课程预约已审核通过。\n\n预约信息如下：\n课程名称：威龙综合训练\n课程描述：威龙教练的综合体能训练课程\n教练：威龙\n课程费用：￥179.00\n课程时长：75分钟\n预约时间：2026-01-09 02:41:33\n\n请您准时参加课程，如有任何问题请及时联系我们。\n\n祝您健身愉快！\n健身房管理团队\n\n这是最后一次提醒，请务必准时参加！', '2026-01-09 02:41:46', 'COMPLETED', 2, 2, '2026-01-09 02:43:02', NULL, NULL, '2026-01-09 02:41:46');
INSERT INTO `scheduled_mail` VALUES (34, 2, NULL, '1290338552@qq.com', '【第2次提醒】【健身房】课程预约成功 - 小狗锻炼法', '【最后一次提醒】\n\n亲爱的卓卓嘛~，\n\n您好！您的课程预约已审核通过。\n\n预约信息如下：\n课程名称：小狗锻炼法\n课程描述：小狗速跑\n教练：威龙\n课程费用：￥100.00\n课程时长：2分钟\n预约时间：2026-01-09 02:43:13\n\n请您准时参加课程，如有任何问题请及时联系我们。\n\n祝您健身愉快！\n健身房管理团队\n\n这是最后一次提醒，请务必准时参加！', '2026-01-09 02:43:24', 'COMPLETED', 2, 2, '2026-01-09 02:44:32', NULL, NULL, '2026-01-09 02:43:24');
INSERT INTO `scheduled_mail` VALUES (35, 7, NULL, '3620296229@aa.com', '【第2次提醒】【健身房】课程预约成功 - 小狗锻炼法', '【最后一次提醒】\n\n亲爱的admin，\n\n您好！您的课程预约已审核通过。\n\n预约信息如下：\n课程名称：小狗锻炼法\n课程描述：小狗速跑\n教练：威龙\n课程费用：￥100.00\n课程时长：2分钟\n预约时间：2026-01-09 02:45:57\n\n请您准时参加课程，如有任何问题请及时联系我们。\n\n祝您健身愉快！\n健身房管理团队\n\n这是最后一次提醒，请务必准时参加！', '2026-01-09 02:46:04', 'COMPLETED', 2, 2, '2026-01-09 02:47:32', NULL, NULL, '2026-01-09 02:46:04');
INSERT INTO `scheduled_mail` VALUES (36, 7, NULL, '3620296229@qq.com', '【第2次提醒】【健身房】课程预约成功 - 小狗锻炼法', '【最后一次提醒】\n\n亲爱的admin，\n\n您好！您的课程预约已审核通过。\n\n预约信息如下：\n课程名称：小狗锻炼法\n课程描述：小狗速跑\n教练：威龙\n课程费用：￥100.00\n课程时长：2分钟\n预约时间：2026-01-09 02:47:47\n\n请您准时参加课程，如有任何问题请及时联系我们。\n\n祝您健身愉快！\n健身房管理团队\n\n这是最后一次提醒，请务必准时参加！', '2026-01-09 02:47:55', 'COMPLETED', 2, 2, '2026-01-09 02:49:02', NULL, NULL, '2026-01-09 02:47:55');
INSERT INTO `scheduled_mail` VALUES (37, 2, NULL, '1290338552@qq.com', '【第2次提醒】【健身房】课程预约成功 - 小猫瑜伽课', '【最后一次提醒】\n\n亲爱的卓卓嘛~，\n\n您好！您的课程预约已审核通过。\n\n预约信息如下：\n课程名称：小猫瑜伽课\n课程描述：猫大人教练的温和瑜伽课程，适合放松身心\n教练：猫大人\n课程费用：￥89.00\n课程时长：45分钟\n预约时间：2026-01-09 08:31:41\n\n请您准时参加课程，如有任何问题请及时联系我们。\n\n祝您健身愉快！\n健身房管理团队\n\n这是最后一次提醒，请务必准时参加！', '2026-01-09 08:31:53', 'COMPLETED', 2, 2, '2026-01-09 08:33:04', NULL, NULL, '2026-01-09 08:31:53');
INSERT INTO `scheduled_mail` VALUES (38, 2, NULL, '1290338552@qq.com', '【第2次提醒】【健身房】课程预约成功 - 威龙综合训练', '【最后一次提醒】\n\n亲爱的卓卓嘛~，\n\n您好！您的课程预约已审核通过。\n\n预约信息如下：\n课程名称：威龙综合训练\n课程描述：威龙教练的综合体能训练课程\n教练：威龙\n课程费用：￥179.00\n课程时长：75分钟\n预约时间：2026-01-09 09:16:56\n\n请您准时参加课程，如有任何问题请及时联系我们。\n\n祝您健身愉快！\n健身房管理团队\n\n这是最后一次提醒，请务必准时参加！', '2026-01-09 09:17:48', 'COMPLETED', 2, 2, '2026-01-09 09:19:05', NULL, NULL, '2026-01-09 09:17:48');
INSERT INTO `scheduled_mail` VALUES (39, 2, NULL, '1290338552@qq.com', '【第2次提醒】【健身房】课程预约成功 - 小狗锻炼法', '【最后一次提醒】\n\n亲爱的卓卓嘛~，\n\n您好！您的课程预约已审核通过。\n\n预约信息如下：\n课程名称：小狗锻炼法\n课程描述：小狗速跑\n教练：威龙\n课程费用：￥100.00\n课程时长：2分钟\n预约时间：2026-03-18 17:23:39\n\n请您准时参加课程，如有任何问题请及时联系我们。\n\n祝您健身愉快！\n健身房管理团队\n\n这是最后一次提醒，请务必准时参加！', '2026-03-18 17:30:12', 'COMPLETED', 2, 2, '2026-03-18 17:31:33', NULL, NULL, '2026-03-18 17:30:12');
INSERT INTO `scheduled_mail` VALUES (40, 2, NULL, '1290338552@qq.com', '【第2次提醒】【健身房】课程预约成功 - 五连鞭基础课程', '【最后一次提醒】\n\n亲爱的卓卓嘛~，\n\n您好！您的课程预约已审核通过。\n\n预约信息如下：\n课程名称：五连鞭基础课程\n课程描述：马保国教练亲自指导的五连鞭基础训练课程，适合初学者\n教练：马保国\n课程费用：￥199.00\n课程时长：60分钟\n预约时间：2026-03-18 17:48:12\n\n请您准时参加课程，如有任何问题请及时联系我们。\n\n祝您健身愉快！\n健身房管理团队\n\n这是最后一次提醒，请务必准时参加！', '2026-03-18 17:48:31', 'COMPLETED', 2, 2, '2026-03-18 17:50:01', NULL, NULL, '2026-03-18 17:48:31');
INSERT INTO `scheduled_mail` VALUES (41, 2, NULL, '1290338552@qq.com', '【第2次提醒】【健身房】课程预约成功 - 艾萨拉专业指导', '【最后一次提醒】\n\n亲爱的卓卓嘛~，\n\n您好！您的课程预约已审核通过。\n\n预约信息如下：\n课程名称：艾萨拉专业指导\n课程描述：艾萨拉教练的专业健身指导课程\n教练：艾萨拉\n课程费用：￥299.00\n课程时长：120分钟\n预约时间：2026-03-18 18:14:04\n\n请您准时参加课程，如有任何问题请及时联系我们。\n\n祝您健身愉快！\n健身房管理团队\n\n这是最后一次提醒，请务必准时参加！', '2026-03-18 18:14:43', 'COMPLETED', 2, 2, '2026-03-18 18:16:01', NULL, NULL, '2026-03-18 18:14:43');
INSERT INTO `scheduled_mail` VALUES (42, 2, NULL, '1290338552@qq.com', '【第2次提醒】【健身房】课程预约成功 - 小猫瑜伽课', '【最后一次提醒】\n\n亲爱的卓卓嘛~，\n\n您好！您的课程预约已审核通过。\n\n预约信息如下：\n课程名称：小猫瑜伽课\n课程描述：猫大人教练的温和瑜伽课程，适合放松身心\n教练：猫大人\n课程费用：￥89.00\n课程时长：45分钟\n预约时间：2026-03-18 21:26:29\n\n请您准时参加课程，如有任何问题请及时联系我们。\n\n祝您健身愉快！\n健身房管理团队\n\n这是最后一次提醒，请务必准时参加！', '2026-03-18 21:26:52', 'COMPLETED', 2, 2, '2026-03-18 21:28:01', NULL, NULL, '2026-03-18 21:26:52');
INSERT INTO `scheduled_mail` VALUES (43, 2, NULL, '1290338552@qq.com', '【第2次提醒】【健身房】课程预约成功 - 力量训练进阶', '【最后一次提醒】\n\n亲爱的卓卓嘛~，\n\n您好！您的课程预约已审核通过。\n\n预约信息如下：\n课程名称：力量训练进阶\n课程描述：达到教练的力量训练进阶课程，提升肌肉力量\n教练：达到\n课程费用：￥159.00\n课程时长：90分钟\n预约时间：2026-03-31 14:40:44\n\n请您准时参加课程，如有任何问题请及时联系我们。\n\n祝您健身愉快！\n健身房管理团队\n\n这是最后一次提醒，请务必准时参加！', '2026-03-31 14:41:14', 'COMPLETED', 2, 2, '2026-03-31 14:42:32', NULL, NULL, '2026-03-31 14:41:14');
INSERT INTO `scheduled_mail` VALUES (44, 2, NULL, '1290338552@qq.com', '【第2次提醒】【健身房】课程预约成功 - 小狗锻炼法', '【最后一次提醒】\n\n亲爱的卓卓嘛~，\n\n您好！您的课程预约已审核通过。\n\n预约信息如下：\n课程名称：小狗锻炼法\n课程描述：小狗速跑\n教练：威龙\n课程费用：￥100.00\n课程时长：2分钟\n预约时间：2026-03-31 14:44:45\n\n请您准时参加课程，如有任何问题请及时联系我们。\n\n祝您健身愉快！\n健身房管理团队\n\n这是最后一次提醒，请务必准时参加！', '2026-03-31 14:45:11', 'COMPLETED', 2, 2, '2026-03-31 14:46:31', NULL, NULL, '2026-03-31 14:45:11');
INSERT INTO `scheduled_mail` VALUES (45, 2, NULL, '1290338552@qq.com', '【第2次提醒】【健身房】课程预约成功 - 小狗锻炼法', '【最后一次提醒】\n\n亲爱的卓卓嘛~，\n\n您好！您的课程预约已审核通过。\n\n预约信息如下：\n课程名称：小狗锻炼法\n课程描述：小狗速跑\n教练：威龙\n课程费用：￥100.00\n课程时长：2分钟\n预约时间：2026-03-31 15:04:15\n\n请您准时参加课程，如有任何问题请及时联系我们。\n\n祝您健身愉快！\n健身房管理团队\n\n这是最后一次提醒，请务必准时参加！', '2026-03-31 15:04:59', 'COMPLETED', 2, 2, '2026-03-31 15:06:01', NULL, NULL, '2026-03-31 15:04:59');
INSERT INTO `scheduled_mail` VALUES (46, 2, NULL, '1290338552@qq.com', '【第2次提醒】【健身房】课程预约成功 - 小狗锻炼法', '【最后一次提醒】\n\n亲爱的卓卓嘛~，\n\n您好！您的课程预约已审核通过。\n\n预约信息如下：\n课程名称：小狗锻炼法\n课程描述：小狗速跑\n教练：威龙\n课程费用：￥100.00\n课程时长：2分钟\n预约时间：2026-03-31 15:19:19\n\n请您准时参加课程，如有任何问题请及时联系我们。\n\n祝您健身愉快！\n健身房管理团队\n\n这是最后一次提醒，请务必准时参加！', '2026-03-31 15:19:48', 'COMPLETED', 2, 2, '2026-03-31 15:21:01', NULL, NULL, '2026-03-31 15:19:48');
INSERT INTO `scheduled_mail` VALUES (47, 2, NULL, '1290338552@qq.com', '【第2次提醒】【健身房】课程预约成功 - 小狗锻炼法', '【最后一次提醒】\n\n亲爱的卓卓嘛~，\n\n您好！您的课程预约已审核通过。\n\n预约信息如下：\n课程名称：小狗锻炼法\n课程描述：小狗速跑\n教练：威龙\n课程费用：￥100.00\n课程时长：2分钟\n预约时间：2026-03-31 20:37:21\n\n请您准时参加课程，如有任何问题请及时联系我们。\n\n祝您健身愉快！\n健身房管理团队\n\n这是最后一次提醒，请务必准时参加！', '2026-03-31 20:39:35', 'COMPLETED', 2, 2, '2026-03-31 20:41:01', NULL, NULL, '2026-03-31 20:39:35');
INSERT INTO `scheduled_mail` VALUES (48, 2, NULL, '1290338552@qq.com', '【第2次提醒】【健身房】课程预约成功 - 艾萨拉专业指导', '【最后一次提醒】\n\n亲爱的卓卓嘛~，\n\n您好！您的课程预约已审核通过。\n\n预约信息如下：\n课程名称：艾萨拉专业指导\n课程描述：艾萨拉教练的专业健身指导课程\n教练：艾萨拉\n课程费用：￥299.00\n课程时长：120分钟\n预约时间：2026-03-31 20:58:40\n\n请您准时参加课程，如有任何问题请及时联系我们。\n\n祝您健身愉快！\n健身房管理团队\n\n这是最后一次提醒，请务必准时参加！', '2026-03-31 20:59:12', 'COMPLETED', 2, 2, '2026-03-31 21:00:30', NULL, NULL, '2026-03-31 20:59:12');
INSERT INTO `scheduled_mail` VALUES (49, 2, NULL, '1290338552@qq.com', '【第2次提醒】【健身房】课程预约成功 - 小狗锻炼法', '【最后一次提醒】\n\n亲爱的卓卓嘛~，\n\n您好！您的课程预约已审核通过。\n\n预约信息如下：\n课程名称：小狗锻炼法\n课程描述：小狗速跑\n教练：威龙\n课程费用：￥100.00\n课程时长：2分钟\n预约时间：2026-04-20 17:02:38\n\n请您准时参加课程，如有任何问题请及时联系我们。\n\n祝您健身愉快！\n健身房管理团队\n\n这是最后一次提醒，请务必准时参加！', '2026-04-20 17:03:10', 'COMPLETED', 2, 2, '2026-04-20 17:04:31', NULL, NULL, '2026-04-20 17:03:10');
INSERT INTO `scheduled_mail` VALUES (50, 2, NULL, '1290338552@qq.com', '【第2次提醒】【健身房】课程预约成功 - 小狗锻炼法', '【最后一次提醒】\n\n亲爱的卓卓嘛~，\n\n您好！您的课程预约已审核通过。\n\n预约信息如下：\n课程名称：小狗锻炼法\n课程描述：小狗速跑\n教练：威龙\n课程费用：￥100.00\n课程时长：2分钟\n预约时间：2026-04-20 17:07:20\n\n请您准时参加课程，如有任何问题请及时联系我们。\n\n祝您健身愉快！\n健身房管理团队\n\n这是最后一次提醒，请务必准时参加！', '2026-04-20 17:07:53', 'COMPLETED', 2, 2, '2026-04-20 17:09:01', NULL, NULL, '2026-04-20 17:07:53');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '账号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '名称',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '电话',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'USER' COMMENT '角色',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  `balance` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '账户余额',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '管理员信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (2, 'zhuo', '123456', '卓卓嘛~', '19978018408', '1290338552@qq.com', 'USER', 'http://localhost:9999/files/download/1776675566652_1750395273839_peach.jpg', 55.00);
INSERT INTO `user` VALUES (7, 'admin', '123456', 'admin', '1345665678', '3620296229@qq.com', 'USER', 'http://localhost:9999/files/download/1767897685963_1750395292768_tutou.png', 0.00);
INSERT INTO `user` VALUES (10, 'ncc', '123456', '大哥哥~', '13456656765', '1290338552@qq.com', 'USER', 'http://localhost:9999/files/download/1776675109266_1750679588434-peach.jpg', 0.00);

-- ----------------------------
-- Triggers structure for table user
-- ----------------------------
DROP TRIGGER IF EXISTS `update_last_login_time`;
delimiter ;;
CREATE TRIGGER `update_last_login_time` BEFORE UPDATE ON `user` FOR EACH ROW BEGIN
    IF NEW.username != OLD.username THEN
        SET NEW.last_login_time = NOW();
    END IF;
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
