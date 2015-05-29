<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../SQLHelper.php");

/** 坐骑 ( 幻化 )次数 */
class S100_CombineMountTimes extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        // 执行 SQL 语句
        CombineHelper::combine(
        	"t_mount_times", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
