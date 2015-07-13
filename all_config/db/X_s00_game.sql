/*
SQLyog Community v11.32 (64 bit)
MySQL - 5.6.24 : Database - X_s00_game
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`X_s00_game` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `X_s00_game`;

/*Table structure for table `t_human` */

DROP TABLE IF EXISTS `t_human`;

CREATE TABLE `t_human` (
  `human_uid` varchar(20) NOT NULL,
  `human_name` varchar(32) DEFAULT NULL,
  `platform_uid` varchar(64) DEFAULT NULL,
  `server_name` varchar(16) DEFAULT NULL,
  `human_level` int(11) DEFAULT NULL,
  `gold` int(11) DEFAULT NULL,
  PRIMARY KEY (`human_uid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Table structure for table `t_player` */

DROP TABLE IF EXISTS `t_player`;

CREATE TABLE `t_player` (
  `platform_uid` varchar(64) NOT NULL,
  `user_name` varchar(32) DEFAULT NULL,
  `user_pass` varchar(128) DEFAULT NULL,
  `create_time` bigint(20) DEFAULT NULL,
  `pf` varchar(32) DEFAULT NULL,
  `last_login_time` bigint(20) DEFAULT NULL,
  `last_login_ip_addr` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`platform_uid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
