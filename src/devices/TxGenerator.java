package devices;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import core.Main;
import core.Strings;
import core.Transaction;
import miner.UnconfirmedTx;
import peers.Node;
import peers.Peers;
import send.BroadcastTx;

public class TxGenerator {
	
	
	ArrayList<TimeStampedToken> createdTx;
	
	public TxGenerator(final int repeats,final String tokenName, final int interval){
		
		createdTx = new ArrayList<TimeStampedToken>();
		Thread t = new Thread(){
			public void run(){
				System.out.println("TxGenerator started");
				Transaction T;
				//on start up, wait 11 seconds before starting
				try {
					Thread.sleep(11000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//create specified number of tokens
				for(int i = 0; i<repeats;i++){
					//sleep for defined interval
					try {
						Thread.sleep(interval);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					//create token with specified name, appending the iteration number
					String tkn = tokenName + String.valueOf(i);
					//if node, create token
					if(Strings.Role.equals("Node")){
						Node n = Peers.getRandomNode();
						T = new Transaction(null, Main.keyClass.returnPublicKey(Main.keyP),n.PK,tkn,Strings.Genesis);
					}
					//if miner, create orphan txs (no-one receives)
					else{
						Node n = new Node("me",Main.keyClass.returnPublicKey(Main.keyP),"Miner" );
						T = new Transaction(null, Main.keyClass.returnPublicKey(Main.keyP),n.PK,tkn,Strings.Genesis);
					}
					T.generateReference();
					//add token to created tokens list
					TimeStampedToken tok = new TimeStampedToken(T.Token);
					createdTx.add(tok);
					//broadcastTx
					new BroadcastTx(T);
					//miners add to local pool
					if(Strings.Role.equals("Miner"))UnconfirmedTx.push(T);
				}
				//wait a minute to print created tx
				try {
					Thread.sleep(60000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				txSendTime();
				TokenFinder.saveTimestamps();
			}
		};
		t.start();
	}
	//saves time Tx were sent to file
		public void txSendTime(){
			try {
				System.out.println("Saving Token Send times");
				
				FileWriter writer = new FileWriter(Strings.FileSentTokens);
				writer.write(String.valueOf(System.currentTimeMillis()) + "\r\n");
				for(TimeStampedToken t : createdTx){
					writer.write(t.ValuesForPrint() + "\r\n");
				}
				writer.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
}
