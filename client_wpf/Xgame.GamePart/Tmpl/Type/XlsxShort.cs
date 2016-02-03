using System;

namespace Xgame.GamePart.Tmpl.Type
{
    /// <summary>
    /// Xlsx 短整数型
    /// </summary>
    public class XlsxShort : PrimitiveTypeCol<short>
    {
        #region 类构造器
        /// <summary>
        /// 类默认构造器
        /// </summary>
        public XlsxShort()
        {
        }

        /// <summary>
        /// 类参数构造器
        /// </summary>
        /// <param name="val"></param>
        public XlsxShort(short val)
        {
            this.Val = val;
        }
        #endregion

        // @Override
        protected override void ReadImpl(XlsxRowReadStream stream)
        {
            this.Val = stream.ReadShort();
        }
    }
}
