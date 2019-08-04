package com.zyy.nio;


import org.junit.Test;

import java.io.*;
import java.nio.*;
import java.nio.channels.FileChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.SQLOutput;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

/**
 * 通道:用于源节点与目标节点的连接，在Java NIO中,本身不存储数据
 * 负责缓冲区的数据传输
 *
 * 通道的主要实现类，java.nio.channel
 *
 * FileChannel,SocketChannel
 * ServerSocketChannel,DataGramChannel
 *
 * 获取通道
 * 1、针对通道的类提供getChannel
 * FileInputStream,FileOutputStream
 * RandomAccessStream
 * 网络IO:
 * Socket,ServerSocket,DatagramSocket
 *
 * 2、在JDK1.7中的NIO针对各个通道提供了静态方法open
 * Files的newByteChannel
 *
 * 通道之间的传输
 * transForm
 * transTo
 *
 * 分散读取(Scatter)与聚集(Gather)
 * 分散读取(Scattering Reads):将通道中的数据分散到多个缓冲区
 * 聚集写入(Gathering Writes):将多个缓冲区中的数据聚集到通道中
 *
 * 字符集:Charset
 * 编码:字符串转换成字节数组
 * 解码:字节数组转换成字符串
 */
public class TestChannel {

    @Test
    public void test6(){
        Charset gbk = Charset.forName("GBK");
        //编码器
        CharsetEncoder ce = gbk.newEncoder();
        //解码起
        CharsetDecoder cd = gbk.newDecoder();
        CharBuffer allocate = CharBuffer.allocate(1024);
        allocate.put("尚硅谷好压");
        allocate.flip();
        //编码
        ByteBuffer byteBuffer = null;
        try {
            byteBuffer = ce.encode(allocate);
        } catch (CharacterCodingException e) {
            e.printStackTrace();
        }

        for(int i=0;i<10;i++){
            System.out.println(byteBuffer.get());
        }
        byteBuffer.flip();
        CharBuffer decode = null;
        try {
            decode = cd.decode(byteBuffer);
        } catch (CharacterCodingException e) {
            e.printStackTrace();
        }
        System.out.println(decode.toString());
    }

    @Test
    public void test5(){

        SortedMap<String,Charset>sm=Charset.availableCharsets();
        Set<Map.Entry<String, Charset>> entries = sm.entrySet();
        for(Map.Entry<String,Charset>entry:entries){
            System.out.println(entry.getKey()+":"+entry.getValue());
        }
    }

    @Test
    public void test4(){
        RandomAccessFile rafs= null;
        FileChannel channel=null;
        try {
            rafs = new RandomAccessFile("git.txt",
                    "rw");
            //获取通道
            channel = rafs.getChannel();
            //分配指定大小的缓冲区
            ByteBuffer allocate = ByteBuffer.allocate(100);
            ByteBuffer allocate1 = ByteBuffer.allocate(1024);
            ByteBuffer[] bufs={allocate,allocate1};
            channel.read(bufs);
            for(ByteBuffer byteBuffer:bufs){
                byteBuffer.flip();
            }
            System.out.println(new String(bufs[0].array(),0,bufs[0].limit()));
            System.out.println("-------------------------------------------------");
            System.out.println(new String(bufs[1].array(),0,bufs[1].limit()));

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(channel!=null) {
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    @Test
    public void test3(){
        Instant start = Instant.now();
        FileChannel inFileChannel = null;
        FileChannel outFileChannel= null;
        try {
            inFileChannel = FileChannel.open(Paths.get("git.txt"),
                    StandardOpenOption.READ, StandardOpenOption.CREATE);
            outFileChannel = FileChannel.open(Paths.get("a.txt"),
                    StandardOpenOption.WRITE, StandardOpenOption.APPEND,
                    StandardOpenOption.CREATE);
            inFileChannel.transferTo(0,inFileChannel.size(),outFileChannel);
            Instant end=Instant.now();
            System.out.println(Duration.between(start,end).toMillis());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(outFileChannel!=null) {
                try {
                    outFileChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(inFileChannel!=null) {
                try {
                    inFileChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    //内存映射的方式完成复制
    @Test
    public void test2(){

        Instant start = Instant.now();
        FileChannel inFileChannel = null;
        FileChannel outFileChannel= null;
        try {
            inFileChannel = FileChannel.open(Paths.get("git.txt"),
                    StandardOpenOption.READ);
            outFileChannel = FileChannel.open(Paths.get("a.txt"),
                    StandardOpenOption.WRITE, StandardOpenOption.READ,
                    StandardOpenOption.CREATE);
            MappedByteBuffer byteBuffer1 = inFileChannel.map(FileChannel.MapMode.READ_ONLY,
                    0, inFileChannel.size());
            MappedByteBuffer byteBuffer2 = outFileChannel.map(FileChannel.MapMode.READ_WRITE,
                    0, inFileChannel.size());
            byte b[]=new byte[byteBuffer1.limit()];
            byteBuffer1.get(b);
            byteBuffer2.put(b);
            Instant end=Instant.now();
            System.out.println(Duration.between(start,end).toMillis());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(outFileChannel!=null) {
                try {
                    outFileChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(inFileChannel!=null) {
                try {
                    inFileChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }



    }

    @Test
    public void test1(){
        //1、利用通道完成文件的赋值
        FileInputStream fis= null;
        FileOutputStream fos=null;
        FileChannel fc1=null;
        FileChannel fc2=null;
        try {
            fis = new FileInputStream("1.jpg");
            fos=new FileOutputStream("2.jpg");
            //获取通道
            fc1=fis.getChannel();
            fc2 = fos.getChannel();

            //建立缓冲区
            ByteBuffer b=ByteBuffer.allocate(10);

            while (fc1.read(b)!=-1){
                b.flip();
                fc2.write(b);
                b.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(fc1!=null) {
                try {
                    fc1.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fc2!=null) {
                try {
                    fc2.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fos!=null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fis!=null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }




}
