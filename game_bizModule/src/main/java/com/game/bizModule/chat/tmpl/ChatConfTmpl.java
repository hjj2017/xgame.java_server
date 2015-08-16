package com.game.bizModule.chat.tmpl;

import java.util.HashMap;
import java.util.Map;

import com.game.part.tmpl.anno.FromXlsxFile;
import com.game.part.tmpl.anno.OneToOne;
import com.game.part.tmpl.type.AbstractXlsxTmpl;
import com.game.part.tmpl.type.XlsxInt;
import com.game.part.tmpl.type.XlsxStr;

/**
 * 聊天配置模版
 *
 * @author hjj2017
 * @since 2015/8/15
 *
 */
@FromXlsxFile(fileName = "chat.xlsx", sheetIndex = 0)
public class ChatConfTmpl extends AbstractXlsxTmpl {
    /** 世界聊天 */
    private static final int KEY_WORLD_CHAT = 1001;

    /** 主键 */
    @OneToOne(groupName = "_key")
    public XlsxInt _key;
    /** 字符串值 */
    public XlsxStr _val;

    /** 配置字典 */
    @OneToOne(groupName = "_key")
    public static final Map<Integer, ChatConfTmpl> _tmplMap = new HashMap<>();

    /**
     * 获取世界聊天 Cd 时间
     *
     * @return
     *
     */
    public static long getWorldChatCdTime() {
        return _tmplMap.get(KEY_WORLD_CHAT)._val.getLongVal();
    }
}
