package com.zdr.demo.ioc.loader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

/**
 * @author zhangdongrun
 * @date 2019/3/10 下午2:30
 */
public class MyClassLoader extends ClassLoader {

    public MyClassLoader() {

    }

    public MyClassLoader(ClassLoader parent) {
        super(parent);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        Class<?> clazz = null;
        // 委托ext classLoader加载
        ClassLoader system = getSystemClassLoader().getParent();
        try {
            clazz = system.loadClass(name);
        } catch (Exception e) {
            e.printStackTrace();
            // ignore
        }
        if (clazz != null) {
            System.out.println(system + " loadClass name:" + name);
            return clazz;
        } else {
            System.out.println("myLoader load name:" + name);
        }
        return findClass(name);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        File file = getClassFile(name);
        try {
            byte[] bytes = getClassBytes(file);
            Class<?> c = this.defineClass(name, bytes, 0, bytes.length);
            return c;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return super.findClass(name);
    }


    private File getClassFile(String name) {
        File file = new File("/Users/finup/Documents/iocDemo/target/classes/com/zdr/demo/ioc/test/String.class");
        return file;
    }

    private byte[] getClassBytes(File file) throws Exception {
        // 这里要读入.class的字节，因此要使用字节流
        FileInputStream fis = new FileInputStream(file);
        FileChannel fc = fis.getChannel();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        WritableByteChannel wbc = Channels.newChannel(baos);
        ByteBuffer by = ByteBuffer.allocate(1024);

        while (true) {
            int i = fc.read(by);
            if (i == 0 || i == -1)
                break;
            by.flip();
            wbc.write(by);
            by.clear();
        }

        fis.close();

        return baos.toByteArray();
    }

    public static void main(String[] args) throws Exception {
        // bootstrap loader使用的路径
        System.out.println("sun.boot.class.path===" + System.getProperty("sun.boot.class.path"));
        // app loader使用的路径
        System.out.println("java.class.path:====" + System.getProperty("java.class.path"));
        // ext loader使用的路径
        System.out.println("java.ext.dirs:====" + System.getProperty("java.ext.dirs"));

        System.out.println("java.security.manager:====" + System.getProperty("java.security.manager"));

        MyClassLoader mcl = new MyClassLoader();
//        MyClassLoader mcl = new MyClassLoader(ClassLoader.getSystemClassLoader().getParent());
//        MyClassLoader mcl = new MyClassLoader(MyClassLoader.class.getClassLoader());
        Class<?> c2 = mcl.loadClass("com.zdr.demo.ioc.test.String");
        System.out.println(c2 + "====" + MyClassLoader.class.getClassLoader());
        Object o2 = c2.newInstance();
        System.out.println(o2);
        System.out.println(o2.getClass().getClassLoader());

        System.out.println("sysLoader:" + ClassLoader.getSystemClassLoader());
        System.out.println("sysLoader father" + ClassLoader.getSystemClassLoader().getParent());
//        System.out.println(ClassLoader.getSystemClassLoader().getParent().getParent());

//        Class<?> c1 = Class.forName("com.zdr.demo.ioc.test.String", false, mcl);
//        Object obj = c1.newInstance();
//        System.out.println(obj);
//        System.out.println(obj.getClass().getClassLoader());
    }

}
