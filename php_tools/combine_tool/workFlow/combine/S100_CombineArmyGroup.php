<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 军团 */
class S100_CombineArmyGroup extends AbstractWorkNode {
	/**
	 * 狼服数据库主键是否已更新?
	 *
	 * @var boolean
	 *
	 */
	private static $_wolfDbPKModified = false;

    // @Override
    function doAction($myParam) {
    	if (self::$_wolfDbPKModified == false) {
			// 重新定义狼服中军团数据表的主键
    		self::renewPk($myParam->_wolfPDO);
    		// 狼服已完成更新!
    		self::$_wolfDbPKModified = true;
    	}

    	// 重新定义羊服中军团数据表的主键
    	self::renewPk($myParam->_wolfPDO);
    	// 执行合服过程
        CombineHelper::combine(
            "t_armygroup", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }

    /**
     * 重新定义军团主键
     * 
     * @param PDO $pdo
     * @return
     * 
     */
    private static function renewPk($pdo) {
    	if ($pdo == null) {
    		// 如果参数对象为空, 
    		// 则直接退出!
    		return;
    	}

    	$sqlArr = array();

    	// 修改军团表中的 Id 列
    	$sqlArr []= <<<__sql
alter table t_armygroup change `Id` `Id` bigint default 0;
__sql;

    	// 设置 Id 为军团长的角色 Id
    	$sqlArr []= <<<__sql
update t_armygroup set `Id` = `creatorRoleId`
__sql;
    	
    	if (SQLHelper::hasColumnAtSpecificDb(
    		$pdo, null, "t_armygroup",  "__new_id")) {
    		// 如果在军团表中有 __new_id 字段,
    		// 则删除该字段!
    		$sqlArr []= <<<__sql
alter table t_armygroup drop column __new_id
__sql;
    	}

    	// 执行 SQL 语句
    	SQLHelper::executeNonQueryArray($pdo, $sqlArr);
    }
}
