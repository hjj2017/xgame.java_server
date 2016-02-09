package com.game.part.util;

/**
 * 装箱器, 将基本类型包装成对象类型
 *
 * @author hjj2017
 * @since 2016/2/8
 *
 */
public final class Boxer {
    /**
     * 类默认构造器
     *
     */
    private Boxer() {
    }

    /**
     * 将 int 装箱为 Integer
     *
     * @param i
     * @return
     *
     */
    public static Integer box(int i) {
        return new Integer(i);
    }

    /**
     * 将 long 装箱为 Long
     *
     * @param l
     * @return
     *
     */
    public static Long box(long l) {
        return new Long(l);
    }

    /**
     * 将 short 装箱为 Short
     *
     * @param $short
     * @return
     *
     */
    public static Short box(short $short) {
        return new Short($short);
    }

    /**
     * 将 float 装箱为 Float
     *
     * @param f
     * @return
     *
     */
    public static Float box(float f) {
        return new Float(f);
    }

    /**
     * 将 double 装箱为 Double
     *
     * @param d
     * @return
     *
     */
    public static Double box(double d) {
        return new Double(d);
    }

    /**
     * 将 char 装箱为 Character
     *
     * @param c
     * @return
     *
     */
    public static Character box(char c) {
        return new Character(c);
    }

    /**
     * 将 byte 装箱为 Byte
     *
     * @param b
     * @return
     *
     */
    public static Byte box(byte b) {
        return new Byte(b);
    }

    /**
     * 将 boolean 装箱为 Boolean
     *
     * @param bool
     * @return
     *
     */
    public static Boolean box(boolean bool) {
        return new Boolean(bool);
    }

    /**
     * 将 Object 装箱为 Object, 其实什么也不做直接返回
     *
     * @param o
     * @return
     *
     */
    public static Object box(Object o) {
        return o;
    }
}
