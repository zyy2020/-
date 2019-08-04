package com.zyy.thread;

public class Windows2 implements Runnable{

    private int ticket=1000;

    @Override
    public void run() {
       while(true){
           synchronized(Windows2.class){
               if(ticket>0) {
                   try {
                       Thread.sleep(10);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
                   System.out.println(Thread.currentThread().getName()+":"+ticket);
                   ticket--;
               }
               else break;
           }

       }
    }
}
