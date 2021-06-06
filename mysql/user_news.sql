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

 Date: 05/06/2021 00:40:23
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user_news
-- ----------------------------
DROP TABLE IF EXISTS `user_news`;
CREATE TABLE `user_news` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id` varchar(255) NOT NULL COMMENT '用户id',
  `news_id` varchar(255) NOT NULL COMMENT '新闻id',
  `action` int(11) NOT NULL COMMENT '0代表点击/浏览，1代表收藏',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `index_action` (`user_id`,`action`,`news_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COMMENT='用户-新闻 行为记录表';

SET FOREIGN_KEY_CHECKS = 1;
