package com.game.part.msg;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;

/**
 * IoBuffer 实用工具类
 * 
 * @author hjj2019
 * @since 2014/4/2
 *
 */
public final class IoBufferUtil {
	/**
	 * 类默认构造器
	 * 
	 */
	private IoBufferUtil() {
	}

	/**
	 * 将 from 添加到 to 的末尾, 
	 * <font color="#990000">注意 : 该过程会修改 to 的 limit 值, 但不会修改 position</font>
	 * 
	 * @param from
	 * @param to 
	 * 
	 */
	public static void append(IoBuffer to, IoBuffer from) {
		if (from == null || 
			from.hasRemaining() == false || 
			to == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return;
		}

		// 获取旧位置
		final int origPos0 = from.position();
		final int origPos1 = to.position();

		// 修改位置准备添加
		from.position(0);
		// 准备接收
		readyToNext(to);

		// 添加 from 到 to 的末尾
		to.put(from);
		// 令 limit = position, position = 0
		to.flip();
		// 还原旧位置
		from.position(origPos0);
		to.position(origPos1);
	}

	/**
	 * 查找指定字节所在的索引位置
	 * 
	 * @param src
	 * @param b
	 * 
	 */
	public static int indexOf(IoBuffer src, byte b) {
		if (src == null || 
			src.hasRemaining() == false) {
			// 如果参数对象为空, 
			// 则直接退出!
			return -1;
		}

		// 获取旧位置
		final int origPos = src.position();
		// 结果索引
		int resultIndex = -1;

		while (src.hasRemaining()) {
			if (src.get() == b) {
				resultIndex = src.position();
				break;
			}
		}

		// 还原位置
		src.position(origPos);
		// 返回结果索引
		return resultIndex;
	}

	/**
	 * 准备接受下次的消息, 
	 * <font color="#990000">注意 : 该过程会修改 containerBuff 的 position 和 limit 值. 
	 * 会令 position = limit, limit = capacity</font>
	 * 
	 * @param container 
	 * 
	 */
	public static void readyToNext(IoBuffer container) {
		if (container == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return;
		}

		// 更新位置和索引
		container.position(container.limit());
	}

	/**
	 * 从参数 from 当前位置起算, 拷贝指定个数的字节
	 * 
	 * @param from
	 * @param count
	 * @return 
	 * 
	 */
	public static IoBuffer copy(IoBuffer from, int count) {
		if (from == null || 
			from.hasRemaining() == false || 
			count <= 0) {
			// 如果参数对象为空, 
			// 则直接退出!
			return null;
		}

		// 获取旧位置
		final int origPos = from.position();
		// 创建新的 Buff
		IoBuffer newBuff = IoBuffer.allocate(count);

		for (int i = 0; i < count; i++) {
			// 拷贝字节
			newBuff.put(from.get());
		}

		// 还原位置
		from.position(origPos);
		// 令 limit = position; position = 0
		newBuff.flip();

		return newBuff;
	}

	/**
	 * 自定义的压缩 Buffer 对象的方法, 将未读数据复制到 Buffer 0 位置, 并收缩 limit 值
	 * 
	 * @param src 
	 * 
	 */
	public static void compact(IoBuffer src) {
		if (src == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return;
		}

		if (src.hasRemaining()) {
			// 如果还有剩余的字节, 
			// 则执行压缩 ...
			final int origPos = src.position();
			final int origLim = src.limit();
			// 压缩空间, 将未读取的字节复制到 Buff 的最开始位置
			src.compact();
			// 令 position = 0
			src.position(0);
			// 令 limit = 剩余的未读字节数
			src.limit(origLim - origPos);
		} else {
			// 如果没有剩余的字节, 
			// 则清除 position 和 limit
			src.position(0);
			src.flip();
		}
	}

	/**
	 * 从 Buff 中读取布尔值
	 * 
	 * @param buff
	 * @return 
	 * @throws MsgError 当 buff 对象为空或者字节数不够时
	 * 
	 */
	public static boolean readBool(IoBuffer buff) {
		return readByte(buff) == (byte)1;
	}

	/**
	 * 写出布尔值
	 * 
	 * @param buff
	 * @param val
	 * 
	 */
	public static void writeBool(IoBuffer buff, boolean val) {
		writeByte(buff, val ? (byte)1 : (byte)0);
	}

	/**
	 * 从 Buff 中读取一个字节
	 * 
	 * @param buff
	 * @return 
	 * @throws MsgError 当 buff 对象为空或者字节数不够时
	 * 
	 */
	public static byte readByte(IoBuffer buff) {
		if (buff == null || 
			buff.remaining() < 1) {
			// 如果参数对象为空, 
			// 则直接退出!
			throw new MsgError("buff 对象为空或者剩余的未读取的字节数 < 1");
		}

		// 读取一个字节
		return buff.get();
	}

	/**
	 * 写入一个字节
	 * 
	 * @param buff
	 * @param val 
	 * 
	 */
	public static void writeByte(IoBuffer buff, byte val) {
		if (buff != null) {
			buff.put(val);
		}
	}

	/**
	 * 读取整数数值
	 * 
	 * @param buff
	 * @return 
	 * @throws MsgError 当 buff 对象为空或者字节数不够时
	 * 
	 */
	public static int readInt(IoBuffer buff) {
		if (buff == null || 
			buff.remaining() < 4) {
			throw new MsgError("buff 对象为空或者剩余的未读取的字节数 < 4");
		} else {
			return buff.getInt();
		}
	}

	/**
	 * 写入整数数值
	 * 
	 * @param buff
	 * @param val
	 * 
	 */
	public static void writeInt(IoBuffer buff, int val) {
		if (buff != null) {
			buff.putInt(val);
		}
	}

	/**
	 * 从 Buff 中读取字符串
	 * 
	 * @param buff
	 * @return 
	 * @throws MsgError 当 buff 对象为空或者字节数不够时
	 * 
	 */
	public static String readStr(IoBuffer buff) {
		if (buff == null || 
			buff.remaining() < 2) {
			// 如果参数对象为空, 
			// 则直接退出!
			throw new MsgError("buff 对象为空或者剩余的未读取的字节数 < 2");
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
			MsgLog.LOG.error(ex.getMessage(), ex);
			return null;
		}
	}

	/**
	 * 写出字符串, Charset = utf-8
	 * 
	 * @param buff
	 * @param val
	 * 
	 */
	public static void writeStr(IoBuffer buff, String val) {
		// 写出字符串
		writeStr(buff, val, Charset.forName("utf-8"));
	}

	/**
	 * 写出字符串
	 * 
	 * @param buff
	 * @param val
	 * @param charset
	 * 
	 */
	public static void writeStr(IoBuffer buff, String val, Charset charset) {
		if (buff == null) {
			// 如果 buff 对象为空, 
			// 则直接退出!
			return;
		}
		
		if (val == null || 
			val.isEmpty()) {
			// 写出长度为 0
			buff.putShort((short)0);
			return;
		}

		// 活取字符串的字节数组
		byte[] byteArr = val.getBytes(charset);
		// 设置字节数组
		buff.putShort((short)byteArr.length);
		buff.put(byteArr);
	}
}
