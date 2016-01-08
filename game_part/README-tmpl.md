#xgame-part-tmpl

在游戏项目中，用于读取 Excel 2007 格式文件，支持简单校验和多语言。该项目是 Xgame 项目的一个子项目！

直接浏览以下这些高级特性：

* @see [CdTmpl 约束](./src/test/java/com/game/part/tmpl/CdTmpl.java)
* @see [BuildingTmpl_1 模板嵌套](./src/test/java/com/game/part/tmpl/BuildingTmpl_0.java)
* @see [BuildingTmpl_2 模板嵌套列表](./src/test/java/com/game/part/tmpl/BuildingTmpl_1.java)
* @see [ShopItemTmpl 验证器](./src/test/java/com/game/part/tmpl/ShopItemTmpl.java)
* @see [SysLangTmpl 多语言变量 ${lang}](./src/test/java/com/game/part/tmpl/SysLangTmpl.java)
* @see [HeroTmpl 多语言字段](./src/test/java/com/game/part/tmpl/HeroTmpl.java)
* @see [CityTmpl 复合键](./src/test/java/com/game/part/tmpl/CityTmpl.java)

浏览 Excel 表：

* @see [cd.xlsx](./src/test/resources/xlsx/equip.xlsx)
* @see [building_0.xlsx](./src/test/resources/xlsx/building_0.xlsx)
* @see [building_1.xlsx](./src/test/resources/xlsx/building_1.xlsx)
* @see [shopItem.xlsx](./src/test/resources/xlsx/shopItem.xlsx)
* @see [sysLang.xlsx](./src/test/resources/xlsx/i18n/en_US/sysLang.xlsx)
* @see [hero.xlsx](./src/test/resources/xlsx/hero.xlsx)
* @see [city.xlsx](./src/test/resources/xlsx/city.xlsx)

----

**快速使用 XlsxTmplServ**

@see [TEST_XlsxTmplServ.java](./src/test/java/com/game/part/tmpl/TEST_XlsxTmplServ.java)；

```
public class TEST_XlsxTmplServ {
    // 应用程序入口
    public static void main(String[] argArr) {
        // 设置 Excel 文件的存放目录
        XlsxTmplServ.OBJ._xlsxFileDir = "/D:/Temp_Test/xlsx";
        // 设置为英语
        // XlsxTmplServ.OBJ._lang = "en_US";
        
        // 模版类数组
        Class<?>[] tmplClazzArr = {
            CdTmpl.class,
            BuildingTmpl_0.class,
            BuildingTmpl_1.class,
            EquipTmpl.class,
            ShopItemTmpl.class,
            HeroTmpl.class,
        };
        
        for (Class<?> tmplClazz : tmplClazzArr) {
            // 强制转型
            Class<AbstractXlsxTmpl> c = (Class<AbstractXlsxTmpl>)tmplClazz;
            // 加载模版类数据并打包
            XlsxTmplServ.OBJ.loadTmplData(c);
            XlsxTmplServ.OBJ.packUp(c);
        }
        
        // 验证所有模版类
        XlsxTmplServ.OBJ.validateAll();
        
        // 获取装备模板 11001
        EquipTmpl tmplObj = EquipTmpl._IdMap.get(11001);
        System.out.println(tmplObj._name.getStrVal());

        // 获取所有穿戴位置 = 1 的模板列表
        List<EquipTmpl> tmplObjList = EquipTmpl._wearPosMap.get(1);
        System.out.println(tmplObjList.size());
    }
}
```

----

<br />

**EquipTmpl 类**

1、从 equip.xlsx 文件的第 1 个页签开始读数据；

2、\[A 列\] = 装备 Id，不允许为空值；

3、\[B 列\] = 装备名称，可以为空值；

4、\[C 列\] = 装备说明，可以为空值；

5、\[D 列\] = 穿戴位置，不能为空！并且只能是 1、2、3、4 这四个数值中的其中一个！默认值 = 1

6、在类中，定义装备 Id 字典，可以通过装备 Id 数值取得 EquipTmpl 对象；

7、在类中，定义装备穿戴字典，可以通过穿戴位置取得 EquipTmpl 对象列表；

@see [EquipTmpl.java](./src/test/java/com/game/part/tmpl/EquipTmpl.java)；

@see [equip.xlsx](./src/test/resources/xlsx/equip.xlsx)；

```
@FromXlsxFile(fileName = "equip.xlsx")
public class EquipTmpl extends AbstractXlsxTmpl {
    /** 装备 Id */
    @OneToOne(groupName = "_equipId")
    public XlsxInt _Id = new XlsxInt(false);
    /** 名称 */
    public XlsxStr _name;
    /** 说明 */
    public XlsxStr _desc;
    /** 穿戴位置 */
    @OneToMany(groupName = "_wearPos")
    public XlsxInt _wearPos = XlsxInt.createByEnum(false, 1, new int[] { 1, 2, 3, 4 });

    /** Id 字典 */
    @OneToOne(groupName = "_equipId")
    public static Map<Integer, EquipTmpl> _IdMap = new HashMap<>();
    /** 穿戴位置字典 */
    @OneToMany(groupName = "_wearPos")
    public static Map<Integer, List<EquipTmpl>> _wearPosMap = new HashMap<>();
}
```

----

# 关注我们

QQ 群：327606822

微信公众号：myXgame

![微信公众号](http://git.oschina.net/afrxprojs/xgame-code_server/raw/master/WeiXinGongZhongHao.jpg "myXgame")
