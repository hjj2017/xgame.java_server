**初始化 XlsxTmplServ**

1、（必选）加载 D 盘 /Temp_Test/Xlsx 目录下的所有 Excel 文件（2007 格式）；

2、（可选）将系统动态生成的 Java 类源码保存到 D 盘 /Temp_Test/Debug 目录下；

3、（可选）设置环境变量 lang = zh_CN；

```
XlsxTmplServ.OBJ._xlsxFileDir = "/D:/Temp_Test/Xlsx";
// XlsxTmplServ.OBJ._debugClazzToDir = "/D:/Temp_Test/Debug";
// XlsxTmplServ.OBJ._propMap = new HashMap<>();
// XlsxTmplServ.OBJ._propMap.put("lang", "zh_CN");
```

----

**注册模版类并验证**

1、加载 BuildingTmpl 类、ShopTmpl 类和 SysLangTmpl 类；

2、验证所有模版类；

```
// 模版类数组
Class<?>[] tmplClazzArr = {
    BuildingTmpl.class, 
    Shop.class,
    SysLangTmpl.class,
};

for (Class<?> tmplClazz : tmplClazzArr) {
    // 加载模版类数据并打包
    XlsxTmplServ.OBJ.loadTmplData(tmplClazz);
    XlsxTmplServ.OBJ.packUp(tmplClazz);
}

// 验证所有模版类
XlsxTmplServ.OBJ.validateAll();
```

----

**BuildingTmpl 类**

1、从 building.xlsx 文件的第 1 个页签开始读数据；

2、定义建筑 Id，不允许为空值；

3、定义建筑类型，不允许为空值，并且只能是：1、2、3、4，这 4 个值中的一个；

4、定义建筑名称，可以为空值；

5、定义建筑说明，可以为空值，但最大长度不能超过 200 个字符；

6、定义建筑 Id 字典，可以通过建筑 Id 数值取得 BuildingTmpl 对象；

7、定义建筑类型字典，可以通过建筑类型取得 BuildingTmpl 对象列表；

```
@FromXlsxFile(fileName = "building.xlsx", sheetIndex = 0)
public class BuildingTmpl extends AbstractXlsxTmpl {
    /** 建筑 Id */
    @OneToOne(groupName = "_Id")
    public XlsxInt _Id = new XlsxInt(false);
    /** 建筑类型 */
    @OneToMany(groupName = "_Type")
    public XlsxInt _typeInt = XlsxInt.createByEnum(false, 1, 2, 3, 4);
    /** 建筑名称 */
    public XlsxStr _buildingName;
    /** 建筑说明 */
    public XlsxStr _buildingDesc = new XlsxStr(true) {
        @Override
        public void validate() {
            super.validate();

            if (this.getStrVal() != null && 
                this.getStrVal().length() > 200) {
                throw new XlsxTmplError(this, "建筑说明大于 200 个字符");
            }
        }
    };

    /** Id 字典 */
    @OneToOne(groupName = "_Id")
    public static Map<Integer, BuildingTmpl> _IdMap = new HashMap<>();
    /** 类型字典 */
    @OneToMany(groupName = "_Type")
    public static Map<Integer, List<BuildingTmpl>> _typeMap = new HashMap<>();
}
```

----

**使用 BuildingTmpl 类**

0、一定先要确保已经调用过：XlsxTmplServ.OBJ.packUp(...); 这一步；

1、根据建筑 Id 获取 BuildingTmpl 对象；

2、根据建筑类型获取 BuildingTmpl 对象列表；

```
BuildingTmpl tmplObj = BuildingTmpl._IdMap.get(1);
List<BuildingTmpl> tmplObjList = BuildingTmpl._typeMap.get(1);
```

----

**复杂的 BuildingTmpl 类**

1、从 building.xlsx 文件的第 1 个页签开始读数据；

2、A 列为建筑 Id，不允许为空值；

3、B 列 ~ D 列，为建筑功能配置；

```
@FromXlsxFile(fileName = "building.xlsx", sheetIndex = 0)
public class BuildingTmpl extends AbstractXlsxTmpl {
    /** 建筑 Id */
    public XlsxInt _Id = new XlsxInt(false);
    /** 建筑功能 */
    public FuncTmplObj _func;
}

// 这里无需 @FromXlsxFile 注解
public class FuncTmplObj extends AbstractXlsxTmpl {
    /** 功能 Id */
    public XlsxInt _funcId;   // 将对应 B 列
    /** 功能名称 */
    public XlsxStr _funcName; // 将对应 C 列
    /** 功能说明 */
    public XlsxStr _funcDesc; // 将对应 D 列
}
```

----

**再复杂一点的 BuildingTmpl 类**

1、从 building.xlsx 文件的第 1 个页签开始读数据；

2、A 列为建筑 Id，不允许为空值；

3、B 列 ~ D 列，为建筑功能 1 配置；

4、E 列 ~ G 列，为建筑功能 2 配置；

```
@FromXlsxFile(fileName = "building.xlsx", sheetIndex = 0)
public class BuildingTmpl extends AbstractXlsxTmpl {
    /** 建筑 Id */
    public XlsxInt _Id = new XlsxInt(false);
    /** 建筑功能 */
    @ElementNum(2)
    public XlsxArrayList<FuncTmplObj> _func;
}
```

----

**ShopTmpl 类**

1、从 shop.xlsx 文件的第 1 个页签开始读数据；

2、A 列为商店类型，不允许为空值；

3、B 列为商店所绑定的建筑 Id，不允许为空值；

3、C 列 ~ G 列，为道具 Id。即，道具 Id 数组，长度为 5；

4、C 列绝对不能为空值！即，商店里至少应该有 1 个道具；

```
@FromXlsxFile(fileName = "shop.xlsx", sheetIndex = 0)
public class ShopTmpl extends AbstractXlsxTmpl {
    /** 商店类型 */
    public XlsxInt _typeInt = new XlsxInt(false);
	/** 绑定的建筑 Id */
	public XlsxInt _buildingId = new XlsxInt(false);
    /** 道具 Id 列表 */
    @ElementNum(5)
    public XlsxArrayList<XlsxInt> _itemIdList = new XlsxArrayList(
        new XlsxInt(false)
    );
}
```

----

**高级验证**

1、商店配置表中不能没有数据；

2、商店所绑定的建筑 Id 必须是建筑配置表里定义过的；

```
@FromXlsxFile(fileName = "shop.xlsx", sheetIndex = 0)
@Validator(clazz = Validator_ShopTmpl.class) // <-- 注意 : 在这里需要指明验证器
public class ShopTmpl extends AbstractXlsxTmpl {
    // ...
}

/** 商店配置验证器 */
public class Validator_ShopTmpl implements IXlsxValidator<ShopTmpl> {
    @Override
    public void validate(List<ShopTmpl> objList) {
        if (objList == null || 
		    objList.isEmpty()) {
		    throw new XlsxTmplError("商店配置表中没有数据");
		}

		objList.forEach(tmplObj -> {
            // 获取商店所绑定的建筑 Id
		    int buildingId = tmplObj._buildingId.getIntVal();

		    if (BuildingTmpl._IdMap.get(buildingId) == null) {
				throw new XlsxTmplError(tmplObj._buildingId, "未找到 Id = " + buildingId + " 的建筑");
            }
        });
    }
}
```

----

**SysLangTmpl 定义**

0、一定先要确保已经掉用过：XlsxTmplServ.OBJ._propMap.put("lang", "zh_CN");

1、需要读取 i18n 目录下的 zh_CN 目录中的 sysLang.xlsx 文件；

2、定义中文文本字段；

3、定义多语言文本字段；

```
@FromXlsxFile(fileName = "i18n/${lang}/sysLang.xlsx")
public class SysLangTmpl extends AbstractXlsxTmpl {
    /** 中文文本 */
    public XlsxStr _zhText;
    /** 多语言文本 */
    public XlsxStr _langText;
}
```
