using System;

namespace Xgame.GamePart.Tmpl.Type
{
    /// <summary>
    /// 模版基础类
    /// </summary>
    public class BaseXlsxTmpl : BaseXlsxCol
    {
        // @Override
        protected override void ReadImpl(XlsxRowReadStream fromStream)
        {
            if (fromStream == null)
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
                helperObj.ReadTmplObjFrom(this, fromStream);
            }
        }
    }
}
