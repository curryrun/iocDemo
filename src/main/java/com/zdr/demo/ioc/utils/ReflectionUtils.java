package com.zdr.demo.ioc.utils;

import java.lang.reflect.Field;

// 通过 Java 的反射原理来完成对象的依赖注入
public class ReflectionUtils {

    public static void injectField(Field field,Object obj,Object value) throws IllegalAccessException {
        if(field != null) {
            field.setAccessible(true);
            field.set(obj, value);
        }
    }
}
