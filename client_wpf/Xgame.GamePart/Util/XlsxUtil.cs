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
        /// <param name="sheetIndex"></param>
        /// <returns></returns>
        public static DataSet GetDS(string xlsxAbsFileName, int sheetIndex = 0)
        {
            // 结果数据集
            DataSet resultDS = new DataSet();

            try
            {
                // 连接字符串
                string connStr = string.Format(@"Provider=Microsoft.Jet.OLEDB.4.0;Data Source={0};Extended Properties='Excel 8.0;HDR=False;IMEX=1'", xlsxAbsFileName);
                // SQL 语句
                string sql = string.Format(@"select * from [Sheet{0}$]", sheetIndex);

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
