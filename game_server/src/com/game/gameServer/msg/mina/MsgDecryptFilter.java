package com.game.gameServer.msg.mina;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;

import com.game.gameServer.msg.SpecialMsgSerialUId;
import com.game.part.msg.IoBuffUtil;
import com.game.part.msg.MsgLog;
import com.game.part.util.BytesUtil;
import com.game.part.util.MD5Util;

/**
 * 消息解密, 该类负责三件事情:
 * <ol>
 * <li>消息体验证;</li>
 * <li>时序验证;</li>
 * <li>消息解密; ( 当然, 消息加密和解密这一步还没具体实现 )</li>
 * </ol>
 * 
 * @author hjj2017
 * @since 2014/3/17
 * 
 */
public class MsgDecryptFilter extends IoFilterAdapter {
	/** 时间戳字典 */
	private static final ConcurrentHashMap<Long, Integer> _tsMap = new ConcurrentHashMap<>();
	/** MD5 Key */
	private static final byte[] MD5_KEY = "F4HQ?zMfA097dpiJ>&L=r8#TRY(Wtxja".getBytes();
	/** 加密标志 */
	private static final AtomicBoolean _enabled = new AtomicBoolean(true);

	@Override
	public void sessionClosed(NextFilter nextFilter, IoSession sessionObj) throws Exception {
		if (nextFilter == null || 
			sessionObj == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return;
		}

		// 从字典中移除时间戳
		_tsMap.remove(sessionObj.getId());
		// 向下传递
		super.sessionClosed(nextFilter, sessionObj);
	}

	@Override
	public void messageReceived(
		NextFilter nextFilter, IoSession sessionObj, Object msgObj) throws Exception {
		if (nextFilter == null || 
			sessionObj == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			MsgLog.LOG.error("null nextFilter or sess");
			return;
		}

		if (!_enabled.get()) {
			// 如果消息没有加密, 
			// 则直接退出!
			super.messageReceived(nextFilter, sessionObj, msgObj);
			return;
		}

		// 获取会话 Id
		long sessionUId = sessionObj.getId();

		if (msgObj == null) {
			// 如果消息对象为空, 
			// 则直接退出!
			MsgLog.LOG.error("null msgObj, sessionUId = " + sessionUId);
			return;
		}

		if (!(msgObj instanceof IoBuffer)) {
			// 如果消息对象不是 ByteBuff, 
			// 则直接向下传递!
			MsgLog.LOG.warn("msgObj is not a ByteBuff, sessionUId = " + sessionUId);
			super.messageReceived(nextFilter, sessionObj, msgObj);
		}

		// 获取 Buff
		IoBuffer buff = (IoBuffer)msgObj;
		// 位置归零
		buff.position(0);

		if (buff.hasRemaining() && 
			validateBuffContent(buff, sessionUId) == false) {
			// 如果验证 Buff 失败, 
			// 则直接退出!
			sessionObj.close(true);
			return;
		}

		// 向下传递
		super.messageReceived(nextFilter, sessionObj, msgObj);
	}

	/**
	 * 验证 Buff 对象
	 * 
	 * @param buff 
	 * @param fromSessionUId
	 * @return 
	 * 
	 */
	private static boolean validateBuffContent(IoBuffer buff, long fromSessionUId) {
		if (buff == null || 
			buff.hasRemaining() == false) {
			// 如果参数对象为空, 
			// 则直接退出!
			return false;
		}

		// 原始位置
		final int origPos = buff.position();
		// 创建消息结构
		MsgStruct msg = createMsgStruct(buff, fromSessionUId);
		// 恢复到原始位置
		buff.position(origPos);

		if (msg == null) {
			// 消息对象无法识别, 
			// 则直接退出!
			MsgLog.LOG.error("null msg, sessionUUId = " + fromSessionUId);
			return false;
		}

		if (msg._exemption) {
			// 如果是不完整消息, 
			// 或者是免检消息, 
			// 则直接退出!
			return true;
		}

		if (!validateTS(msg)) {
			// 如果时间戳验证失败, 
			// 则直接退出!
			MsgLog.LOG.error("msg.ts error, sessionUId = " + fromSessionUId);
			return false;
		}

		if (!validateMD5(msg)) {
			// 如果 MD5 验证失败, 
			// 则直接退出!
			MsgLog.LOG.error("msg._md5 error, sessionUId = " + fromSessionUId);
			return false;
		}

		return true;
	}

	/**
	 * 根据 Buff 对象, 创建消息结构. 
	 * <font color="#990000">注意 : 创建过程中会修改 buff 的 position 值</font>
	 * 
	 * @param buff
	 * @param fromSessionUUId 
	 * @return 
	 * 
	 */
	private static MsgStruct createMsgStruct(IoBuffer buff, long fromSessionUUId) {
		if (buff == null || 
			buff.remaining() < 4) {
			// 如果参数对象为空, 
			// 或者剩余字节数 < 4 ( 没法分析出消息长度和类型 )
			// 则直接退出!
			MsgLog.LOG.error("null buff or remaining < 4, sessionUId = " + fromSessionUUId);
			return null;
		}

		// 回到起始位置
		buff.position(0);
		// 创建消息结构
		MsgStruct msg = new MsgStruct();
		// 来自会话 UUId
		msg._sessionUId = fromSessionUUId;
		// 获取消息长度
		msg._len = IoBuffUtil.readShort(buff);
		// 获取消息类型
		msg._serialUId = IoBuffUtil.readShort(buff);

		if (msg._serialUId == SpecialMsgSerialUId.CG_FLASH_POLICY || 
			msg._serialUId == SpecialMsgSerialUId.CG_QQ_TGW) {
			// 如果是 Flash 安全策略消息或者是 QQ 网关消息, 
			// 则设置为免检并返回!
			msg._exemption = true;
			return msg;
		}

		// 获取时间戳
		msg._ts = IoBuffUtil.readInt(buff);
		// 获取 MD5 字符串
		msg._md5 = IoBuffUtil.readStr(buff);

		if (msg._md5 == null || 
			msg._md5.isEmpty()) {
			// 如果 MD5 字符串为空, 
			// 则直接退出!
			MsgLog.LOG.error("null or empty md5, sessionUId " + fromSessionUUId);
			return null;
		}

		// 消息体长度
		int bodyLen = (msg._len - 10 - msg._md5.length());

		if (bodyLen < 0) {
			// 如果消息长度不够, 
			// 则直接退出!
			MsgLog.LOG.error(
				"bodyLen < 0, msgTypeID = " + msg._serialUId 
				+ ", sessionUId = " + fromSessionUUId
			);
			return null;
		}

		// 创建消息体字节数组
		msg._body = new byte[bodyLen];
		// 从 Buff 中获取数组
		buff.get(msg._body);

		return msg;
	}

	/**
	 * 验证时间戳
	 * 
	 * @param msg
	 * @return 
	 * 
	 */
	private static boolean validateTS(MsgStruct msg) {
		if (msg == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return false;
		}

		if (msg._exemption) {
			// 如果是免检消息, 
			// 则直接退出!
			return true;
		}

		// 获取消息时间戳
		int newTS = msg._ts;
		// 获取旧时间戳
		Integer oldTS = _tsMap.get(msg._sessionUId);

		if (oldTS == null) {
			oldTS = Integer.MIN_VALUE;
		}

		if (newTS <= oldTS) {
			// 可能是重发消息, 
			// 直接断开!
			return false;
		}

		// 更新时间戳
		_tsMap.put(msg._sessionUId, newTS);
		return true;
	}

	/**
	 * 验证 MD5 字符串
	 * 
	 * @param msg
	 * @return 
	 * 
	 */
	private static boolean validateMD5(MsgStruct msg) {
		if (msg == null || 
			msg._md5 == null || 
			msg._md5.isEmpty()) {
			// 如果参数对象为空, 
			// 则直接退出!
			return false;
		}

		// 获取时间戳字节数组
		byte[] tsBytes = BytesUtil.int2bytes(msg._ts);
		// 连接消息内容和时间戳
		byte[] bytes_md5 = ArrayUtils.addAll(msg._body, tsBytes);
		// 添加 MD5 Key
		bytes_md5 = ArrayUtils.addAll(bytes_md5, MD5_KEY);
		// 进行 MD5 加密
		String str_md5 = MD5Util.encodeByMD5(bytes_md5);
		str_md5 = str_md5.toLowerCase();
		// 验证消息中的 MD5 字符串, 
		// 注意 : 消息中的 MD5 字符串是一个子集 ...
		// 所以要用包含来验证
		return str_md5.contains(msg._md5);
	}

	/**
	 * 消息结构
	 * 
	 * @author hjj2017
	 *
	 */
	private static class MsgStruct {
		/** 会话 UUId */
		private long _sessionUId = -1L;
		/** 消息长度 */
		private short _len = -1;
		/** 消息序列化 Id */
		private short _serialUId = -1;
		/** 免检消息 ? */
		private boolean _exemption = false;
		/** 时间戳 */
		private int _ts = -1;
		/** MD5 字符串 */
		private String _md5 = null;
		/** 消息体 */
		private byte[] _body = null;
	}

	/**
	 * 是否已开启 ?
	 * 
	 * @return
	 * 
	 */
	public static boolean isEnabled() {
		return _enabled.get();
	}

	/**
	 * 设置开启状态
	 * 
	 * @param value 
	 * 
	 */
	public static void setEnabled(boolean value) {
		_enabled.set(value);
	}
}
