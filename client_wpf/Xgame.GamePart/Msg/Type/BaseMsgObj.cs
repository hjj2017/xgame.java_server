using System;
using System.IO;

namespace Xgame.GamePart.Msg.Type
{
    /// <summary>
    /// 消息对象
    /// </summary>
    public abstract class BaseMsgObj : BaseMsgField
    {
        // @Override
        public override void ReadFrom(BinaryReader br)
        {
            if (br == null)
            {
                // 如果参数对象为空, 
                // 则直接退出!
                return;
            }

            // 获取帮助器对象
            IReadHelper helperObj = ReadHelperMaker.Make(this.GetType());

            if (helperObj != null)
            {
                // 如果帮助器不为空, 
                // 则读取消息对象!
                helperObj.ReadMsgObjFrom(this, br);
            }
        }

        // @Override
        public override void WriteTo(BinaryWriter bw)
        {
            if (bw == null)
            {
                // 如果参数对象为空, 
                // 则直接退出!
                return;
            }

            IWriteHelper helperObj = WriteHelperMaker.Make(this.GetType());

            if (helperObj != null)
            {
                // 如果帮助器不为空, 
                // 则写出消息对象!
                helperObj.WriteMsgObjTo(this, bw);
            }
        }
    }
}
