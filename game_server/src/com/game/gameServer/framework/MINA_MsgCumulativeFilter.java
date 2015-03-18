package com.game.gameServer.framework;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;

import com.game.gameServer.msg.SpecialMsgSerialUId;

/**
 * 消息粘包处理
 * 
 * @author hjj2017
 * @since 2014/3/17
 * 
 */
class MINA_MsgCumulativeFilter extends IoFilterAdapter {
	/** 
	 * 从客户端接收的消息估计长度,
	 * {@value} 字节, 
	 * 对于从客户端接收的数据来说, 都是简单的命令! 
	 * 很少超过 {@value}B
	 * 
	 */
	private static final int DECODE_MSG_LEN = 64;
	/** 容器 Buff 字典 */
	private static final ConcurrentHashMap<Long, IoBuffer> _containerBuffMap = new ConcurrentHashMap<>();

	@Override
	public void sessionClosed(NextFilter nextFilter, IoSession sessionObj) throws Exception {
		if (nextFilter == null || 
			sessionObj == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			FrameworkLog.LOG.error("null nextFilter or sess");
			return;
		}

		// 移除容器 Buff
		removeContainerBuff(sessionObj);
		// 向下传递
		super.sessionClosed(nextFilter, sessionObj);
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

		// 获取会话 UUId
		long sessionUUId = sess.getId();

		if (!(msgObj instanceof IoBuffer)) {
			// 如果消息对象不是 ByteBuff, 
			// 则直接向下传递!
			FrameworkLog.LOG.warn("msgObj is not a IoBuff, sessionUUId = " + sessionUUId);
			super.messageReceived(nextFilter, sess, msgObj);
		}

		// 获取输入 Buff
		IoBuffer inBuff = (IoBuffer)msgObj;

		if (!inBuff.hasRemaining()) {
			// 如果没有剩余内容, 
			// 则直接退出!
			FrameworkLog.LOG.error("inBuff has not remaining, sessionUUId = " + sessionUUId);
			return;
		} else if (inBuff.remaining() <= 8) {
			// 如果 <= 8 字节, 
			// 那还是执行粘包处理过程吧 ...
			// 8 字节 = 消息长度 ( Short ) + 消息类型 ( Short ) + 时间戳 ( Int )
			// 如果比这个长度都小, 
			// 那肯定不是一条完整消息 ...
			this.msgRecv_0(nextFilter, sess, inBuff);
			return;
		}

		// 获取消息长度
		final int msgSize = inBuff.getShort();
		inBuff.position(0);

		if (msgSize == inBuff.limit() && 
			containerBuffIsEmpty(sess)) {
			// 
			// 如果消息长度和极限值刚好相同, 
			// 并且容器 Buff 中没有任何内容 ( 即, 上一次消息没有粘包 ),
			// 那么直接向下传递!
			// 
			super.messageReceived(
				nextFilter, sess, inBuff
			);
		} else {
			// 
			// 如果消息长度和极限值不同, 
			// 则说明是网络粘包!
			// 这时候跳转到粘包处理过程 ...
			// 
			this.msgRecv_0(nextFilter, sess, inBuff);
		}
	}

	/**
	 * 接收连包消息
	 * 
	 * @param nextFilter
	 * @param sess
	 * @param inBuff
	 * @throws Exception 
	 * 
	 */
	private void msgRecv_0(
		NextFilter nextFilter, IoSession sess, IoBuffer inBuff) throws Exception {
		if (nextFilter == null || 
			sess == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			FrameworkLog.LOG.error("null nextFilter or sess");
			return;
		}

		// 获取会话 UUId
		long sessionUUId = sess.getId();
		// 获取容器 Buff
		IoBuffer containerBuff = getContainerBuff(sess);

		// 添加新 Buff 到容器 Buff 的末尾
		IoBufferUtil.append(containerBuff, inBuff);
		// 令 position = 0
		containerBuff.position(0);

//		// 记录调试信息
//		KernalLog.LOG.debug("\nin = [ " + inBuff.getHexDump() + " ]");

		for (int i = 0; ; i++) {
//			// 记录调试信息
//			KernalLog.LOG.debug(
//				"i = " + i 
//				+ "\nco = [ " + containerBuff.getHexDump() + " ]"
//				+ "\nco.pos = " + containerBuff.position() 
//				+ "\nco.lim = " + containerBuff.limit()
//			);

			if (containerBuff.remaining() < 4) {
				// 
				// 如果剩余字节数 < 4, 
				// 这样根本无法识别出消息类型 msgSerialUId ...
				// 直接退出!
				// 在退出前, 
				// 准备好接收下一次消息!
				// 
				IoBufferUtil.readyToNext(containerBuff);
				return;
			}

			// 获取原始位置
			final int oldPos = containerBuff.position();
			// 获取消息长度和类型
			final int msgSize = containerBuff.getShort();
			final int msgSerialUId = containerBuff.getShort();

//			// 记录调试信息
//			KernalLog.LOG.debug(
//				"i = " + i 
//				+ "\nmsgSize = " + msgSize
//				+ "\nmsgSerialUId = " + msgSerialUId
//			);

			// 还原原始位置
			containerBuff.position(oldPos);

			if (msgSerialUId == SpecialMsgSerialUId.CG_FLASH_POLICY || 
				msgSerialUId == SpecialMsgSerialUId.CG_QQ_TGW) {
				// 
				// 如果是 Flash 安全策略消息, 
				// 或者是腾讯网关消息, 
				// 则尝试找一下 0 字节的位置 ...
				// 
				int pos0 = IoBufferUtil.indexOf(containerBuff, (byte)0);

				if (pos0 <= -1) {
					// 如果找不到 0 字节的位置, 
					// 则说明消息还没接收完, 
					// 准备接受下次消息并直接退出!
					IoBufferUtil.readyToNext(containerBuff);
					return;
				}

				// 复制 Buff 内容
				containerBuff.position(0);
				IoBuffer realBuff = IoBufferUtil.copy(containerBuff, pos0);

				// 更新 Buff 位置
				final int newPos = containerBuff.position() + pos0;
				containerBuff.position(newPos);
				// 压缩容器 Buff
				IoBufferUtil.compact(containerBuff);

				// 向下传递
				super.messageReceived(
					nextFilter, sess, realBuff
				);
				continue;
			}

			if (msgSize <= 0) {
				// 
				// 如果消息长度 <= 0, 
				// 则直接退出!
				// 这种情况可能是消息已经乱套了 ...
				// 还是重新来过吧!
				// 
				FrameworkLog.LOG.error("i = " + i + ", msgSize = " + msgSize + ", sessionUUId = " + sessionUUId);
				// 将容器 Buff 内容清空
				containerBuff.position(0);
				containerBuff.flip();
				// 压缩容器 Buff
				IoBufferUtil.compact(containerBuff);
				return;
			}

			if (containerBuff.remaining() < msgSize) {
				// 
				// 如果消息长度不够, 
				// 则可能是出现网络粘包情况了 ...
				// 直接退出就可以了!
				// 
				FrameworkLog.LOG.warn(
					"i = " + i
					+ ", msgSize = " + msgSize 
					+ ", containerBuff.remaining = " + containerBuff.remaining()
					+ ", sessionUUId = " + sessionUUId
				);

				// 准备接受下一次消息
				IoBufferUtil.readyToNext(containerBuff);
				return;
			}

			// 创建新 Buff 并复制字节内容
			IoBuffer realBuff = IoBufferUtil.copy(containerBuff, msgSize);

			if (realBuff == null) {
				// 
				// 如果真实的 Buff 为空, 
				// 则直接退出!
				// 这种情况可能也是消息乱套了 ...
				// 记录一下错误信息
				// 
				FrameworkLog.LOG.error("i = " + i + ", null realBuff, sessionUUId = " + sessionUUId);
			} else {
//				// 记录调试信息
//				KernalLog.LOG.debug(
//					"i = " + i
//					+ "\nreal = [ " + realBuff.getHexDump() + " ]"
//					+ "\nreal.pos = " + realBuff.position()
//					+ "\nreal.lim = " + realBuff.limit()
//				);

				// 向下传递
				super.messageReceived(
					nextFilter, sess, realBuff
				);
			}

			// 更新位置
			containerBuff.position(containerBuff.position() + msgSize);
			// 压缩容器 Buff
			IoBufferUtil.compact(containerBuff);
		}
	}
	
	/**
	 * 获取玩家的 Buff, 如果为空则新建一个!
	 * 
	 * @param sess
	 * @return 
	 * 
	 */
	private static IoBuffer getContainerBuff(IoSession sess) {
		if (sess == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return null;
		}

		// 获取会话 UUId
		long sessionUUId = sess.getId();
		// 获取容器 Buff
		IoBuffer containerBuff = _containerBuffMap.get(sessionUUId);

		if (containerBuff == null) {
			// 创建缓存 Buff
			containerBuff = IoBuffer.allocate(DECODE_MSG_LEN);
			containerBuff.setAutoExpand(true);
			containerBuff.setAutoShrink(true);
			containerBuff.position(0);
			containerBuff.flip();
			// 缓存  Buff 对象
			Object oldVal = _containerBuffMap.putIfAbsent(sessionUUId, containerBuff);

			if (oldVal != null) {
				FrameworkLog.LOG.warn("exists oldVal");
			}
		}

		return containerBuff;
	}

	/**
	 * 移除容器 Buff
	 * 
	 * @param sess 
	 * 
	 */
	private static void removeContainerBuff(IoSession sess) {
		if (sess == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return;
		}

		// 获取会话 UUId
		long sessionUUId = sess.getId();
		// 获取容器 Buff
		IoBuffer containerBuff = _containerBuffMap.get(sessionUUId);

		if (containerBuff != null) {
			// 是否所占资源
			containerBuff.clear();
		}

		// 移除玩家的 Buff 对象
		_containerBuffMap.remove(sessionUUId);
	}

	/**
	 * 容器 Buff 为空 ?
	 * 
	 * @param sess
	 * @return 
	 * 
	 */
	private static boolean containerBuffIsEmpty(IoSession sess) {
		if (sess == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return false;
		}

		// 获取容器 Buff
		IoBuffer containerBuff = getContainerBuff(sess);

		if (containerBuff == null) {
			// 如果容器为空, 
			// 则直接退出!
			FrameworkLog.LOG.error("null containerBuff, sessionUUId = " + sess.getId());
			return false;
		} else {
			// 如果当前位置和极限值都为 0, 
			// 则判定为空!
			return (containerBuff.position() == 0 
				 && containerBuff.limit() == 0);
		}
	}
}
