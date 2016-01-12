using System;
using System.Configuration;
using System.Windows;

using LitJson;

using Xgame.GameClient.Msg;
using Xgame.GameClient.Login.Msg;
using Xgame.GamePart.Msg;
using Xgame.GamePart.Msg.Type;

namespace Xgame.WpfApp
{
    /// <summary>
    /// MainWindow.xaml 主界面
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
            // 注册消息类
            this.RegAllMsgType();
        }

        /// <summary>
        /// 注册所有消息类
        /// </summary>
        private void RegAllMsgType()
        {
            // 获取该程序集下的所有类定义
            System.Type[] typeArr = this.GetType().Assembly.GetTypes();

            foreach (System.Type type in typeArr)
            {
                if (type.IsSubclassOf(typeof(BaseCGMsg)))
                {
                    // 创建消息对象
                    BaseCGMsg cgMSG = (BaseCGMsg)Activator.CreateInstance(type);
                    // 如果是 CG 消息类, 
                    // 则注册消息...
                    MsgServ.OBJ.RegMsgType(
                        cgMSG.MsgSerialUId, cgMSG.GetType()
                    );
                }
            }
        }

        /// <summary>
        /// 登陆按钮点击事件
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void _btnLogin_Click(object sender, RoutedEventArgs e)
        {
            if (!ClientServer.OBJ.Connected)
            {
                // 连接到游戏服
                ClientServer.OBJ.GameServerIpAddr = ConfigurationManager.AppSettings["GameServerIpAddr"];
                ClientServer.OBJ.GameServerPort = Convert.ToInt32(ConfigurationManager.AppSettings["GameServerPort"]);
                ClientServer.OBJ.ConnectToGameServer();
            }

            string userName = this._txtUserName.Text;
            string userPass = this._txtUserPass.Password;

            JsonData jsonData = new JsonData();
            jsonData["protocol"] = "dbUser";
            jsonData["userName"] = userName;
            jsonData["password"] = userPass;

            // 创建 CG 消息
            CGLogin cgMSG = new CGLogin();
            cgMSG._platformUId = new MsgStr(userName);
            cgMSG._loginStr = new MsgStr(jsonData.ToJson());
            // 发送 CG 消息
            ClientServer.OBJ.AddHandler<GCLogin, Handler_GCLogin>("gcLogin", new Handler_GCLogin());
            ClientServer.OBJ.SendCGMsg(cgMSG);
        }

        /// <summary>
        /// GC 登陆消息处理器
        /// </summary>
        private class Handler_GCLogin : GCMsgHandleAdapter<GCLogin>
        {
            // @Override
            public override void Handle(GCLogin gcMSG)
            {
                gcMSG._success.GetBoolVal();
            }
        }
    }
}
