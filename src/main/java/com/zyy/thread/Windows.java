package com.zyy.thread;
/**
 * 1.问题:买票过程中，出现了重票，错票即出现了线程安全问题
 * 2.问题出现的原因:当某个线程操作车票的过程中，尚未完成时，其他的线程
 * 参与进来，也操作车票
 * 3.如何解决:当一个线程a在操作ticket的时候，其他的线程不能参与进来
 * 直到线程a操作完成ticket时，线程才可以操作ticket。这种情况即使线程
 * a出现阻塞，也不能被改变
 *
 * 4.java中解决的办法
 *
 * 方式一:同步代码块
 * synchronized(同步监视器){
 *     需要同步的代码
 * }
 *
 * 说明:操作共享数据的代码即同步代码
 * 共享数据:多个线程共同操作的变量，比如ticket
 * 同步监视器即锁:任何一个类的对象
 * 要求:多个线程必须要共用同一把锁，类也是一个对象
 * .class也可以充当监视器，它只会加载一次
 *
 * 方式二:同步方法
 *
 * 如果操作共享数据的代码完成的声明在同一个方法中，不妨使用同步方法
 *
 * 解决了线程的安全问题,操作同步代码时，但只能有一个线程参与，相当于单线程
 *
 *
 */
public class Windows extends Thread{

    private static int ticket=1000;

    @Override
    public void run() {
        while(true){
            synchronized(Windows.class){
                if(ticket>0){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName()+":"+ticket);
                    ticket--;
                }else break;
            }

        }
    }
}

