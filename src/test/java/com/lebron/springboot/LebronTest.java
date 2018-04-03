package com.lebron.springboot;

import java.util.concurrent.atomic.AtomicInteger;

public class LebronTest {
	private static AtomicInteger count = new AtomicInteger(0);
	public static void main(String[] args) {
		int number = count.getAndAdd(1);
		System.out.println(number);
		System.out.println(count);
	}

}
