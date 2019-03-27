package com.zdr.demo.ioc.proxy;

/**
 * @author zhangdongrun
 * @date 2019/3/12 下午4:24
 * 静态代理
 */
public class ProgrammerStaticProxy implements Programmer {

    private Programmer P;

    public ProgrammerStaticProxy(Programmer p) {
        P = p;
    }

    @Override
    public void say(){
        System.out.println("i am proxy");
        P.say();
    }

    public static void main(String[] args) {
        Programmer programmer = new ProgrammerImpl();
        ProgrammerStaticProxy proxy = new ProgrammerStaticProxy(programmer);
        proxy.say();
    }
}
