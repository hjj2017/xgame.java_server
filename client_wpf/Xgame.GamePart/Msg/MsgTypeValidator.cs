using System;
using System.Reflection;

using Xgame.GamePart.Msg.Type;
using Xgame.GamePart.Util;

namespace Xgame.GamePart.Msg
{
    /// <summary>
    /// 消息类验证器
    /// </summary>
    class MsgTypeValidator
    {
        /// <summary>
        /// 类默认构造器
        /// </summary>
        private MsgTypeValidator()
        {
        }

        /// <summary>
        /// 验证消息类
        /// </summary>
        /// <param name="msgType"></param>
        internal static void Validate(System.Type msgType)
        {
            // 断言参数不为空
            Assert.NotNull(msgType, "msgType");

            if (msgType.IsSubclassOf(typeof(BaseMsgObj))  == false
             || msgType.IsAbstract)
            {
                // 1: 看看 msgType 是不是 BaseMsgObj 的具体实现类, 
                // 如果不是, 
                // 则直接抛出异常!
                throw new MsgError(string.Format(
                    "类 {0} 不是 {1} 的具体子类, 要么 {2} 是抽象类, 要么根本不是继承自 {3}",
                    msgType.Name,
                    typeof(BaseMsgObj).Name, 
				    msgType.Name, 
				    typeof(BaseMsgObj).Name
			    ));
            }

            // 验证类构造器
            ValidateCtor(msgType);
            // 获取字段信息数组
            FieldInfo[] fiArr = msgType.GetFields();

            foreach (FieldInfo fi in fiArr)
            {
                if (fi != null
                 && fi.FieldType.IsSubclassOf(typeof(BaseMsgField)))
                {
                    // 如果字段是 BaseMsgField 的子类, 
                    // 则验证当前字段...
                    ValidateField(fi);
                }
            }
        }

        /// <summary>
        /// 验证类构造器
        /// </summary>
        /// <param name="msgType"></param>
        private static void ValidateCtor(System.Type msgType)
        {
            // 断言参数不为空
            Assert.NotNull(msgType, "msgType");
            // 获取构造器信息数组
            ConstructorInfo[] ctorArr = msgType.GetConstructors();
            // 成功标志
            bool ok = false;

            foreach (ConstructorInfo ctor in ctorArr)
            {
                if (ctor != null
                 && ctor.IsPublic
                 && ctor.GetParameters().Length <= 0)
                {
                    // 如果有公共的无参数构造器, 
                    // 则验证成功!
                    ok = true;
                }
            }

            if (ok == false)
            {
                // 如果验证不成功,
                // 则抛出异常!
                throw new MsgError(string.Format(
                    "类 {0} 没有定义公有的、无参数的默认构造器", 
                    msgType.Name
                ));
            }
        }

        /// <summary>
        /// 验证字段
        /// </summary>
        /// <param name="fi"></param>
        private static void ValidateField(FieldInfo fi)
        {
            // 断言参数不为空
            Assert.NotNull(fi, "fi");

            if (fi.IsPublic == false)
            {
                // 如果字段不是公共的, 
                // 则抛出异常!
                throw new MsgError(string.Format(
                    "类 {0} 字段 {1}, 没有定义为公有的 ( public ) !!", 
                    fi.DeclaringType.Name, 
                    fi.Name
                ));
            }

            if (fi.IsStatic)
            {
                // 如果字段是静态的, 
                // 则抛出异常!
                throw new MsgError(string.Format(
                    "类 {0} 字段 {1} 不能冠以 static", 
                    fi.DeclaringType.Name,
                    fi.Name
                ));
            }

            if (fi.FieldType.IsSubclassOf(typeof(BaseMsgObj)))
            {
                // 如果字段是 BaseMsgObj 类型, 
                // 则递归验证!
                Validate(fi.FieldType);
            }
        }
    }
}
