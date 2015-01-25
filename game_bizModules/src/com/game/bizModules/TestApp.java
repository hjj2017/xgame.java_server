package com.game.bizModules;

import com.game.bizModules.building.tmpl.BuildingTmpl;
import com.game.part.tmpl.XlsxTmplServ;
import com.game.part.utils.XSSFUtil;

public class TestApp {
	public static void main(String[] args) {
		XlsxTmplServ.OBJ._baseDir = "/D:/Temp_Test/";
		XlsxTmplServ.OBJ.loadTmplData(BuildingTmpl.class);
//		XlsxTmplServ.OBJ.validateObjList(BuildingTmpl.class);
//
//		System.out.println(BuildingTmpl._IDMap);
//		System.out.println(BuildingTmpl._IDMap.get(1));
	}
}
