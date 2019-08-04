package com.zyy.thread;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 解决问题三:Lock
 *
 * synchronized 和lock异同:
 *  都可以解决线程安全问题
 *
 *  lock必须手动锁定解锁，synchronized机制执行完相应的代码后
 *  自动释放同步监视器
 *
 *
 */
public class Lock {

    private ReentrantLock lock=new ReentrantLock();

}
