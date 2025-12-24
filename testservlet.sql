/*
 Navicat Premium Dump SQL

 Source Server         : MySQL
 Source Server Type    : MySQL
 Source Server Version : 80407 (8.4.7)
 Source Host           : localhost:3306
 Source Schema         : testservlet

 Target Server Type    : MySQL
 Target Server Version : 80407 (8.4.7)
 File Encoding         : 65001

 Date: 24/12/2025 09:04:07
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for book_admin
-- ----------------------------
DROP TABLE IF EXISTS `book_admin`;
CREATE TABLE `book_admin`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '密码',
  `nickname` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '昵称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of book_admin
-- ----------------------------
INSERT INTO `book_admin` VALUES (1, 'admin', '111111', '管理员');
INSERT INTO `book_admin` VALUES (2, 'test', '111111', '李主管');

-- ----------------------------
-- Table structure for book_category
-- ----------------------------
DROP TABLE IF EXISTS `book_category`;
CREATE TABLE `book_category`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '分类名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of book_category
-- ----------------------------
INSERT INTO `book_category` VALUES (1, '小说');
INSERT INTO `book_category` VALUES (2, '教材');
INSERT INTO `book_category` VALUES (4, '计算机');
INSERT INTO `book_category` VALUES (5, '科幻');

-- ----------------------------
-- Table structure for book_info
-- ----------------------------
DROP TABLE IF EXISTS `book_info`;
CREATE TABLE `book_info`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '书名',
  `category_id` int NULL DEFAULT NULL COMMENT '分类id',
  `publisher` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '出版社',
  `description` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '描述',
  `author` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '作者',
  `publish_date` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '出版日期',
  `status` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '状态',
  `stock` int NULL DEFAULT NULL COMMENT '库存',
  `cover_url` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '封面图片地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of book_info
-- ----------------------------
INSERT INTO `book_info` VALUES (1, '射雕英雄传', 1, '清华出版社', '《射雕英雄传》以宋宁宗庆元五年（1199年）至成吉思汗逝世（1227年）这段历史为背景，反映了南宋抵抗金国与蒙古两大强敌的斗争，充满爱国的民族主义情愫。《射雕英雄传》由靖康之变引出主人公的名字，讲述了郭靖一步步由为报父仇的青年成长为一代“为国为民”的大侠的故事。', '金庸', '2000-10-10', '上架', 3, 'static/upload/1766537746050_sdyxz.jpg');
INSERT INTO `book_info` VALUES (4, '计算机程序的构造和解释', 4, '人民大学出版社', '该书源于美国麻省理工学院多年使用的一本教材，已被世界上100多所高等院校采纳为教材，包括斯坦福大学、普林斯顿大学等。它从理论上讲解计算机程序的创建、执行和研究。', '哈罗德·阿贝尔森', '2007-07-01', '上架', 1, 'static/upload/1766537786969_s1113106.jpg');
INSERT INTO `book_info` VALUES (5, '算法导论', 4, '清华出版社', '由MIT四大名师联手铸就，将严谨性和全面性融为一体，深入讨论各类算法。麻省理工学院、卡内基梅隆大学、斯坦福大学等国内外千余所高校采用该书作为教材。', '托马斯·科尔曼', '2000-10-10', '上架', 3, '');
INSERT INTO `book_info` VALUES (6, 'C程序设计语言', 4, '人民大学出版社', 'C语言的设计者之一Dennis M.Ritchie和著名的计算机科学家Brian W.Kernighan合著的权威经典著作。原著第1版中介绍的C语言成为后来广泛使用的C语言版本——标准C的基础。', '布莱恩·克尼汉', '2007-07-01', '上架', 2, '');
INSERT INTO `book_info` VALUES (7, '大话设计模式', 4, '清华出版社', '故事形式讲解设计模式', '程杰', '2007-07-01', '上架', 1, '');

-- ----------------------------
-- Table structure for book_reader
-- ----------------------------
DROP TABLE IF EXISTS `book_reader`;
CREATE TABLE `book_reader`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '密码',
  `name` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `mobile` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `id_card` varchar(18) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '身份证',
  `status` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of book_reader
-- ----------------------------
INSERT INTO `book_reader` VALUES (1, 'zhangsan', '333333', '张三', '13111110000', '110110', '正常');
INSERT INTO `book_reader` VALUES (2, 'lisi', '222222', '里斯', '13244446666', '230231', '禁用');
INSERT INTO `book_reader` VALUES (3, 'lilei', '111111', '李磊', '13244445555', '234234', '正常');
INSERT INTO `book_reader` VALUES (9, 'zhongzhenyu', '123456', '钟振宇', '12322223333', '123123', '正常');
INSERT INTO `book_reader` VALUES (11, 'test1', '111111', '张秀英', '12322223333', '123123', '正常');

-- ----------------------------
-- Table structure for book_record
-- ----------------------------
DROP TABLE IF EXISTS `book_record`;
CREATE TABLE `book_record`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `book_id` int NULL DEFAULT NULL COMMENT '书id',
  `book_title` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '书名',
  `user_id` int NULL DEFAULT NULL COMMENT '用户id',
  `borrow_time` datetime NULL DEFAULT NULL COMMENT '借书日期',
  `return_time` datetime NULL DEFAULT NULL COMMENT '还书日期',
  `expire_time` datetime NULL DEFAULT NULL COMMENT '超时时间',
  `status` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '状态',
  `price` double(10, 2) NULL DEFAULT NULL COMMENT '缴费金额',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of book_record
-- ----------------------------
INSERT INTO `book_record` VALUES (8, 1, '射雕英雄传', 1, '2024-09-05 11:14:10', NULL, '2024-10-05 11:14:10', '借阅中', 0.00);
INSERT INTO `book_record` VALUES (9, 3, 'C程序设计语言', 1, '2024-09-05 11:14:25', '2024-09-05 11:14:54', '2024-10-05 11:14:25', '已归还', NULL);
INSERT INTO `book_record` VALUES (10, 5, '算法导论', 1, '2024-09-05 11:14:35', '2024-09-05 11:18:17', '2024-10-05 11:14:35', '已归还', 0.00);
INSERT INTO `book_record` VALUES (11, 1, '射雕英雄传', 3, '2024-09-05 11:15:14', '2025-12-07 09:04:18', '2024-10-05 11:15:14', '已归还', 397.00);
INSERT INTO `book_record` VALUES (12, 3, 'C程序设计语言', 3, '2024-09-05 11:15:17', '2024-09-05 11:18:49', '2024-10-05 11:15:17', '已归还', 0.00);
INSERT INTO `book_record` VALUES (13, 5, '算法导论', 1, '2025-12-05 14:35:20', '2025-12-05 14:35:25', '2026-01-04 14:35:20', '已归还', NULL);
INSERT INTO `book_record` VALUES (14, 1, '射雕英雄传', 10, '2025-12-07 08:54:50', '2025-12-07 08:54:56', '2026-01-06 08:54:50', '已归还', NULL);

SET FOREIGN_KEY_CHECKS = 1;
