using System;

namespace Xgame.GamePart.Util
{
    /// <summary>
    /// 断言工具类
    /// </summary>
    public sealed class Assert
    {
        /// <summary>
        /// 类默认构造器
        /// </summary>
        private Assert()
        {
        }

        /// <summary>
        /// 断言对象不为空
        /// </summary>
        /// <param name="objVal"></param>
        public static void NotNull(object objVal)
        {
            if (objVal == null)
            {
                // 如果对象为空, 
                // 则抛出异常!
                throw new NullReferenceException();
            }
        }

        /// <summary>
        /// 断言对象不为空
        /// </summary>
        /// <param name="objVal"></param>
        /// <param name="objName"></param>
        public static void NotNull(object objVal, string objName)
        {
            if (objVal == null)
            {
                // 如果对象为空, 
                // 则抛出异常!
                throw new NullReferenceException(objName);
            }
        }
    }
}
