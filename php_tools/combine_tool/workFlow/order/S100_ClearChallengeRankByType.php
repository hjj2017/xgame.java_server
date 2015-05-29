<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../SQLHelper.php");

/** 清楚所有过关斩将首次击杀记录 */
class S100_ClearChallengeRankByType extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        SQLHelper::executeNonQuery(
            $myParam->_wolfPDO,
            "delete from t_challenge_rank where `type` = 0"
        );
    }
}
