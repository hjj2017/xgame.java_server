<?php
// @import
require_once(__DIR__ . "/../../AbstractWorkNode.php");
require_once(__DIR__ . "/../../SQLHelper.php");

/** 过关斩将 */
class S100_RenameChallengeRank extends AbstractWorkNode {
    // @Overrid
    public function doAction($myParam) {
        // 更新创建者名称
        SQLHelper::executeNonQuery(
            $myParam->_gamePDO,
            "update t_challenge_rank as A, t_human_renaming as B
                set A.charName = B.`human_new_full_name`
              where A.charId = B.`human_uuid`"
        );
    }
}
