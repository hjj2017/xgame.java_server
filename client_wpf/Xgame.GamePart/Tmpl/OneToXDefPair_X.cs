using System;
using Xgame.GamePart.Util;

namespace Xgame.GamePart.Tmpl
{
    /// <summary>
    /// OneToXDefPair_X
    /// </summary>
    class OneToXDefPair_X
    {
        /// <summary>
        /// 类参数构造器
        /// </summary>
        /// <param name="groupName"></param>
        /// <param name="fromType"></param>
        OneToXDefPair_X(string groupName, System.Type fromType)
        {
            // 断言参数不为空
            Assert.NotNull(groupName, "groupName");
            Assert.NotNull(fromType, "fromType");

            // 设置属性
            this.GroupName = groupName;
            this.FromType = fromType;
        }

        /// <summary>
        /// 获取或设置分组名称
        /// </summary>
        public string GroupName
        {
            get;
            private set;
        }

        /// <summary>
        /// 获取或设置模板类
        /// </summary>
        public System.Type FromType
        {
            get;
            private set;
        }
    }
}
