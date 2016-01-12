using System;

using Xgame.GameClient.Login.Msg.Event;

namespace Xgame.GameClient.Login.Msg
{
    partial class GCLogin
    {
        // @Override
        public override void Exec()
        {
            LoginEvent.OBJ.OnGCLogin(this);
        }
    }
}
