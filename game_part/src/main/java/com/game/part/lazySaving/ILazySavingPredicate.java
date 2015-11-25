package com.game.part.lazySaving;

/**
 * 延迟保存断言
 * 
 * @author hjj2017
 * @since 2015/3/31
 * 
 */
@FunctionalInterface
public interface ILazySavingPredicate {
	/**
	 * 无条件保存所有数据,
	 * 可以用这个条件来完成停服前的数据保存操作...
	 * 代码示例:
	 * <pre>
	 * LazySavingHelper.OBJ.execUpdateWithPredicate(ILazySavingPredicate.unreserved);
	 * </pre>
	 * 
	 */
	public static final ILazySavingPredicate unreserved = (lso) -> true;

	/**
	 * 断言 LSO, 主要用于判断业务对象是否需要保存?
	 * 
	 * @param lso
	 * @return
	 * 
	 */
	boolean predicate(ILazySavingObj<?> lso);
}
