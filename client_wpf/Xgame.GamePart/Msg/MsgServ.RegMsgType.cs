using System;

using Xgame.GamePart.Msg.Type;
using Xgame.GamePart.Util;

namespace Xgame.GamePart.Msg
{
    partial class MsgServ
    {
        /// <summary>
        /// 注册消息类型
        /// </summary>
        /// <param name="msgSerialUId"></param>
        /// <param name="newMsgType"></param>
        public void RegMsgType(short msgSerialUId, System.Type newMsgType)
        {
            // 断言参数不为空
            Assert.NotNull(newMsgType, "newMsgType");

            if (newMsgType.IsSubclassOf(typeof(BaseMsgObj)) == false)
            {
                // 如果不是 BaseMsgObj 的子类, 
                // 则抛出异常!
                throw new MsgError(string.Format(
                    "{0} 类不是 {1} 的具体实现类, 不能注册到消息服务!", 
                    newMsgType.Name, 
                    typeof(BaseMsgObj).Name
                ));
            }

            // 获取已有的消息类定义
            System.Type oldMsgType = this.GetMsgTypeBySerialUId(msgSerialUId);

            if (oldMsgType == null)
            {
                // 添加消息类型到字典
                this.PutMsgTypeBySerialUId(msgSerialUId, newMsgType);

                // 验证消息类
                MsgTypeValidator.Validate(newMsgType);
                return;
            }

            if (oldMsgType.Equals(newMsgType))
            {
                // 如果两个类相同, 
                // 那就算了...
                return;
            }
            else
            {
                // 否则抛出异常!
                throw new MsgError(string.Format(
                    "注册消息类 {0} 失败, 已经使用 msgSerialUId = {1} 的数值注册过消息类 {2}",
                    newMsgType.Name,
                    msgSerialUId,
                    oldMsgType.Name
                ));
            }
        }
    }
}
