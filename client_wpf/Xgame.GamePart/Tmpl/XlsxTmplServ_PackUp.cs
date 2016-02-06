using System;
using System.Collections;

using Xgame.GamePart.Tmpl.Type;

namespace Xgame.GamePart.Tmpl
{
    partial class XlsxTmplServ
    {
        /// <summary>
        /// 打包
        /// </summary>
        /// <param name="byTmplType"></param>
        public void PackUp(System.Type byTmplType)
        {
            if (byTmplType == null)
            {
                // 如果参数对象为空, 
                // 则直接退出!
                return;
            }

            // 模板对象列表
            IList tmplObjList = this._tmplObjListMap.ContainsKey(byTmplType) ? tmplObjList = this._tmplObjListMap[byTmplType] : null;

            if (tmplObjList == null 
             || tmplObjList.Count <= 0)
            {
                // 如果模板对象列表为空, 
                // 则直接退出!
                return;
            }

            // 构建打包器
            IXlsxPacker packer = XlsxPackerMaker.Make(byTmplType);

            if (packer == null)
            {
                // 如果打包器为空,
                // 则抛出异常!
                throw new XlsxTmplError(string.Format(
                    "未能构建类 {0} 的打包器",
                    byTmplType.Name
                ));
            }

            foreach (Object tmplObj in tmplObjList)
            {
                if (tmplObj != null)
                {
                    packer.PackUp(tmplObj as BaseXlsxTmpl);
                }
            }
        }
    }
}
