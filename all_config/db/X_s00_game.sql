/*
SQLyog Community v11.32 (64 bit)
MySQL - 5.6.24 : Database - x_s00_game
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`x_s00_game` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `X_s00_game`;

/*Table structure for table `t_building` */

DROP TABLE IF EXISTS `t_building`;

CREATE TABLE `t_building` (
  `human_uid` bigint(20) NOT NULL,
  `building_1_level` int(11) DEFAULT NULL,
  `building_2_level` int(11) DEFAULT NULL,
  `building_3_level` int(11) DEFAULT NULL,
  `building_4_level` int(11) DEFAULT NULL,
  `building_5_level` int(11) DEFAULT NULL,
  `building_6_level` int(11) DEFAULT NULL,
  `building_7_level` int(11) DEFAULT NULL,
  `building_8_level` int(11) DEFAULT NULL,
  PRIMARY KEY (`human_uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `t_cd_timer_0` */

DROP TABLE IF EXISTS `t_cd_timer_0`;

CREATE TABLE `t_cd_timer_0` (
  `uid_str` varchar(64) NOT NULL,
  `human_uid` bigint(20) DEFAULT NULL,
  `cd_type_int` int(11) DEFAULT NULL,
  `start_time` bigint(20) DEFAULT NULL,
  `end_time` bigint(20) DEFAULT NULL,
  `opened` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`uid_str`),
  UNIQUE KEY `human_uuid_2` (`human_uid`,`cd_type_int`),
  KEY `human_uuid` (`human_uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_cd_timer_1` */

DROP TABLE IF EXISTS `t_cd_timer_1`;

CREATE TABLE `t_cd_timer_1` (
  `uid_str` varchar(64) NOT NULL,
  `human_uid` bigint(20) DEFAULT NULL,
  `cd_type_int` int(11) DEFAULT NULL,
  `start_time` bigint(20) DEFAULT NULL,
  `end_time` bigint(20) DEFAULT NULL,
  `opened` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`uid_str`),
  UNIQUE KEY `human_uuid_2` (`human_uid`,`cd_type_int`),
  KEY `human_uuid` (`human_uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_cd_timer_2` */

DROP TABLE IF EXISTS `t_cd_timer_2`;

CREATE TABLE `t_cd_timer_2` (
  `uid_str` varchar(64) NOT NULL,
  `human_uid` bigint(20) DEFAULT NULL,
  `cd_type_int` int(11) DEFAULT NULL,
  `start_time` bigint(20) DEFAULT NULL,
  `end_time` bigint(20) DEFAULT NULL,
  `opened` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`uid_str`),
  UNIQUE KEY `human_uuid_2` (`human_uid`,`cd_type_int`),
  KEY `human_uuid` (`human_uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_cd_timer_3` */

DROP TABLE IF EXISTS `t_cd_timer_3`;

CREATE TABLE `t_cd_timer_3` (
  `uid_str` varchar(64) NOT NULL,
  `human_uid` bigint(20) DEFAULT NULL,
  `cd_type_int` int(11) DEFAULT NULL,
  `start_time` bigint(20) DEFAULT NULL,
  `end_time` bigint(20) DEFAULT NULL,
  `opened` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`uid_str`),
  UNIQUE KEY `human_uuid_2` (`human_uid`,`cd_type_int`),
  KEY `human_uuid` (`human_uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_cd_timer_4` */

DROP TABLE IF EXISTS `t_cd_timer_4`;

CREATE TABLE `t_cd_timer_4` (
  `uid_str` varchar(64) NOT NULL,
  `human_uid` bigint(20) DEFAULT NULL,
  `cd_type_int` int(11) DEFAULT NULL,
  `start_time` bigint(20) DEFAULT NULL,
  `end_time` bigint(20) DEFAULT NULL,
  `opened` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`uid_str`),
  UNIQUE KEY `human_uuid_2` (`human_uid`,`cd_type_int`),
  KEY `human_uuid` (`human_uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_cd_timer_5` */

DROP TABLE IF EXISTS `t_cd_timer_5`;

CREATE TABLE `t_cd_timer_5` (
  `uid_str` varchar(64) NOT NULL,
  `human_uid` bigint(20) DEFAULT NULL,
  `cd_type_int` int(11) DEFAULT NULL,
  `start_time` bigint(20) DEFAULT NULL,
  `end_time` bigint(20) DEFAULT NULL,
  `opened` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`uid_str`),
  UNIQUE KEY `human_uuid_2` (`human_uid`,`cd_type_int`),
  KEY `human_uuid` (`human_uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_cd_timer_6` */

DROP TABLE IF EXISTS `t_cd_timer_6`;

CREATE TABLE `t_cd_timer_6` (
  `uid_str` varchar(64) NOT NULL,
  `human_uid` bigint(20) DEFAULT NULL,
  `cd_type_int` int(11) DEFAULT NULL,
  `start_time` bigint(20) DEFAULT NULL,
  `end_time` bigint(20) DEFAULT NULL,
  `opened` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`uid_str`),
  UNIQUE KEY `human_uuid_2` (`human_uid`,`cd_type_int`),
  KEY `human_uuid` (`human_uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_cd_timer_7` */

DROP TABLE IF EXISTS `t_cd_timer_7`;

CREATE TABLE `t_cd_timer_7` (
  `uid_str` varchar(64) NOT NULL,
  `human_uid` bigint(20) DEFAULT NULL,
  `cd_type_int` int(11) DEFAULT NULL,
  `start_time` bigint(20) DEFAULT NULL,
  `end_time` bigint(20) DEFAULT NULL,
  `opened` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`uid_str`),
  UNIQUE KEY `human_uuid_2` (`human_uid`,`cd_type_int`),
  KEY `human_uuid` (`human_uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_cd_timer_8` */

DROP TABLE IF EXISTS `t_cd_timer_8`;

CREATE TABLE `t_cd_timer_8` (
  `uid_str` varchar(64) NOT NULL,
  `human_uid` bigint(20) DEFAULT NULL,
  `cd_type_int` int(11) DEFAULT NULL,
  `start_time` bigint(20) DEFAULT NULL,
  `end_time` bigint(20) DEFAULT NULL,
  `opened` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`uid_str`),
  UNIQUE KEY `human_uuid_2` (`human_uid`,`cd_type_int`),
  KEY `human_uuid` (`human_uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_cd_timer_9` */

DROP TABLE IF EXISTS `t_cd_timer_9`;

CREATE TABLE `t_cd_timer_9` (
  `uid_str` varchar(64) NOT NULL,
  `human_uid` bigint(20) DEFAULT NULL,
  `cd_type_int` int(11) DEFAULT NULL,
  `start_time` bigint(20) DEFAULT NULL,
  `end_time` bigint(20) DEFAULT NULL,
  `opened` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`uid_str`),
  UNIQUE KEY `human_uuid_2` (`human_uid`,`cd_type_int`),
  KEY `human_uuid` (`human_uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_human` */

DROP TABLE IF EXISTS `t_human`;

CREATE TABLE `t_human` (
  `human_uid` bigint(20) NOT NULL DEFAULT '0',
  `platform_uid_str` varchar(64) DEFAULT NULL,
  `full_name` varchar(48) DEFAULT NULL,
  `human_name` varchar(32) DEFAULT NULL,
  `server_name` varchar(16) DEFAULT NULL,
  `human_level` int(11) DEFAULT NULL,
  `gold` int(11) DEFAULT NULL,
  `newer_reward_checkout` tinyint DEFAULT NULL,
  PRIMARY KEY (`human_uid`),
  UNIQUE KEY `full_name` (`full_name`),
  KEY `human_name` (`human_name`),
  KEY `platform_uid_str` (`platform_uid_str`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `t_player` */

DROP TABLE IF EXISTS `t_player`;

CREATE TABLE `t_player` (
  `platform_uid_str` varchar(64) NOT NULL,
  `user_name` varchar(32) DEFAULT NULL,
  `user_pass` varchar(128) DEFAULT NULL,
  `create_time` bigint(20) DEFAULT NULL,
  `passbook_create_time` bigint(20) DEFAULT NULL,
  `pf` varchar(32) DEFAULT NULL,
  `last_login_time` bigint(20) DEFAULT NULL,
  `last_login_ip_addr` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`platform_uid_str`),
  KEY `user_name` (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/* hero 0 */
DROP TABLE IF EXISTS `t_hero_0`;

CREATE TABLE `t_hero_0` (
  `uid_str` VARCHAR(64) NOT NULL,
  `human_uid` BIGINT(20) DEFAULT NULL,
  `tmpl_id` INT(11) DEFAULT NULL,
  `hire_time` BIGINT(20) DEFAULT NULL,
  `curr_exp` TINYINT(4) DEFAULT NULL,
  `hero_level` INT(11) DEFAULT NULL,
  `star_level` INT(11) DEFAULT NULL,
  `p_attk_add` INT(11) DEFAULT NULL,
  `m_attk_add` INT(11) DEFAULT NULL,
  `p_defn_add` INT(11) DEFAULT NULL,
  `m_defn_add` INT(11) DEFAULT NULL,
  `hp_add` INT(11) DEFAULT NULL,
  PRIMARY KEY (`uid_str`),
  KEY `human_uid` (`human_uid`)
)
ENGINE = INNODB
CHARACTER SET utf8
COLLATE utf8_general_ci;

/* hero 1 */
DROP TABLE IF EXISTS `t_hero_1`;

CREATE TABLE `t_hero_1` (
  `uid_str` VARCHAR(64) NOT NULL,
  `human_uid` BIGINT(20) DEFAULT NULL,
  `tmpl_id` INT(11) DEFAULT NULL,
  `hire_time` BIGINT(20) DEFAULT NULL,
  `curr_exp` TINYINT(4) DEFAULT NULL,
  `hero_level` INT(11) DEFAULT NULL,
  `star_level` INT(11) DEFAULT NULL,
  `p_attk_add` INT(11) DEFAULT NULL,
  `m_attk_add` INT(11) DEFAULT NULL,
  `p_defn_add` INT(11) DEFAULT NULL,
  `m_defn_add` INT(11) DEFAULT NULL,
  `hp_add` INT(11) DEFAULT NULL,
  PRIMARY KEY (`uid_str`),
  KEY `human_uid` (`human_uid`)
)
ENGINE = INNODB
CHARACTER SET utf8
COLLATE utf8_general_ci;

/* hero 2 */
DROP TABLE IF EXISTS `t_hero_2`;

CREATE TABLE `t_hero_2` (
  `uid_str` VARCHAR(64) NOT NULL,
  `human_uid` BIGINT(20) DEFAULT NULL,
  `tmpl_id` INT(11) DEFAULT NULL,
  `hire_time` BIGINT(20) DEFAULT NULL,
  `curr_exp` TINYINT(4) DEFAULT NULL,
  `hero_level` INT(11) DEFAULT NULL,
  `star_level` INT(11) DEFAULT NULL,
  `p_attk_add` INT(11) DEFAULT NULL,
  `m_attk_add` INT(11) DEFAULT NULL,
  `p_defn_add` INT(11) DEFAULT NULL,
  `m_defn_add` INT(11) DEFAULT NULL,
  `hp_add` INT(11) DEFAULT NULL,
  PRIMARY KEY (`uid_str`),
  KEY `human_uid` (`human_uid`)
)
ENGINE = INNODB
CHARACTER SET utf8
COLLATE utf8_general_ci;

/* hero 3 */
DROP TABLE IF EXISTS `t_hero_3`;

CREATE TABLE `t_hero_3` (
  `uid_str` VARCHAR(64) NOT NULL,
  `human_uid` BIGINT(20) DEFAULT NULL,
  `tmpl_id` INT(11) DEFAULT NULL,
  `hire_time` BIGINT(20) DEFAULT NULL,
  `curr_exp` TINYINT(4) DEFAULT NULL,
  `hero_level` INT(11) DEFAULT NULL,
  `star_level` INT(11) DEFAULT NULL,
  `p_attk_add` INT(11) DEFAULT NULL,
  `m_attk_add` INT(11) DEFAULT NULL,
  `p_defn_add` INT(11) DEFAULT NULL,
  `m_defn_add` INT(11) DEFAULT NULL,
  `hp_add` INT(11) DEFAULT NULL,
  PRIMARY KEY (`uid_str`),
  KEY `human_uid` (`human_uid`)
)
ENGINE = INNODB
CHARACTER SET utf8
COLLATE utf8_general_ci;

/* hero 4 */
DROP TABLE IF EXISTS `t_hero_4`;

CREATE TABLE `t_hero_4` (
  `uid_str` VARCHAR(64) NOT NULL,
  `human_uid` BIGINT(20) DEFAULT NULL,
  `tmpl_id` INT(11) DEFAULT NULL,
  `hire_time` BIGINT(20) DEFAULT NULL,
  `curr_exp` TINYINT(4) DEFAULT NULL,
  `hero_level` INT(11) DEFAULT NULL,
  `star_level` INT(11) DEFAULT NULL,
  `p_attk_add` INT(11) DEFAULT NULL,
  `m_attk_add` INT(11) DEFAULT NULL,
  `p_defn_add` INT(11) DEFAULT NULL,
  `m_defn_add` INT(11) DEFAULT NULL,
  `hp_add` INT(11) DEFAULT NULL,
  PRIMARY KEY (`uid_str`),
  KEY `human_uid` (`human_uid`)
)
ENGINE = INNODB
CHARACTER SET utf8
COLLATE utf8_general_ci;

/* hero 5 */
DROP TABLE IF EXISTS `t_hero_5`;

CREATE TABLE `t_hero_5` (
  `uid_str` VARCHAR(64) NOT NULL,
  `human_uid` BIGINT(20) DEFAULT NULL,
  `tmpl_id` INT(11) DEFAULT NULL,
  `hire_time` BIGINT(20) DEFAULT NULL,
  `curr_exp` TINYINT(4) DEFAULT NULL,
  `hero_level` INT(11) DEFAULT NULL,
  `star_level` INT(11) DEFAULT NULL,
  `p_attk_add` INT(11) DEFAULT NULL,
  `m_attk_add` INT(11) DEFAULT NULL,
  `p_defn_add` INT(11) DEFAULT NULL,
  `m_defn_add` INT(11) DEFAULT NULL,
  `hp_add` INT(11) DEFAULT NULL,
  PRIMARY KEY (`uid_str`),
  KEY `human_uid` (`human_uid`)
)
ENGINE = INNODB
CHARACTER SET utf8
COLLATE utf8_general_ci;

/* hero 2 */
DROP TABLE IF EXISTS `t_hero_6`;

CREATE TABLE `t_hero_6` (
  `uid_str` VARCHAR(64) NOT NULL,
  `human_uid` BIGINT(20) DEFAULT NULL,
  `tmpl_id` INT(11) DEFAULT NULL,
  `hire_time` BIGINT(20) DEFAULT NULL,
  `curr_exp` TINYINT(4) DEFAULT NULL,
  `hero_level` INT(11) DEFAULT NULL,
  `star_level` INT(11) DEFAULT NULL,
  `p_attk_add` INT(11) DEFAULT NULL,
  `m_attk_add` INT(11) DEFAULT NULL,
  `p_defn_add` INT(11) DEFAULT NULL,
  `m_defn_add` INT(11) DEFAULT NULL,
  `hp_add` INT(11) DEFAULT NULL,
  PRIMARY KEY (`uid_str`),
  KEY `human_uid` (`human_uid`)
)
ENGINE = INNODB
CHARACTER SET utf8
COLLATE utf8_general_ci;

/* hero 7 */
DROP TABLE IF EXISTS `t_hero_7`;

CREATE TABLE `t_hero_7` (
  `uid_str` VARCHAR(64) NOT NULL,
  `human_uid` BIGINT(20) DEFAULT NULL,
  `tmpl_id` INT(11) DEFAULT NULL,
  `hire_time` BIGINT(20) DEFAULT NULL,
  `curr_exp` TINYINT(4) DEFAULT NULL,
  `hero_level` INT(11) DEFAULT NULL,
  `star_level` INT(11) DEFAULT NULL,
  `p_attk_add` INT(11) DEFAULT NULL,
  `m_attk_add` INT(11) DEFAULT NULL,
  `p_defn_add` INT(11) DEFAULT NULL,
  `m_defn_add` INT(11) DEFAULT NULL,
  `hp_add` INT(11) DEFAULT NULL,
  PRIMARY KEY (`uid_str`),
  KEY `human_uid` (`human_uid`)
)
ENGINE = INNODB
CHARACTER SET utf8
COLLATE utf8_general_ci;

/* hero 8 */
DROP TABLE IF EXISTS `t_hero_8`;

CREATE TABLE `t_hero_8` (
  `uid_str` VARCHAR(64) NOT NULL,
  `human_uid` BIGINT(20) DEFAULT NULL,
  `tmpl_id` INT(11) DEFAULT NULL,
  `hire_time` BIGINT(20) DEFAULT NULL,
  `curr_exp` TINYINT(4) DEFAULT NULL,
  `hero_level` INT(11) DEFAULT NULL,
  `star_level` INT(11) DEFAULT NULL,
  `p_attk_add` INT(11) DEFAULT NULL,
  `m_attk_add` INT(11) DEFAULT NULL,
  `p_defn_add` INT(11) DEFAULT NULL,
  `m_defn_add` INT(11) DEFAULT NULL,
  `hp_add` INT(11) DEFAULT NULL,
  PRIMARY KEY (`uid_str`),
  KEY `human_uid` (`human_uid`)
)
ENGINE = INNODB
CHARACTER SET utf8
COLLATE utf8_general_ci;

/* hero 9 */
DROP TABLE IF EXISTS `t_hero_9`;

CREATE TABLE `t_hero_9` (
  `uid_str` VARCHAR(64) NOT NULL,
  `human_uid` BIGINT(20) DEFAULT NULL,
  `tmpl_id` INT(11) DEFAULT NULL,
  `hire_time` BIGINT(20) DEFAULT NULL,
  `curr_exp` TINYINT(4) DEFAULT NULL,
  `hero_level` INT(11) DEFAULT NULL,
  `star_level` INT(11) DEFAULT NULL,
  `p_attk_add` INT(11) DEFAULT NULL,
  `m_attk_add` INT(11) DEFAULT NULL,
  `p_defn_add` INT(11) DEFAULT NULL,
  `m_defn_add` INT(11) DEFAULT NULL,
  `hp_add` INT(11) DEFAULT NULL,
  PRIMARY KEY (`uid_str`),
  KEY `human_uid` (`human_uid`)
)
ENGINE = INNODB
CHARACTER SET utf8
COLLATE utf8_general_ci;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
