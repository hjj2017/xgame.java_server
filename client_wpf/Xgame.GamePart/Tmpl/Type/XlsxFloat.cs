using System;

namespace Xgame.GamePart.Tmpl.Type
{
    /// <summary>
    /// Xlsx 浮点型
    /// </summary>
    public class XlsxFloat : PrimitiveTypeCol<float>
    {
        #region 类构造器
        /// <summary>
        /// 类默认构造器
        /// </summary>
        public XlsxFloat()
        {
        }

        /// <summary>
        /// 类参数构造器
        /// </summary>
        /// <param name="val"></param>
        public XlsxFloat(float val)
        {
            this.Val = val;
        }
        #endregion

        // @Override
        protected override void ReadImpl(XlsxRowReadStream stream)
        {
            this.Val = stream.ReadFloat();
        }
    }
}
