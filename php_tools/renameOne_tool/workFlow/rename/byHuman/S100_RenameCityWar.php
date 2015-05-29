<?php
// @import
require_once(__DIR__ . "/../../AbstractWorkNode.php");
require_once(__DIR__ . "/../../SQLHelper.php");

/** 逐鹿中原 */
class S100_RenameCityWar extends AbstractWorkNode {
    // @Overrid
    public function doAction($myParam) {
        // 更新收件人名称
        SQLHelper::executeNonQuery(
            $myParam->_gamePDO,
            "update t_city_war as A,
                    t_human_renaming as B
                set A.`humanName` = B.`human_new_full_name`
              where A.`human_uuid` = B.`human_uuid`"
        );
    }
}
