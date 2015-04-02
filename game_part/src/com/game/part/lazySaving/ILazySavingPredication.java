package com.game.core.persistance;

/**
 * PO 断言
 * 
 * @author hjj2017
 * @since 2015/3/31
 * 
 */
public interface IPOPredication {
	/** 无条件保存所有数据 */
	public static final IPOPredication unreserved = new IPOPredication() {
		@Override
		public boolean predicate(
			IPersistanceObject<?, ?> po) {
			return true;
		}
	};

	/**
	 * 断言 PO, 主要用于判断 PO 是否需要更新
	 * 
	 * @param po
	 * @return
	 * 
	 */
	boolean predicate(IPersistanceObject<?, ?> po);
}
