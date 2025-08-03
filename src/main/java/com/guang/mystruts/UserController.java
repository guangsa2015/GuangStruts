// UserController.java
package com.guang.mystruts;


import com.guang.mystruts.framework.annotation.GuangAction;
import com.guang.mystruts.framework.annotation.GuangDo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@GuangAction("user") // 模块路径：/user
public class UserController {

    @GuangDo("abc") // 完整路径：/user/abc
    public String handleAbc(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("进入abc处理");
        req.setAttribute("message", "处理 /user/abc 请求成功！");
        return "success"; // 视图名（对应 WEB-INF/views/success.jsp）
    }

    @GuangDo("login") // 完整路径：/user/login
    public String handleLogin(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("进入login处理");
        String username = req.getParameter("username");
        req.setAttribute("user", username);
        return "login";
    }

    @GuangDo("test")
    public String  testJsp(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("进入test处理");
        return "test";
    }
}
