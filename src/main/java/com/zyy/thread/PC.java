package com.zyy.thread;

public class PC {

    public static void main(String[] args) {

        Clerk clerk=new Clerk();

        Thread t1=new Thread(new Producer(clerk));
        Thread t2=new Thread(new Consumer(clerk));

        t1.setName("生产者");
        t2.setName("消费者");

        t1.start();
        t2.start();
    }

}

class Clerk{

    private int productCount=0;

    synchronized void  produce(){

        if(productCount<20){
            productCount++;
            System.out.println(Thread.currentThread().getName()+":" + productCount);
            notify();
        }else{
            //生产结束后,wait()
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    synchronized void shell() {
        if(productCount>0){

            System.out.println(Thread.currentThread().getName()+":" + productCount);
            productCount--;
            //可以生产产品了
            notify();
        }else {
            //产品没有了，等一下
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}

class Producer implements Runnable{

    private Clerk clerk;

    public Producer(Clerk clerk) {
        this.clerk = clerk;
    }

    @Override
    public void run() {
        System.out.println("开始生产产品了.............................");

        while (true){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            clerk.produce();
        }

    }
}


class Consumer implements Runnable{

    private Clerk clerk;

    public Consumer(Clerk clerk) {
        this.clerk = clerk;
    }

    @Override
    public void run() {
        System.out.println("开始消费产品了.............................");
        while (true){
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            clerk.shell();
        }

    }
}