using System;

using Xgame.GameClient.Human.Msg.Event;

namespace Xgame.GameClient.Human.Msg
{
    partial class GCEnterHuman
    {
        // @Override
        public override void Exec()
        {
            HumanEvent.OBJ.OnGCEnterHuman(this);
        }
    }
}
