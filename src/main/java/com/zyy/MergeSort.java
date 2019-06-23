package com.zyy;

public class MergeSort implements Sorter {
	
	public <T extends Comparable<T>> void sort(T[] list) {
		
		T[] temp = (T[]) new Comparable[list.length];
		sort2(list,temp,0,list.length-1);
		
	}

	private <T extends Comparable<T>> void sort2(T[] list,T[] temp, int low, int high) {
		if(low==high)return;
		else {
			int mid=(low+high)/2;
		    sort2(list,temp,low,mid);
		    sort2(list,temp,mid+1,high);
		   merge(list,temp,low,mid,high);
		}
	}

	private <T extends Comparable<T>>void merge(T[] list,T[] temp, int low, int mid, int high) {
		
		int i=low;
		int j=mid+1;
		int first=low;
		while(i<=mid&&j<=high) {
			if(list[i].compareTo(list[j])<0)
				temp[first++]=list[i++];
			else
				temp[first++]=list[j++];
			
		}
		while(i<=mid)
			temp[first++]=list[i++];
		
		while(j<=high) 
			temp[first++]=list[j++];
		
		for(int n=low;n<=high;n++) {
			list[n]=temp[n];
		}
	}

}
