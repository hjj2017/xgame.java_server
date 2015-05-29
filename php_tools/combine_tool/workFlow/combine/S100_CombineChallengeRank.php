<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 精英副本挑战 */
class S100_CombineChallengeRank extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
        CombineHelper::combine(
            "t_challenge_rank", null, $myParam->_sheepPDO, $myParam->_wolfPDO
        );
    }
}
