using System;

namespace Xgame.GamePart.Msg
{
    /// <summary>
    /// 消息错误
    /// </summary>
    public class MsgError : Exception
    {
        /// <summary>
        /// 类默认构造器
        /// </summary>
        public MsgError() : base()
        {
        }

        /// <summary>
        /// 类参数构造器
        /// </summary>
        /// <param name="msg"></param>
        public MsgError(string msg) : base(msg)
        {
        }

        /// <summary>
        /// 类参数构造器
        /// </summary>
        /// <param name="msg"></param>
        /// <param name="innerError"></param>
        public MsgError(string msg, Exception innerError) : base(msg, innerError)
        {
        }
    }
}
