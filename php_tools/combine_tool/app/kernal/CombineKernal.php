<?php
// @define
define("__WORKFLOW_DIR", __DIR__ . "/../../workFlow");
// @import
require_once(__DIR__ . "/../MyLog.php");
require_once(__DIR__ . "/../util/FileUtil.php");
require_once(__DIR__ . "/Kernal_ClearByHuman.php");
require_once(__DIR__ . "/Kernal_ClearPublic.php");
require_once(__DIR__ . "/Kernal_Combine.php");
require_once(__DIR__ . "/Kernal_Order.php");

/**
 * 合服工具核心类, 
 * 合服过程中的两个最核心的步骤是:
 * <ol>
 * <li>清理无用数据 ( clear )</li>
 * <li>合并数据 ( combine )</li>
 * <li>整理数据 ( order )</li>
 * </ol>
 *
 * <p>
 * 合服工具采用目录结构式的架构方式, 
 * <ol>
 * <li>"清理无用数据", 脚本文件放置在 workFlow/clear 目录下;</li>
 * <li>"合并数据", 脚本文件放置在 workFlow/combine 目录下;</li>
 * <li>"整理数据", 脚本文件放置在 workFlow/order 目录下;</li>
 * <ol>
 * </p>
 * 
 * <p><font color="#990000">注意 : 合服脚本文件的命名排序决定了合服过程的执行顺序!</font></p>
 * 
 * <p>
 * 合服过程中, 更名过程其实也是必要的过程! 但是,
 * 鉴于新玩家的命名规则将要修改为 "服务器名+角色名称" 的方式,
 * 名称重复的概率几乎为 0...
 * 所以, 在合服过程中的角色重命名步骤就被取消了
 * </p>
 *
 * @author jinhaijiang
 * @since 2014/12/19
 *
 */
class CombineKernal {
    /**
     * 开始合服, 合服过程包括:
     * <ol>
     * <li>清理无用数据 ( clear )</li>
     * <li>合并数据 ( combine )</li>
     * <li>整理数据 ( order )</li>
     * </ol>
     *
     */
    public function start() {
        // 清理 out 目录内容
        self::rmOutDir();
        
        // 自定义参数
        $myParam = new MyParam();
        // 附上狼服配置
        self::attachWolfConf($myParam);

        // 获取数据库配置
        $dbCfg = $GLOBALS["dbConfig"];
        // 关键字数组
        $keyArr = array_keys($dbCfg);

        $t0 = time();

        foreach ($keyArr as $key) {
            if (stripos($key, "sheepDb_") === false) {
                // 如果没有找到羊服配置 "sheepDb_",
                // 则直接跳过...
                continue;
            }

            // 附上羊服配置
            self::attachSheepConf($key, $myParam);
            // 记录日志
            MyLog::LOG()->info("* 处理羊服数据 : ${key}");
            
            // 开始合并
            ///////////////////////////////////////////////////////////
            // 
            // 1. 清除无用数据
            // 包括玩家数据和公共数据
            // 注意 : 在清理数据时分为 byHuman 和 public 两种形式,
            // byHuman 有一个明显特征,
            // 那就是在删除 SQL 中需要使用 "... in ( select human_uuid from combine_clearByHuman )"
            // 这样的子查询!
            MyLog::LOG()->info(">>> 清理无用数据");
            Kernal_ClearByHuman::process($myParam);
            Kernal_ClearPublic::process($myParam);

            // 2. 合并数据
            // 合并玩家数据和公共数据到狼服
            MyLog::LOG()->info(">>> 合并数据");
            Kernal_Combine::process($myParam);
        }

        // 3. 最后整理数据
        MyLog::LOG()->info(">>> 整理狼服数据");
        Kernal_Order::process($myParam);

        $t1 = time();
        $costTime = $t1 - $t0;

        // 4. 显示全部结束
        MyLog::LOG()->info("合服全部完成! 总耗时 = ${costTime} 秒");
    }

    /**
     * 清理 out 目录内容
     *
     */
    private static function rmOutDir() {
        // 删除 out 目录内容
        $outDir = __DIR__ . "/../../out";
        $outFiles = $outDir . "/*";
        // 删除所有文件
        @unlink($outFiles);
    }

    /**
     * 附上狼服配置
     *
     * @param $myParam MyParams
     * @return void
     *
     */
    private static function attachWolfConf($myParam) {
        if ($myParam == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        // 获取数据库配置
        $dbCfg = $GLOBALS["dbConfig"];
        // 获取狼服数据库配置
        $wolfDbCfg = $dbCfg["wolfDb"];

        // 数据库名称
        $dbName = $wolfDbCfg["dbName"];
        // 数据库服务器地址
        $dbHost = $wolfDbCfg["dbHost"];

        $dbConnStr = "mysql:host=${dbHost};dbname=${dbName}";
        $dbUser = $wolfDbCfg["dbUser"];
        $dbPass = $wolfDbCfg["dbPass"];

        try {
            // 记录日志文本
            MyLog::LOG()->info("创建数据库连接 = ${dbConnStr}");
            // 创建 PDO 对象
            $pdo = new PDO(
                $dbConnStr, $dbUser, $dbPass,
                array( PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION, )
            );

            // 设置编码
            $pdo->exec("set names 'utf8'");

            // 附上狼服数据库名称和 PDO 对象
            $myParam->_wolfDbName = $dbName;
            $myParam->_wolfPDO = $pdo;
        } catch (Exception $ex) {
            // 记录错误日志
            MyLog::LOG()->error($ex->getMessage(), $ex);
            die;
        }
    }

    /**
     * 附上羊服配置
     *
     * @param $key String
     * @param $myParam MyParams
     * @return void
     *
     */
    private static function attachSheepConf($key, $myParam) {
        if ($key == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        // 获取数据库配置
        $dbCfg = $GLOBALS["dbConfig"];
        // 获取羊服数据库配置
        $sheepDbCfg = $dbCfg[$key];

        if ($sheepDbCfg == null ||
            count($sheepDbCfg) <= 0) {
            // 如果羊服配置为空,
            // 则直接退出!
            return;
        }

        // 数据库名称
        $dbName = $sheepDbCfg["dbName"];
        // 数据库服务器地址
        $dbHost = $sheepDbCfg["dbHost"];

        $dbConnStr = "mysql:host=${dbHost};dbname=${dbName}";
        $dbUser = $sheepDbCfg["dbUser"];
        $dbPass = $sheepDbCfg["dbPass"];

        try {
            // 记录日志文本
            MyLog::LOG()->info("创建数据库连接 = ${dbConnStr}");
            // 创建 PDO 对象
            $pdo = new PDO(
                $dbConnStr, $dbUser, $dbPass,
                array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,)
            );

            // 设置编码
            $pdo->exec("set names 'utf8'");

            // 附上羊服数据库名称和 PDO 对象
            $myParam->_sheepDbName = $dbName;
            $myParam->_sheepPDO = $pdo;
        } catch (Exception $ex) {
            // 记录错误日志
            MyLog::LOG()->error($ex->getMessage(), $ex);
            die;
        }
    }
}

