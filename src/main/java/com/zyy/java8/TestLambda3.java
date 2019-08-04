package com.zyy.java8;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Java8 内置的四大核心函数式接口
 *
 * Consumer<T>:消费型接口
 * void accept(T t)
 *
 * Supplier<T>: 供给型接口
 * T get()</>
 *
 * Function<T,R>:函数式接口
 * R apply（T t）</>
 *
 * Predicate<T>:断言型接口
 * boolean test(T t)</>
 */
public class TestLambda3 {

    @Test
    public void test2(){
        List<Integer> numList = getNumList(10, () -> (int) (Math.random() * 100));
        for (Integer i:numList) {
            System.out.println(i);
        }
    }

    public List<Integer>getNumList(int num, Supplier<Integer>sup){
        List<Integer>list=new ArrayList<>();
        for (int i=0;i<num;i++){
            list.add(sup.get());
        }
        return list;
    }

    @Test
    public void test1(){
        happy(10000,(d)->System.out.println("消费"+d));
    }

    public void happy(double money,Consumer<Double>con){
        con.accept(money);
    }


}
