package devices;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import core.Block;
import core.BlockChain;
import core.Main;
import core.Strings;
import core.Transaction;

public class TokenFinder {
	

	static ArrayList<TimeStampedToken> receivedTokens;
	
	public TokenFinder(){

		receivedTokens = new ArrayList<TimeStampedToken>();
		Thread t = new Thread(){
			public void run(){
				int chainLength = 0;//used to check whether blockchain has changed length. if it has, the program will check for tokens
				int newChainLength;
				//stores discoveredtx
				ArrayList<Transaction> ownedTx;
				while(true){
					if(BlockChain.MainChain.size() != chainLength){
						newChainLength = BlockChain.MainChain.size();
						ownedTx = new ArrayList<Transaction>(findTokens());
						timestamp(ownedTx);
						chainLength = newChainLength;
					}
					else{
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		};
		t.start();
	}
	
	//searches the blockchain for received tokens
	public ArrayList<Transaction> findTokens(){
		//create return array
		ArrayList<Transaction> ownedTxArr = new ArrayList<Transaction>();
		//return public key;
		String pubk = Main.keyClass.returnPublicKey(Main.keyP);
		Block b;
		//if blockchain is big enough to be searched
		int chainLength = BlockChain.MainChain.size();
//		System.out.println(String.valueOf(chainLength));
		if(chainLength > 7){
			for(int i = 0; i < chainLength - 7; i++){
				b = BlockChain.MainChain.get(i);
				for(Transaction T : b.TxList){
					if(T.To.equals(pubk)){
						ownedTxArr.add(T);
					}

				}
				if(b.gen.To.equals(pubk)){
					ownedTxArr.add(b.gen);
				}
			}
			System.out.println("Found " + String.valueOf(ownedTxArr.size()) + " Tokens");
		}
		else{
			System.out.println("Blockchain too short for token search");
		}
		
		return ownedTxArr;
	}
	//returns unspent transactions from a list of transactions. currently UNUSED, would be used in real life implementation.
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

	public void timestamp(ArrayList<Transaction> txarr ){
		TimeStampedToken timedToken;
		String token;
		for(Transaction T : txarr){
			token = T.Token;
			if(stampCheck(token)){
				timedToken = new TimeStampedToken(token);
				receivedTokens.add(timedToken);
			}
		}
	}	
	//checks to see if token has already been stamped. returns false if it has
	public boolean stampCheck(String tok){
		for(TimeStampedToken t : receivedTokens){
			if(t.token.equals(tok)){
				return false;
			}
		}
		return true;
	}
	//save time txs were received
	public static void saveTimestamps(){
		try {
			System.out.println("Saving received token times");
			FileWriter writer = new FileWriter(Strings.FileTimeStamp);
			for(int i = 0; i < receivedTokens.size();i++){
				writer.write(receivedTokens.get(i).ValuesForPrint() + "\r\n");
			}
			writer.close();
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
