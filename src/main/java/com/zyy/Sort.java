package com.zyy;

public class Sort  implements Sorter{
	
	public <T extends Comparable<T>> void sort(T[] list) {
	    
		quickSort(list,0,list.length-1);
	}
	
	private <T extends Comparable<T>>void quickSort(T[] list,int low,int high ) {
		int pivot;
		if(low<high) {
		  pivot=partition(list,low,high);
		  quickSort(list, low, pivot-1);
		  quickSort(list, pivot+1, high);
		}
	}

	private <T extends Comparable<T>> int partition(T[] list, int low, int high) {
	
		int i=low;
		int j=high;
		T temp;
		while(i<j) {
			while(i<j&&list[i].compareTo(list[j])<=0)j--;
			if(i<j) {
				temp=list[i];
				list[i]=list[j];
				list[j]=temp;
				i++;
			}
			while(i<j&&list[i].compareTo(list[j])<=0) i++;
			if(i<j) {
				temp=list[i];
				list[i]=list[j];
				list[j]=temp;
				j--;
			}
		}
		
		return i;
	}
	

}
