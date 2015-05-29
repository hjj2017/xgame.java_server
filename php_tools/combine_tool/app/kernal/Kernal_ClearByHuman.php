<?php
// @import
require_once(__DIR__ . "/../../workFlow/MyParam.php");
require_once(__DIR__ . "/../../workFlow/SQLHelper.php");
require_once(__DIR__ . "/../MyLog.php");
require_once(__DIR__ . "/../util/FileUtil.php");
require_once(__DIR__ . "/Callback_CollectClazzObj.php");
require_once(__DIR__ . "/CombineKernal.php");

/**
 * 根据玩家角色删除数据, 给羊服瘦身!
 * 注意 : 为了保证性能要求,
 * 会在羊服上建立临时表用来记录要删除的玩家角色 UUId...
 * 合服工具不会清理狼服上的数据!
 * 因为没有那个必要...
 *
 * @author jinhaijiang
 * @since 2014/12/20
 *
 */
class Kernal_ClearByHuman {
    /**
     * 清理数据
     *
     * @param $myParam MyParams
     * @return void
     *
     */
    static function process(
        $myParam) {
        // 创建临时表
        self::createTempTable($myParam->_sheepPDO);
        // 创建收集器
        $collector = new Callback_CollectClazzObj();
        // 遍历 clear/byHuman 目录,
        // 完成清理工作
        FileUtil::traverseDir(
            __WORKFLOW_DIR . "/clear/byHuman",
            $collector
        );

        // 获取类对象数组
        $clazzObjArr = $collector->getClazzObjArr();

        if (count($clazzObjArr) <= 0) {
            // 如果类对象数组为空,
            // 则直接退出!
            return;
        }

        // 记录日志
        MyLog::LOG()->info(">>> 准备清理玩家数据");

        foreach ($clazzObjArr as $clazzObj) {
            // 记录日志
            MyLog::LOG()->info("执行 : " . get_class($clazzObj));
            // 开始工作
            $clazzObj->doAction($myParam);
        }
    }

    /**
     * 删除羊服上的无用玩家数据, 
     * 其实现思路是:
     * 创建一个临时表, 记录要删除的玩家角色的 UUId, 
     * 在相关表中通过 where ... in ... 的查询方式进行删除...
     * 这样做可以保证执行效率!
     * 
     * 注意 : 被清号的玩家需要同时满足以下条件:
     *
     * <ol>
     * <li>14 天未登陆过;</li>
     * <li>VIP 等级为 0;</li>
     * <li>角色等级 40 级以下;</li>
     * </ol>
     *
     * @param $pdo PDO
     * @return array
     *
     */
    private static function createTempTable($pdo) {
        if ($pdo == null) {
            // 如果参数对象为空,
            // 则直接退出!
            MyLog::LOG()->error("参数对象为空");
            return null;
        }

        $sqlArr = array();

        $sqlArr []= <<<__sql
drop table if exists combine_clearByHuman
__sql;

        $sqlArr []= <<<__sql
create table if not exists combine_clearByHuman (
    human_uuid bigint primary key
)
__sql;

        $sqlArr []= <<<__sql
insert into combine_clearByHuman ( human_uuid )
select `id`
  from `t_human_info`
 where `level` <= 39
   and `vipLevel` <= 0
   and datediff(now(), lastLoginTime) >= 14
   and `id` not in ( 
select `leaderRoleId`
  from `t_armygroup`
)
__sql;

        // 执行 SQL 语句
    	SQLHelper::executeNonQueryArray($pdo, $sqlArr);
    }
}
