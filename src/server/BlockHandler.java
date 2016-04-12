package server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import core.Block;
import core.BlockChain;
import core.Strings;
import core.Transaction;
import miner.Validation;

public class BlockHandler {

//	public static ArrayList<Block> altChain;
	public ArrayList<Block> altChain;
	
	public BlockHandler(){
		altChain = new ArrayList<Block>();
	}

	public int totalDifficulty(ArrayList<Block> blkChn){
		int difficulty = 0;
		for(Block b : blkChn){
			difficulty += b.difficulty;
		}
		return difficulty;
	}

	public int blockReceive(String s, boolean addToAltChain){//should only be true when receiving blockchain
		Transaction GenTx;
		Block b;
		ArrayList<Transaction> txArr = new ArrayList<Transaction>();
		String[] Header = null;
		String[] Meta = null;
		String[] Gen = null;
		String[] Transactions = null;
		String h;
		String m;
		String t;
		String g;
		int headStart;
		int headFin;
		int metaStart;
		int metaFin;
		int genStart;
		int genFin;
		int txStart;
		int txFin;
		
//		System.out.println(String.valueOf("Importing block: " + s));
		
		//split all the sections
		headStart = s.indexOf(Strings.HeadDelim) + Strings.HeadDelim.length() - 1;
		headFin = s.indexOf(Strings.HeadDelim, headStart +1);
		metaStart = s.indexOf(Strings.MetaDelim) + Strings.MetaDelim.length() - 1;
		metaFin = s.indexOf(Strings.MetaDelim, metaStart + 1);
		genStart = s.indexOf(Strings.GenDelim) + Strings.GenDelim.length() - 1;
		genFin = s.indexOf(Strings.GenDelim, genStart + 1);
		txStart = s.indexOf(Strings.TxDelim) + Strings.TxDelim.length() - 1;
		txFin = s.indexOf(Strings.TxDelim, txStart + 1);
//		System.out.println(String.valueOf(headStart + " " + headFin));
		//trim the sections to remove spaces
		h = s.substring(headStart+2, headFin);
		m = s.substring(metaStart+2, metaFin);
		g = s.substring(genStart+2, genFin);
		t = s.substring(txStart+2, txFin);
		//print
//		System.out.println("hmgt vals");
//		System.out.println(h);
//		System.out.println(m);
//		System.out.println(g);
//		System.out.println(t);
		//split up the sections and remove the blanks
		Header = removeBlanks(h.split(" "));
		Meta = removeBlanks(m.split(" "));
		Gen = removeBlanks(g.split(" "));
		Transactions = removeBlanks(t.split(" "));
		//see if there is a correct number of transactions(should never be called)
		if(Transactions.length % 5 != 0){
			System.out.println("Error: Transactions in imported block incorrect or corrupt");
		}
		else{//create transactions
			for(int it = 0;it <= Transactions.length/5 - 1;it++){
				Transaction Tr = new Transaction(Transactions[it *5 ],Transactions[it * 5 + 1],Transactions[it*5+2],Transactions[it*5+3],Transactions[it*5+4]);
				txArr.add(Tr);
				//validate all of the transactions
//				if(Validation.checkTx(Tr) == false){
//					return -1;
//				}
			}
		}
		//create genTx
		GenTx = new Transaction(Gen);
		//create newblock
		b = new Block(Header, Meta, txArr);
		//add the genTx
		b.gen = GenTx;
		//see if block fits on the chain
		if(!BlockChain.MainChain.isEmpty()){
			if(BlockChain.MainChain.get(BlockChain.MainChain.size()-1).hashHeader.equals(b.hashPrevBlock) && addToAltChain == false){
				if(Validation.checkBlock(b)){
					System.out.println("Block added to MainChain");
					BlockChain.MainChain.add(b);
					BlockChain.saveBlockChain();
					return -1;
				}
				else{
					System.out.println("Block Validation failed!");
					return -1;
				}

			}
			else if(addToAltChain == true){//if not, add it to alt chain and return difficulty
				altChain.add(b);
				return b.difficulty;
			}
			else{
				return b.difficulty;
			}
		}
		else{
			return b.difficulty;
		}
		
	}
	


	public void printChain(){
		for(int i = 0; i < altChain.size(); i++){
			System.out.println(altChain.get(i).allValues());
		}

	}
	public boolean containsLetters(String s) {
		char[] chars = s.toCharArray();

		for (char c : chars) {
			if(Character.isLetter(c)) {
				return false;
			}
		}
		return true;
	}
	static String[] removeBlanks(String[] input){//removes all blank elements of the array
		List<String> list = new ArrayList<String>(Arrays.asList(input));
		list.removeAll(Arrays.asList("", null));
		return list.toArray(new String[list.size()]);
	}
}
