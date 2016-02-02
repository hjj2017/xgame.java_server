using System;

namespace Xgame.GamePart.Tmpl.Attr
{
    /// <summary>
    /// 指定由哪个类进行验证
    /// </summary>
    [AttributeUsage(AttributeTargets.Class, AllowMultiple = false, Inherited = true)]
    public class ValidatorAttribute : Attribute
    {
        /// <summary>
        /// 获取或设置验证器类型
        /// </summary>
        public System.Type Type
        {
            get;
            set;
        }
    }
}
