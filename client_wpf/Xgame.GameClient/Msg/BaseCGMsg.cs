using System;
using System.IO;

using Xgame.GamePart.Msg.Type;

namespace Xgame.GameClient.Msg
{
    /// <summary>
    /// CG 消息基础类
    /// </summary>
    public abstract class BaseCGMsg : BaseMsgObj
    {
        /// <summary>
        /// 获取或设置版本修订, 主要用于消息加密
        /// </summary>
        public MsgInt Revision
        {
            get;
            set;
        }

        /// <summary>
        /// 获取或设置 Md5 字符串, 主要用于消息加密
        /// </summary>
        public MsgStr Md5
        {
            get;
            set;
        }

        /// <summary>
        /// 获取消息序列化 UId
        /// </summary>
        /// <returns></returns>
        public abstract short MsgSerialUId
        {
            get;
        }

        // @Override
        public override void ReadFrom(BinaryReader br)
        {
            throw new NotSupportedException();
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

            // 获取原始位置
            long oldPos = bw.BaseStream.Position;

            // 首先, 写出消息长度和消息序列化 Id.
            // 因为这时候还不能确定消息长度,
            // 所以在开头写出一个 short 类型的 -1 用于占位!
            bw.Write((short)-1);
            bw.Write(this.MsgSerialUId);

            if (this.Revision == null)
            {
                // 如果为空,
                // 写出一个空值...
                MsgInt.Empty.WriteTo(bw);
            }
            else
            {
                // 写出版本号
                this.Revision.WriteTo(bw);
            }

            if (this.Md5 == null)
            {
                // 如果为空, 
                // 写出一个空值...
                MsgStr.Empty.WriteTo(bw);
            }
            else
            {
                // 写出 Md5 字符串
                this.Md5.WriteTo(bw);
            }

            // 继续写出到二进制流
            base.WriteTo(bw);

            // 好了, 到现在位置,
            // 我们可以确定消息的长度了...
            // 回滚到 0 位置 ( 准备改写占位符 ), 
            // 写出消息长度!
            bw.BaseStream.Position = oldPos;
            bw.Write((short)bw.BaseStream.Length);

            // 再次复原位置
            bw.BaseStream.Position = oldPos;
        }
    }
}
