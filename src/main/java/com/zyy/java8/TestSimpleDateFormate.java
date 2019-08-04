package com.zyy.java8;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

public class TestSimpleDateFormate {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");

        ExecutorService pool= Executors.newFixedThreadPool(10);
        List<Future<Date>>results=new ArrayList<>();
        for(int i=0;i<10;i++){
            results.add(pool.submit(()->{
                Date convert=null;
                try {
                    convert= DateFormatThreadLocal.convert("20151228");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return convert;
            }));
        }

        for (Future<Date>future:results){
            System.out.println(future.get());
        }
        pool.shutdown();
    }
}
