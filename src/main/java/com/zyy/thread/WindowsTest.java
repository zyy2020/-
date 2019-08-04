package com.zyy.thread;

import org.junit.Test;

public class WindowsTest {

    @Test
    public void threadTest() {
        Windows w1=new Windows();
        Windows w2=new Windows();
        Windows w3=new Windows();

        w1.setName("窗口一");
        w2.setName("窗口二");
        w3.setName("窗口三");
        w1.start();
        w2.start();
        w3.start();
        System.out.println("线程结束");
    }

    public static void main(String[] args) {
        Windows2 w1 = new Windows2();

        Thread t1 = new Thread(w1);
        Thread t2 = new Thread(w1);
        Thread t3 = new Thread(w1);

        t1.setName("窗口一");
        t2.setName("窗口二");
        t3.setName("窗口三");

        t1.start();
        t2.start();
        t3.start();
    }
}
