using System;
using System.Collections.Generic;
using System.Data;

using Xgame.GamePart.Tmpl.Type;
using Xgame.GamePart.Util;

namespace Xgame.GamePart.Tmpl
{
    partial class XlsxTmplServ_LoadTmplData
    {
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

            DataSet ds = XlsxUtil.GetDS(@"D:\Temp_Test\building.xlsx");


        }

        /// <summary>
        /// 构建对象列表
        /// </summary>
        /// <typeparam name="T"></typeparam>
        /// <param name="byType"></param>
        /// <param name="fromDS"></param>
        /// <param name="startFromRowIndex"></param>
        /// <param name="xlsxFileName"></param>
        /// <returns></returns>
        private static IList<T> MakeObjList<T>(System.Type byType, DataSet fromDS, int startFromRowIndex, string xlsxFileName) where T : BaseXlsxCol
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
            IList<T> objList = new List<T>(rowCount);

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
                T newObj = Activator.CreateInstance(byType) as T;
                // 读取行数据
                newObj.ReadXlsxRow(new XlsxRowReadStream(rowObj, xlsxFileName));
                // 添加对象到列表
                objList.Add(newObj);
            }

            return objList;
        }
    }
}
