package miner;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import core.Strings;
import core.Transaction;

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
						} catch (InterruptedException e) {
							e.printStackTrace();
							System.out.println(Strings.ErrorThreadWait);
						}
					}
					else{//whenever there's txs in the queue...
						System.out.println(Strings.NoteTxPoolPopulated + queue.size());
						while(!queue.isEmpty()){
							//validate tx
							Transaction T = queue.poll();
							if(Validation.checkTx(T)){
								c.add(T);//add if valid, discard if not
							}
						}
					}
				}
			}
		};
		Thread thr = new Thread(r);
		thr.start();
	}
	//add to queue
	public static void push(Transaction T){
		queue.add(T);
	}
	public static ArrayList<Transaction> getCopy(){
		return new ArrayList<Transaction>(queue);
	}
}