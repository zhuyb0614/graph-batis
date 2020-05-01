/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 50718
Source Host           : 127.0.0.1:3306
Source Database       : graph-batis

Target Server Type    : MYSQL
Target Server Version : 50718
File Encoding         : 65001

Date: 2020-04-25 15:57:25
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_room
-- ----------------------------
DROP TABLE IF EXISTS `t_room`;
CREATE TABLE `t_room` (
  `room_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `room_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`room_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_room
-- ----------------------------
INSERT INTO `t_room` VALUES ('1', '高三一班');
INSERT INTO `t_room` VALUES ('2', '高三二班');
INSERT INTO `t_room` VALUES ('3', '高二一班');

-- ----------------------------
-- Table structure for t_student
-- ----------------------------
DROP TABLE IF EXISTS `t_student`;
CREATE TABLE `t_student` (
  `student_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `room_id` int(11) DEFAULT NULL,
  `student_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`student_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_student
-- ----------------------------
INSERT INTO `t_student` VALUES ('1', '1', '张小三');
INSERT INTO `t_student` VALUES ('2', '2', '李小四');
INSERT INTO `t_student` VALUES ('3', '3', '王小五');
INSERT INTO `t_student` VALUES ('4', '1', '赵小六');
INSERT INTO `t_student` VALUES ('5', '2', '孙小七');

-- ----------------------------
-- Table structure for t_subject
-- ----------------------------
DROP TABLE IF EXISTS `t_subject`;
CREATE TABLE `t_subject` (
  `subject_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `subject_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`subject_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_subject
-- ----------------------------
INSERT INTO `t_subject` VALUES ('1', '历史');
INSERT INTO `t_subject` VALUES ('2', '政治');

-- ----------------------------
-- Table structure for t_teacher
-- ----------------------------
DROP TABLE IF EXISTS `t_teacher`;
CREATE TABLE `t_teacher` (
  `teacher_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `teacher_name` varchar(50) DEFAULT NULL,
  `subject_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`teacher_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_teacher
-- ----------------------------
INSERT INTO `t_teacher` VALUES ('1', '老唐', '1');
INSERT INTO `t_teacher` VALUES ('2', '老孙', '2');
INSERT INTO `t_teacher` VALUES ('3', '老猪', '1');
INSERT INTO `t_teacher` VALUES ('4', '老沙', '2');

-- ----------------------------
-- Table structure for t_teacher_room
-- ----------------------------
DROP TABLE IF EXISTS `t_teacher_room`;
CREATE TABLE `t_teacher_room` (
  `teacher_room_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `teacher_id` int(11) DEFAULT NULL,
  `room_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`teacher_room_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_teacher_room
-- ----------------------------
INSERT INTO `t_teacher_room` VALUES ('1', '1', '1');
INSERT INTO `t_teacher_room` VALUES ('2', '2', '1');
INSERT INTO `t_teacher_room` VALUES ('3', '1', '2');
INSERT INTO `t_teacher_room` VALUES ('4', '2', '2');
INSERT INTO `t_teacher_room` VALUES ('5', '3', '3');
