using System;

using Xgame.GamePart.Tmpl.Type;

namespace Xgame.GamePart.Tmpl
{
    /// <summary>
    /// 打包接口
    /// </summary>
    public interface IXlsxPacker
    {
        /// <summary>
        /// 打包模板对象
        /// </summary>
        /// <param name="tmplObj"></param>
        void PackUp(BaseXlsxTmpl tmplObj);
    }
}
