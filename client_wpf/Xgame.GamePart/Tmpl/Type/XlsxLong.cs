using System;

namespace Xgame.GamePart.Tmpl.Type
{
    /// <summary>
    /// Xlsx 长整数型
    /// </summary>
    public class XlsxLong : PrimitiveTypeCol<long>
    {
        #region 类构造器
        /// <summary>
        /// 类默认构造器
        /// </summary>
        public XlsxLong()
        {
        }

        /// <summary>
        /// 类参数构造器
        /// </summary>
        /// <param name="val"></param>
        public XlsxLong(long val)
        {
            this.Val = val;
        }
        #endregion

        // @Override
        protected override void ReadImpl(XlsxRowReadStream stream)
        {
            this.Val = stream.ReadLong();
        }
    }
}
