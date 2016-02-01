using System;
using System.Data;

namespace Xgame.GamePart.Tmpl
{
    /// <summary>
    /// Xlsx 数据行读取流
    /// </summary>
    public class XlsxRowReadStream
    {
        /** 数据行对象 */
        private DataRow _rowObj = null;

        /// <summary>
        /// 类参数构造器
        /// </summary>
        /// <param name="rowObj"></param>
        /// <param name="xlsxFileName"></param>
        public XlsxRowReadStream(DataRow rowObj, string xlsxFileName)
        {
            if (rowObj == null)
            {
                // 如果参数对象为空, 
                // 直接抛出异常!
                throw new ArgumentNullException("rowObj");
            }

            this._rowObj = rowObj;
            this.XlsxFileName = xlsxFileName;
            this.SheetName = this._rowObj.Table.TableName;
            this.CurrCellIndex = 0;
        }

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
        /// 读取单元格数值
        /// </summary>
        /// <returns></returns>
        public object ReadCellVal()
        {
            return this._rowObj[this.CurrCellIndex++];
        }

        /// <summary>
        /// 读取一个 Int 数值
        /// </summary>
        /// <returns></returns>
        public int ReadInt()
        {
            return Convert.ToInt32(this.ReadCellVal());
        }

        /// <summary>
        /// 读取一个 Long 数值
        /// </summary>
        /// <returns></returns>
        public long ReadLong()
        {
            return Convert.ToInt64(this.ReadCellVal());
        }

        /// <summary>
        /// 读取一个 Short 数值
        /// </summary>
        /// <returns></returns>
        public short ReadShort()
        {
            return Convert.ToInt16(this.ReadCellVal());
        }

        /// <summary>
        /// 读取一个 String 值
        /// </summary>
        /// <returns></returns>
        public string ReadStr()
        {
            return Convert.ToString(this.ReadCellVal());
        }

        /// <summary>
        /// 读取一个 Bool 值
        /// </summary>
        /// <returns></returns>
        public bool ReadBool()
        {
            return Convert.ToBoolean(this.ReadCellVal());
        }

        /// <summary>
        /// 读取一个 Float 数值
        /// </summary>
        /// <returns></returns>
        public float ReadFloat()
        {
            return Convert.ToSingle(this.ReadCellVal());
        }

        /// <summary>
        /// 读取一个 Double 数值
        /// </summary>
        /// <returns></returns>
        public double ReadDouble()
        {
            return Convert.ToDouble(this.ReadCellVal());
        }
    }
}
