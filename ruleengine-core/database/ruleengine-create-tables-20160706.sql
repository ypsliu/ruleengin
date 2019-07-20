
/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50610
Source Host           : localhost:3306
Source Database       : ruleengine

Target Server Type    : MYSQL
Target Server Version : 50610
File Encoding         : 65001

Date: 2016-07-06 08:44:13
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for re_biztype
-- ----------------------------
DROP TABLE IF EXISTS `re_biztype`;
CREATE TABLE `re_biztype` (
  `code` varchar(50) NOT NULL,
  `name` varchar(150) DEFAULT NULL,
  `comment` varchar(200) DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `enabled` bit(1) NOT NULL DEFAULT b'1',
  `ruleSetCode` varchar(50) DEFAULT NULL,
  `ruleSetVersion` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`code`),
  KEY `idx_enabled` (`enabled`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for re_datasource
-- ----------------------------
DROP TABLE IF EXISTS `re_datasource`;
CREATE TABLE `re_datasource` (
  `code` varchar(50) NOT NULL,
  `name` varchar(100) NOT NULL,
  `comment` varchar(150) DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `enabled` bit(1) NOT NULL DEFAULT b'1',
  `driverClass` varchar(150) NOT NULL,
  `url` varchar(150) NOT NULL,
  `user` varchar(50) NOT NULL,
  `password` varchar(50) DEFAULT NULL,
  `maxPoolSize` int(11) DEFAULT '5',
  `validationSql` varchar(100) DEFAULT 'select 1',
  PRIMARY KEY (`code`),
  KEY `idx_enabled` (`enabled`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for re_extractsql
-- ----------------------------
DROP TABLE IF EXISTS `re_extractsql`;
CREATE TABLE `re_extractsql` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ruleCode` varchar(50) NOT NULL,
  `datasourceCode` varchar(50) DEFAULT NULL,
  `inputParams` longtext,
  `sql` longtext,
  `tableName` varchar(100) DEFAULT NULL,
  `columnsValue` longtext,
  `conditionsValue` longtext,
  `ruleVersion` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_ruleCode` (`ruleCode`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for re_extractsql_history
-- ----------------------------
DROP TABLE IF EXISTS `re_extractsql_history`;
CREATE TABLE `re_extractsql_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ruleCode` varchar(50) NOT NULL,
  `datasourceCode` varchar(50) DEFAULT NULL,
  `inputParams` longtext,
  `sql` longtext,
  `tableName` varchar(100) DEFAULT NULL,
  `columnsValue` longtext,
  `conditionsValue` longtext,
  `ruleVersion` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_ruleCode_version` (`ruleCode`,`ruleVersion`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for re_match_stage
-- ----------------------------
DROP TABLE IF EXISTS `re_match_stage`;
CREATE TABLE `re_match_stage` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `executionId` varchar(100) NOT NULL,
  `stageId` int(11) NOT NULL,
  `parentStageId` int(11) NOT NULL,
  `ruleCode` varchar(50) NOT NULL,
  `ruleVersion` bigint(20) NOT NULL,
  `tracesValue` longtext,
  `result` longtext,
  `errorMessage` longtext,
  `timeInMs` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_executionId` (`executionId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for re_reference_data
-- ----------------------------
DROP TABLE IF EXISTS `re_reference_data`;
CREATE TABLE `re_reference_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bizTypeCode` varchar(50) NOT NULL,
  `providerTaskId` varchar(100) NOT NULL,
  `time` datetime NOT NULL,
  `conditionsValue` varchar(255) NOT NULL,
  `responseValue` longtext,
  `responseRaw` longtext,
  `errorCode` varchar(50) DEFAULT NULL,
  `errorMessage` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_condition` (`conditionsValue`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for re_reference_data_status
-- ----------------------------
DROP TABLE IF EXISTS `re_reference_data_status`;
CREATE TABLE `re_reference_data_status` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bizTypeCode` varchar(50) NOT NULL,
  `done` bit(1) NOT NULL,
  `providerTaskId` varchar(100) NOT NULL,
  `conditionsValue` varchar(255) NOT NULL,
  `insertTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_condition` (`conditionsValue`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for re_result
-- ----------------------------
DROP TABLE IF EXISTS `re_result`;
CREATE TABLE `re_result` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bizTypeCode` varchar(50) NOT NULL,
  `bizCode` varchar(200) NOT NULL,
  `ruleCode` varchar(50) NOT NULL,
  `matchType` varchar(20) NOT NULL,
  `inputParams` longtext,
  `errorMessage` longtext,
  `time` datetime NOT NULL,
  `result` longtext,
  `ruleVersion` bigint(20) NOT NULL DEFAULT '0',
  `executionId` varchar(100) NOT NULL,
  `ruleSetCode` varchar(50) NOT NULL,
  `ruleSetVersion` varchar(50) NOT NULL,
  `sortId` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_bizCode` (`bizCode`,`matchType`,`bizTypeCode`,`ruleSetCode`,`ruleSetVersion`,`sortId`) USING BTREE
) ENGINE=InnoD DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for re_rule
-- ----------------------------
DROP TABLE IF EXISTS `re_rule`;
CREATE TABLE `re_rule` (
  `code` varchar(50) NOT NULL,
  `name` varchar(150) DEFAULT NULL,
  `comment` varchar(200) DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `enabled` bit(1) NOT NULL DEFAULT b'1',
  `bizTypeCode` varchar(50) NOT NULL,
  `expression` longtext NOT NULL,
  `matchType` varchar(20) NOT NULL,
  `executorClass` varchar(200) DEFAULT NULL,
  `inputParams` longtext,
  `datasourceCodes` varchar(500) DEFAULT NULL,
  `exprSegments` longtext,
  `version` bigint(20) NOT NULL DEFAULT '0',
  `designValue` longtext,
  PRIMARY KEY (`code`),
  KEY `idx_bizTypeCode` (`bizTypeCode`) USING BTREE,
  KEY `idx_enabled` (`enabled`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for re_ruleset
-- ----------------------------
DROP TABLE IF EXISTS `re_ruleset`;
CREATE TABLE `re_ruleset` (
  `code` varchar(50) NOT NULL,
  `name` varchar(150) NOT NULL,
  `comment` varchar(200) DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `bizTypeCode` varchar(50) NOT NULL,
  `rulesValue` longtext NOT NULL,
  `version` bigint(20) NOT NULL DEFAULT '1',
  `enabled` bit(1) NOT NULL DEFAULT b'1',
  PRIMARY KEY (`code`),
  KEY `idx_bizType` (`bizTypeCode`,`enabled`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for re_ruleset_history
-- ----------------------------
DROP TABLE IF EXISTS `re_ruleset_history`;
CREATE TABLE `re_ruleset_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(50) NOT NULL,
  `name` varchar(150) NOT NULL,
  `comment` varchar(200) DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `bizTypeCode` varchar(50) NOT NULL,
  `rulesValue` longtext NOT NULL,
  `version` bigint(20) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `idx_code_version` (`code`,`version`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for re_rule_history
-- ----------------------------
DROP TABLE IF EXISTS `re_rule_history`;
CREATE TABLE `re_rule_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(50) NOT NULL,
  `name` varchar(150) DEFAULT NULL,
  `comment` varchar(200) DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `enabled` bit(1) NOT NULL DEFAULT b'1',
  `bizTypeCode` varchar(50) NOT NULL,
  `expression` longtext NOT NULL,
  `matchType` varchar(20) NOT NULL,
  `executorClass` varchar(200) DEFAULT NULL,
  `inputParams` longtext,
  `datasourceCodes` varchar(500) DEFAULT NULL,
  `exprSegments` longtext,
  `version` bigint(20) NOT NULL DEFAULT '0',
  `designValue` longtext,
  PRIMARY KEY (`id`),
  KEY `idx_code_version` (`code`,`version`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
