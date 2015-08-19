package com.game.part.tmpl;

import org.junit.Test;

/**
 * 模版服务测试类
 *
 * @author hjj2017
 * @since 2015/8/19
 *
 */
public class TEST_XlsxTmplServ {
    @Test
    public void mainTest() {
        // 设置 Excel 文件路径
        XlsxTmplServ.OBJ._xlsxFileDir = "";
        // 加载 Test 模版
        XlsxTmplServ.OBJ.loadTmplData(TestTmpl.class);
        XlsxTmplServ.OBJ.packUp(TestTmpl.class);
        // 验证所有数据
        XlsxTmplServ.OBJ.validateAll();
    }
}
