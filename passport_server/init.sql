set names 'utf8';

-- 创建数据表
CREATE TABLE `t_passport` (
  `qid` bigint(20) NOT NULL,
  `create_time` bigint(20) DEFAULT NULL,
  `pf` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `last_login_time` bigint(20) DEFAULT NULL,
  `last_game_server_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`qid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
/*!50100 PARTITION BY HASH (qid)
PARTITIONS 256 */
