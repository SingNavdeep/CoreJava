package com.study.multithreading;

import java.util.ArrayList;
import java.util.List;

class Processor {
	int counter = 0;
	public final int LOWER = 0;
	public final int UPPER = 5;
	public Object lock = new Object();
	public List<Integer> intList = new ArrayList<>();

	public void producer() throws InterruptedException
	{
		while(true)
		{
			if (intList.size() == UPPER)
			{
				synchronized (lock)
				{
					System.out.println("Waiting for list to be emptied...");
					lock.wait();
				}
			}
			else
			{
				System.out.println("Adding..." + counter);
				synchronized (lock)
				{
					intList.add(counter++);
					lock.notify();
				}
			}
			
			Thread.sleep(500);
		}
	}

	public void consume() throws InterruptedException
	{
		while(true)
		{
			if (intList.size() == LOWER)
			{
				synchronized (lock)
				{
					System.out.println("waiting for list to be populated...");
					lock.wait();
				}
			}
			else
			{
				synchronized (lock)
				{
					System.out.println("Removing: " + intList.remove(intList.size() - 1));
					lock.notify();
				}
			}

			Thread.sleep(500);
		}
	}
}

public class ProdConsWithSyncBlock {
	public static void main(String... args) {
		Processor processor = new Processor();

		Thread t1 = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					processor.producer();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		Thread t2 = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					processor.consume();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		t1.start();
		t2.start();
	}
}
