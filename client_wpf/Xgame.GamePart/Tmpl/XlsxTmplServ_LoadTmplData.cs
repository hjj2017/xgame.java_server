using System;
using System.Collections;
using System.Collections.Generic;
using System.Data;

using Xgame.GamePart.Tmpl.Attr;
using Xgame.GamePart.Tmpl.Type;
using Xgame.GamePart.Util;

namespace Xgame.GamePart.Tmpl
{
    partial class XlsxTmplServ
    {
        /** 模板数据列表字典 */
        private IDictionary<System.Type, IList> _tmplObjListMap = new Dictionary<System.Type, IList>();

        /// <summary>
        /// 加载模板数据
        /// </summary>
        /// <param name="byTmplType"></param>
        public void LoadTmplData(System.Type byTmplType)
        {
            if (byTmplType == null)
            {
                // 如果参数对象为空, 
                // 则直接退出!
                return;
            }

            // 获取属性标签
            FromXlsxFileAttribute attrObj = ClazzUtil.GetAttribute<FromXlsxFileAttribute>(byTmplType);

            if (attrObj == null)
            {
                // 如果属性标签为空, 
                // 则抛出异常
                throw new XlsxError(string.Format(
                    "类 {0} 没有标注 {1} 属性", 
                    byTmplType.FullName, 
                    typeof(FromXlsxFileAttribute).FullName
                ));
            }

            // 获取文件名、页签索引及起始行索引
            string xlsxFileName = attrObj.FileName;
            string sheetName = attrObj.SheetName;
            int startFromRowIndex = attrObj.StartFromRowIndex;

            // 文件绝对路径
            string xlsAbsFileName = string.Format(
                @"{0}\{1}", 
                this.XlsxFileDir, 
                xlsxFileName
            );

            // 获取数据集
            DataSet ds = XlsxUtil.GetDS(xlsAbsFileName, sheetName);

            if (ds == null 
             || ds.Tables.Count <= 0)
            {
                // 如果没有数据表, 
                // 则直接退出!
                return;
            }

            // 构建模板对象列表
            IList objList = MakeObjList(byTmplType, ds, startFromRowIndex, xlsxFileName);

            if (objList != null 
             && objList.Count > 0)
            {
                this._tmplObjListMap.Add(byTmplType, objList);
            }
        }

        /// <summary>
        /// 构建对象列表
        /// </summary>
        /// <param name="byTmplType"></param>
        /// <param name="fromDS"></param>
        /// <param name="startFromRowIndex"></param>
        /// <param name="xlsxFileName"></param>
        /// <returns></returns>
        private static IList MakeObjList(System.Type byTmplType, DataSet fromDS, int startFromRowIndex, string xlsxFileName)
        {
            if (byTmplType == null 
             || fromDS == null 
             || fromDS.Tables.Count <= 0)
            {
                // 如果参数对象为空, 
                // 则直接退出!
                return null;
            }

            // 获取第一个数据表
            DataTable tableObj = fromDS.Tables[0];
            // 获取数据行个数
            int rowCount = tableObj.Rows.Count;
            // 创建列表对象
            IList objList = new ArrayList(rowCount);

            for (int i = startFromRowIndex; i < rowCount; i++)
            {
                // 获取行数据
                DataRow rowObj = tableObj.Rows[i];

                if (rowObj == null) {
                    // 如果行数据为空,
                    // 则直接跳过!
                    continue;
                }

                // 创建模板对象
                BaseXlsxCol newColObj = Activator.CreateInstance(byTmplType) as BaseXlsxCol;
                // 创建读入流, 并读取数据
                XlsxRowReadStream fromStream = new XlsxRowReadStream(rowObj, xlsxFileName);
                newColObj.ReadFrom(fromStream);
                // 添加对象到列表
                objList.Add(newColObj);
            }

            return objList;
        }
    }
}
