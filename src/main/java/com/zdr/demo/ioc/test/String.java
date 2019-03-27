package com.zdr.demo.ioc.test;

/**
 * @author zhangdongrun
 * @date 2019/3/8 下午6:31
 */
public class String {
    static {
        System.out.println("static run");
    }

    public static void main(String[] args) {
        while (true){
            try {
                System.out.println("1");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
