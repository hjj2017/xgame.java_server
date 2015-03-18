package com.game.bizModules.flashPolicy.msg;

import com.game.bizModules.flashPolicy.handler.Handler_CGFlashPolicyMsg;
import com.game.gameServer.msg.AbstractCGMsgObj;
import com.game.gameServer.msg.SpecialMsgSerialUId;

/**
 * Flash 安全安全策略文件
 * 
 * @author hjj2017
 * @since 2015/3/18
 * 
 */
public class CGFlashPolicyMsg extends AbstractCGMsgObj<Handler_CGFlashPolicyMsg> {
	@Override
	public Handler_CGFlashPolicyMsg getSelfHandler() {
		// 返回消息处理器
		return new Handler_CGFlashPolicyMsg();
	}

	@Override
	public short getSerialUId() {
		return SpecialMsgSerialUId.CG_FLASH_POLICY;
	}
}
