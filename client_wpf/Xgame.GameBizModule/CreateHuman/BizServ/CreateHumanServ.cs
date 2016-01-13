using System;
using System.Collections.Generic;

using Xgame.GameBizModule.CreateHuman.Tmpl;

namespace Xgame.GameBizModule.CreateHuman.BizServ
{
    /// <summary>
    /// 创建角色服务
    /// </summary>
    public sealed class CreateHumanServ
    {
        /** 单例对象 */
        public static readonly CreateHumanServ OBJ = new CreateHumanServ();

        /// <summary>
        /// 类默认构造器
        /// </summary>
        private CreateHumanServ()
        {
        }

        /// <summary>
        /// 获取角色创建配置数据
        /// </summary>
        /// <returns></returns>
        public IEnumerable<CreateHumanTmpl> GetCreateHumanTmplList()
        {
            yield return new CreateHumanTmpl(10, "Hero_10.png");
            yield return new CreateHumanTmpl(11, "Hero_11.png");
            yield return new CreateHumanTmpl(20, "Hero_20.png");
            yield return new CreateHumanTmpl(21, "Hero_21.png");
            yield return new CreateHumanTmpl(30, "Hero_30.png");
        }
    }
}
