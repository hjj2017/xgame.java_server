package com.game.part.util;

/**
 * 输出参数
 * 
 * @author hjj2017
 * @param <T> 
 * 
 */
public class Out<T> {
    /** 参数值 */
    private T _val = null;

    /**
     * 获取参数值
     *
     * @return
     *
     */
    public T getVal() {
        return this._val;
    }

    /**
     * 设置参数值
     *
     * @param value
     *
     */
    public void setVal(T value) {
        this._val = value;
    }

    /**
     * 获取输出值, 如果输出值为空则使用备选值
     *
     * @param out
     * @param optVal
     * @param <T>
     * @return
     *
     */
    public static<T> T optVal(Out<T> out, T optVal) {
        return (out.getVal() == null) ? optVal : out.getVal();
    }

    /**
     * 如果输出参数不为空则设置数值
     *
     * @param out
     * @param val
     *
     */
    public static<T> void putVal(Out<T> out, T val) {
        if (out != null) {
            out.setVal(val);
        }
    }
}

