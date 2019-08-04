package com.zyy.java8;

import org.junit.Test;

import java.util.Comparator;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * Lambda表达式基础语法，Java8引入了一个新的操作"->"该操作符箭头操作符Lambda表达式
 * 拆成两部分
 * 左侧:参数,对应抽象方法的参数
 * 右侧:实现接口的代码
 *
 * 语法一:无参数，无返回值
 * ()->System.out.println("")
 *
 * 语法二:有一个参数，并且无返回值
 * (x)->System.out.println(x)
 *
 * 语法三:只有一个参数，小括号可以不写
 *
 * 语法四:有两个以上参数，有返回值，并且方法体多行
 *
 * 语法格式五:有一条语句，return和大括号都可以不写
 *
 * 语法六:Lambda表达式的参数列表可以不写，因为JVM
 * 编译器可以通过上下文推断出数据类型
 *
 * 二、Lambda表达式需要函数式接口
 * 若接口中只有一个抽象方法时就是@FunctionInterface
 * 修饰，可以检查是否是函数式接口
 *
 */
public class TestLambda2 {
    @Test
    public void test1(){
        int num=1;//jdk 1.7之前，必须是final,现在不用手动写，但还是final
        Runnable r1=new Runnable() {
            @Override
            public void run() {
                System.out.println("hello r1,word");
            }
        };
        r1.run();
        Runnable r2=()->System.out.println("hello r2,word");
        r2.run();
    }

    @Test
    public void test2(){
        Consumer<String>consumer=(s)->System.out.println(s);
        consumer.accept("zyy");
    }

    @Test
    public void test3(){
        Comparator<Integer>com=(x,y)->{
          System.out.println("函数式接口");
          return Integer.compare(x,y);
        };
        //调用的方法要与抽象方法的参数与返回值一致,才能引用
        Comparator<String>com2= String::compareTo;
        Comparator<Integer>com3= Comparator.comparing(Integer::valueOf);
    }
}
