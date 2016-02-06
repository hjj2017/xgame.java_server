using System;

namespace Xgame.GamePart.Tmpl.Type
{
    /// <summary>
    /// Xlsx 多语言字段
    /// </summary>
    public class XlsxMultiLang : BaseXlsxCol
    {
        // @Override
        protected override void ReadImpl(XlsxRowReadStream stream)
        {
            stream.ReadCellVal();
        }
    }
}
