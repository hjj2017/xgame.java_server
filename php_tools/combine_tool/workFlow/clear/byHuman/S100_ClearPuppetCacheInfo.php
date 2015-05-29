<?php
// @import
require_once(__DIR__ . "/../../AbstractWorkNode.php");
require_once(__DIR__ . "/../../SQLHelper.php");

/** 傀儡缓存 */
class S100_ClearPuppetCacheInfo extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        // 执行 SQL 语句
        SQLHelper::executeNonQuery(
            $myParam->_sheepPDO,
            "delete from t_puppetcache_info where charId in ( select human_uuid from combine_clearByHuman )"
        );
    }
}
