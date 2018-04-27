package com.study.multithreading;

/**
 * Class demonstrates usage of java.util.concurrent.locks.Lock to create sync blocks
 */
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockDemo
{
	private static int counter;
	private static Lock lock = new ReentrantLock();
	
	public static void increment()
	{
		for(int i = 0; i < 10000; i++)
		{
			//critical section inside lock and unlock
			//we dont want 2 threads to simultaneously access counter
			lock.lock();
			counter++;
			lock.unlock();
		}
			
	}
	
	public static void main(String ... args)
	{
		//Lock lock = new Lock();
		
		Thread t1 = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				increment();
			}
		});
		
		Thread t2 = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				increment();
			}
		});
		
		t1.start();
		t2.start();
		
		try
		{
			t1.join();
			t2.join();
			
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		System.out.println("Counter: " + counter);
	}
}