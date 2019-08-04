package com.zyy.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;

import org.junit.Test;

/**
 * 缓冲区:负责数据的存储。本质是数组，用于存储不同数据 类型的数据
 *
 * 根据数据类型不同，提供相应的缓冲区 ByteBuffer ShortBuffer IntBuffer LongBuffer FloatBuffer
 * DoubleBuffer
 *
 * 上诉缓冲区管理方式几乎一致 通过allocate获取缓冲区
 *
 * 存储数据的核心方法 put,get
 *
 * 缓冲区的四个核心属性 capacity:容量，标识最大的存储容量，不可变 limit:界限，标识缓冲区中可以操作数据的大小，limit后面的数据不可读写
 * position:标识缓冲区正在操作数据的位置
 *
 * mark 可以回到position的位置，通过reset恢复到mark的位置
 *
 * 非直接缓冲区：通过allocate方法分配缓冲区，将缓冲区 建立在JVM的内存中
 *
 * 直接缓冲区：通过allocateDirect方法，将缓冲区建立在物理内存中，加快 效率
 */
public class TestBuffer {

	@Test
	public void test3() {

		try {
			FileInputStream fis = new FileInputStream("d://user");
			FileOutputStream fos = new FileOutputStream("");
			InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(""));
		    FileReader fileReader = new FileReader("");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void test2() {

		ByteBuffer allocate = ByteBuffer.allocate(1024);
		allocate.put("abcdefg".getBytes());
		allocate.flip();
		byte b[] = new byte[7];
		allocate.get(b, 0, 2);
		allocate.mark();
		allocate.get(b, 2, 4);
		// 恢复成上一次读取的位置
		allocate.reset();
		allocate.get(b, 6, 1);
		System.out.println(new String(b));
	}

	@Test
	public void test1() {
		// 分配一个指定大小的缓冲区
		ByteBuffer buf = ByteBuffer.allocate(1024);
		System.out.println(buf.capacity());
		System.out.println(buf.limit());
		System.out.println(buf.position());
		System.out.println(buf.mark());
		System.out.println("-----------------");
		String str = "abcdef";
		buf.put(str.getBytes());
		System.out.println(buf.capacity());
		System.out.println(buf.limit());
		System.out.println(buf.position());
		System.out.println(buf.mark());
		System.out.println("-----------------");
		// 读取数据的模式
		buf.flip();
		System.out.println(buf.capacity());
		System.out.println(buf.limit());
		System.out.println(buf.position());
		System.out.println(buf.mark());

		// 读取数据
		byte[] dst = new byte[buf.limit()];
		buf.get(dst);
		System.out.println(new String(dst, 0, buf.limit()));

		System.out.println(buf.capacity());
		System.out.println(buf.limit());
		System.out.println(buf.position());
		System.out.println(buf.mark());

		// 重读
		buf.rewind();
		System.out.println(buf.capacity());
		System.out.println(buf.limit());
		System.out.println(buf.position());
		System.out.println(buf.mark());

		// 清空缓冲区,但是缓冲区的数据还在,但是处于"被遗忘状态"
		buf.clear();
		System.out.println(buf.capacity());
		System.out.println(buf.limit());
		System.out.println(buf.position());
		System.out.println(buf.mark());
		System.out.println((char) buf.get());

		System.out.println(6.9 / 0.3);
	}
}
