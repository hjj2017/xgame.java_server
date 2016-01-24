using System;
using System.Windows.Controls;

using Xgame.GameBizModule.Human.Msg;
using Xgame.GameClient.Msg;
using Xgame.GamePart.Msg.Type;

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
                // 如果没有任何角色, 
                // 则跳转到角色创建界面
                this.Dispatcher.BeginInvoke(new Action(this.GotoCreateHuman));
            }
            else
            {
                // 选择一个角色, 
                // 进入游戏!
            }
        }

        /// <summary>
        /// 跳转到创建角色界面
        /// </summary>
        private void GotoCreateHuman()
        {
            this.Content = new Page_CreateHuman();
        }
    }
}
