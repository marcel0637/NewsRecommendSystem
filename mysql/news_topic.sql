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

 Date: 05/06/2021 00:40:09
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for news_topic
-- ----------------------------
DROP TABLE IF EXISTS `news_topic`;
CREATE TABLE `news_topic` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `docid` varchar(255) NOT NULL COMMENT '新闻id',
  `topicid` int(11) NOT NULL COMMENT 'topic id',
  `weight` double NOT NULL COMMENT '权重',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=290881 DEFAULT CHARSET=utf8mb4 COMMENT='新闻-主题 权重表';

SET FOREIGN_KEY_CHECKS = 1;
