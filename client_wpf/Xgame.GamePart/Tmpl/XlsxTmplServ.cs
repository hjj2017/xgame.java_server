using System;

namespace Xgame.GamePart.Tmpl
{
    /// <summary>
    /// Xlsx 模板服务
    /// </summary>
    public sealed class XlsxTmplServ
    {
        /** 单例对象 */
        public static readonly XlsxTmplServ OBJ = new XlsxTmplServ();

        #region 类构造器
        /// <summary>
        /// 类默认构造器
        /// </summary>
        private XlsxTmplServ()
        {
            this.Lang = "zh_CN";
        }
        #endregion

        /// <summary>
        /// 获取或设置 Excel 文件所在目录
        /// </summary>
        public string XlsxFileDir
        {
            get;
            set;
        }

        /// <summary>
        /// 获取或设置多语言环境, 默认 = zh_CN 简体中文
        /// </summary>
        public string Lang
        {
            get;
            set;
        }

        /// <summary>
        /// 获取或设置输出类文件到目标目录, 主要用于调试
        /// </summary>
        public string DebugClazzToDir
        {
            get;
            set;
        }
    }
}
