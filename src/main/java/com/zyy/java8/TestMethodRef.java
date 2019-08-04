package com.zyy.java8;

import org.junit.Test;

import java.util.function.BiPredicate;
import java.util.function.Consumer;

/**
 * 方法引用:若Lambda表达式已经实现，我们可以使用
 * 主要有三种格式
 * 对象::实例方法名
 * 类::静态方法名
 *
 * 类::实例方法名
 *
 *Lambda体中注意:
 * 参数方法和返回值要与函数式接口的抽象方法函数列表和返回值保持一致
 *
 *
 * 构造器引用
 *
 * ClassName::new
 */
public class TestMethodRef {

    @Test
    public void test1(){
        Consumer<String>con=(s)->System.out.println(s);
    }

    @Test
    public void test2(){
        BiPredicate<String,String>bp=(x,y)->x.equals(y);
        //只有第一个参数调用方法，第二的参数作为方法参数才可以这样做,不是静态方法
        BiPredicate<String,String>bp2=String::equals;
    }
}
