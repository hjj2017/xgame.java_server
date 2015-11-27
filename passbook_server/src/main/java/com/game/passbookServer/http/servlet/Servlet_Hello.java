package com.game.passbookServer.http.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Hello, 主要用于测试! 请求示例:
 * <code>
 * http://127.0.0.1:8007/hello
 * </code>
 *
 * @author hjj2017
 * @since 2015/11/27
 *
 */
public class Servlet_Hello extends HttpServlet {
    @Override
    protected void doPost(
        final HttpServletRequest req, final HttpServletResponse res)
        throws ServletException, IOException {
        this.doGet(req, res);
    }

    @Override
    protected void doGet(
        final HttpServletRequest req, final HttpServletResponse res)
        throws ServletException, IOException {
        if (req == null ||
            res == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        res.setContentType("application/json; charset=utf-8");
        res.setStatus(HttpServletResponse.SC_OK);
        res.getWriter().println("hello");
    }
}
