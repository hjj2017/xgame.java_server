using System;

namespace Xgame.GameBizModule.CreateHuman.Tmpl
{
    /// <summary>
    /// 角色创建模版
    /// </summary>
    public class CreateHumanTmpl
    {
        /// <summary>
        /// 类默认构造器
        /// </summary>
        public CreateHumanTmpl()
        {
        }

        /// <summary>
        /// 类参数构造器
        /// </summary>
        /// <param name="Id"></param>
        /// <param name="heroImgName"></param>
        public CreateHumanTmpl(int Id, string heroImgName)
        {
            this.Id = Id;
            this.HeroImgName = heroImgName;
        }

        /// <summary>
        /// 获取或设置 Id
        /// </summary>
        public int Id
        {
            get;
            set;
        }

        /// <summary>
        /// 获取或设置武将图片名称
        /// </summary>
        public string HeroImgName
        {
            get;
            set;
        }
    }
}
