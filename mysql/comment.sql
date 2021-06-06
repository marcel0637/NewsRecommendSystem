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

 Date: 05/06/2021 00:39:45
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `docid` varchar(255) NOT NULL,
  `content` varchar(255) NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `avatar` varchar(255) NOT NULL,
  `nick_name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `index_comment` (`docid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;
