package devices;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import miner.UnconfirmedTx;
import send.BroadcastTx;
import utils.BlockChain;
import utils.Main;
import utils.Strings;
import utils.Transaction;

public class Device1 {
	
	ArrayList<String[]> receivedTx;
	
	public Device1(){
		Thread t = new Thread(){
			public void run(){
				System.out.println("Device 1 started");
				for(int i = 0; i<10000;i++){
//					try {
//						Thread.sleep(150);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
					String tkn = "DeviceOne" + String.valueOf(i);
					Transaction T = new Transaction(null, Main.keyClass.returnPublicKey(Main.keyP),"0MIIBtzCCASwGByqGSM44BAEwggEfAoGBAP1%2FU4EddRIpUt9KnC7s5Of2EbdSPO9EAMMeP4C2USZpRV1AIlH7WT2NWPq%2FxfW6MPbLm1Vs14E7gB00b%2FJmYLdrmVClpJ%2Bf6AR7ECLCT7up1%2F63xhv4O1fnxqimFQ8E%2B4P208UewwI1VBNaFpEy9nXzrith1yrv8iIDGZ3RSAHHAhUAl2BQjxUjC8yykrmCouuEC%2FBYHPUCgYEA9%2BGghdabPd7LvKtcNrhXuXmUr7v6OuqC%2BVdMCz0HgmdRWVeOutRZT%2BZxBxCBgLRJFnEj6EwoFhO3zwkyjMim4TwWeotUfI0o4KOuHiuzpnWRbqN%2FC%2FohNWLx%2B2J6ASQ7zKTxvqhRkImog9%2FhWuWfBpKLZl6Ae1UlZAFMO%2F7PSSoDgYQAAoGAOpQFLirWwZgm0LyI5ptp9celwBIKvjB%2BhMUopvM4B4RsMr5Sw41sHoKsE4peQhqoiloowpJ2WCnAxBrxDOlTMWvv8aYsFISU8b%2BqiTfJc0M0BlyoUMl61jS6tLtO7RBnqNgy4bFPlniWQ1T77DXgfnldzbbCO9wDl4eeuowoVrM%3D",tkn,Strings.Genesis);
					T.generateReference();
					new BroadcastTx(T);
					if(Strings.Role.equals("Miner"))UnconfirmedTx.push(T);
					
				}
			}
		};
		t.start();
		tokenProcess();
	}
	public void tokenProcess(){
		final TokenFinder tf = new TokenFinder();
		Thread t = new Thread(){
			public void run(){
				int chainLength = 0;
				ArrayList<Transaction> ownedTx;
				while(true){
					if(BlockChain.MainChain.size() != chainLength){
						chainLength = BlockChain.MainChain.size();
						ownedTx = new ArrayList<Transaction>(tf.findTokens());
						timestamp(ownedTx);
					}
					else{
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		};
		t.start();
	}
	public void saveTimestamps(){
		try {
			FileWriter writer = new FileWriter(Strings.FileTimeStamp, true);
			for(String[] s : receivedTx){
				writer.write(s[0] + " " + s[1] + "\r\n");
			}
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public void timestamp(ArrayList<Transaction> txarr ){
		String[] timeToken = new String[2];
		for(Transaction T : txarr){
			if(notAlreadyReceived(T.Token)){
				timeToken[1] = String.valueOf(System.currentTimeMillis());
				timeToken[0] = T.Token;
			}
		}
	}
	public boolean notAlreadyReceived(String s){
		for(String[] str : receivedTx){
			if(str[0].equals(s)){
				return false;
			}
		}
		return true;
	}


}
