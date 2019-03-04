package com.zdr.demo.ioc.core;

public interface BeanFactory {

    Object getBean(String name) throws Exception;

    Object getNoCircleBean(String name) throws Exception;

}
