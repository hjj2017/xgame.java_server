package com.game.part.msg;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.List;

import com.game.part.msg.type.AbstractMsgField;
import com.game.part.msg.type.AbstractMsgObj;
import com.game.part.msg.type.MsgArrayList;
import com.game.part.utils.Assert;
import com.game.part.utils.ClazzUtil;
import com.game.part.utils.FieldUtil;

/**
 * 类定义验证器
 * 
 * @author hjj2017
 * @since 2015/3/17
 * 
 */
class ClazzDefValidator {
	/** 代码提醒 : MsgArrayList */
	private static final String CODE_HINT_msgArrayList = "请使用类似 : public MsgArrayList<MsgInt> _funcIdList = new MsgArrayList<>(() -> new MsgInt()); 这样的定义";

	/**
	 * 类默认构造器
	 * 
	 */
	private ClazzDefValidator() {
	}

	/**
	 * 验证消息类定义
	 * 
	 * @param msgClazz
	 * 
	 */
	static void validate(Class<?> msgClazz) {
		// 断言参数不为空
		Assert.notNull(msgClazz, "msgClazz");

		// 验证构造器
		validateConstructor(msgClazz);

		// 获取字段列表
		List<Field> fl = ClazzUtil.listField(
			msgClazz, f -> {
			return f != null && AbstractMsgField.class.isAssignableFrom(f.getType());
		});

		if (fl == null || 
			fl.isEmpty()) {
			// 如果字段列表为空, 
			// 则直接退出!
			return;
		}

		// 逐一验证每个字段
		fl.forEach(f -> validateField(f));
	}

	/**
	 * 验证构造器, 看看类上是否有不带参数的默认构造器
	 * 
	 * @param msgClazz
	 * 
	 */
	private static void validateConstructor(Class<?> msgClazz) {
		// 断言参数不为空
		Assert.notNull(msgClazz, "msgClazz");
		
		try {
			// 活取构造器数组
			Constructor<?>[] cArr = msgClazz.getConstructors();
			// 是否 OK ?
			boolean ok = false;

			for (Constructor<?> c : cArr) {
				if (c != null && 
					0 != (c.getModifiers() & Modifier.PUBLIC) &&
					c.getParameterCount() <= 0) {
					ok = true;
				}
			}

			if (!ok) {
				// 如果不 OK, 
				// 则直接抛出异常!
				throw new MsgError(MessageFormat.format(
					"类 {0} 没有定义公有的、无参数的默认构造器", 
					msgClazz.getName()
				));
			}
		} catch (MsgError err) {
			// 抛出异常
			throw err;
		} catch (Exception ex) {
			// 记录错误日志
			MsgLog.LOG.error(ex.getMessage(), ex);
			// 并抛出异常
			throw new MsgError(ex);
		}
	}

	/**
	 * 验证字段
	 * 
	 * @param f
	 * 
	 */
	private static void validateField(Field f) {
		// 断言参数不为空
		Assert.notNull(f, "f");
		// 获取定义这个字段的类
		Class<?> fromClazz = f.getDeclaringClass();

		if ((f.getModifiers() & Modifier.ABSTRACT) != 0) {
			// 如果字段是 abstract 的, 
			// 则直接抛出异常!
			throw new MsgError(MessageFormat.format(
				"类 {0} 字段 {1} 不能冠以 abstract", 
				fromClazz, f.getName()
			));
		}

		if ((f.getModifiers() & Modifier.PUBLIC) == 0) {
			// 如果字段是 private 或者 protected 的, 
			// 则直接抛出异常!
			throw new MsgError(MessageFormat.format(
				"类 {0} 字段 {1}, 没有定义为公有的 ( public ) !!", 
				fromClazz, f.getName()
			));
		}

		if ((f.getModifiers() & Modifier.STATIC) != 0) {
			// 如果字段是 static 的, 
			// 则直接抛出异常!
			throw new MsgError(MessageFormat.format(
				"类 {0} 字段 {1} 不能冠以 static", 
				fromClazz, f.getName()
			));
		}

		// 验证消息数组列表字段
		validateField_msgArrayList(f);
		// 获取字段类型
		final Class<?> fType = f.getType();

		if (AbstractMsgObj.class.isAssignableFrom(fType)) {
			// 如果字段的类型是 AbstractMsgObj 的子类, 
			// 则递归验证字段的类型
			validate(fType);
		}
	}

	/**
	 * 验证 MsgArrayList 类型的字段
	 * 
	 * @param f
	 * 
	 */
	private static void validateField_msgArrayList(Field f) {
		// 断言参数不为空
		Assert.notNull(f, "f");
		// 获取字段类型
		Class<?> fType = f.getType();

		if (MsgArrayList.class.isAssignableFrom(fType) == false) {
			// 如果字段不是 MsgArrayList 类型, 
			// 则直接退出!
			return;
		}

		// 获取定义这个字段的类
		Class<?> fromClazz = f.getDeclaringClass();
		// 如果字段是 MsgArrayList 类型, 
		// 获取泛型类型中的真实类型
		Type aType = FieldUtil.getGenericTypeA(f);

		if (aType == null) {
			// 如果没有定义真实类型, 
			// 则直接抛出异常!
			throw new MsgError(MessageFormat.format(
				"类 {0} 字段 {1} 为 MsgArrayList 类型, 但是没有定义泛型参数! {2}", 
				fromClazz, 
				f.getName(), 
				CODE_HINT_msgArrayList
			));
		}

		if (AbstractMsgObj.class.isAssignableFrom((Class<?>)aType)) {
			// 如果真实类型是 AbstractMsgObj 的子类, 
			// 则递归验证真实类型
			validate(fType);
		}

		try {
			// 创建消息对象
			Object msgObj = fromClazz.newInstance();
			// 获取字段值
			Object fVal = f.get(msgObj);

			if (fVal == null) {
				// 如果字段值为 null, 
				// 则直接抛出异常!
				throw new MsgError(MessageFormat.format(
					"类 {0} 字段 {1} 为 MsgArrayList 类型, 但却是空值 null... {2}", 
					fromClazz.getName(), 
					f.getName(), 
					CODE_HINT_msgArrayList
				));
			}
		} catch (MsgError err) {
			// 抛出异常
			throw err;
		} catch (Exception ex) {
			// 记录错误日志
			MsgLog.LOG.error(ex.getMessage(), ex);
			// 并抛出异常!
			throw new MsgError(ex);
		}
	}
}
