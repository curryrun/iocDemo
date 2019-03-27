package com.zdr.demo.ioc.test;

/**
 * @author zhangdongrun
 * @date 2019/3/17 下午2:20
 */
public class MyTest {

    public static int test(){
        int count = 0;
        try {
            return count;
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
