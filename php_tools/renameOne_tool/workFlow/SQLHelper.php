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
     * 判断当前数据库中的某个表是否含有某个字段 ?
     * 注意 : 在这个函数中,
     * 建立 PDO 对象的时候已经确定要操作哪个数据库, 例如数据库连接字符串 = "mysql:host=${dbHost};dbname=${dbName}"
     * 所以无需特别指定数据库名称!
     * 另外一种情况是, 使用 "mysql:host=${dbHost};dbname="
     * 这样的数据库连接字符串建立 PDO 对象,
     * 那么就需要指定数据库名称 ...
     *
     * @param $pdo
     * @param $tableName
     * @param $columnName
     * @return bool
     * @see #hasColumnAtSpecificDb
     *
     */
    static function hasColumnAtCurrDb($pdo, $tableName, $columnName) {
        if ($pdo == null ||
            $tableName == null ||
            $columnName == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return false;
        }

        $sql = <<<__sql
select -1
  from `information_schema`.`COLUMNS`
 where `TABLE_SCHEMA` = database()
   and `TABLE_NAME` = '${tableName}'
   and `COLUMN_NAME` = '${columnName}'
__sql;

        try {
            // 准备 SQL 查询
            $stmt = $pdo->prepare($sql);
            // 执行查询
            $stmt->execute();
            // 获取记录集
            $rowArr = $stmt->fetchAll();

            return count($rowArr) > 0;
        } catch (Exception $ex) {
            // 记录错误日志
            MyLog::LOG()->error($ex->getMessage(), $ex);
        }

        return false;
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
    static function hasColumnAtSpecificDb($pdo, $dbName, $tableName, $columnName) {
        if ($pdo == null ||
            $tableName == null ||
            $columnName == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return false;
        }

        $sql = <<<__sql
select -1
  from `information_schema`.`COLUMNS`
 where `TABLE_SCHEMA` = '${dbName}'
   and `TABLE_NAME` = '${tableName}'
   and `COLUMN_NAME` = '${columnName}'
__sql;

        try {
            // 准备 SQL 查询
            $stmt = $pdo->prepare($sql);
            // 执行查询
            $stmt->execute();
            // 获取记录集
            $rowArr = $stmt->fetchAll();

            return count($rowArr) > 0;
        } catch (Exception $ex) {
            // 记录错误日志
            MyLog::LOG()->error($ex->getMessage(), $ex);
        }

        return false;
    }
}
