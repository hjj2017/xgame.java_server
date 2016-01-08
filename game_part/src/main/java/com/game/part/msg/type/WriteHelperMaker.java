package com.game.part.msg.type;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.CtNewMethod;

import com.game.part.msg.MsgError;
import com.game.part.msg.MsgLog;
import com.game.part.msg.MsgServ;
import com.game.part.util.Assert;
import com.game.part.util.ClazzUtil;

/**
 * 读取帮助器构建者
 * 
 * @author hjj2017
 * @since 2015/3/15
 * 
 */
public final class WriteHelperMaker {
	/** 帮助者字典 */
	private static final Map<Class<?>, IWriteHelper> _helperMap = new ConcurrentHashMap<>();

	/**
	 * 类默认构造器
	 *
	 */
	private WriteHelperMaker() {
	}

	/**
	 * 构建帮助器
	 * 
	 * @param byClazz
	 * @return
	 * 
	 */
	public static IWriteHelper make(Class<?> byClazz) {
		// 断言参数不为空
		Assert.notNull(byClazz, "byClazz");
		// 获取帮助者
		IWriteHelper helper = _helperMap.get(byClazz);

		if (helper == null) {
			try {
				// 构建帮助者类并创建对象
				Class<IWriteHelper> clazz = buildHelperClazz(byClazz);
				helper = clazz.newInstance();
				// 缓存到字典
				_helperMap.put(byClazz, helper);
			} catch (MsgError err) {
				// 直接抛出异常
				throw err;
			} catch (Exception ex) {
				// 抛出异常
				MsgLog.LOG.error(ex.getMessage(), ex);
				throw new MsgError(ex);
			}
		}

		return helper;
	}

	/**
	 * 构建帮助者类
	 * 
	 * @param byClazz
	 * @return
	 * 
	 */
	private static Class<IWriteHelper> buildHelperClazz(Class<?> byClazz) {
		// 断言参数不为空
		Assert.notNull(byClazz, "byClazz");
		// 设置解析器名称
		final String helperClazzName = byClazz.getPackage().getName()
			+ ".WriteHelper_" 
			+ byClazz.getSimpleName();

		try {
			// 获取类池
			ClassPool pool = ClassPool.getDefault();
			// 获取接口类
			CtClass helperInterface = pool.getCtClass(IWriteHelper.class.getName());
			// 
			// 创建解析器 JAVA 类
			// 会生成如下代码 :
			// public class WriteHelper_GCUpgradeBuilding implements IWriteHelper 
			CtClass cc = pool.makeClass(helperClazzName);
			cc.addInterface(helperInterface);
			// 
			// 设置默认构造器
			// 会生成如下代码 :
			// WriteHelper_GCUpgradeBuilding() {}
			putDefaultConstructor(cc);

			// 创建代码上下文
			CodeContext codeCtx = new CodeContext();		
			// 
			// 将所有必须的类都导入进来, 
			// 会生成如下代码 : 
			// import org.apache.mina.core.buffer.IoBuffer;
			// import byClazz;
			codeCtx._importClazzSet.add(ByteBuffer.class);
			codeCtx._importClazzSet.add(byClazz);
			// 构建函数体
			buildFuncText(byClazz, codeCtx);

			// 生成方法之前先导入类
			importPackage(pool, codeCtx._importClazzSet);

			// 创建解析方法
			CtMethod cm = CtNewMethod.make(
				codeCtx._codeText.toString(), cc
			);

			// 添加方法
			cc.addMethod(cm);

			if (MsgServ.OBJ._outputClazzToDir != null &&
				MsgServ.OBJ._outputClazzToDir.isEmpty() == false) {
				// 如果输出目录不为空, 
				// 则写出类文件用作调试
				cc.writeFile(MsgServ.OBJ._outputClazzToDir);
			}

			// 获取 JAVA 类
			@SuppressWarnings("unchecked")
			Class<IWriteHelper> javaClazz = (Class<IWriteHelper>)cc.toClass();
			// 返回 JAVA 类
			return javaClazz;
		} catch (Exception ex) {
			// 抛出异常
			throw new MsgError(ex);
		}
	}

	/**
	 * 构建函数文本
	 * 
	 * @param byClazz
	 * @param codeCtx
	 * @return 
	 * 
	 */
	private static void buildFuncText(Class<?> byClazz, CodeContext codeCtx) {
		// 断言参数不为空
		Assert.notNull(byClazz, "byClazz");
		Assert.notNull(codeCtx, "codeCtx");

		// 函数头
		codeCtx._codeText.append("public void writeBuff(AbstractMsgObj msgObj, IoBuffer buff) {\n");
		// 增加空值判断
		codeCtx._codeText.append("if (msgObj == null || buff == null) { return; }\n");
		// 定义大 O 参数避免转型问题
		codeCtx._codeText.append(byClazz.getSimpleName())
			.append(" O = (")
			.append(byClazz.getSimpleName())
			.append(")msgObj;\n");

		// 构建写出字段文本
		buildWriteFieldText(byClazz, codeCtx);
		// 函数脚
		codeCtx._codeText.append("}");
	}

	/**
	 * 构建写出字段文本
	 * 
	 * @param byClazz
	 * @param codeCtx
	 * 
	 */
	private static void buildWriteFieldText(Class<?> byClazz, CodeContext codeCtx) {
		// 断言参数不为空
		Assert.notNull(byClazz, "byClazz");
		Assert.notNull(codeCtx, "codeCtx");

		// 
		// 获取类型为 AbstractMsgField 字段, 
		// 子类字段也算上
		List<Field> fl = ClazzUtil.listField(
			byClazz, f -> AbstractMsgField.class.isAssignableFrom(f.getType())
		);

		if (fl == null || 
			fl.isEmpty()) {
			// 如果字段列表为空, 
			// 则直接退出!
			return;
		}

		fl.forEach(f -> {
			if (ClazzUtil.isDrivedClazz(f.getType(), PrimitiveTypeField.class)) {
				// 如果是普通字段, 生成如下代码 : 
				// msgObj._funcId = MsgInt.ifNullThenCreate(msgObj._funcId);
				// msgObj._worker = MsgStr.ifNullThenCreate(msgObj._worker);
				codeCtx._codeText.append("O.")
					.append(f.getName())
					.append(" = ")
					.append(f.getType().getSimpleName())
					.append(".ifNullThenCreate(O.")
					.append(f.getName())
					.append(");\n");
			} else if (f.getType().equals(MsgArrayList.class)) {
				// 如果是列表字段, 则生成如下代码 : 
				// MsgArrayList.writeBuff(tmplObj._funcIdList, buff);
				// 在 write 函数中会自动判断 null, 
				// 并作相应处理...
				codeCtx._codeText.append("MsgArrayList.writeBuff(O.")
					.append(f.getName())
					.append(", buff);\n");

				// 添加到 import
				codeCtx._importClazzSet.add(MsgArrayList.class);
				// 直接退出, 不要再继续向下生成 : 
				// msgObj._funcIdList.writeBuff(buff);
				// 这样的代码了...
				return;
			} else if (ClazzUtil.isDrivedClazz(f.getType(), AbstractMsgObj.class)) {
				// 
				// 如果是嵌套的消息体, 生成如下代码 : 
				// if (msgObj._userInfo == null) {
				//     msgObj._userInfo = new UserInfo();
				// }
				// 
				// 先判断是否为空 ?
				codeCtx._codeText.append("if (O.")
					.append(f.getName())
					.append(" == null) {\n");

				// 如果为空则创建新对象
				codeCtx._codeText.append("O.")
					.append(f.getName())
					.append(" = new ")
					.append(f.getType().getSimpleName())
					.append("();\n");

				// 结束代码段
				codeCtx._codeText.append("}\n");
			} else {
				// 如果即不是 MsgInt, MsgStr ..., MsgArrayList, 
				// 也不是 AbstractMsgObj, 
				// 则直接退出!
				return;
			}

			codeCtx._importClazzSet.add(f.getType());
			// 生成如下代码 : 
			// msgObj._funcId.writeBuff(buff);
			codeCtx._codeText.append("O.")
				.append(f.getName())
				.append(".writeBuff(buff);\n");
		});
	}

	/**
	 * 添加 import 代码
	 * 
	 * @param pool
	 * @param importClazzSet 
	 * 
	 */
	private static void importPackage(ClassPool pool, Set<Class<?>> importClazzSet) {
		if (pool == null || 
			importClazzSet == null || 
			importClazzSet.isEmpty()) {
			// 如果参数对象为空, 
			// 则直接退出!
			return;
		}

		importClazzSet.forEach((c) -> {
			// 导入要用到的类
			pool.importPackage(c.getPackage().getName());
		});
	}

	/**
	 * 设置默认构造器, 会生成如下代码 : 
	 * <pre>
	 * Parser_Building() {}
	 * </pre>
	 * 
	 * @param cc
	 * @throws Exception 
	 * 
	 */
	private static void putDefaultConstructor(CtClass cc) throws Exception {
		if (cc == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return;
		}

		// 创建默认构造器
		CtConstructor constructor = new CtConstructor(new CtClass[0], cc);
		// 空函数体
		constructor.setBody("{}");
		// 添加默认构造器
		cc.addConstructor(constructor);
	}

	/**
	 * 代码上下文
	 * 
	 * @author hjj2017
	 * 
	 */
	private static class CodeContext {
		/** 引用类集合 */
		public final Set<Class<?>> _importClazzSet = new HashSet<>();
		/** 用于输出的代码文本 */
		public final StringBuilder _codeText = new StringBuilder();
	}
}
