package com.zyy.nio;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 是呀NIO完成网络通信的三个核心
 * 通道
 * 缓冲区
 * java.nio.channels.Channel接口:
 * |--SelectableChannel
 *     |--SocketChannel
 *     |--ServerSocketChannel
 *     |--DatagramChannel
 * 选择器:SelectableChannel的多路复用
 * 用于监控SelectableChannel的IO状况
 */
public class TestBlocking {
    //客户端,发送数据给服务端
    @Test
    public void client(){

        //获取通道
        SocketChannel socketChannel = null;
        FileChannel channel= null;
        try {
            socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));
            //分配指定大小的的缓冲区

            ByteBuffer buffer = ByteBuffer.allocate(1024);

            channel = FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);

            while(channel.read(buffer)!=-1){
                buffer.flip();
                socketChannel.write(buffer);
                buffer.clear();
            }
            int len;
            //发送完毕
            socketChannel.shutdownOutput();
            //接收服务端的反馈
            while((len=socketChannel.read(buffer))!=-1){
                buffer.flip();
                System.out.println(new String(buffer.array(),0,len));
                buffer.clear();
            }

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
            if(socketChannel!=null) {
                try {
                    socketChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void server(){

        ServerSocketChannel ssc = null;
        FileChannel outChannel = null;
        SocketChannel accept = null;
        try {
            ssc = ServerSocketChannel.open();
            outChannel= FileChannel.open(Paths.get("6.jpg"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
            ssc.bind(new InetSocketAddress(9898));
            accept = ssc.accept();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while(accept.read(buffer)!=-1){
                buffer.flip();
                outChannel.write(buffer);
                buffer.clear();
            }
            buffer.put("数据接收完毕!".getBytes());
            buffer.flip();
            accept.write(buffer);
            accept.shutdownOutput();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(accept!=null) {
                try {
                    accept.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(outChannel!=null) {
                try {
                    outChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(ssc!=null) {
                try {
                    ssc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
