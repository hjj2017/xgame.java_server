using System;

namespace Xgame.GamePart.Tmpl
{
    /// <summary>
    /// Xlsx 模板错误
    /// </summary>
    public class XlsxTmplError : Exception
    {
        /// <summary>
        /// 类默认构造器
        /// </summary>
        public XlsxTmplError() : base()
        {
        }

        /// <summary>
        /// 类参数构造器
        /// </summary>
        /// <param name="msg"></param>
        public XlsxTmplError(string msg) : base(msg)
        {
        }

        /// <summary>
        /// 类参数构造器
        /// </summary>
        /// <param name="msg"></param>
        /// <param name="innerErr"></param>
        public XlsxTmplError(string msg, Exception innerErr) : base(msg, innerErr)
        {
        }
    }
}
