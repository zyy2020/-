package com.zyy.java8;

import org.junit.Test;

import java.awt.font.OpenType;
import java.util.Optional;

/**
 * optional常用方法
 */
public class TestOptional {

    @Test
    public void test1(){
        Optional<Employee> employee = Optional.of(new Employee());
        Employee employee1 = employee.get();
        System.out.println(employee1);
    }

    @Test
    public void test2(){
        Optional<Employee> empty = Optional.empty();
        if(empty.isPresent()){
            Employee employee = empty.get();
            System.out.println(employee);
        }

    }

    @Test
    public void test3(){
        Optional<Employee> o = Optional.ofNullable(new Employee());
        if(o.isPresent()){
            System.out.println(o.get());
        }
    }

}
