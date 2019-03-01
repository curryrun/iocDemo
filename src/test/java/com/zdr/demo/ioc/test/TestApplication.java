package com.zdr.demo.ioc.test;

import com.zdr.demo.ioc.core.JsonApplicationContext;
import com.zdr.demo.ioc.test.entity.Robot;

/**
 * @author zhangdongrun
 * @date 2019/3/1 下午5:51
 */
public class TestApplication {

    public static void main(String[] args) throws Exception {
        JsonApplicationContext context = new JsonApplicationContext("application.json");
        context.init();
        Robot myRobot = (Robot) context.getBean("robot");
        myRobot.show();
    }

}
