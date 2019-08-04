package com.zyy.java8;

import org.junit.Test;

import java.text.DateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Set;

public class TestLocalDateTime {

    //1、LocalDate LocalTime LocalDateTime
    //2、Instant 时间戳
    //3、Duration,Period:两个时间之间的操作
    //TemporalAdjuster :时间检验器
    //日期格式化
    //DateTimeFormatter:格式化时间
    //ZoneDate、ZoneTime、ZoneDateTime

    @Test
    public void test7(){
        Set<String> availableZoneIds = ZoneId.getAvailableZoneIds();
        availableZoneIds.forEach(System.out::println);
    }
    @Test
    public void test6(){
        DateTimeFormatter dtf = DateTimeFormatter.ISO_DATE;
        LocalDateTime ldt=LocalDateTime.now();
        String format = ldt.format(dtf);
        System.out.println(format);

        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String format1 = ldt.format(dtf2);
        System.out.println(format1);

        LocalDateTime localDateTime = LocalDateTime.parse(format1, dtf2);
        System.out.println(localDateTime);
    }

    @Test
    public void test5(){
        LocalDateTime ldt=LocalDateTime.now();
        LocalDateTime with = ldt.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
        System.out.println(with);

        //自定义
        LocalDateTime ldt3 = ldt.with((l) -> {
            LocalDateTime ldt2 = (LocalDateTime) l;
            if (DayOfWeek.MONDAY.equals(ldt2.getDayOfWeek())) {
                return ldt2.plusDays(7);
            } else return ldt2.plusDays(3);
        });
        System.out.println(ldt3);
    }

    @Test
    public void  test4(){
         LocalDate ld1=LocalDate.now();
         LocalDate ld2=LocalDate.of(2015,9,1);
         Period pd=Period.between(ld1,ld2);
         System.out.println(pd);
    }
    @Test
    public void test3() {
        Instant ins1 = Instant.now();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Instant ins2 = Instant.now();
        Duration between = Duration.between(ins1, ins2);
        System.out.println(between.toMillis());
    }

    @Test
    public void test2(){
        Instant ins1 = Instant.now();
        System.out.println(ins1);
        OffsetDateTime offsetDateTime = ins1.atOffset(ZoneOffset.ofHours(8));
        System.out.println(offsetDateTime);
        System.out.println(ins1.toEpochMilli());
    }

    @Test
    public void test(){
        LocalDateTime ldt=LocalDateTime.now();
        System.out.println(ldt);

        LocalDateTime ldt2 = LocalDateTime.of(2015, 10, 19, 12, 33, 44);
        System.out.println(ldt2);

        LocalDateTime ldt3 = ldt.plusYears(2);
        System.out.println(ldt3);
    }


}
