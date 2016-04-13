package miner;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import core.Block;
import core.BlockChain;
import core.Main;
import core.Strings;
import core.Transaction;

public class Validation {
	//validate Tx against blockchain
	//collate blockchain???
	//append the rest of the txs in the current block to the list
	//collate all txs with sender as receiver
	//check in those to see if theyve received token from end of chain
	//check forwards to see if its been sent again
	public static boolean checkTx(Transaction T){	
		String[] vals = T.valuesArr();
		if(checkSyntax(vals) && !isGenTx(vals) && verifySig(vals)){
			return true;
		}
//		System.out.println("Error: Tx False");
//		System.out.println(String.valueOf(checkSyntax(vals)));
//		System.out.println(String.valueOf(!isGenTx(vals)));
//		System.out.println(String.valueOf(verifySig(vals)));
		
		//hardwired to true for experiment
//		return false;
		return true;
	}


	//1 & 2
	private static boolean checkSyntax(String[] ref){
		if(ref.length == 5 && containsNoEmptyElements(ref) == true ){
			return true;
		}
		System.out.println(ref[0] + " has invalid syntax");
		return false;
	}

	//5
	private static boolean isGenTx(String[] T){
		if(T[1].equals(Strings.Genesis) && T[4].equals(Strings.Genesis)){
			return true;
		}
		return false;
	}
	//8 & 9
	static boolean referenceChecker(String[] ref){//returns false if another transaction has the same reference
		if(ref[4].equals(Strings.Genesis)){//for experiments, in practice this piece of code should be modified to check if this transaction is a genuine genTx
			return true;
		}
		//check the pool
		List<Transaction> unconfirmedPool = UnconfirmedTx.getCopy();
		for(Transaction T : unconfirmedPool){
			if(T.RefTx.equals(ref[4])){
				System.out.println(ref[0] + " reference already used in TxPool");
				return false;
			}	
		}
		//check the blockchain
		for(int i = BlockChain.MainChain.size()-1; i>= 0;i--){
			Block b = BlockChain.MainChain.get(i);
			for(Transaction Tx : b.TxList){
				if(Tx.RefTx.equals(ref[4])){
					System.out.println(ref[0] + " reference already used in blockchain");
					return false;

				}	
			}
		}
		//if in neither, return true
		return true;
	}
	//16
	private static boolean verifySig(String[] ref){
		try {
			boolean b = Main.keyClass.verifySig(ref[0], ref[1], ProofOfWork.sha256(ref[1] + ref[2] + ref[3] + ref[4]));
			return b;
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	//utils
	private static boolean containsNoEmptyElements(String[] ref){
		for(String s : ref){
			if(s.isEmpty()||s.equals("")||s.equals(null)){
				return false;
			}
		}
		return true;
	}
	//--------------------------CHECKS BLOCK
	public static boolean checkBlock(Block b){
		if(checkHash(b) && checkTxList(b.TxList,b.TxCount)){
			return true;
		}
//		return false;
		//again, hardwired for experiment
		return true;
	}
	//checks the Tx in the block, as well as the length mentioned in the header to see if it all matches up
	private static boolean checkTxList(ArrayList<Transaction> input, int length){
		if(input.size() != length){
			return false;
		}
		for(Transaction T : input){
			if(checkTx(T) == false){
				return false;
			}
		}
		return true;
	}
	//checks to see if nonce is correct
	public static boolean checkHash(Block b){
		String merkle;
		String merkleandPBH;
		String hash;

		merkle = Merkle.root(b.TxList);
		if(!merkle.equals(b.hashMerkleRoot)){
			System.out.println("Incorrect Merkle Root");
			return false;
		}
		merkleandPBH = ProofOfWork.sha256(b.hashMerkleRoot + b.hashPrevBlock);
		hash = ProofOfWork.sha256(merkleandPBH + b.Nonce);
		if(!hash.startsWith(Strings.Difficulty)){
			System.out.println("Incorrect Nonce");
			return false;
		}
		return true;
	}
}


