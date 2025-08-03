[作品介绍]<br/>
1、实现了类似Struts的请求路由效果，后端通过@GuangAction注解标记处理类，使用@GuangDo标记为处理函数；<br/>
2、处理完成返回视图名称，会自动到WEB-INF下面找jsp;<br/>
<br/><br/>

[使用方法]<br/>
1、添加GuangStruts-1.0.0.jar依赖包;<br/>
2、在web.xml中添加过滤器;<br/>
      \<servlet><br/>
  &nbsp;&nbsp;\<servlet-name>dispatcher\</servlet-name><br/>
  &nbsp;&nbsp;\<servlet-class>com.guang.mystruts.framework.DispatcherServlet\</servlet-class><br/>
   &nbsp;&nbsp;\<load-on-startup>1\</load-on-startup><br/>
    \</servlet><br/>
    \<servlet-mapping><br/>
    &nbsp;&nbsp;\<servlet-name>dispatcher\</servlet-name><br/>
    &nbsp;&nbsp;\<url-pattern>/\</url-pattern><br/>
    \</servlet-mapping><br/>
3、配置处理类;<br/>
    @GuangAction("/main")<br/>
	public class Controller {
		//这个方法的全路径为: http://localhost:8080/工程名/main/test<br/>
		&nbsp;&nbsp;@GuangDo("/test")<br/>
		&nbsp;&nbsp;public String  mainPage(HttpServletRequest req, HttpServletResponse resp){<br/>
			&nbsp;&nbsp;&nbsp;&nbsp;System.out.println("处理了3");<br/>
			&nbsp;&nbsp;&nbsp;&nbsp;return  "test";<br/>
		&nbsp;&nbsp;}<br/>
	}<br/>
4、在WEB-INF下面创建views/test.jsp<br/>
