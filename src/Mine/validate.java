package Mine;

import Main.Block;
import Main.BlockChain;
import Main.Strings;
import Main.Transaction;

public class validate {
//validate Tx against blockchain
	//collate blockchain???
	//append the rest of the txs in the current block to the list
	//collate all txs with sender as receiver
	//check in those to see if theyve received token from end of chain
	//check forwards to see if its been sent again
	public boolean checkTx(Transaction T){
		for(int i = BlockChain.MainChain.size(); i>= 0;i--){
			Block b = BlockChain.MainChain.get(i);
			for(Transaction Tx : b.TxList){
				//0=TxNumber, 1 = From, 2 = to, 3 = Token, 4 = RefTx
				if(T.RefTx.equals(Tx.TxNumber) || b.gen.TxNumber == T.RefTx){
					if(T.Token.equals(Tx.Token)|| T.Token.equals(b.gen.Token)){
						//continues with the rest of the program
						T.generateReference();
						return true;
					}
					else{
						System.out.println("Error: Tx " + T.TxNumber +  " From " + T.From + " has invalid token in reference Transaction");
						return false;
					}	
				}
				else{
					//					System.out.println("Error: Tx " + T.TxNumber +  " From " + T.From + " has invalid TxNumber!");
					return false;
				}
			}
		}
		System.out.println("Error: Tx " + T.TxNumber +  " From " + T.From + " has an ERROR from ValidateTx!");
		return false;
	}
	public boolean checkBlock(Block b){
		String merkle;
		String merkleandPBH;
		String hash;
		for(int i = BlockChain.MainChain.size(); i>= 0;i--){
			Block blk = BlockChain.MainChain.get(i);
			if(blk.hashHeader.equals(b.hashPrevBlock)){
				merkle = Merkle.root(b.TxList);
				merkleandPBH = findNonce.sha256(merkle + b.hashPrevBlock);
				hash = findNonce.sha256(merkleandPBH + b.Nonce);
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
