package com.game.bizModule.chat.bizServ;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.game.bizModule.chat.msg.CGWorldChat;
import com.game.bizModule.chat.msg.GCCommChat;
import com.game.bizModule.chat.tmpl.ChatConfTmpl;
import com.game.bizModule.human.Human;
import com.game.bizModule.multiLang.MultiLangDef;
import com.game.bizModule.multiLang.bizServ.MultiLangServ;
import com.game.bizModule.time.TimeServ;
import com.game.part.msg.type.MsgStr;
import com.game.part.util.Assert;

/**
 * 世界聊天打字员
 *
 * @author hjj2017
 * @since 2015/8/15
 *
 */
public final class WorldChatTypist implements ITypist<CGWorldChat> {
    /** 单例对象 */
    public static final WorldChatTypist OBJ = new WorldChatTypist();

    /** Cd 字典 */
    private Map<Long, Long> _cdMap = new HashMap<>();

    /**
     * 类默认构造器
     *
     */
    private WorldChatTypist() {
    }

    @Override
    public GCCommChat type(Human h, CGWorldChat cgMSG, final List<Long> outHumanUIdList) {
        // 断言参数不为空
        Assert.notNull(h, "h");
        Assert.notNull(cgMSG, "cgMSG");

        // 创建 GC 消息
        GCCommChat gcMSG = new GCCommChat();

        // 看看是否有 Cd?
        if (this.hasCd(h)) {
            // 如果还有 Cd 时间,
            // 那么直接退出!
            gcMSG._text = new MsgStr(MultiLangServ.OBJ.getLangText(MultiLangDef.LANG_CHAT_hasCd));
            return gcMSG;
        }

        // 更新 Cd 时间
        this.updateCd(h);
        // 设置聊天内容
        gcMSG._text = cgMSG._text;

        if (outHumanUIdList != null) {
            // 获取角色列表
            List<Human> hl = Human.getAllOnlineHuman();

            if (hl != null &&
                hl.isEmpty() == false) {
                // 将角色 UId 添加到输出参数
                hl.forEach(hCurr -> outHumanUIdList.add(hCurr._humanUId));
            }
        }

        return gcMSG;
    }

    /**
     * 是否有 Cd
     *
     * @param h
     * @return
     *
     */
    private boolean hasCd(Human h) {
        if (h == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return false;
        }

        // 获取上一次说话时间
        Long lastChatTime = this._cdMap.get(h._humanUId);

        if (lastChatTime != null &&
            isInCdTime(lastChatTime)) {
            // 如果距离上次说话时间还不到 1 秒,
            // 则 Cd 时间未结束
            return true;
        } else {
            // 如果已经超过 1 秒,
            // 则 Cd 时间已结束
            return false;
        }
    }

    /**
     * 是否在 Cd 时间内?
     *
     * @param time
     * @return
     *
     */
    private static boolean isInCdTime(long time) {
        // 获取当前时间
        long now = TimeServ.OBJ.now();
        // 比较时间点
        return time >= (now - ChatConfTmpl.getWorldChatCdTime());
    }

    /**
     * 更新 Cd 时间
     *
     * @param h
     *
     */
    private void updateCd(Human h) {
        if (h == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        // 更新 Cd 字典
        this._cdMap.put(h._humanUId, TimeServ.OBJ.now());
        // 清理过期数据
        this._cdMap.entrySet().removeIf(e -> {
            return e.getValue() == null || isInCdTime(e.getValue());
        });
    }
}
