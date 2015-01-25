package com.game.part.tmpl.codeGen;

import com.game.part.tmpl.anno.ObjColumn;
import com.game.part.tmpl.anno.ObjListColumn;
import com.game.part.tmpl.anno.PlainColumn;
import com.game.part.tmpl.anno.PlainListColumn;
import com.game.part.tmpl.codeGen.impl.Read_Obj;
import com.game.part.tmpl.codeGen.impl.Read_ObjList;
import com.game.part.tmpl.codeGen.impl.Read_Plain;
import com.game.part.tmpl.codeGen.impl.Read_PlainList;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 代码生成管理器
 * 
 * @author hjj2019
 * @since 2014/10/2
 * 
 */
class RegisteredReadCodeGen {
	/** 代码生成器字典 */
	private static volatile Map<Class<?>, IReadCodeGen> _genMap = null;

	/**
	 * 类默认构造器
	 * 
	 */
	private RegisteredReadCodeGen() {
	}

	/**
	 * 获取代码生成器
	 * 
	 * @param clazz
	 * @return 
	 * 
	 */
	public static IReadCodeGen getGen(Class<?> clazz) {
		if (_genMap == null) {
			synchronized (RegisteredReadCodeGen.class) {
				if (_genMap == null) {
					_genMap = new ConcurrentHashMap<Class<?>, IReadCodeGen>();
					_genMap.put(PlainColumn.class, new Read_Plain());
					_genMap.put(PlainListColumn.class, new Read_PlainList());
					_genMap.put(ObjColumn.class, new Read_Obj());
					_genMap.put(ObjListColumn.class, new Read_ObjList());
				}
			}
		}

		return _genMap.get(clazz);
	}
}
