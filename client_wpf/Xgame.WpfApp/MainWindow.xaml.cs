using System;
using System.Windows;

using Xgame.GameBizModule.Login.Msg;
using Xgame.GameClient.Msg;
using Xgame.GamePart.Msg;
using Xgame.WpfApp.Login;

namespace Xgame.WpfApp
{
    /// <summary>
    /// MainWindow.xaml 主窗口
    /// </summary>
    public partial class MainWindow : Window
    {
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

            // 注册消息类
            this.RegAllMsgType();

            // 跳转到登陆界面
            this.Content = new Page_Login();
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
    }
}
