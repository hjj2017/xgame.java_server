/*
SQLyog Community v11.32 (64 bit)
MySQL - 10.1.8-MariaDB : Database - x_passbook
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`x_passbook` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_bin */;

USE `x_passbook`;

/*Table structure for table `t_passbook_0` */

DROP TABLE IF EXISTS `t_passbook_0`;

CREATE TABLE `t_passbook_0` (
  `platform_uid_str` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `create_time` bigint(20) DEFAULT NULL,
  `pf` varchar(16) COLLATE utf8_bin DEFAULT NULL,
  `last_login_time` bigint(20) DEFAULT NULL,
  `last_login_ip_addr` varchar(64) DEFAULT NULL,
  `last_game_server_id` int(11) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `t_passbook_1` */

DROP TABLE IF EXISTS `t_passbook_1`;

CREATE TABLE `t_passbook_1` (
  `platform_uid_str` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `create_time` bigint(20) DEFAULT NULL,
  `pf` varchar(16) COLLATE utf8_bin DEFAULT NULL,
  `last_login_time` bigint(20) DEFAULT NULL,
  `last_login_ip_addr` varchar(64) DEFAULT NULL,
  `last_game_server_id` int(11) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `t_passbook_2` */

DROP TABLE IF EXISTS `t_passbook_2`;

CREATE TABLE `t_passbook_2` (
  `platform_uid_str` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `create_time` bigint(20) DEFAULT NULL,
  `pf` varchar(16) COLLATE utf8_bin DEFAULT NULL,
  `last_login_time` bigint(20) DEFAULT NULL,
  `last_login_ip_addr` varchar(64) DEFAULT NULL,
  `last_game_server_id` int(11) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `t_passbook_3` */

DROP TABLE IF EXISTS `t_passbook_3`;

CREATE TABLE `t_passbook_3` (
  `platform_uid_str` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `create_time` bigint(20) DEFAULT NULL,
  `pf` varchar(16) COLLATE utf8_bin DEFAULT NULL,
  `last_login_time` bigint(20) DEFAULT NULL,
  `last_login_ip_addr` varchar(64) DEFAULT NULL,
  `last_game_server_id` int(11) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `t_passbook_4` */

DROP TABLE IF EXISTS `t_passbook_4`;

CREATE TABLE `t_passbook_4` (
  `platform_uid_str` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `create_time` bigint(20) DEFAULT NULL,
  `pf` varchar(16) COLLATE utf8_bin DEFAULT NULL,
  `last_login_time` bigint(20) DEFAULT NULL,
  `last_login_ip_addr` varchar(64) DEFAULT NULL,
  `last_game_server_id` int(11) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `t_passbook_5` */

DROP TABLE IF EXISTS `t_passbook_5`;

CREATE TABLE `t_passbook_5` (
  `platform_uid_str` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `create_time` bigint(20) DEFAULT NULL,
  `pf` varchar(16) COLLATE utf8_bin DEFAULT NULL,
  `last_login_time` bigint(20) DEFAULT NULL,
  `last_login_ip_addr` varchar(64) DEFAULT NULL,
  `last_game_server_id` int(11) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `t_passbook_6` */

DROP TABLE IF EXISTS `t_passbook_6`;

CREATE TABLE `t_passbook_6` (
  `platform_uid_str` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `create_time` bigint(20) DEFAULT NULL,
  `pf` varchar(16) COLLATE utf8_bin DEFAULT NULL,
  `last_login_time` bigint(20) DEFAULT NULL,
  `last_login_ip_addr` varchar(64) DEFAULT NULL,
  `last_game_server_id` int(11) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `t_passbook_7` */

DROP TABLE IF EXISTS `t_passbook_7`;

CREATE TABLE `t_passbook_7` (
  `platform_uid_str` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `create_time` bigint(20) DEFAULT NULL,
  `pf` varchar(16) COLLATE utf8_bin DEFAULT NULL,
  `last_login_time` bigint(20) DEFAULT NULL,
  `last_login_ip_addr` varchar(64) DEFAULT NULL,
  `last_game_server_id` int(11) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `t_passbook_8` */

DROP TABLE IF EXISTS `t_passbook_8`;

CREATE TABLE `t_passbook_8` (
  `platform_uid_str` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `create_time` bigint(20) DEFAULT NULL,
  `pf` varchar(16) COLLATE utf8_bin DEFAULT NULL,
  `last_login_time` bigint(20) DEFAULT NULL,
  `last_login_ip_addr` varchar(64) DEFAULT NULL,
  `last_game_server_id` int(11) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `t_passbook_9` */

DROP TABLE IF EXISTS `t_passbook_9`;

CREATE TABLE `t_passbook_9` (
  `platform_uid_str` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `create_time` bigint(20) DEFAULT NULL,
  `pf` varchar(16) COLLATE utf8_bin DEFAULT NULL,
  `last_login_time` bigint(20) DEFAULT NULL,
  `last_login_ip_addr` varchar(64) DEFAULT NULL,
  `last_game_server_id` int(11) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
