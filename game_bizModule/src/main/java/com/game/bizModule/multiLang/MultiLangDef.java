package com.game.bizModule.multiLang;

/**
 * 多语言定义, 命名规则: LANG_ 前缀 + 系统名称 + 错误名称
 *
 * @author hjj2019
 * @since 2015/7/26
 *
 */
public final class MultiLangDef {
    /** 内置值 */
    private static int INNER_VAL = 1000000;

    @LangText("系统")
    public static final int LANG_COMM_system = ++INNER_VAL;
    @LangText("参数错误")
    public static final int LANG_COMM_paramError = ++INNER_VAL;
    @LangText("管理器对象为空")
    public static final int LANG_COMM_nullMngrObj = ++INNER_VAL;

// 建筑系统
///////////////////////////////////////////////////////////////////////

    @LangText("主城等级不能大于角色等级")
    public static final int LANG_BUILDING_homeLevelCannotGTHumanLevel = ++INNER_VAL;
    @LangText("建筑等级不能大于主城等级")
    public static final int LANG_BUILDING_buildingLevelCannotGTHomeLevel = ++INNER_VAL;
    @LangText("建筑尚未开放")
    public static final int LANG_BUILDING_notOpenYet = ++INNER_VAL;

// Cd 系统
///////////////////////////////////////////////////////////////////////

    @LangText("找不到可以累计时间的 Cd 类型")
    public static final int LANG_CD_cannotFoundCdTypeToAddTime = ++INNER_VAL;
    @LangText("累计 Cd 时间小于等于 0")
    public static final int LANG_CD_addTimeLeq0 = ++INNER_VAL;
    @LangText("Cd 类型尚未开启")
    public static final int LANG_CD_cdTypeNotOpened = ++INNER_VAL;
    @LangText("不能超过阈值")
    public static final int LANG_CD_diffTimeGeqThreshold = ++INNER_VAL;

// 聊天系统
///////////////////////////////////////////////////////////////////////

    @LangText("Cd 时间未结束, 不能发送世界消息")
    public static final int LANG_CHAT_worldChatHasCd = ++INNER_VAL;


    /**
     * 类默认构造器
     *
     */
    private MultiLangDef() {
    }
}
