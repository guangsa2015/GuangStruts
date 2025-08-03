package com.guang.mystruts.framework.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface GuangAction {
    String value(); //模块路径
}
