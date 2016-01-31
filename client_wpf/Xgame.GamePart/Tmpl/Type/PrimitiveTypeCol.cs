using System;

namespace Xgame.GamePart.Tmpl.Type
{
    /// <summary>
    /// 基本类型字段
    /// </summary>
    /// <typeparam name="T"></typeparam>
    public abstract class PrimitiveTypeCol<T> : BaseXlsxCol
    {
        #region 类构造器
        /// <summary>
        /// 类默认构造器
        /// </summary>
        protected PrimitiveTypeCol()
        {
            this.Val = default(T);
        }

        /// <summary>
        /// 类参数构造器
        /// </summary>
        /// <param name="val"></param>
        protected PrimitiveTypeCol(T val)
        {
            this.Val = val;
        }
        #endregion

        /// <summary>
        /// 获取或设置数值
        /// </summary>
        public T Val
        {
            get;
            protected set;
        }

        /// <summary>
        /// 获取整数值
        /// </summary>
        /// <returns></returns>
        public int GetIntVal()
        {
            return Convert.ToInt32(this.Val);
        }

        /// <summary>
        /// 获取字符串值
        /// </summary>
        /// <returns></returns>
        public string GetStrVal()
        {
            return Convert.ToString(this.Val);
        }

        /// <summary>
        /// 获取长整型数值
        /// </summary>
        /// <returns></returns>
        public long GetLongVal()
        {
            return Convert.ToInt64(this.Val);
        }

        /// <summary>
        /// 获取单精度浮点数值
        /// </summary>
        /// <returns></returns>
        public float GetFloatVal()
        {
            return Convert.ToSingle(this.Val);
        }

        /// <summary>
        /// 获取双精度浮点数值
        /// </summary>
        /// <returns></returns>
        public double GetDoubleVal()
        {
            return Convert.ToDouble(this.Val);
        }

        /// <summary>
        /// 获取布尔值
        /// </summary>
        /// <returns></returns>
        public bool GetBoolVal()
        {
            return Convert.ToBoolean(this.Val);
        }

        /// <summary>
        /// 获取字符值
        /// </summary>
        /// <returns></returns>
        public char GetCharVal()
        {
            return Convert.ToChar(this.Val);
        }

        /// <summary>
        /// 获取字节值
        /// </summary>
        /// <returns></returns>
        public byte GetByteVal()
        {
            return Convert.ToByte(this.Val);
        }
    }
}
