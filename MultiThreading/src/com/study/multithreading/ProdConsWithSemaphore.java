package com.study.multithreading;

/**
 * prod cons with semaphores.
 * Just for demo...not the best use case for Semaphores
 */
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

class ProcessorSemaphore
{
	private Semaphore countSemaphore = new Semaphore(5, true);
	private List<Integer> intList = new ArrayList<>();
	private int counter;
	private int UPPER = 5;
	private int LOWER = 0;
	
	public void producer() throws InterruptedException
	{
		while(true)
		{
			try
			{
				countSemaphore.acquire();
				
				if(intList.size() < UPPER)
				{
					System.out.println("Adding: " + counter);
					intList.add(counter++);
				}
				
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally
			{
				countSemaphore.release();
			}
			
			Thread.sleep(2000);
		}
	}
	
	public void consumer()
	{
		while(true)
		{
			try
			{
				countSemaphore.acquire();
				
				if(intList.size() > LOWER)
				{
					System.out.println("Retrieved: " + intList.remove(intList.size() - 1));
				}
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally
			{
				countSemaphore.release();
			}
		}
		
	}
}
public class ProdConsWithSemaphore
{
	public static void main(String... args) {
		ProcessorSemaphore processor = new ProcessorSemaphore();

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
				//try {
					processor.consumer();
				//} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				//}
			}
		});
		
		t1.start();
		t2.start();
	}
}
