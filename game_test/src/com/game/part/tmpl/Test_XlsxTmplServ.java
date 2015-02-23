package com.game.part.tmpl;

import java.util.List;

import org.junit.Test;

import com.game.bizModules.building.tmpl.BuildingTmpl;

/**
 * 模板服务测试
 * 
 * @author hjj2019
 * @since 2015/2/22
 *
 */
public class Test_XlsxTmplServ {
	@Test
	public void test() {
		XlsxTmplServ.OBJ._baseDir = "/Data/Temp_Test/";
		XlsxTmplServ.OBJ.loadTmplData(BuildingTmpl.class);
		
		List<?> objList = XlsxTmplServ.OBJ.getObjList(BuildingTmpl.class);
		System.out.println(objList);
	}
}
