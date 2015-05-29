<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 军团成员 */
class S100_CombineArmyGroupMember extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
    	// 删除原有主键
    	self::dropOldPk($myParam->_wolfPDO);
    	// 执行合服过程
        CombineHelper::combine(
            "t_armygroupmember", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
        // 构建新的 Id
        self::makeNewId($myParam->_wolfPDO);
    }

    /**
     * 取消狼服中军团表的主键
     *
     * @param PDO $wolfPDO
     * @return
     *
     */
    private static function dropOldPk($wolfPDO) {
    	if ($wolfPDO == null) {
    		// 如果参数对象为空,
    		// 则直接退出!
    		return;
    	}

    	$sqlArr = array();
    	
    	// 修改军团表中的 Id 列
    	$sqlArr []= <<<__sql
alter table t_armygroupmember change `Id` `Id` bigint default 0;
__sql;

    	// 删除军团表的主键
    	$sqlArr []= <<<__sql
alter table t_armygroupmember drop primary key
__sql;

    	// 执行 SQL 语句
    	SQLHelper::executeNonQueryArray($wolfPDO, $sqlArr);
    }
    
    /**
     * 构建新的 Id, 
     * 军团成员 Id 直接用玩家的角色 Id 即可...
     *
     * @param PDO $wolfPDO
     * @return
     *
     */
    private static function makeNewId($wolfPDO) {
    	if ($wolfPDO == null) {
    		// 如果参数对象为空,
    		// 则直接退出!
    		return;
    	}

    	$sqlArr = array();

    	// 更新军团成员 Id
    	$sqlArr []= <<<__sql
update t_armygroupmember set `Id` = RoleId
__sql;

    	// 修改军团表的主键为 Id 字段
    	$sqlArr []= <<<__sql
alter table t_armygroupmember add primary key ( `Id` )
__sql;

    	// 执行 SQL 语句
    	SQLHelper::executeNonQueryArray($wolfPDO, $sqlArr);
    }
}
