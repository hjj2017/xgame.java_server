using System;
using System.Windows.Controls;

using Xgame.GameBizModule.Human.Msg;
using Xgame.GameClient.Msg;
using Xgame.GamePart.Msg.Type;
using Xgame.WpfApp.Home;

namespace Xgame.WpfApp.Human
{
    /// <summary>
    /// Page_EnterHumanAndLoading.xaml 进入角色并加载数据
    /// </summary>
    public partial class Page_EnterHumanAndLoading : Page
    {
        /// <summary>
        /// 类默认构造器
        /// </summary>
        public Page_EnterHumanAndLoading()
        {
            // 初始化控件
            this.InitializeComponent();

            // 
            // 如果登陆成功了, 
            // 则请求角色入口列表
            // 
            // 创建 CG 消息
            CGQueryHumanEntryList cgMSG = new CGQueryHumanEntryList();
            cgMSG._serverName = new MsgStr("LM1");
            // 发送 CG 消息
            ClientServer.OBJ.AddGCMsgHandler<GCQueryHumanEntryList>(this.Handle_GCQueryHumanEntryList);
            ClientServer.OBJ.SendCGMsg(cgMSG);
        }

        /// <summary>
        /// 处理 GCQueryHumanEntryList 消息
        /// </summary>
        /// <param name="gcMSG"></param>
        private void Handle_GCQueryHumanEntryList(GCQueryHumanEntryList gcMSG)
        {
            if (gcMSG._humanEntryList == null
             || gcMSG._humanEntryList.Count <= 0)
            {
                this.Dispatcher.BeginInvoke(new Action(() => {
                    // 如果没有任何角色, 
                    // 则跳转到角色创建界面
                    MainWindow.TheWnd.Content = new Page_CreateHuman();
                }));
                return;
            }

            // 获取角色入口
            HumanEntryMO mo = gcMSG._humanEntryList[0];

            if (mo == null)
            {
                // TODO : 
                return;
            }

            // 创建 CG 消息登陆角色
            CGEnterHuman cgMSG = new CGEnterHuman();
            cgMSG._humanUId = mo._humanUId;
            cgMSG._humanUIdStr = mo._humanUIdStr;
            // 发送 CG 消息
            ClientServer.OBJ.AddGCMsgHandler<GCEnterHuman>(this.Handle_GCEnterHuman);
            ClientServer.OBJ.SendCGMsg(cgMSG);
        }

        /// <summary>
        /// 处理 GCEnterHuman 消息
        /// </summary>
        /// <param name="gcMSG"></param>
        private void Handle_GCEnterHuman(GCEnterHuman gcMSG)
        {
            if (gcMSG._ready.GetBoolVal())
            {
                // 如果已经是就绪状态, 
                // 则进入主城!
                this.Dispatcher.BeginInvoke(new Action(() => {
                    // 跳转到角色入口页面
                    MainWindow.TheWnd.Content = new Page_Home();
                }));
            }
        }
    }
}
