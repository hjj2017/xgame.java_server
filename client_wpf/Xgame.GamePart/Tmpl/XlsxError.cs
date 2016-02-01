using System;

namespace Xgame.GamePart.Tmpl
{
    /// <summary>
    /// Xlsx 错误
    /// </summary>
    public class XlsxError : Exception
    {
        /// <summary>
        /// 类默认构造器
        /// </summary>
        public XlsxError() : base()
        {
        }

        /// <summary>
        /// 类参数构造器
        /// </summary>
        /// <param name="msg"></param>
        public XlsxError(string msg) : base(msg)
        {
        }

        /// <summary>
        /// 类参数构造器
        /// </summary>
        /// <param name="msg"></param>
        /// <param name="innerErr"></param>
        public XlsxError(string msg, Exception innerErr) : base(msg, innerErr)
        {
        }
    }
}
