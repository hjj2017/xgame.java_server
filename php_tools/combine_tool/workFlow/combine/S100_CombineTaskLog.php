<?php
// @import
require_once(__DIR__ . "/../AbstractWorkNode.php");
require_once(__DIR__ . "/../CombineHelper.php");

/** 任务 */
class S100_CombineTaskLog extends AbstractWorkNode {
    // @Override
    function doAction($myParam) {
    	// 
    	// 任务合服时与其他功能模块不同, 
    	// 首先任务日志表中的 Id 列是自增列, 
    	// 所以在合服时不能合并该列!
    	// 另外, 任务中有一类是日常任务, 
    	// 日常任务是会被反复接取的...
    	// 但我们只记录一次就可以了!
    	// 否则数据量就太大、太大、太大、太大了...
        CombineHelper::combineByWhere(
            "t_task_log", 
        	array( "charId", "taskId", "finishTime" ), 
        	"group by charId, taskId", 
        	$myParam->_sheepPDO, 
        	$myParam->_wolfPDO
        );
    }
}
