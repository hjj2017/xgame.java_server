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
        /// <param name="fromStream"></param>
        public void ReadFrom(XlsxRowReadStream fromStream)
        {
            if (fromStream == null)
            {
                // 如果参数对象为空, 
                // 则直接退出!
                return;
            }

            // 设置 Xlsx 文件名、页签名和索引位置
            this.XlsxFileName = fromStream.XlsxFileName;
            this.SheetName = fromStream.SheetName;
            this.RowIndex = fromStream.RowIndex;
            this.ColIndex = fromStream.CurrCellIndex;

            // 读取数据
            this.ReadImpl(fromStream);
        }

        /// <summary>
        /// 读取实现
        /// </summary>
        /// <param name="stream"></param>
        protected abstract void ReadImpl(XlsxRowReadStream stream);

        /// <summary>
        /// 从 Xlsx 行数据流中读取模版对象, 并返回
        /// </summary>
        /// <typeparam name="TXlsxCol"></typeparam>
        /// <param name="toXlsxColObj"></param>
        /// <param name="fromStream"></param>
        /// <returns></returns>
        public static TXlsxCol ReadXlsxColFrom<TXlsxCol>(TXlsxCol toXlsxColObj, XlsxRowReadStream fromStream) 
        where TXlsxCol : BaseXlsxCol, new()
        {
            if (toXlsxColObj == null)
            {
                // 如果消息对象为空, 
                // 则直接新建!
                toXlsxColObj = new TXlsxCol();
            }

            // 从二进制流中读取数据
            toXlsxColObj.ReadFrom(fromStream);
            // 返回消息对象
            return toXlsxColObj;
        }
    }
}
