using System;

namespace Xgame.GamePart.Tmpl.Attr
{
    /// <summary>
    /// 一个关键字对应多个模板对象
    /// </summary>
    [AttributeUsage(AttributeTargets.Field | AttributeTargets.Property | AttributeTargets.Method, AllowMultiple = false, Inherited = true)]
    public class OneToManyAttribute : Attribute
    {
        #region 类构造器
        /// <summary>
        /// 类参数构造器
        /// </summary>
        /// <param name="groupName"></param>
        public OneToManyAttribute(string groupName)
        {
            this.GroupName = groupName;
        }
        #endregion

        /// <summary>
        /// 获取或设置分组名称
        /// </summary>
        public string GroupName
        {
            get;
            set;
        }
    }
}
