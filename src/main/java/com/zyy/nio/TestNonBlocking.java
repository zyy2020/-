package com.zyy.nio;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

public class TestNonBlocking {

    @Test
    public void client() {

        SocketChannel sc = null;
        try {
            sc = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));
            //转换成非阻塞
            sc.configureBlocking(false);
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            Scanner scanner = new Scanner(System.in);
           /* buffer.put((new Date().toString() + "\n" + "hello,你好").getBytes());
            buffer.flip();
            sc.write(buffer);
            buffer.clear();*/

            String str = scanner.next();
            buffer.put((new Date().toString() + "\n" + str).getBytes());
            buffer.flip();
            sc.write(buffer);
            buffer.clear();
            /*while (scanner.hasNext()){
                String str = scanner.next();
                buffer.put((new Date().toString() + "\n" + str).getBytes());
                buffer.flip();
                sc.write(buffer);
                buffer.clear();
            }*/
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(sc!=null) {
                try {
                    sc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void server() throws IOException {

        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ssc.bind(new InetSocketAddress(9898));
        Selector selector = Selector.open();
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        //轮询获取已经准备就绪的事件
        while(selector.select()>0){
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while(iterator.hasNext()){
                SelectionKey key = iterator.next();
                if(key.isAcceptable()){
                    //若接收就绪，获取客户端连接
                    SocketChannel sc = ssc.accept();
                    //切换为非阻塞状态
                    sc.configureBlocking(false);
                    //将该通道注册到选择器上
                    sc.register(selector,SelectionKey.OP_READ);
                }else if(key.isReadable()){
                    SocketChannel channel = (SocketChannel)key.channel();
                    int len=0;
                    //读取数据
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    while((len=channel.read(buffer))>1){
                        buffer.flip();
                        System.out.println(new String(buffer.array(),0,len));
                        buffer.clear();
                    }
                }
                iterator.remove();
            }
        }
    }
}
