package miner;

import java.util.ArrayList;

import utils.Strings;
import utils.Transaction;

public class UnconfirmedTx {
	//whenever a broadcast is received, add it to the list
	// some code from http://stackoverflow.com/questions/16240014/stack-array-using-pop-and-push
	private static ArrayList<Transaction> stack;
	public Runnable r;

	public UnconfirmedTx() 
	{
		stack = new ArrayList<Transaction>();
		BlockBuilder c = new BlockBuilder();
		
		r = new Runnable(){
			public void run(){
				System.out.println(Strings.NoteUnconfirmedTxUp);
				while(true){
					if(UnconfirmedTx.isEmpty()){
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							e.printStackTrace();
							System.out.println(Strings.ErrorThreadWait);
						}
					}
					else{
						System.out.println(Strings.NoteTxPoolPopulated + stack.size());
						while(!UnconfirmedTx.isEmpty()){
							c.add(pop());
						}
					}
				}
			}
		};
		Thread thr = new Thread(r);
		thr.start();
	}

	public static void push(utils.Transaction i) 
	{
		stack.add(0,i);
	}

	public static Transaction pop() 
	{ 
		if(!stack.isEmpty()){
			Transaction t= stack.get(0);
			stack.remove(0);
			return t;
		} else{
			System.out.println(Strings.ErrorPop);
			return null;
		}
	}

	public static utils.Transaction peek()
	{
		if(!stack.isEmpty()){
			return stack.get(0);
		} else{
			return null;
		}
	}

	public static boolean isEmpty() 
	{
		return stack.isEmpty();
	}
}