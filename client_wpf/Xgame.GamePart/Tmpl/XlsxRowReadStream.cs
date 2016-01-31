using System;

using Microsoft.Office.Interop.Excel;

namespace Xgame.GamePart.Tmpl
{
    /// <summary>
    /// Xlsx 数据行读取流
    /// </summary>
    public class XlsxRowReadStream
    {
        /// <summary>
        /// 获取或设置 Xlsx 文件名
        /// </summary>
        public string XlsxFileName
        {
            get;
            set;
        }

        /// <summary>
        /// 获取或设置页签名称
        /// </summary>
        public string SheetName
        {
            get;
            set;
        }

        /// <summary>
        /// 获取或设置行索引
        /// </summary>
        public int RowIndex
        {
            get;
            set;
        }

        /// <summary>
        /// 获取或设置当前表格索引
        /// </summary>
        public int CurrCellIndex
        {
            get;
            set;
        }

        /// <summary>
        /// 读取一个 Int 数值
        /// </summary>
        /// <returns></returns>
        public int ReadInt()
        {
            return 0;
        }
    }
}
