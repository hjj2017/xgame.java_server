using System;
using System.Collections;
using System.Collections.Generic;
using System.Data;
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
        /// <param name="byType"></param>
        public void LoadTmplData(System.Type byType)
        {
            if (byType == null)
            {
                // 如果参数对象为空, 
                // 则直接退出!
                return;
            }

            // 获取文件名
            string xlsxAbsFileName = @"D:\Temp_Test\building.xlsx";
            // 获取数据集
            DataSet ds = XlsxUtil.GetDS(xlsxAbsFileName);

            MakeObjList(byType, ds, 0, xlsxAbsFileName);
        }

        /// <summary>
        /// 构建对象列表
        /// </summary>
        /// <param name="byType"></param>
        /// <param name="fromDS"></param>
        /// <param name="startFromRowIndex"></param>
        /// <param name="xlsxFileName"></param>
        /// <returns></returns>
        private static IList MakeObjList(System.Type byType, DataSet fromDS, int startFromRowIndex, string xlsxFileName)
        {
            if (byType == null 
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

            for (int i = startFromRowIndex; i <= rowCount; i++)
            {
                // 获取行数据
                DataRow rowObj = tableObj.Rows[i];

                if (rowObj == null) {
                    // 如果行数据为空,
                    // 则直接跳过!
                    continue;
                }

                // 创建模板对象
                BaseXlsxCol newColObj = Activator.CreateInstance(byType) as BaseXlsxCol;
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
