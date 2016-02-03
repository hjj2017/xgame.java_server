using System;

namespace Xgame.GamePart.Tmpl.Type
{
    /// <summary>
    /// Xlsx 整数型
    /// </summary>
    public class XlsxInt : PrimitiveTypeCol<int>
    {
        #region 类构造器
        /// <summary>
        /// 类默认构造器
        /// </summary>
        public XlsxInt()
        {
        }

        /// <summary>
        /// 类参数构造器
        /// </summary>
        /// <param name="val"></param>
        public XlsxInt(int val) : base(val)
        {
        }
        #endregion

        // @Override
        protected override void ReadImpl(XlsxRowReadStream stream)
        {
            this.Val = stream.ReadInt();
        }
    }
}
