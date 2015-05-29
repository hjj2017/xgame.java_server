<?php
// @import
require_once(__DIR__ . "/../../AbstractWorkNode.php");
require_once(__DIR__ . "/../../SQLHelper.php");

/** 坐骑幻化 */
class S100_ClearMountHuanHua extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        // 执行 SQL 语句
        SQLHelper::executeNonQuery(
            $myParam->_sheepPDO,
            "delete from t_mount_huanhua where `uuid` in ( select human_uuid from combine_clearByHuman )"
        );
    }
}
