using System;

namespace Xgame.GamePart.Tmpl.Type
{
    /// <summary>
    /// Excel 列
    /// </summary>
    public abstract class BaseXlsxCol
    {
        /// <summary>
        /// 获取或设置 Xlsx 文件名
        /// </summary>
        public string XlsxFileName
        {
            get;
            private set;
        }

        /// <summary>
        /// 获取或设置页签名称
        /// </summary>
        public string SheetName
        {
            get;
            private set;
        }

        /// <summary>
        /// 获取或设置行索引
        /// </summary>
        public int RowIndex
        {
            get;
            private set;
        }

        /// <summary>
        /// 获取或设置列索引
        /// </summary>
        public int ColIndex
        {
            get;
            private set;
        }

        /// <summary>
        /// 读取 Xlsx 行数据
        /// </summary>
        /// <param name="stream"></param>
        public void ReadXlsxRow(XlsxRowReadStream stream)
        {
            if (stream == null)
            {
                // 如果参数对象为空, 
                // 则直接退出!
                return;
            }

            // 设置 Xlsx 文件名、页签名和索引位置
            this.XlsxFileName = stream.XlsxFileName;
            this.SheetName = stream.SheetName;
            this.RowIndex = stream.RowIndex;
            this.ColIndex = stream.CurrCellIndex;

            // 读取数据
            this.ReadImpl(stream);
        }

        /// <summary>
        /// 读取实现
        /// </summary>
        /// <param name="stream"></param>
        protected abstract void ReadImpl(XlsxRowReadStream stream);
    }
}
