using System;

namespace Xgame.GamePart.Tmpl.Type
{
    /// <summary>
    /// Xlsx 双精度型
    /// </summary>
    public class XlsxDouble : PrimitiveTypeCol<double>
    {
        // @Override
        protected override void ReadImpl(XlsxRowReadStream stream)
        {
            this.Val = stream.ReadDouble();
        }
    }
}
