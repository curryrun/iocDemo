package com.zdr.demo.ioc.core;

import com.zdr.demo.ioc.bean.BeanDefinition;
import com.zdr.demo.ioc.bean.ConstructorArg;
import com.zdr.demo.ioc.utils.BeanUtils;
import com.zdr.demo.ioc.utils.ClassUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import com.zdr.demo.ioc.utils.ReflectionUtils;
import com.zdr.demo.ioc.utils.ThreadPoolExecutorUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author zhangdongrun
 * @date 2019/1/29 下午5:33
 */
public class BeanFactoryImpl implements BeanFactory {

    private static final ConcurrentHashMap<String, Object> beanMap = new ConcurrentHashMap<>();

    private static final ConcurrentHashMap<String, BeanDefinition> beanDefineMap = new ConcurrentHashMap<>();

    private static final Set<String> beanNameSet = Collections.synchronizedSet(new HashSet<>());

    private final Map<String, Object> earlySingletonObjects = new HashMap<String, Object>(16);

    @Override
    public Object getBean(String name) throws Exception {
        Object bean = beanMap.get(name);
        if (null != beanMap.get(name)) {
            return bean;
        }
        bean = createBean(beanDefineMap.get(name));
        if (null != bean) {
            populatebean(bean);
            beanMap.put(name, bean);
        }

        return bean;
    }

    @Override
    public Object getNoCircleBean(String name) throws Exception {
        //查找对象是否已经实例化过
        Object bean = beanMap.get(name);
        if (bean != null) {
            return bean;
        }
        Object earlyBean = earlySingletonObjects.get(name);
        if (earlyBean != null) {
            System.out.println("循环依赖，提前返回尚未加载完成的bean:" + name);
            return earlyBean;
        }
        //如果没有实例化，那就需要调用createBean来创建对象
        BeanDefinition beanDefinition = beanDefineMap.get(name);
        bean = createBean(beanDefinition);
        if (bean != null) {
            earlySingletonObjects.put(name, bean);
            //对象创建成功以后，注入对象需要的参数
            populatebean(bean);
            //再吧对象存入Map中方便下次使用。
            beanMap.put(name, bean);
            //从早期单例Map中移除
            earlySingletonObjects.remove(name);
        }
        //结束返回
        return bean;
    }

    // BeanDefinition 是对bean做的一个定义
    // 构造一个对象
    private Object createBean(BeanDefinition beanDefinition) throws Exception {
        String className = beanDefinition.getClassName();
        // Thread.currentThread().getContextClassLoader() 获取当前线程的类加载器来加载class
        // 如果你使用Test.class.getClassLoader()，可能会导致和当前线程所运行的类加载器不一致（因为Java天生的多线程）
        Class myClass = ClassUtils.loadClass(className);
        if (myClass == null) {
            throw new Exception("can not find bean by beanName");
        }
        // 构造方法里的参数列表
        List<ConstructorArg> constructorArgs = beanDefinition.getConstructorArgs();
        if (!CollectionUtils.isEmpty(constructorArgs)) {
            List<Object> objects = new ArrayList<>();
            for (ConstructorArg constructorArg : constructorArgs) {
                if (constructorArg.getValue() != null) {
                    objects.add(constructorArg.getValue());
                } else {
                    objects.add(getBean(constructorArg.getRef()));
                }
            }
//            Class[] constructorArgTypes = objects.stream().map(it -> it.getClass()).collect(Collectors.toList()).toArray(new Class[]{});

            Class[] constructorArgTypes = new Class[objects.size()];
            for (int i = 0; i < objects.size(); ++i) {
                constructorArgTypes[i] = objects.get(i).getClass();
            }
            // 获取这个类的构造方法
            Constructor constructor = myClass.getConstructor(constructorArgTypes);
            // 通过cglib的Enhancer实例化这个对象
            return BeanUtils.instanceByCglib(myClass, constructor, objects.toArray());
        }
        // 如果构造方法没有入参
        else {
            return BeanUtils.instanceByCglib(myClass, null, null);
        }
    }

    // 将对象父类的属性进行实例化
    private void populatebean(Object bean) throws Exception {
        // bean.getClass().getSuperclass() 获取父类class
        // getDeclaredFields() getFields()只能获取此类public修饰的字段，而getDeclaredFields()获取此类所有的字段，不管是私有还是公有
        Field[] fields = bean.getClass().getSuperclass().getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                String beanName = field.getName();
                // 首字母小写
                beanName = StringUtils.uncapitalize(beanName);
                if (beanNameSet.contains(field.getName())) {
                    Object fieldBean = getBean(beanName);
                    if (fieldBean != null) {
                        ReflectionUtils.injectField(field, bean, fieldBean);
                    }
                }
            }
        }
    }

    protected void registerBean(String name, BeanDefinition bd) {
        beanDefineMap.put(name, bd);
        beanNameSet.add(name);
    }

    public static void main(String[] args) throws Exception {
        ConcurrentHashMap c = new ConcurrentHashMap();
//        Runnable r = () -> 42;
        ThreadPoolExecutorUtil.getInstance().getThreadPoolExecutorService().execute(() -> System.out.println("1123"));
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
        list.stream().map((x) -> x * x).forEach((x) -> System.out.println(x));
        list.stream().map((x) -> x * x).forEach(System.out::println);
        String s = new String("hello123");
        s.hashCode();
        ReentrantLock lock = new ReentrantLock(true);
        lock.lock();
        lock.unlock();
        lock.tryLock(1L, TimeUnit.MILLISECONDS);
        lock.unlock();

        CountDownLatch latch = new CountDownLatch(5);
        latch.countDown();
        latch.await();

        CyclicBarrier cyclicBarrier = new CyclicBarrier(5);
        cyclicBarrier.await();
    }

}
