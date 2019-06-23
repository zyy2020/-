package com.zyy;

public interface Sorter {
    
	public <T extends Comparable<T>>void sort(T[]list);
	
}
