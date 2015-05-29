<?php
// @import
require_once(__DIR__ . "/SQLHelper.php");

/**
 * 合并工具
 *
 * @author jinhaijiang
 * @since 2014/12/26
 *
 */
class CombineHelper {
	/**
	 * 合并, 将羊服数据插入到狼服
	 *
	 * @param String $tableName
	 * @param Array<String> $colArr
	 * @param PDO $sheepPDO
	 * @param PDO $wolfPDO
	 * @return void
	 *
	 */
	static function combine($tableName, $colArr, $sheepPDO, $wolfPDO) {
		// 执行合服操作
		self::combineByWhere(
			$tableName, 
			$colArr, 
			null, 
			$sheepPDO, 
			$wolfPDO
		);
	}

    /**
     * 合并, 将羊服数据插入到狼服
     *
     * @param String $tableName
     * @param Array<String> $colArr
     * @param $sqlWhere 
     * @param PDO $sheepPDO
     * @param PDO $wolfPDO
     * @return void
     *
     */
	static function combineByWhere($tableName, $colArr, $sqlWhere, $sheepPDO, $wolfPDO) {
        if ($tableName == null ||
            $sheepPDO == null ||
            $wolfPDO == null) {
            // 如果参数对象为空,
            // 则直接退出!
            MyLog::LOG()->error("参数对象为空");
            return;
        }

        // 获取狼服 DB 名称
        $wolfDbName = self::getDbName($wolfPDO);
        // 获取羊服 DB 名称
        $sheepDbName = self::getDbName($sheepPDO);

        // 以下为合服过程,
        // 插入数据时会忽略重复主键
        // 
        if (SQLHelper::hasTableAtSpecificDb(
        	$sheepPDO, $sheepDbName, $tableName) == false) {
        	// 如果在羊服中不存在数据表,
        	// 则直接退出!
        	MyLog::LOG()->error("羊服 ${sheepDbName} 中没有 ${tableName} 数据表");
        	return;
        }

        if (SQLHelper::hasTableAtSpecificDb(
        	$wolfPDO, $wolfDbName, $tableName) == false) {
        	// 如果在狼服中不存在数据表, 
        	// 则直接退出!
        	MyLog::LOG()->error("狼服 ${wolfDbName} 中没有 ${tableName} 数据表");
        	return;
        }

        // 注意, 在合服之前先显示羊服和狼服上的数据个数,
        // 以避免数据丢失...
        // 显示羊服数据库中的数据个数
        $rowCount0 = self::countX($tableName, $sheepPDO);
        MyLog::LOG()->info("羊服 ${sheepDbName}.${tableName} 数据表中有 {$rowCount0} 条记录");
        // 显示狼服数据库中的数据个数
        $rowCount1 = self::countX($tableName, $wolfPDO);
        MyLog::LOG()->info("狼服 ${wolfDbName}.${tableName} 数据表中有 {$rowCount1} 条记录");

        $sql = null;

        if (count($colArr) <= 0) {
        	// 如果是没有指明要插入哪些列, 
        	// 则直接使用 *
        	$sql = <<<__sql
insert ignore into ${wolfDbName}.${tableName} select * from ${sheepDbName}.${tableName} ${sqlWhere}
__sql;
        } else {
        	// 如果指明了要插入哪些列, 
        	// 则使用标注列名称
        	$colSQL = join($colArr, "`, `");
        	$colSQL = "`" . $colSQL . "`";

        	$sql = <<<__sql
insert ignore into ${wolfDbName}.${tableName} ( $colSQL ) select $colSQL from ${sheepDbName}.${tableName} ${sqlWhere}
__sql;
        }

        try {
            // 记录日志信息
            MyLog::LOG()->debug("准备执行 :\n>>> ${sql}");
            // 执行 SQL 语句
           	$wolfPDO->exec($sql);
        } catch (Exception $ex) {
            // 记录错误日志
            MyLog::LOG()->error($ex->getMessage(), $ex);
        }

        // 
        // 合服结束之后, 
        // 输出狼服上的数据个数...
        // 
        $rowCount2 = self::countX($tableName, $wolfPDO);
        MyLog::LOG()->info("狼服 ${wolfDbName}.${tableName} 数据表中有 {$rowCount2} 条记录");

        // 记录完成日志
        MyLog::LOG()->info("合并数据表 ${tableName} 完成 <<<");

        if ($rowCount2 < ($rowCount0 + $rowCount1)) {
        	// 如果两个数值不等, 
        	// 则记录错误日志!
        	MyLog::LOG()->error("合并 ${tableName} 表的时候, 狼服数据丢失");
        }
    }

    /**
     * 获取 DB 名称
     * 
     * @param PDO $pdo
     * @return String
     * 
     */
    private static function getDbName($pdo) {
    	if ($pdo == null) {
    		// 如果参数对象为空, 
    		// 则直接退出!
    		return "";
    	}

    	// 准备并执行 SQL 语句
    	$stmt = $pdo->prepare("select database()");
    	$stmt->execute();

    	// 获取行记录
    	$row = $stmt->fetch();
    	$dbName = $row[0];

    	if ($row) {
    		return "${dbName}";
    	} else {
    		return "";
    	}
    }

    /**
     * 统计表记录数量
     * 
     * @param String $tableName
     * @param PDO $pdo
     * 
     */
    private static function countX($tableName, $pdo) {
    	if ($tableName == null || 
    		$pdo == null) {
    		// 如果参数对象为空, 
    		// 则直接退出!
    		return;
    	}

   		$sql = <<<__sql
select count(-1) as __count from ${tableName}
__sql;

   		$stmt = $pdo->prepare($sql);
   		$stmt->execute();

   		// 获取行记录
   		$row = $stmt->fetch();
   		$dbName = $row[0];
   		
   		if ($row) {
   			return intval($row["__count"]);
   		} else {
   			return 0;
   		}
    }
}
