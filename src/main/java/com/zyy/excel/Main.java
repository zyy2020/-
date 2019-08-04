package com.zyy.excel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
	
	public static void main(String[] args) {
		Pattern pattern = Pattern.compile("^([A-Z]{1,})");
		Matcher m = pattern.matcher("A1");
		if(m.find()) {
			System.out.println( m.group(0));
			System.out.println( m.group(1));
		}
	}

}
