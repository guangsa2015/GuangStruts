package com.guang.mystruts.framework;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class ClassScanner {
    public static List<Class<?>> scan(String basePackage) throws IOException, ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        String packagePath = basePackage.replace(".", "/");
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        Enumeration<URL> resources = classLoader.getResources(packagePath);
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            if ("file".equals(resource.getProtocol())) {
                // 关键修复：对URL路径进行解码，处理空格和中文
                String decodedPath = URLDecoder.decode(resource.getFile(), StandardCharsets.UTF_8.name());
                //System.out.println("解码后："+decodedPath);
                scanDirectory(new File(decodedPath), basePackage, classes);
            }
        }
        return classes;
    }

    // 递归扫描目录
    private static void scanDirectory(File dir, String basePackage, List<Class<?>> classes) throws ClassNotFoundException {
        if (!dir.exists() || !dir.isDirectory()) return;

        File[] files = dir.listFiles(file ->
                file.isDirectory() || file.getName().endsWith(".class")
        );

        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                scanDirectory(file, basePackage + "." + file.getName(), classes);
            } else {

                String className = basePackage + "." + file.getName().substring(0, file.getName().length() - 6);
                //System.out.println("原路径:"+basePackage+"||"+className);
                if(className.startsWith(".")){
                    className = className.replaceFirst(".","");
                }
                classes.add(Class.forName(className));
            }
        }
    }
}
