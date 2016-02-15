using System;
using System.Collections;
using System.Collections.Generic;
using System.Reflection;
using Xgame.GamePart.Util;

namespace Xgame.GamePart.Tmpl
{
    /// <summary>
    /// OneToOne, OneToMany 关键字及字典对应关系定义
    /// </summary>
    class OneToXDefPair
    {
        /** 已验证的类型字典 */
        private static readonly IDictionary<System.Type, IList<OneToXDefPair_X>> _validatedTypeDict = new Dictionary<System.Type, IList<OneToXDefPair_X>>();

        #region 类构造器
        /// <summary>
        /// 类参数构造器
        /// </summary>
        /// <param name="keyDef"></param>
        /// <param name="mapDef"></param>
        /// <param name="oneToOne"></param>
        private OneToXDefPair(MemberInfo keyDef, MemberInfo mapDef, bool oneToOne)
        {
            this.KeyDef = keyDef;
            this.MapDef = mapDef;
            this.OneToOne = oneToOne;
        }
        #endregion

        /// <summary>
        /// 获取或设置关键字定义
        /// </summary>
        public MemberInfo KeyDef
        {
            get;
            private set;
        }

        /// <summary>
        /// 获取或设置字典定义
        /// </summary>
        public MemberInfo MapDef
        {
            get;
            private set;
        }

        /// <summary>
        /// 获取是否为 1 对 1 键值对?
        /// </summary>
        public bool OneToOne
        {
            get;
            private set;
        }

        static void CollectOneToXAttr(System.Type tmplType)
        {
            if (tmplType == null)
            {
                // 如果参数对象为空, 
                // 则直接退出!
                return;
            }

            // 创建辅助字典
            IDictionary<string, OneToXDefPair_X> helpDict = new Dictionary<string, OneToXDefPair_X>();
            // 获取成员数组
            MemberInfo[] mArr = tmplType.GetMembers(
                BindingFlags.Public | BindingFlags.GetField | BindingFlags.GetProperty
            );

            foreach (MemberInfo m in mArr)
            {
                
            }
        }

        private static void FindAttr(MemberInfo m, IDictionary<string, OneToXDefPair_X> helpDict)
        {
            
        }
    }
}
