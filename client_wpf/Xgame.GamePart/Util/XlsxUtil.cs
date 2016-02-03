using System;
using System.Data;
using System.Data.OleDb;

namespace Xgame.GamePart.Util
{
    /// <summary>
    /// Xlsx 实用工具类
    /// </summary>
    public sealed class XlsxUtil
    {
        #region 类构造器
        /// <summary>
        /// 类默认构造器
        /// </summary>
        private XlsxUtil()
        {
        }
        #endregion

        /// <summary>
        /// 获取工作表单
        /// </summary>
        /// <param name="xlsxAbsFileName"></param>
        /// <param name="sheetName"></param>
        /// <returns></returns>
        public static DataSet GetDS(string xlsxAbsFileName, string sheetName)
        {
            // 结果数据集
            DataSet resultDS = new DataSet();

            try
            {
                // 连接字符串
                string connStr = string.Format(@"Provider=Microsoft.Ace.OleDb.12.0;Data Source={0};Extended Properties='Excel 12.0;HDR=False;IMEX=1'", xlsxAbsFileName);
                // SQL 语句
                string sql = string.Format(@"select * from [{0}$]", sheetName);
                //
                // 注意 : 这里使用的是 Microsoft.Ace.OleDb.12.0 数据连接器, 
                // 可以到这里下载 : 
                // http://download.microsoft.com/download/7/0/3/703ffbcb-dc0c-4e19-b0da-1463960fdcdb/AccessDatabaseEngine.exe

                using (OleDbConnection connObj = new OleDbConnection(connStr))
                {
                    // 打开数据库连接
                    connObj.Open();
                    // 获取数据适配器
                    OleDbDataAdapter adapterObj = new OleDbDataAdapter(sql, connObj);
                    // 填充数据
                    adapterObj.Fill(resultDS, "0");
                }
            }
            catch (Exception err)
            {
                // 直接抛出异常!
                throw err;
            }

            return resultDS;
        }
    }
}
