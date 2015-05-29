<?php

/**
 * SQL 帮助类
 *
 * @author jinhaijiang
 * @since 2014/12/23
 *
 */
class SQLHelper {
	/**
	 * 执行非查询 SQL 数组
	 *
	 * @param $pdo PDO
	 * @param $sql Array<String>
	 * @param $paramArr
	 * @return void
	 *
	 */
	static function executeNonQueryArray($pdo, $sqlArr, $paramArr = null) {
		if (is_array($sqlArr) == false) {
			// 如果参数对象错误, 
			// 则直接退出!
			MyLog::LOG()->error("sqlArr 参数错误, 该参数不是数组");
			return;
		}

		foreach ($sqlArr as $sql) {
			// 执行 SQL 语句
			self::executeNonQuery($pdo, $sql, $paramArr);
		}
	}

    /**
     * 执行非查询 SQL
     *
     * @param $pdo PDO
     * @param $sql String
     * @param $paramArr
     * @return void
     *
     */
    static function executeNonQuery($pdo, $sql, $paramArr = null) {
        try {
            if ($paramArr == null ||
                count($paramArr) <= 0) {
                // 如果参数数组为空,
                // 则直接执行查询
                $pdo->exec($sql);
            } else {
                // 准备 SQL 查询
                $stmt = $pdo->prepare($sql);
                // 获取参数关键字
                $keyArr = array_keys($paramArr);

                foreach ($keyArr as $key) {
                    // 绑定参数
                    $stmt->bindParam($key, $paramArr[$key]);
                }

                // 执行查询
                $stmt->execute();
            }
        } catch (Exception $ex) {
            // 记录错误日志
            MyLog::LOG()->error($ex->getMessage(), $ex);
        }
    }

    /**
     * 获取单一记录集
     *
     * @param $pdo PDO
     * @param $sql String
     * @param $paramArr
     * @return array
     *
     */
    static function executeQuery($pdo, $sql, $paramArr = null) {
        try {
            // 准备 SQL 查询
            $stmt = $pdo->prepare($sql);

            if ($paramArr != null &&
                count($paramArr) >= 0) {
                // 获取参数关键字
                $keyArr = array_keys($paramArr);

                foreach ($keyArr as $key) {
                    // 绑定参数
                    $stmt->bindParam($key, $paramArr[$key]);
                }
            }

            // 执行查询
            $stmt->execute();
            // 获取记录集
            $rowArr = $stmt->fetchAll();

            // 获取记录集
            return $rowArr;
        } catch (Exception $ex) {
            // 记录错误日志
            MyLog::LOG()->error($ex->getMessage(), $ex);
        }
    }

    /**
     * 获取表、字段定义
     * 
     * @param $pdo
     * @param $dbName 
     * @param $tableName
     * @param $colName
     * @return Array
     * 
     */
    private static function getInfoSchema($pdo, $dbName, $tableName, $colName = null) {
    	if ($pdo == null ||
    		$tableName == null) {
    		// 如果参数对象为空,
    		// 则直接退出!
    		return array();
    	}

    	$sql = "";
    	
    	if (strlen($dbName) > 0) {
    		$sql = <<<__sql
select *
  from `information_schema`.`COLUMNS`
 where `TABLE_SCHEMA` = '${dbName}'
   and `TABLE_NAME` = '${tableName}'
__sql;
    	} else {
    		$sql = <<<__sql
select *
  from `information_schema`.`COLUMNS`
 where `TABLE_SCHEMA` = database()
   and `TABLE_NAME` = '${tableName}'
__sql;
    	}

    	if (strlen($colName) > 0) {
    		// 如果列名称不为空, 
    		$sql = $sql . " and `COLUMN_NAME` = '${colName}'";
    	}

    	try {
    		// 准备 SQL 查询
    		$stmt = $pdo->prepare($sql);
    		// 执行查询
    		$stmt->execute();
    		// 获取记录集
    		$rowArr = $stmt->fetchAll();
    		return $rowArr;
    	} catch (Exception $ex) {
    		// 记录错误日志
    		MyLog::LOG()->error($ex->getMessage(), $ex);
    	}

    	return array();
    }

    /**
     * 判断某个数据库中是否含有某个表 ?
     *
     * @param $pdo
     * @param $dbName
     * @param $tableName
     * @param $columnName
     * @return bool
     *
     */
    public static function hasTableAtSpecificDb($pdo, $dbName, $tableName) {
    	// 获取表定义
    	$rowArr = self::getInfoSchema($pdo, $dbName, $tableName);
    	// 个数是否 > 0
   		return count($rowArr) > 0;
    }

    /**
     * 判断某个数据库中的某个表是否含有某个字段 ?
     *
     * @param $pdo
     * @param $dbName
     * @param $tableName
     * @param $columnName
     * @return bool
     *
     */
    static function hasColumnAtSpecificDb($pdo, $dbName, $tableName, $colName) {
    	// 获取表定义
    	$rowArr = self::getInfoSchema($pdo, $dbName, $tableName, $colName);
    	// 个数是否 > 0
    	return count($rowArr) > 0;
    }

    /**
     * 判断某个数据库中的某个表的某个字段是不是主键 ?
     *
     * @param $pdo
     * @param $dbName
     * @param $tableName
     * @param $columnName
     * @return bool
     *
     */
    static function isPrimaryKey($pdo, $dbName, $tableName, $colName) {
    	// 获取表定义
    	$rowArr = self::getInfoSchema($pdo, $dbName, $tableName, $colName);
    	// 个数是否 <= 0
    	if (count($rowArr) <= 0) {
    		return false;
    	}

    	// 获取第一条数据
    	$row = $rowArr[0];
    	// 获取关键字类型
    	$keyType = strval($row["COLUMN_KEY"]);

    	// 是否为主键 ?
    	return $keyType == "PRI";
    }

    /**
     * 判断某个数据库中的某个表的某个字段是不是键 ?
     *
     * @param $pdo
     * @param $dbName
     * @param $tableName
     * @param $columnName
     * @return bool
     *
     */
    static function isKey($pdo, $dbName, $tableName, $colName) {
    	// 获取表定义
    	$rowArr = self::getInfoSchema($pdo, $dbName, $tableName, $colName);
    	// 个数是否 <= 0
    	if (count($rowArr) <= 0) {
    		return false;
    	}
    
    	// 获取第一条数据
    	$row = $rowArr[0];
    	// 获取关键字类型
    	$keyType = strval($row["COLUMN_KEY"]);
    
    	// 是否为键 ?
    	return strlen($keyType) > 0;
    }
}
