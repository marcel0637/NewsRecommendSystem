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

 Date: 05/06/2021 00:40:02
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for news
-- ----------------------------
DROP TABLE IF EXISTS `news`;
CREATE TABLE `news` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `docid` varchar(255) NOT NULL COMMENT '新闻id',
  `title` text NOT NULL COMMENT '新闻标题',
  `cat` varchar(255) NOT NULL COMMENT '新闻分类',
  `comment_count` int(11) NOT NULL COMMENT '评论数目/热度',
  `newstime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新闻发布时间',
  `url` varchar(255) NOT NULL COMMENT '新闻链接',
  `img` varchar(255) NOT NULL COMMENT '图片链接',
  `content` text NOT NULL COMMENT '新闻内容',
  `newsFrom` varchar(255) NOT NULL COMMENT '新闻来源',
  PRIMARY KEY (`id`),
  KEY `index3` (`docid`)
) ENGINE=InnoDB AUTO_INCREMENT=1213 DEFAULT CHARSET=utf8mb4 COMMENT='新闻信息表';

SET FOREIGN_KEY_CHECKS = 1;
