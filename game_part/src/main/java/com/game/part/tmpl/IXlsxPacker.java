package com.game.part.tmpl;

import com.game.part.tmpl.type.AbstractXlsxTmpl;

/**
 * 打包接口
 * 
 * @author hjj2017
 * @since 2015/2/27
 * 
 */
public interface IXlsxPacker {
    /**
     * 打包模板对象
     *
     * @param tmplObj
     *
     */
    public void packUp(AbstractXlsxTmpl tmplObj);
}
