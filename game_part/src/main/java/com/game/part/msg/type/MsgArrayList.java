package com.game.part.msg.type;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.game.part.msg.IoBuffUtil;
import com.game.part.util.Assert;

/**
 * 消息数组列表
 * 
 * @author hjj2017
 * @param <T>
 * 
 */
public final class MsgArrayList<T extends AbstractMsgField> extends AbstractMsgField implements List<T> {
	/** 数值列表 */
	private final List<T> _objValList = new ArrayList<>();
	/** 列表项目创建器 */
	private final IItemCreator<T> _creator;

	/**
	 * 类参数构造器
	 * 
	 * @param creator
	 * 
	 */
	public MsgArrayList(IItemCreator<T> creator) {
		// 断言参数不为空
		Assert.notNull(creator, "条目创建者为空, 这是不允许的");
		// 设置条目创建者
		this._creator = creator;
	}

	@Override
	public void readBuff(ByteBuffer buff) {
		// 事先读取长度
		int len = IoBuffUtil.readShort(buff);

		for (int i = 0; i < len; i++) {
			// 创建新条目
			T newItem = this._creator.create();

			if (newItem != null) {
				// 如果新条目不为空, 
				// 则令新条目读取 Buff 中的数据
				newItem.readBuff(buff);
				this.add(newItem);
			}
		}
	}

	@Override
	public void writeBuff(ByteBuffer buff) {
		if (buff == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return;
		}

		// 获取列表长度
		short len = (short)this.size();
		// 写出列表长度
		IoBuffUtil.writeShort(len, buff);

		for (T currItem : this._objValList) {
			// 写出条目到 Buff
			currItem.writeBuff(buff);
		}
	}

	@Override
	public int size() {
		return this._objValList.size();
	}

	@Override
	public boolean isEmpty() {
		return this._objValList.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return this._objValList.contains(o);
	}

	@Override
	public Iterator<T> iterator() {
		return this._objValList.iterator();
	}

	@Override
	public Object[] toArray() {
		return this._objValList.toArray();
	}

	@Override
	public<A> A[] toArray(A[] a) {
		return this._objValList.toArray(a);
	}

	@Override
	public boolean add(T e) {
		if (e != null) {
			return this._objValList.add(e);
		} else {
			return false;
		}
	}

	@Override
	public boolean remove(Object e) {
		if (e != null) {
			return this._objValList.remove(e);
		} else {
			return false;
		}
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		if (c != null) {
			return this._objValList.contains(c);
		} else {
			return false;
		}
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		if (c != null) {
			return this._objValList.addAll(c);
		} else {
			return false;
		}
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		if (c != null) {
			return this._objValList.addAll(index, c);
		} else {
			return false;
		}
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		if (c != null) {
			return this._objValList.remove(c);
		} else {
			return false;
		}
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		if (c != null) {
			return this._objValList.retainAll(c);
		} else {
			return false;
		}
	}

	@Override
	public void clear() {
		this._objValList.clear();
	}

	@Override
	public T get(int index) {
		return this._objValList.get(index);
	}

	@Override
	public T set(int index, T element) {
		if (element != null) {
			return this._objValList.set(index, element);
		} else {
			return null;
		}
	}

	@Override
	public void add(int index, T element) {
		if (element != null) {
			this._objValList.add(index, element);
		}
	}

	@Override
	public T remove(int index) {
		return this._objValList.remove(index);
	}

	@Override
	public int indexOf(Object o) {
		if (o != null) {
			return this._objValList.indexOf(o);
		} else {
			return -1;
		}
	}

	@Override
	public int lastIndexOf(Object o) {
		if (o != null) {
			return this._objValList.lastIndexOf(o);
		} else {
			return -1;
		}
	}

	@Override
	public ListIterator<T> listIterator() {
		return this._objValList.listIterator();
	}

	@Override
	public ListIterator<T> listIterator(
		int index) {
		return this._objValList.listIterator(index);
	}

	@Override
	public List<T> subList(
		int fromIndex, 
		int toIndex) {
		return this._objValList.subList(
			fromIndex, toIndex
		);
	}

	/**
	 * 读入消息数组列表, 会做 null 断言
	 * 
	 * @param arrayList
	 * @param buff
	 * 
	 */
	public static void readBuff(MsgArrayList<?> arrayList, ByteBuffer buff) {
		if (buff == null) {
			// 如果 buff 对象为空, 
			// 则直接退出!
			return;
		}

		// 断言参数不为空
		Assert.notNull(arrayList, "消息数组列表为 null");
		// 从 buff 中读取数据
		arrayList.readBuff(buff);
	}

	/**
	 * 写出消息数组列表, 会做 null 判断
	 * 
	 * @param arrayList
	 * @param buff
	 * 
	 */
	public static void writeBuff(MsgArrayList<?> arrayList, ByteBuffer buff) {
		if (buff == null) {
			// 如果 buff 对象为空, 
			// 则直接退出!
			return;
		}

		if (arrayList == null || 
			arrayList.isEmpty()) {
			// 如果数组列表为空, 
			// 则只写出一个 0
			IoBuffUtil.writeShort((short)0, buff);
		} else {
			// 写出整个列表
			arrayList.writeBuff(buff);
		}
	}

	/**
	 * 条目创建器接口
	 * 
	 * @author hjj2017
	 * @param <T>
	 * 
	 */
	@FunctionalInterface
	public static interface IItemCreator<T> {
		/**
		 * 创建一个条目
		 * 
		 * @return
		 * 
		 */
		public T create();
	}
}
