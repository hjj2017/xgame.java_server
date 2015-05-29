<?php

/**
 * 文件实用工具类
 *
 * @author jinhaijiang
 * @since 2014/12/19
 *
 */
class FileUtil {
    /**
     * 遍历目录及子目录
     *
     * @param $dir String
     * @param $callbackObj ITraverseDirCallback
     * @return void
     *
     */
    public static function traverseDir($dir, $callbackObj) {
        if ($dir == null ||
            $callbackObj == null) {
            // 如果参数对象为空,
            // 则直接退出!
            MyLog::LOG()->error("参数对象为空");
            return;
        }

        // 输出日志信息
        MyLog::LOG()->info("准备遍历目录 ${dir}");
        // 收集文件数组
        $fileArr = FileUtil::collect($dir);
        // 排序
        sort($fileArr);

        foreach ($fileArr as $file) {
            // 执行回调
            $callbackObj->doAction($file);
        }
    }

    /**
     * 收集目录下得所有文件及子目录下得所有文件
     *
     * @param $dir
     * @return array | null
     *
     */
    private static function collect($dir) {
        if ($dir == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return null;
        }

        // 获取句柄
        $handle = opendir($dir);

        if ($handle == false) {
            // 如果文件句柄为空,
            // 则直接推出!
            return null;
        }

        // 先收集目录列表
        $fileArr = array();

        while(($file = readdir($handle)) != false) {
            if ($file == "." ||
                $file == "..") {
                continue;
            }

            $fileAbsName = "$dir/$file";

            if (is_file($fileAbsName)) {
                $fileArr []= $fileAbsName;
            }

            if (is_dir($fileAbsName)) {
                // 递归遍历目录及子目录
                $subDirFileArr = FileUtil::collect($fileAbsName);

                foreach ($subDirFileArr as $subDirFile) {
                    // 添加子目录文件
                    $fileArr []= $subDirFile;
                }
            }
        }

        // 关闭目录
        closedir($handle);
        // 返回文件数组
        return $fileArr;
    }

    /**
     * 获取文件名
     *
     * @param $path
     * @return String
     *
     */
    public static function getFileName($path) {
        // 根据 '/' 切割字符串
        $strArr = split("/", $path);

        if (count($strArr) <= 0) {
            // 如果字符串数组数量 <= 0,
            // 则直接返回原字符串
            return $path;
        } else {
            // 否则返回数组中最后的元素
            return $strArr[count($strArr) -1];
        }
    }
}

/**
 * 遍历目录接口
 *
 * @author jinhaijiang
 * @since 2014/12/19
 *
 */
interface ITraverseDirCallback {
    /**
     * 执行动作
     *
     * @param $pathName String
     * @return void
     *
     */
    function doAction($pathName);
}

