/*
 Navicat Premium Data Transfer

 Source Server         : aly
 Source Server Type    : MySQL
 Source Server Version : 50733
 Source Host           : 47.111.112.196:3306
 Source Schema         : news_recommend

 Target Server Type    : MySQL
 Target Server Version : 50733
 File Encoding         : 65001

 Date: 05/06/2021 00:40:16
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `openid` varchar(255) NOT NULL COMMENT 'openid/唯一标识符',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `index_user` (`openid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';

SET FOREIGN_KEY_CHECKS = 1;
