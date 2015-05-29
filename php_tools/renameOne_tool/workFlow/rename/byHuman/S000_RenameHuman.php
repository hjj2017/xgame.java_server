<?php
// @import
require_once(__DIR__ . "/../../AbstractWorkNode.php");
require_once(__DIR__ . "/../../SQLHelper.php");

/**
 * 玩家角色更名
 *
 * @author jinhaijiang
 * @since 2014/12/24
 *
 */
class S000_RenameHuman extends AbstractWorkNode {
    // @Overrid
    public function doAction($myParam) {
        if ($myParam == null) {
            // 如果参数对象为空,
            // 则直接退出!
            MyLog::LOG()->error("参数对象为空");
            return;
        }

        // 创建 t_human_renaming 数据表
        $this->createHumanRenameTable($myParam);
        // 清理 t_human_renaming 数据表
        SQLHelper::executeNonQuery(
            $myParam->_gamePDO,
            "truncate table t_human_renaming"
        );

        // 玩家角色 UUId
        $humanUUId = $myParam->_humanUUId;
        // 新的原始名称
        $newOrigName = $myParam->_newOrigName;
        // 新的用户全名
        $newFullName = $myParam->_newFullName;

        // 插入数据到 t_human_renaming 表
        $sql = <<< __sql
insert into `t_human_renaming` (
       `human_uuid`,
       `human_old_full_name`,
       `human_new_orig_name`,
       `human_new_full_name` )
select `id`,
       `name`,
       '${newOrigName}', 
       '${newFullName}'
  from `t_human_info`
 where `id` = ${humanUUId};
__sql;
        // 执行 SQL 语句
        SQLHelper::executeNonQuery($myParam->_gamePDO, $sql);
        // 查询玩家信息
        $rowSet = SQLHelper::executeQuery(
            $myParam->_gamePDO,
            "select * from t_human_renaming where human_uuid = ${humanUUId}"
        );

        if (count($rowSet) <= 0) {
            // 如果记录集为空,
            // 则直接结束!
            MyLog::LOG()->error("无法找到玩家的旧名称");
            die;
        }

        // 获取旧名称
        $myParam->_oldFullName = $rowSet[0]["human_old_full_name"];

        // 插入数据到 t_human_renamed 表
        $sql = <<< __sql
insert into `t_human_renamed` (
       `human_uuid`,
       `human_old_full_name`,
       `human_new_orig_name`,
       `human_new_full_name`,
       `start_time` )
select `id`,
       `name`,
       '${newOrigName}', 
       '${newFullName}', 
       now()
  from `t_human_info`
 where `id` = ${humanUUId};
__sql;
        // 执行 SQL 查询
        SQLHelper::executeNonQuery($myParam->_gamePDO, $sql);

        // 更新 t_human_info 表
        SQLHelper::executeNonQuery(
            $myParam->_gamePDO,
            "update t_human_info as A, t_human_renaming as B
                set name = B.human_new_full_name,
        		    orig_name = B.human_new_orig_name
              where A.id = B.human_uuid"
        );
    }

    /**
     * 创建角色改名表
     *
     * @param $myParam MyParams
     * @return void
     *
     */
    private function createHumanRenameTable($myParam) {
        if ($myParam == null) {
            // 如果参数对象为空,
            // 则直接退出!
            MyLog::LOG()->error("参数对象为空");
            return;
        }

		// 事先删除数据表
        SQLHelper::executeNonQuery(
        	$myParam->_gamePDO,
        	"drop table if exists `t_human_renaming`"
        );
        
        $sql = <<<__sql
create table if not exists `t_human_renaming` (
  `human_uuid` bigint(20) not null default '0',
  `human_old_full_name` varchar(64) default null,
  `human_new_orig_name` varchar(64) default null,
  `human_new_full_name` varchar(64) default null,
  primary key (`human_uuid`)
) engine=InnoDB default charset=utf8 collate=utf8_bin
__sql;

        // 执行 SQL
        SQLHelper::executeNonQuery(
            $myParam->_gamePDO,
            $sql
        );

        // 事先删除数据表
        SQLHelper::executeNonQuery(
        		$myParam->_gamePDO,
        		"drop table if exists `t_human_renamed`"
        );

        $sql = <<<__sql
create table if not exists `t_human_renamed` (
  `id` int(11) not null auto_increment,
  `human_uuid` bigint(20) default '0',
  `human_old_full_name` varchar(64) default null,
  `human_new_orig_name` varchar(64) default null,
  `human_new_full_name` varchar(64) default null,
  `start_time` datetime default null,
  primary key (`id`),
  key ( human_uuid )
) engine=InnoDB default charset=utf8 collate=utf8_bin;
__sql;

        // 执行 SQL
        SQLHelper::executeNonQuery(
            $myParam->_gamePDO,
            $sql
        );
    }
}
