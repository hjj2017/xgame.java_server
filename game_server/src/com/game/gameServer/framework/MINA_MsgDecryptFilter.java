package com.game.gameServer.framework;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;

import com.game.gameServer.msg.CoreMsgSerialUId;
import com.game.part.utils.BytesUtil;
import com.game.part.utils.MD5Util;

/**
 * 消息解密, 该类负责三件事情:
 * <ol>
 * <li>消息体验证</li>
 * <li>时序验证</li>
 * <li>消息解密</li>
 * </ol>
 * 
 * @author hjj2017
 * @since 2014/3/17
 * 
 */
class MINA_MsgDecryptFilter extends IoFilterAdapter {
	/** 时间戳字典 */
	private static final ConcurrentHashMap<Long, Integer> _tsMap = new ConcurrentHashMap<>();
	/** MD5 Key */
	private static final byte[] MD5_KEY = "F4HQ?zMfA097dpiJ>&L=r8#TRY(Wtxja".getBytes();
	/** 加密标志 */
	private static final AtomicBoolean _enabled = new AtomicBoolean(true);

	@Override
	public void sessionClosed(NextFilter nextFilter, IoSession sess) throws Exception {
		if (nextFilter == null || 
			sess == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			FrameworkLog.LOG.error("null nextFilter or sess");
			return;
		}

		// 从字典中移除时间戳
		_tsMap.remove(sess.getId());
		// 向下传递
		super.sessionClosed(nextFilter, sess);
	}

	@Override
	public void messageReceived(
		NextFilter nextFilter, IoSession sess, Object msgObj) throws Exception {
		if (nextFilter == null || 
			sess == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			FrameworkLog.LOG.error("null nextFilter or sess");
			return;
		}

		if (!_enabled.get()) {
			// 如果消息没有加密, 
			// 则直接退出!
			super.messageReceived(nextFilter, sess, msgObj);
			return;
		}

		// 获取会话 UUID
		long sessionUUID = sess.getId();

		if (msgObj == null) {
			// 如果消息对象为空, 
			// 则直接退出!
			FrameworkLog.LOG.error("null msgObj, sessionUUID = " + sessionUUID);
			return;
		}

		if (!(msgObj instanceof IoBuffer)) {
			// 如果消息对象不是 ByteBuff, 
			// 则直接向下传递!
			FrameworkLog.LOG.warn("msgObj is not a ByteBuff, sessionUUID = " + sessionUUID);
			super.messageReceived(nextFilter, sess, msgObj);
		}

		// 获取 Buff
		IoBuffer buff = (IoBuffer)msgObj;
		// 位置归零
		buff.position(0);

		if (buff.hasRemaining() && 
			validateBuffContent(buff, sessionUUID) == false) {
			// 如果验证 Buff 失败, 
			// 则直接退出!
			sess.close(true);
			return;
		}

		// 向下传递
		super.messageReceived(nextFilter, sess, msgObj);
	}

	/**
	 * 验证 Buff 对象
	 * 
	 * @param buff 
	 * @param fromSessionUUID 
	 * @return 
	 * 
	 */
	private static boolean validateBuffContent(IoBuffer buff, long fromSessionUUID) {
		if (buff == null || 
			buff.hasRemaining() == false) {
			// 如果参数对象为空, 
			// 则直接退出!
			return false;
		}

		// 原始位置
		final int origPos = buff.position();
		// 创建消息结构
		MsgStruct msg = createMsgStruct(buff, fromSessionUUID);
		// 恢复到原始位置
		buff.position(origPos);

		if (msg == null) {
			// 消息对象无法识别, 
			// 则直接退出!
			FrameworkLog.LOG.error("null msg, sessionUUID = " + fromSessionUUID);
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
			FrameworkLog.LOG.error("msg.ts error, sessionUUID = " + fromSessionUUID);
			return false;
		}

		if (!validateMD5(msg)) {
			// 如果 MD5 验证失败, 
			// 则直接退出!
			FrameworkLog.LOG.error("msg._md5 error, sessionUUID = " + fromSessionUUID);
			return false;
		}

		return true;
	}

	/**
	 * 根据 Buff 对象, 创建消息结构. 
	 * <font color="#990000">注意 : 创建过程中会修改 buff 的 position 值</font>
	 * 
	 * @param buff
	 * @param fromSessionUUID 
	 * @return 
	 * 
	 */
	private static MsgStruct createMsgStruct(IoBuffer buff, long fromSessionUUID) {
		if (buff == null || 
			buff.remaining() < 4) {
			// 如果参数对象为空, 
			// 或者剩余字节数 < 4 ( 没法分析出消息长度和类型 )
			// 则直接退出!
			FrameworkLog.LOG.error("null buff or remaining < 4, sessionUUID = " + fromSessionUUID);
			return null;
		}

		// 回到起始位置
		buff.position(0);
		// 创建消息结构
		MsgStruct msg = new MsgStruct();
		// 来自会话 UUID
		msg._sessionUUId = fromSessionUUID;
		// 获取消息长度
		msg._len = readShort(buff);
		// 获取消息类型
		msg._serialUId = readShort(buff);

		if (msg._serialUId == CoreMsgSerialUId.CG_FLASH_POLICY || 
			msg._serialUId == CoreMsgSerialUId.CG_QQ_TGW) {
			// 如果是 Flash 安全策略消息或者是 QQ 网关消息, 
			// 则设置为免检并返回!
			msg._exemption = true;
			return msg;
		}

		// 获取时间戳
		msg._ts = readInt(buff);
		// 获取 MD5 字符串
		msg._md5 = readString(buff);

		if (msg._md5 == null || 
			msg._md5.isEmpty()) {
			// 如果 MD5 字符串为空, 
			// 则直接退出!
			FrameworkLog.LOG.error("null or empty md5, sessionUUID " + fromSessionUUID);
			return null;
		}

		// 消息体长度
		int bodyLen = (msg._len - 10 - msg._md5.length());

		if (bodyLen < 0) {
			// 如果消息长度不够, 
			// 则直接退出!
			FrameworkLog.LOG.error(
				"bodyLen < 0, msgTypeID = " + msg._serialUId 
				+ ", sessionUUID = " + fromSessionUUID
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
		Integer oldTS = _tsMap.get(msg._sessionUUId);

		if (oldTS == null) {
			oldTS = Integer.MIN_VALUE;
		}

		if (newTS <= oldTS) {
			// 可能是重发消息, 
			// 直接断开!
			return false;
		}

		// 更新时间戳
		_tsMap.put(msg._sessionUUId, newTS);
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
	 * 读取短整形数值
	 * 
	 * @param buff
	 * @return 
	 * 
	 */
	private static short readShort(IoBuffer buff) {
		if (buff == null || 
			buff.remaining() < 2) {
			FrameworkLog.LOG.error("null buff or remaining < 2");
			return -1;
		} else {
			return buff.getShort();
		}
	}

	/**
	 * 获取整数数值
	 * 
	 * @param buff
	 * @return 
	 * 
	 */
	private static int readInt(IoBuffer buff) {
		if (buff == null || 
			buff.remaining() < 4) {
			FrameworkLog.LOG.error("null buff or remaining < 4");
			return -1;
		} else {
			return buff.getInt();
		}
	}

	/**
	 * 从 Buff 中读取字符串
	 * 
	 * @param buff
	 * @return 
	 * 
	 */
	private static String readString(IoBuffer buff) {
		if (buff == null || 
			buff.remaining() < 2) {
			// 如果参数对象为空, 
			// 则直接退出!
			FrameworkLog.LOG.error("null buff or remaining < 2");
			return null;
		}

		// 获取字符串长度
		short len = buff.getShort();
		// 创建字节数组
		byte[] byteArr = new byte[len];
		// 从 Buff 对象中获取字节数组
		buff.get(byteArr);

		try {
			// 返回字符串
			return new String(byteArr, "utf-8");
		} catch (Exception ex) {
			// 记录异常信息
			FrameworkLog.LOG.error(ex.getMessage(), ex);
			return null;
		}
	}

	/**
	 * 消息结构
	 * 
	 * @author hjj2017
	 *
	 */
	private static class MsgStruct {
		/** 会话 UUId */
		private long _sessionUUId = -1L;
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
