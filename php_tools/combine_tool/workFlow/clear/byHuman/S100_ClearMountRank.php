<?php
// @import
require_once(__DIR__ . "/../../AbstractWorkNode.php");
require_once(__DIR__ . "/../../SQLHelper.php");

/** 坐骑排行榜 */
class S100_ClearMountRank extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        // 执行 SQL 语句
        SQLHelper::executeNonQuery(
            $myParam->_sheepPDO,
            "delete from t_mount_rank where `id` in ( select human_uuid from combine_clearByHuman )"
        );
    }
}
