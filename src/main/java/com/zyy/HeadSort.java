package com.zyy;

public class HeadSort  implements Sorter{
	
	public <T extends Comparable<T>> void sort(T[] list) {
		
		sort2(list);
		
	}

	private <T extends Comparable<T>>void sort2(T[] list) {
	
		//初始化堆
		createHead(list,list.length-1);
		//调整堆
		T temp;
		for(int i=list.length-1;i>0;i--) {
			temp=list[i];
			list[i]=list[0];
			list[0]=temp;
			adjustHead(list, 0, i-1);
		}
	}

	private <T extends Comparable<T>> void createHead(T[] list,int length) {
		
		int n=length;
		for(int i=n/2;i>=0;i--) {
			adjustHead(list,i,n);
		}
		
	}

	/**
	 * 
	 * 从下到上
	 * @param list 排序数组
	 * @param i    数组开始下标
	 * @param n   长度
	 */
	private <T extends Comparable<T>>void adjustHead(T[] list, int i, int n) {
		T temp;
		for(int j=2*i+1;j<=n;j=2*j+1) {//以j为顶元素
			if(j<n&&list[j].compareTo(list[j+1])<0)j=j+1;
			if(list[(2*j-2)/4].compareTo(list[j])>0)break;
			temp=list[(2*j-2)/4];
			list[(2*j-2)/4]=list[j];
			list[j]=temp;
		}
		
		
	}

}
