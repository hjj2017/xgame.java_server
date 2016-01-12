using System;
using System.IO;

using Xgame.GamePart.Msg.Type;

namespace Xgame.GameClient.Kernel.Msg
{
    /// <summary>
    /// GC 消息基础类
    /// </summary>
    public abstract class BaseGCMsg : BaseMsgObj
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
            if (br == null)
            {
                // 如果参数对象为空, 
                // 则直接退出!
                return;
            }

            // 首先, 读掉开头的消息长度和消息序列化 Id
            br.ReadInt16();
            br.ReadInt16();

            // 读取版本号和 Md5 字符串
            this.Revision = BaseMsgObj.ReadMsgObjFrom(this.Revision, br);
            this.Md5 = BaseMsgObj.ReadMsgObjFrom(this.Md5, br);

            // 继续读取二进制流
            this.ReadFromImpl(br);
        }

        /// <summary>
        /// 读取消息的具体实现
        /// </summary>
        /// <param name="br"></param>
        protected virtual void ReadFromImpl(BinaryReader br)
        {
            throw new NotImplementedException();
        }

        // @Override
        public override void WriteTo(BinaryWriter bw)
        {
            throw new NotSupportedException();
        }

        /// <summary>
        /// 执行自身
        /// </summary>
        public abstract void Exec();
    }
}
