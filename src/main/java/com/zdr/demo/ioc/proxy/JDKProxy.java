package com.zdr.demo.ioc.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author zhangdongrun
 * @date 2019/3/12 下午4:53
 */
public class JDKProxy implements InvocationHandler {

    Object obj;

    public Object bind(Object obj) {
        this.obj = obj;
        // 把 1被代理的对象的类加载器 2被代理的对象 3自己 传进去, 会返回一个被InvocationHandler代理的类
        return Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj
                .getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        System.out.println("I'm proxy!");
        Object res = method.invoke(obj, args);
        return res;
    }

    public static void main1(String[] args) {
        JDKProxy proxy = new JDKProxy();
        ProgrammerImpl programmerImpl = new ProgrammerImpl();
        Programmer programmer = (Programmer) proxy.bind(programmerImpl);
        programmer.say();
    }

    public static int test(){
        int count = 0;
        try {
            return 0;
        }catch (Throwable e){
            e.printStackTrace();
        }finally {
            ++count;
            return count;
        }
    }

    public static void main(String[] args) {
        System.out.println(test());
    }

}
