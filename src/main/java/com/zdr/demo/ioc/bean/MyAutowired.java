package com.zdr.demo.ioc.bean;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author:liuchuandong
 * @date:2018/10/11 上午11:30
 * @name:OAuth2Check
 * @desc:
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyAutowired {
}
