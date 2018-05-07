package com.study.multithreading;

/**
 * producer consumer with Lock and condition.
 * Note that with Locks, a critical section has to be locked and unlocked manually, unlike sync blocks.
 * 
 * 
 */
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ProcessorLock {
	int counter = 0;
	private final int LOWER = 0;
	private final int UPPER = 5;
	private List<Integer> intList = new ArrayList<>();
	private Lock lock = new ReentrantLock();
	private Condition cond = lock.newCondition();

	public void producer() throws InterruptedException
	{
		while(true)
		{
			if (intList.size() == UPPER)
			{
				lock.lock();
				
				System.out.println("Waiting for list to be emptied...");
				//lock.wait();
				cond.await();
				
				lock.unlock();
			}
			else
			{
				System.out.println("Adding..." + counter);
				
				lock.lock();
				intList.add(counter++);
				cond.signal();
				lock.unlock();
			}
			
//			Thread.sleep(500);
			
		}
	}
	public void consume() throws InterruptedException
	{
		while(true)
		{
			if (intList.size() == LOWER)
			{
				lock.lock();
				
				System.out.println("waiting for list to be populated...");
				cond.await();
				lock.unlock();
			}
			else
			{
				lock.lock();
				
				System.out.println("Removing: " + intList.remove(intList.size() - 1));
				cond.signal();
				lock.unlock();
			}

			Thread.sleep(500);
		}
	}
}

public class ProdConsWithLocks
{
	public static void main(String... args) {
		ProcessorLock processor = new ProcessorLock();

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
