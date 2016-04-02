package miner;

import java.util.ArrayList;
import java.util.PriorityQueue;

import utils.Strings;
import utils.Transaction;

public class UnconfirmedTx {
	//whenever a broadcast is received, add it to the queue
	
	private static PriorityQueue<Transaction> queue;
	public Runnable r;

	public UnconfirmedTx() 
	{

		queue = new PriorityQueue<Transaction>();
		BlockBuilder c = new BlockBuilder();
		
		r = new Runnable(){
			public void run(){
				System.out.println(Strings.NoteUnconfirmedTxUp);
				while(true){
					if(queue.isEmpty()){
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							e.printStackTrace();
							System.out.println(Strings.ErrorThreadWait);
						}
					}
					else{
						System.out.println(Strings.NoteTxPoolPopulated + queue.size());
						while(!queue.isEmpty()){
							c.add(queue.poll());
						}
					}
				}
			}
		};
		Thread thr = new Thread(r);
		thr.start();
	}
	public static void push(Transaction T){
		queue.add(T);
	}
}