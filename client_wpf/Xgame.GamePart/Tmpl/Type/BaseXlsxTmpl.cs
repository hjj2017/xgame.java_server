using System;
using System.Collections.Generic;

namespace Xgame.GamePart.Tmpl.Type
{
    /// <summary>
    /// 模版基础类
    /// </summary>
    public class BaseXlsxTmpl : BaseXlsxCol
    {
        // @Override
        protected override void ReadImpl(XlsxRowReadStream fromStream)
        {
            if (fromStream == null)
            {
                // 如果参数对象为空, 
                // 则直接退出!
                return;
            }

            // 获取帮助器对象
            IReadHelper helperObj = ReadHelperMaker.Make(this.GetType());

            if (helperObj != null)
            {
                // 如果帮助器不为空, 
                // 则读取消息对象!
                helperObj.ReadTmplObjFrom(this, fromStream);
            }
        }

        /// <summary>
        /// 打包一对一数据到目标字典
        /// </summary>
        /// <param name="objKey"></param>
        /// <param name="objVal"></param>
        /// <param name="targetDict"></param>
        public static void PackOneToOne(object objKey, BaseXlsxTmpl objVal, IDictionary<object, BaseXlsxTmpl> targetDict)
        {
            if (objKey == null 
             || objVal == null
             || targetDict == null)
            {
                // 如果参数对象为空,
                // 则直接退出!
                return;
            }

            // 定义真实关键字
            Object realKey = objKey;

            if (objKey is PrimitiveTypeCol<object>) {
                // 如果是基本类型字段,
                // 则获取真实关键字!
                realKey = ((PrimitiveTypeCol<object>)objKey).Val;
            }

            if (realKey == null)
            {
                return;
            }

            targetDict[realKey] = objVal;
        }

        /// <summary>
        /// 打包一对多数据到目标字典
        /// </summary>
        /// <param name="objKey"></param>
        /// <param name="objVal"></param>
        /// <param name="targetDict"></param>
        public static void PackOneToMany(object objKey, BaseXlsxTmpl objVal, IDictionary<object, IList<BaseXlsxTmpl>> targetDict)
        {
            if (objKey == null
             || objVal == null
             || targetDict == null)
            {
                // 如果参数对象为空,
                // 则直接退出!
                return;
            }

            // 定义真实关键字
            Object realKey = objKey;

            if (objKey is PrimitiveTypeCol<object>)
            {
                // 如果是基本类型字段,
                // 则获取真实关键字!
                realKey = ((PrimitiveTypeCol<object>)objKey).Val;
            }

            if (realKey == null)
            {
                return;
            }

            IList<BaseXlsxTmpl> tmplList = null;
            tmplList = targetDict.ContainsKey(objKey) ? targetDict[objKey] : new List<BaseXlsxTmpl>();

            tmplList.Add(objVal);
            targetDict[realKey] = tmplList;
        }
    }
}
