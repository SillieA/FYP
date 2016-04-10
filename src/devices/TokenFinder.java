package devices;

import java.util.ArrayList;

import utils.Block;
import utils.BlockChain;
import utils.Main;
import utils.Transaction;

public class TokenFinder {

	//searches the blockchain for 
	public ArrayList<Transaction> findTokens(){
		ArrayList<Transaction> ownedTxArr = new ArrayList<Transaction>();
		String pubk = Main.keyClass.returnPublicKey(Main.keyP);
		Block b;
		for(int i = BlockChain.MainChain.size() - 7; i > 0; i--){
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
}
