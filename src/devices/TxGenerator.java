package devices;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import miner.UnconfirmedTx;
import peers.Node;
import peers.Peers;
import send.BroadcastTx;
import utils.BlockChain;
import utils.Main;
import utils.Strings;
import utils.Transaction;

public class TxGenerator {
	
	
	ArrayList<String[]> createdTx;
	
	public TxGenerator(final int repeats,final String tokenName){
		
		createdTx = new ArrayList<String[]>();
		Thread t = new Thread(){
			public void run(){
				System.out.println("Device 1 started");
				try {
					Thread.sleep(11000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				for(int i = 0; i<repeats;i++){
//					try {
//						Thread.sleep(20);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
					String tkn = tokenName + String.valueOf(i);
					Node n = Peers.getRandomNode();
					Transaction T = new Transaction(null, Main.keyClass.returnPublicKey(Main.keyP),n.PK,tkn,Strings.Genesis);
					T.generateReference();
					String[] Str = new String[]{T.Token,String.valueOf(System.currentTimeMillis())};
					createdTx.add(Str);
					new BroadcastTx(T);
					if(Strings.Role.equals("Miner"))UnconfirmedTx.push(T);
				}
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				txSendTime();
			}
		};
		t.start();
	}
	//saves time Tx were sent to file
		public void txSendTime(){
			try {
				
				FileWriter writer = new FileWriter(Strings.FileSentTokens, true);
				writer.write(String.valueOf(System.currentTimeMillis()) + "\r\n");
				for(String[] s : createdTx){
					writer.write(s[0] + " " + s[1] + "\r\n");
				}
				writer.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
}
