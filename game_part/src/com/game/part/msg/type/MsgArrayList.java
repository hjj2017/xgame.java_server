package com.game.part.msg.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.mina.core.buffer.IoBuffer;

import com.game.part.msg.IoBuffUtil;
import com.game.part.utils.Assert;

/**
 * 消息数组列表
 * 
 * @author hjj2017
 * @param <T>
 * 
 */
public class MsgArrayList<T extends AbstractMsgField> extends AbstractMsgField implements List<T> {
	/** 数值列表 */
	private final List<T> _objValList = new ArrayList<>();
	/** 列表项目创建器 */
	private final IItemCreator<T> _creator;

	/**
	 * 类默认构造器
	 * 
	 */
	public MsgArrayList(IItemCreator<T> creator) {
		Assert.notNull(creator);
		this._creator = creator;
	}

	@Override
	public void readBuff(IoBuffer buff) {
		int len = IoBuffUtil.readInt(buff);

		for (int i = 0; i < len; i++) {
			T item = this._creator.create();
			item.readBuff(buff);
			this.add(item);
		}
	}

	@Override
	public void writeBuff(IoBuffer buff) {
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
	public<X> X[] toArray(X[] a) {
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
	 * objVal 不能为空, 但如果真为空值, 则自动创建
	 * 
	 * @param objVal
	 * @param <T>
	 * @return
	 * 
	 */
	static<T extends AbstractMsgField> MsgArrayList<T> ifNullThenCreate(MsgArrayList<T> objVal) {
		if (objVal == null) {
			// 创建对象
			objVal = new MsgArrayList<T>(() -> null);
		}

		return objVal;
	}

	@FunctionalInterface
	public static interface IItemCreator<T> {
		public T create();
	}
}
