package com.zyy.reflection;

import org.junit.Test;

/**
 * 1.类加载过程
 * 程序经过javac.exe命令以后，会生成一个或多个字节码文件
 * .class.接着我们使用java.exe命令对某个字节码文件进行
 * 解释运行，相当于将某个字节码文件加载到内存中。
 * 此过程就成为类的加载。加载到内存中的类，我们jiu
 * 称为运行时类，此运行时类就作为Class的一个实例
 *
 * 2.换句话说，Class的实例就对应着一个运行时类
 * 3.加载到内存中的运行时类，会缓存一定的时间。在此时间内
 * 我们可以通过不同的方式来获取此运行时类
 */
public class reflectionTest {

    @Test
    public void test(){

    }
}
