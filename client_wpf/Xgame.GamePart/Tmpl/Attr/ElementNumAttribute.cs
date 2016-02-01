using System;

namespace Xgame.GamePart.Tmpl.Attr
{
    /// <summary>
    /// 元素个数
    /// </summary>
    [AttributeUsage(AttributeTargets.Field | AttributeTargets.Property | AttributeTargets.Method, AllowMultiple = false, Inherited = true)]
    public class ElementNumAttribute : Attribute
    {
        /// <summary>
        /// 获取或设置数值
        /// </summary>
        public int Value
        {
            get;
            set;
        }
    }
}
