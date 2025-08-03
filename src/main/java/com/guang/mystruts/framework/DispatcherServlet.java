// DispatcherServlet.java
package com.guang.mystruts.framework;

import com.guang.mystruts.framework.annotation.GuangDo;
import com.guang.mystruts.framework.annotation.GuangAction;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@WebServlet("/*") // 拦截所有请求
public class DispatcherServlet extends HttpServlet {
    // 路径映射：请求路径 → 处理方法（控制器实例 + 方法）
    private Map<String, Handler> handlerMap = new HashMap<>();

    // 内部类：封装控制器和方法
    private static class Handler {
        private Object controller;
        private Method method;

        public Handler(Object controller, Method method) {
            this.controller = controller;
            this.method = method;
        }

        // getter
        public Object getController() {
            return controller;
        }

        public Method getMethod() {
            return method;
        }
    }

    @Override
    public void init() throws ServletException {
        try {
            // 1. 扫描控制器所在包（自定义为 org.example.controller）
//            List<Class<?>> classes = ClassScanner.scan("com.guang.mystruts");
            List<Class<?>> classes = ClassScanner.scan("");

            //System.out.println("加载类数量4："+classes.size());
            // 2. 解析注解，建立路径映射
            for (Class<?> clazz : classes) {
                if (clazz.isAnnotationPresent(GuangAction.class)) {
                    GuangAction controllerAnn = clazz.getAnnotation(GuangAction.class);
                    String module = controllerAnn.value(); // 模块路径（如 "user"）
                    //System.out.println("初始化模块："+module);
                    // 遍历类中的方法
                    Method[] methods = clazz.getDeclaredMethods();
                    for (Method method : methods) {
                        if (method.isAnnotationPresent(GuangDo.class)) {
                            GuangDo actionAnn = method.getAnnotation(GuangDo.class);

                            // 关键：校验被@GuangDo注解的方法是否有参数
                            validateMethodParameters(method);

                            String action = actionAnn.value(); // 动作路径（如 "abc"）

                            // 完整路径：/模块/动作（如 /user/abc）
                            String url = ("/" + module + "/" + action).replaceAll("//","/");
                            handlerMap.put(url, new Handler(clazz.newInstance(), method));
                            //System.out.println("映射路径：" + url + " → " + clazz.getName() + "." + method.getName());
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new ServletException("框架初始化失败", e);
        }
    }


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");

        // 1. 获取请求路径（去除上下文路径）
        String contextPath = req.getContextPath();
        String requestURI = req.getRequestURI();
        String path = requestURI.replace(contextPath, "");
        //System.out.println("请求路径18：" + path);
        // 2. 查找对应的处理器
        Handler handler = handlerMap.get(path);
        if (handler != null) {
            //System.out.println("框架开始处理请求:"+path);
            // 3. 反射调用处理方法
            try {
                Method method = handler.getMethod();
                // 确保方法参数为 HttpServletRequest 和 HttpServletResponse
                if (method.getParameterTypes().length == 2 &&
                        method.getParameterTypes()[0] == HttpServletRequest.class &&
                        method.getParameterTypes()[1] == HttpServletResponse.class) {

                    // 调用方法，获取视图名（如 "success.jsp"）
                    String view = (String) method.invoke(handler.getController(), req, resp);

                    // 4. 转发到视图
                    if (view != null) {
                        req.getRequestDispatcher("/WEB-INF/views/" + view + ".jsp").forward(req, resp);
                    }
                } else {
                    resp.sendError(500, "方法参数必须为 HttpServletRequest 和 HttpServletResponse");
                }
            } catch (Exception e) {
                throw new ServletException("处理请求失败", e);
            }
        }
    }

    /**
     * 校验被注解的方法必须包含参数（例如必须有 HttpServletRequest 和 HttpServletResponse）
     */
    private void validateMethodParameters(Method method) throws IllegalArgumentException {
        Class<?>[] parameterTypes = method.getParameterTypes();

        // 示例1：要求方法必须有至少1个参数
        if (parameterTypes.length == 0) {
            throw new IllegalArgumentException(
                    "方法 " + method.getDeclaringClass().getName() + "." + method.getName() +
                            " 使用了@GuangDo注解，但未定义任何参数！"
            );
        }

        // 示例2：要求方法必须有且仅有 HttpServletRequest 和 HttpServletResponse 两个参数
        if (parameterTypes.length != 2 ||
                parameterTypes[0] != HttpServletRequest.class ||
                parameterTypes[1] != HttpServletResponse.class) {
            throw new IllegalArgumentException(
                    "方法 " + method.getDeclaringClass().getName() + "." + method.getName() +
                            " 使用了@GuangDo注解，但参数必须为 HttpServletRequest 和 HttpServletResponse！"
            );
        }
    }
}
