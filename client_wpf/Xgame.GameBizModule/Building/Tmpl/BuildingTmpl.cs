using System;

using Xgame.GamePart.Tmpl.Attr;
using Xgame.GamePart.Tmpl.Type;

namespace Xgame.GameBizModule.Building.Tmpl
{
    /// <summary>
    /// 建筑配置
    /// </summary>
    [FromXlsxFile("building.xlsx", "建筑")]
    public class BuildingTmpl : BaseXlsxTmpl
    {
        /** 建筑 Id */
        public XlsxInt _Id;
        /** 建筑名称 */
        public XlsxStr _name;
        /** 建筑说明 */
        public XlsxStr _desc;
    }
}
