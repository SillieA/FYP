package devices;

import java.util.ArrayList;

import utils.Block;
import utils.BlockChain;
import utils.Main;
import utils.Transaction;

public class TokenFinder {
	public TokenFinder(String token){
		
	}
	//searches the blockchain for 
	public ArrayList<Transaction> findToken(String token){
		ArrayList<Transaction> txArr = new ArrayList<Transaction>();
		for(Block B : BlockChain.MainChain){
			if(B.gen.Token.equals(token)){
				txArr.add(B.gen);
			}
			for(Transaction T : B.TxList){
				if(T.Token.equals(token)){
					txArr.add(T);
				}
			}
		}
		return txArr;
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
