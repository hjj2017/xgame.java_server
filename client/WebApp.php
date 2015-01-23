<?php
ini_set("memory_limit", "256M");
ini_set("date.timezone", "Asia/Chongqing");

// import
require_once("MyLog.php");

/**
 * 框架类
 *
 */
class WebApp {
    /**
     * 开始工作
     *
     */
    function start($action) {
        if (!$action) {
            // 如果 action 为空,
            // 则直接退出!
            MyLog::LOG()->error("action 为空");
            return;
        }

        // 获取类名称和方法名称
        $clazzAndMethod = $this::getClazzAndMethod($action);
        $clazz = $this::getClazz($clazzAndMethod);
        $method = $this::getMethod($clazzAndMethod);

        // 获取类文件
        $clazzFilePath = __DIR__ . "/" . $this::getClazzFilePath($action) . ".php";
        MyLog::LOG()->info("包含类文件 : $clazzFilePath");
        require_once($clazzFilePath);

        // 创建类对象
        $newObj = new $clazz;

        if (!property_exists($newObj, $method)) {
            // 如果不存在指定的方法,
            // 则直接退出!
            MyLog::LOG()->error(
                "类 $clazz 中没有名为 $method 的方法"
            );
        }

        // 记录日志信息
        MyLog::LOG()->info("调用类 $clazz 的 $method 方法");
        $newObj->$method();
    }

    /**
     * 获取类名称和方法名称
     *
     * @param $src
     * @return String
     *
     */
    private static function getClazzAndMethod($src) {
        // 根据斜杠分割字符串
        $arr = preg_split("[\\/]", $src);
        $arrCount = count($arr);

        if ($arrCount > 0) {
            return $arr[$arrCount - 1];
        } else {
            return $src;
        }
    }

    /**
     * 获取类名称
     *
     * @param $str
     * @return String
     *
     */
    private static function getClazz($str) {
        $arr = preg_split("[:]", $str);
        return $arr[0];
    }

    /**
     * 获取方法名称
     *
     * @param $str
     * @return String
     *
     */
    private static function getMethod($str) {
        $arr = preg_split("[:]", $str);

        if (count($arr) > 1) {
            return $arr[2];
        } else {
            return "render";
        }
    }

    /**
     * 获取类文件路径
     *
     * @param $str
     * @return String
     *
     */
    private static function getClazzFilePath($str) {
        $arr = preg_split("[:]", $str);
        $arrCount = count($arr);

        if ($arrCount > 2) {
            return $arr[$arrCount - 3];
        } else {
            return $str;
        }
    }

    /**
     * 获取文件的物理路径
     *
     * @param $fileName
     * @return String
     *
     */
    public static function getPhysicalPath($fileName) {
        return __DIR__ . "/" . $fileName;
    }
}

// 全局应用程序
$theApp = new WebApp();

