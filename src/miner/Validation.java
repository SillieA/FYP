package miner;

import utils.Block;
import utils.BlockChain;
import utils.Strings;
import utils.Transaction;

public class Validation {
	//validate Tx against blockchain
	//collate blockchain???
	//append the rest of the txs in the current block to the list
	//collate all txs with sender as receiver
	//check in those to see if theyve received token from end of chain
	//check forwards to see if its been sent again
	public static boolean checkTx(Transaction T){
		for(int i = BlockChain.MainChain.size()-1; i>= 0;i--){
			Block b = BlockChain.MainChain.get(i);
			for(Transaction Tx : b.TxList){
				//0=TxNumber, 1 = From, 2 = to, 3 = Token, 4 = RefTx
				if(T.RefTx.equals(Tx.TxNumber) || b.gen.TxNumber.equals(T.RefTx)){
					if(T.Token.equals(Tx.Token)|| T.Token.equals(b.gen.Token)){
						//continues with the rest of the program
						//						T.generateReference();
						if(referenceChecker(T.RefTx) == true){
							System.out.println("Transaction Valid!");
							return true;
						}
						else{
							System.out.println("token has already been spent!");
						}
					}
					else{
						System.out.println("Error: Tx " + T.TxNumber +  " From " + T.From + " has invalid token in reference Transaction");
						return false;
					}	
				}
				else{
					System.out.println("Error: Tx " + T.TxNumber +  " From " + T.From + " has invalid TxNumber!");
					return false;
				}
			}
		}
		System.out.println("Error: Tx " + T.TxNumber +  " From " + T.From + " has an ERROR from ValidateTx!");
		return false;
	}
	//makes sure no other transactions have referenced the transaciton
	static boolean referenceChecker(String ref){
		for(int i = BlockChain.MainChain.size()-1; i>= 0;i--){
			Block b = BlockChain.MainChain.get(i);
			for(Transaction Tx : b.TxList){
				if(Tx.RefTx.equals(ref)||b.gen.RefTx.equals(ref)){
					return false;
				}
			}
		}
		return true;

	}
	
	public static boolean checkBlock(Block b){
		String merkle;
		String merkleandPBH;
		String hash;
		for(int i = BlockChain.MainChain.size(); i>= 0;i--){
			Block blk = BlockChain.MainChain.get(i);
			if(blk.hashHeader.equals(b.hashPrevBlock)){
				merkle = Merkle.root(b.TxList);
				merkleandPBH = ProofOfWork.sha256(merkle + b.hashPrevBlock);
				hash = ProofOfWork.sha256(merkleandPBH + b.Nonce);
				if(hash.startsWith(Strings.Difficulty)){
					if(BlockChain.MainChain.indexOf(blk) == BlockChain.MainChain.size() - 1){
						return true;
					}
					else{
						
					}
				}
				else{
					System.out.println(Strings.NoteFalseBlock);
				}
				
				
				//H(Merkleroot, PBH, Nonce)
				//if true....
					//if anotherblock has the same previous block....
						//see which one has more 0's in history
						//dump all tx's back into unconfirmed pool
				//if false....
				
			}
		}
		
		
		
		
		return false;
	}
}
