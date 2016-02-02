using System;

namespace Xgame.GamePart.Util
{
    /// <summary>
    /// 类实用工具
    /// </summary>
    public sealed class ClazzUtil
    {
        #region 类默认构造器
        /// <summary>
        /// 类默认构造器
        /// </summary>
        private ClazzUtil()
        {
        }
        #endregion

        /// <summary>
        /// 获取属性标签
        /// </summary>
        /// <typeparam name="TAttr"></typeparam>
        /// <param name="byClazz"></param>
        /// <returns></returns>
        public static TAttr GetAttribute<TAttr>(System.Type byClazz) where TAttr : Attribute
        {
            // 断言参数不为空
            Assert.NotNull(byClazz, "byClazz");

            // 获取属性标签数组
            object[] objArr = byClazz.GetCustomAttributes(byClazz, true);

            if (objArr == null 
             || objArr.Length <= 0)
            {
                return null;
            }
            else
            {
                return objArr[0] as TAttr;
            }
        }
    }
}
