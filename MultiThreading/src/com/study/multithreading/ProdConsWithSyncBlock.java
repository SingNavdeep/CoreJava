package com.study.multithreading;

/**
 * Producer- consumer with sync blocks.
 * Note the usage of an arbritrary object to acquire a lock.
 * If we obtain a lock on an instance or processor, then threads accessing any of the methods is blocked.
 * We do not want a thread executing producer to block while a thread is executing consumer.
 */
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
			synchronized (lock)
			{
				//if list is not full, add element, else wait
				if(intList.size() < UPPER)
				{
					System.out.println("Adding to list: " + counter);
					intList.add(counter++);
					lock.notify();
				}
				else
				{
					System.out.println("Waiting for list to be emptied...");
					lock.wait();
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
				//lock acquired here to enable waiting.
				//else thread will continue in while loop thereby wasting CPU cycles.
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
