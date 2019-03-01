package com.zdr.demo.ioc.bean;

import java.util.List;

/**
 * @author zhangdongrun
 * @date 2019/1/29 下午4:53
 */
public class BeanDefinition {

    private String name;

    private String className;

    private String interFaceName;

    // 构造函数的传参的列表
    private List<ConstructorArg> constructorArgs;

    // 需要注入的参数列表
    private List<PropertyArg> propertyArgs;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getInterFaceName() {
        return interFaceName;
    }

    public void setInterFaceName(String interFaceName) {
        this.interFaceName = interFaceName;
    }

    public List<ConstructorArg> getConstructorArgs() {
        return constructorArgs;
    }

    public void setConstructorArgs(List<ConstructorArg> constructorArgs) {
        this.constructorArgs = constructorArgs;
    }

    public List<PropertyArg> getPropertyArgs() {
        return propertyArgs;
    }

    public void setPropertyArgs(List<PropertyArg> propertyArgs) {
        this.propertyArgs = propertyArgs;
    }
}
