package devices;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import utils.Block;
import utils.BlockChain;
import utils.Main;
import utils.Strings;
import utils.Transaction;

public class TokenFinder {
	
	ArrayList<Transaction> receivedTx;
	ArrayList<Long> receivedTime;
	
	public TokenFinder(){
		receivedTx = new ArrayList<Transaction>();
		receivedTime = new ArrayList<Long>();
		Thread t = new Thread(){
			public void run(){
				int chainLength = 0;//used to check whether blockchain has changed length. if it has, the program will check for tokens
				int newChainLength;
				ArrayList<Transaction> ownedTx;
				while(true){
					if(BlockChain.MainChain.size() != chainLength){
						newChainLength = BlockChain.MainChain.size();
						ownedTx = new ArrayList<Transaction>(findTokens(chainLength, newChainLength));
						timestamp(ownedTx);
						chainLength = newChainLength;
					}
					else{
						try {
							Thread.sleep(2000);
							saveTimestamps();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		};
		t.start();
	}
	
	//searches the blockchain for 
	public ArrayList<Transaction> findTokens(int start, int fin){
		ArrayList<Transaction> ownedTxArr = new ArrayList<Transaction>();
		String pubk = Main.keyClass.returnPublicKey(Main.keyP);
		Block b;
		if(BlockChain.MainChain.size() > 9){
			for(int i = fin - 7; i > start - 7; i--){
				b = BlockChain.MainChain.get(i);
				for(Transaction T : b.TxList){
					if(T.To.equals(pubk)){
						ownedTxArr.add(T);
					}
				}
				//			if(b.gen.To.equals(pubk)){
				//				ownedTxArr.add(b.gen);
				//			}
			}
			System.out.println("Found " + String.valueOf(ownedTxArr.size()) + " Tokens");
		}
		else{
			System.out.println("Blockchain too short for token search");
		}
		
		return ownedTxArr;
	}
	//returns unspent transactions from a list of transactions
	public ArrayList<Transaction> spendableTx(ArrayList<Transaction> txArr){
		ArrayList<Transaction> spendableTx = new ArrayList<Transaction>();
		for(Transaction T : txArr){
			for(Block b : BlockChain.MainChain){
				for(Transaction Tr : b.TxList){
					if(!Tr.RefTx.equals(T.TxNumber)){
						spendableTx.add(T);
					}
				}
			}
		}
		return spendableTx;
	}
	public ArrayList<Transaction> ownedTx(ArrayList<Transaction> txArr){
		ArrayList<Transaction> ownedTx = new ArrayList<Transaction>();
		for(Transaction T : txArr){
			if(T.To.equals(Main.keyClass.returnPublicKey(Main.keyP))){
				ownedTx.add(T);
			}
		}
		return ownedTx;
	}
	public void timestamp(ArrayList<Transaction> txarr ){
		String[] timeToken = new String[2];
		for(Transaction T : txarr){
				timeToken[1] = String.valueOf(System.currentTimeMillis());
				timeToken[0] = T.Token;
				receivedTx.add(T);
				receivedTime.add(System.currentTimeMillis());
			}
		
	}
	//save time txs were received
	public void saveTimestamps(){
		try {
			FileWriter writer = new FileWriter(Strings.FileTimeStamp);
			for(int i = 0; i < receivedTx.size();i++){
				writer.write(receivedTx.get(i).Token + " " + String.valueOf(receivedTime.get(i)) + "\r\n");
			}
			writer.close();
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
