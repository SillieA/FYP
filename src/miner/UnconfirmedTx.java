package miner;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import utils.BlockChain;
import utils.Strings;
import utils.Transaction;

public class UnconfirmedTx {
	//whenever a broadcast is received, add it to the queue
	
	private static Queue<Transaction> queue;
	public Runnable r;

	public UnconfirmedTx() 
	{

		queue = new LinkedList<Transaction>();
		final BlockBuilder c = new BlockBuilder();
		
		r = new Runnable(){
			public void run(){
				System.out.println(Strings.NoteUnconfirmedTxUp);
				while(true){
					if(queue.isEmpty()){
						try {
							Thread.sleep(3000);
							BlockChain.saveBlockChain();
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
	public static ArrayList<Transaction> getCopy(){
		return new ArrayList<Transaction>(queue);
	}
}