using System;

namespace Xgame.GamePart.Tmpl.Type
{
    /// <summary>
    /// 跳过 Xlsx 的一个列
    /// </summary>
    public sealed class XlsxSkip : BaseXlsxCol
    {
        // @Override
        protected override void ReadImpl(XlsxRowReadStream stream)
        {
            stream.ReadCellVal();
        }
    }
}
