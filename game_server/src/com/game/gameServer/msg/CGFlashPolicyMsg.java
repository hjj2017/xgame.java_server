package com.game.gameServer.msg;

import com.game.gameServer.handler.Handler_CGFlashPolicyMsg;
import com.game.part.msg.anno.MsgSerialUId;

/**
 * Flash 安全安全策略文件
 * 
 * @author hjj2017
 * @since 2015/3/18
 * 
 */
@MsgSerialUId(CoreMsgSerialUId.CG_FLASH_POLICY)
public class CGFlashPolicyMsg extends AbstractCGMsgObj<Handler_CGFlashPolicyMsg> {
	@Override
	public Handler_CGFlashPolicyMsg getSelfHandler() {
		// 返回消息处理器
		return new Handler_CGFlashPolicyMsg();
	}
}
