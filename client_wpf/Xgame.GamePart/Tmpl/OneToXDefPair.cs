using System;
using System.Collections.Generic;
using System.Reflection;

namespace Xgame.GamePart.Tmpl
{
    class OneToXDefPair
    {
        /// <summary>
        /// 获取是否为 1 对 1 键值对?
        /// </summary>
        public bool OneToOne
        {
            get;
        }

        public MemberInfo KeyDef
        {
            get;
        }

        public MemberInfo MapDef
        {
            get;
        }

        public static IList<OneToXDefPair> ListAll(System.Type byTmplType)
        {
            return null;
        }
    }
}
