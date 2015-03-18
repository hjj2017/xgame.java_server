package com.game.gameServer.msg;

import com.game.gameServer.handler.Handler_CGFlashPolicyMsg;

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
		return CoreMsgSerialUId.CG_FLASH_POLICY;
	}
}
