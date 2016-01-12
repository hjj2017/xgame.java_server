using System;

using Xgame.GameClient.Human.Msg.Event;

namespace Xgame.GameClient.Human.Msg
{
    partial class GCQueryHumanEntryList
    {
        // @Override
        public override void Exec()
        {
            HumanEvent.OBJ.OnGCQueryHumanEntryList(this);
        }
    }
}
