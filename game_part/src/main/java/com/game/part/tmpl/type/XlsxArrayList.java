package com.game.part.tmpl.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.game.part.tmpl.XSSFRowReadStream;
import com.game.part.tmpl.XlsxTmplError;

/**
 * 列表字段
 * 
 * @author hjj2019
 * @since 2015/2/23
 * 
 */
public class XlsxArrayList<T extends AbstractXlsxCol> extends AbstractXlsxCol implements List<T> {
    /** 数值列表 */
    private final List<T> _objValList = new ArrayList<>();
    /** 最大个数 */
    private final int _maxNum;
    /** Xlsx 列的类定义 */
    private final Class<T> _clazzOfCol;

    /**
     * 类参数构造器
     *
     * @param maxNum 最大数量
     * @param clazzOfCol Xlsx 列的类定义
     * @throws IllegalArgumentException if num <= 0 || clazzOfCol == null
     *
     */
    public XlsxArrayList(int maxNum, Class<T> clazzOfCol) {
        if (maxNum <= 0 ||
            clazzOfCol == null) {
            // 如果参数对象为空,
            // 则抛出异常!
            throw new IllegalArgumentException("num <= 0 || clazzOfCol == null");
        }

        this._maxNum = maxNum;
        this._clazzOfCol = clazzOfCol;
    }

    /**
     * 类参数构造器
     *
     * @param tArr
     *
     */
    public XlsxArrayList(T ... tArr) {
        this(tArr == null ? 0 : tArr.length, tArr);
    }

    /**
     * 类参数构造器
     *
     * @param maxNum 最大数量
     * @param tArr Xlsx 列对象数组
     * @throws IllegalArgumentException if maxNum <= 0 || null or empty tArr
     *
     */
    public XlsxArrayList(int maxNum, T ... tArr) {
        if (maxNum <= 0 ||
            tArr == null ||
            tArr.length <= 0 ||
            tArr[0] == null) {
            // 如果参数对象为空,
            // 则抛出异常!
            throw new IllegalArgumentException("maxNum <= 0 || null or empty tArr");
        }

        this._maxNum = maxNum;
        this._clazzOfCol = (Class<T>)tArr[0].getClass();

        for (int i = 0; i < tArr.length; i++) {
            if (tArr[i] != null) {
                this._objValList.add(tArr[i]);
            }
        }
    }

    @Override
    public void validate() {
        if (this._objValList == null ||
            this._objValList.isEmpty()) {
            return;
        }

        this._objValList.forEach(o -> {
            if (o != null) {
                o.validate();
            }
        });
    }

    @Override
    protected void readImpl(XSSFRowReadStream fromStream) {
        if (fromStream == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        try {
            for (int i = 0; i < this._maxNum; i++) {
                if (fromStream.isEol()) {
                    // 如果已经读取到行尾,
                    // 则直接退出!
                    return;
                }

                T xlsxCol;

                if (i >= this.size()) {
                    // 创建新的对象
                    xlsxCol = this._clazzOfCol.newInstance();
                } else {
                    // 获取旧的对象
                    xlsxCol = this.get(i);
                }

                // 读取 Xlsx 行数据并添加到列表
                xlsxCol.readFrom(fromStream);
                this.add(xlsxCol);
            }
        } catch (Exception ex) {
            // 包装并抛出异常!
            throw new XlsxTmplError(ex);
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
}
