using System;

namespace Xgame.GamePart.Tmpl.Type
{
    /// <summary>
    /// 读取帮助者
    /// </summary>
    public interface IReadHelper
    {
        /// <summary>
        /// 从 Xlsx 行数据流中读取数据给模板对象
        /// </summary>
        /// <param name="toTmplObj"></param>
        /// <param name="fromStream"></param>
        void ReadTmplObjFrom(BaseXlsxTmpl toTmplObj, XlsxRowReadStream fromStream);
    }
}
