using System;
using System.Configuration;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;

using LitJson;

using Xgame.GameBizModule;
using Xgame.GameBizModule.Login.Msg;
using Xgame.GameClient.Msg;
using Xgame.GamePart.Msg.Type;
using Xgame.WpfApp.Human;

namespace Xgame.WpfApp.Login
{
    /// <summary>
    /// Page_Login.xaml 登陆界面
    /// </summary>
    public partial class Page_Login : Page
    {
        /// <summary>
        /// 类默认构造器
        /// </summary>
        public Page_Login()
        {
            // 初始化控件
            this.InitializeComponent();
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

            Player.OBJ._userName = userName;
            Player.OBJ._selectedServerName = "LM1";

            JsonData jsonData = new JsonData();
            jsonData["protocol"] = "dbUser";
            jsonData["userName"] = userName;
            jsonData["password"] = userPass;

            // 创建 CG 消息
            CGLogin cgMSG = new CGLogin();
            cgMSG._platformUId = new MsgStr(userName);
            cgMSG._loginStr = new MsgStr(jsonData.ToJson());
            // 发送 CG 消息
            ClientServer.OBJ.AddGCMsgHandler<GCLogin>(this.Handle_GCLogin);
            ClientServer.OBJ.SendCGMsg(cgMSG);
        }

        /// <summary>
        /// 处理 GCLogin 消息
        /// </summary>
        /// <param name="gcMSG"></param>
        private void Handle_GCLogin(GCLogin gcMSG)
        {
            // 获取登陆成功标志
            bool loginOk = gcMSG._success.GetBoolVal();

            if (!loginOk)
            {
                // 如果登陆失败, 
                // 则直接退出!
                // TODO : 需要弹出登陆失败提示
                return;
            }

            this.Dispatcher.BeginInvoke(new Action(() => {
                // 跳转到角色入口页面
                MainWindow.TheWnd.Content = new Page_EnterHumanAndLoading();
            }));
        }

        /// <summary>
        /// 密码框输入事件
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void _txtUserPass_KeyDown(object sender, KeyEventArgs e)
        {
            if (e.Key == Key.Enter)
            {
                // 模拟登陆按钮点击事件
                this._btnLogin_Click(null, null);
            }
        }
    }
}
