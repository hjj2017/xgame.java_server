using System;
using System.Windows;

using Xgame.GameBizModule.Hero.Tmpl;
using Xgame.GameBizModule.Login.Msg;
using Xgame.GameClient.Msg;
using Xgame.GamePart.Msg;
using Xgame.GamePart.Tmpl;
using Xgame.GamePart.Tmpl.Type;
using Xgame.WpfApp.Login;

using Conf = System.Configuration.ConfigurationManager;

namespace Xgame.WpfApp
{
    /// <summary>
    /// MainWindow.xaml 主窗口
    /// </summary>
    public partial class MainWindow : Window
    {
        /** 主窗口引用 */
        private static MainWindow _theWnd;

        /// <summary>
        /// 类默认构造器
        /// </summary>
        public MainWindow()
        {
            // 初始化控件
            this.InitializeComponent();
            // 关闭窗口时关闭服务器
            this.Closed += delegate (object sender, EventArgs e)
            {
                if (ClientServer.OBJ.Connected)
                {
                    // 关闭服务连接
                    ClientServer.OBJ.Shutdown();
                }
            };

            // 加载配置项
            this.LoadConfig();

            // 注册消息类和模板类
            this.RegAllMsgType();
            this.RegAllXlsxTmplType();

            // 设置主窗口引用
            _theWnd = this;

            // 跳转到登陆界面
            this.Content = new Page_Login();
        }

        /// <summary>
        /// 获取主窗口引用
        /// </summary>
        public static MainWindow TheWnd
        {
            get
            {
                return _theWnd;
            }
        }

        /// <summary>
        /// 加载配置项
        /// </summary>
        private void LoadConfig()
        {
            XlsxTmplServ.OBJ.XlsxFileDir = Conf.AppSettings["XlsxTmplServ.XlsxFileDir"];
            XlsxTmplServ.OBJ.DebugClazzToDir = Conf.AppSettings["XlsxTmplServ.DebugClazzToDir"];
            XlsxTmplServ.OBJ.Lang = Conf.AppSettings["XlsxTmplServ.Lang"];
        }

        /// <summary>
        /// 注册所有消息类
        /// </summary>
        private void RegAllMsgType()
        {
            // 获取该程序集下的所有类定义
            System.Type[] typeArr = typeof(GCLogin).Assembly.GetTypes();

            foreach (System.Type type in typeArr)
            {
                if (type.IsSubclassOf(typeof(BaseGCMsg)))
                {
                    // 创建消息对象
                    BaseGCMsg cgMSG = (BaseGCMsg)Activator.CreateInstance(type);
                    // 如果是 CG 消息类, 
                    // 则注册消息...
                    MsgServ.OBJ.RegMsgType(
                        cgMSG.MsgSerialUId, cgMSG.GetType()
                    );
                }
            }
        }

        /// <summary>
        /// 注册所有 Xlsx 模板类
        /// </summary>
        private void RegAllXlsxTmplType()
        {
            // 获取该程序集下的所有类定义
            System.Type[] typeArr = typeof(HeroTmpl).Assembly.GetTypes();

            foreach (System.Type type in typeArr)
            {
                if (type.IsSubclassOf(typeof(BaseXlsxTmpl)))
                {
                    // 如果是 Xlsx 模板类, 
                    // 则注册模板...
                    XlsxTmplServ.OBJ.LoadTmplData(type);
                }
            }
        }
    }
}
