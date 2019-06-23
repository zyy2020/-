package com.zyy;

import org.junit.Test;

public class SortTest {
	
	@Test
	public  void quickSort() {
		
		Integer list[]= {3,7,2,11,8,9,17,6};
		Sort sort=new Sort();
		sort.sort(list);
		for (Integer integer : list) {
			System.out.println(integer);
		}
	}
	
	@Test
	public void mergeSort() {
		Integer list[]= {3,7,2,11,8,9,17,6};
		MergeSort sort=new MergeSort();
		sort.sort(list);
		for (Integer integer : list) {
			System.out.println(integer);
		}
	}
	
	@Test
	public void headSort() {
		Integer list[]= {3,7,2,11,8,9,17,6};
		HeadSort sort=new HeadSort();
		sort.sort(list);
		for (Integer integer : list) {
			System.out.println(integer);
		}
	}
	
	

}
