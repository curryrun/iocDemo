package com.zdr.demo.ioc.test.entity;

public class Mouth {

    private String name;

    public Mouth(String name) {
        this.name = name;
    }

    public void speak() {

        System.out.println("say hello world, my name is:" + name);

    }

}
