<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../SQLHelper.php");

/** 刷新单人竞技场排行榜 */
class S100_UpdateSingleArenaRank extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
    	if ($myParam == null) {
    		// 如果参数对象为空, 
    		// 则直接退出!
    		return;
    	}

    	// 获取 PDO 对象
    	$pdo = $myParam->_wolfPDO;
    	
    	// 首先创建临时表
        self::createTempTable($pdo);
        // 插入排行榜数据
        self::insertRankData($pdo);
        // 回写排名数据
        self::rewriteRankData($pdo);
    }

    /**
     * 首先创建一个临时表
     * 
     */
    private static function createTempTable($pdo) {
    	if ($pdo == null) {
    		// 如果参数对象为空, 
    		// 则直接退出!
    		return;
    	}

    	$sqlArr = array();

    	// 删除临时表
    	$sqlArr []= <<<__sql
drop table if exists combine_refreshSingleArenaRank
__sql;

    	// 创建临时表
    	$sqlArr []= <<<__sql
create table combine_refreshSingleArenaRank (
    human_uuid bigint,
    rank_val int not null auto_increment,
    primary key ( human_uuid ),
    unique key ( rank_val )
)
__sql;

    	// 执行 SQL 语句
    	SQLHelper::executeNonQueryArray($pdo, $sqlArr);
    }

    /**
     * 插入排行榜数据
     * 
     * @param PDO $pdo
     * @return void
     * 
     */
    private static function insertRankData($pdo) {
    	if ($pdo == null) {
    		// 如果参数对象为空,
    		// 则直接退出!
    		return;
    	}

    	// 插入数据表
    	$sql = <<<__sql
insert ignore into `combine_refreshSingleArenaRank` ( `human_uuid` ) select `id` as `human_uuid` from `t_single_arena` where `rank` > 0 order by `rank` asc
__sql;
    	// 执行 SQL 语句
    	SQLHelper::executeNonQuery($pdo, $sql);
    }

    /**
     * 回写排行榜数据
     * 
     * @param PDO $pdo
     * 
     */
    private static function rewriteRankData($pdo) {
    	if ($pdo == null) {
    		// 如果参数对象为空,
    		// 则直接退出!
    		return;
    	}

    	$sqlArr = array();

    	// 事先清除已有的排名数据
    	$sqlArr []= <<<__sql
update `t_single_arena` set `rank` = -4096;
__sql;

    	// 插入数据表
    	$sqlArr []= <<<__sql
update `t_single_arena` as A, `combine_refreshSingleArenaRank` as B 
   set A.rank = B.rank_val
 where A.id = B.human_uuid
__sql;

    	// 执行 SQL 语句
    	SQLHelper::executeNonQueryArray($pdo, $sqlArr);
    }
}
