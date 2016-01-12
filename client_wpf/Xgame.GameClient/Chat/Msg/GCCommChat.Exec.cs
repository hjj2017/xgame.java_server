using System;

using Xgame.GameClient.Chat.Msg.Event;

namespace Xgame.GameClient.Chat.Msg
{
    partial class GCCommChat
    {
        // @Override
        public override void Exec()
        {
            ChatEvent.OBJ.OnGCCommChat(this);
        }
    }
}
