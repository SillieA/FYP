package server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import peers.Node;
import send.NewClient;
import utils.Block;
import utils.BlockChain;
import utils.Strings;
import utils.Transaction;

public class BlockHandler {

//	public static ArrayList<Block> altChain;
	public ArrayList<Block> altChain;

	public int totalDifficulty(ArrayList<Block> blkChn){
		int difficulty = 0;
		for(Block b : blkChn){
			difficulty += b.difficulty;
		}
		return difficulty;
	}

	public ArrayList<Block> requestChain(Node p){
		NewClient c = new NewClient(p.IP,Strings.ServerPort);
		c.sendMessage("#BLR");
		while(true){

			return BlockChain.MainChain;

		}
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
		
		//split all the sections
		headStart = s.indexOf(Strings.HeadDelim);
		headFin = s.indexOf(Strings.HeadDelim, headStart +1);
		metaStart = s.indexOf(Strings.MetaDelim);
		metaFin = s.indexOf(Strings.MetaDelim, metaStart + 1);
		genStart = s.indexOf(Strings.GenDelim);
		genFin = s.indexOf(Strings.GenDelim, genStart + 1);
		txStart = s.indexOf(Strings.TxDelim);
		txFin = s.indexOf(Strings.TxDelim, txStart + 1);
		System.out.println(String.valueOf(headStart + " " + headFin));
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
			System.out.println("Error: " + Arrays.toString(Transactions));
		}
		else{//create transactions
			for(int it = 0;it <= Transactions.length/5;it++){
				Transaction Tr = new Transaction(Transactions[it*5],Transactions[it * 5 + 1],Transactions[it*5+2],Transactions[it*5+3],Transactions[it*5+4]);
				txArr.add(Tr);
			}
		}
		//create genTx
		GenTx = new Transaction(Gen);
		//create newblock
		b = new Block(Header, Meta, txArr);
		//add the genTx
		b.gen = GenTx;
		//see if block fits on the chain
		if(BlockChain.MainChain.get(BlockChain.MainChain.size()-1).hashHeader.equals(b.hashPrevBlock)){
			System.out.println("Block added to MainChain");
			BlockChain.MainChain.add(b);
			return -1;
		}
		else if(addToAltChain == true){//if not, add it to alt chain and return difficulty
			altChain.add(b);
			return BlockChain.chainDifficulty();
		}
		else{
			return BlockChain.chainDifficulty();
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
