using System;

namespace Xgame.GamePart.Tmpl.Type
{
    /// <summary>
    /// Xlsx 字符串型
    /// </summary>
    public class XlsxStr : PrimitiveTypeCol<string>
    {
        // @Override
        protected override void ReadImpl(XlsxRowReadStream stream)
        {
            this.Val = stream.ReadStr();
        }
    }
}
