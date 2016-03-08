package server;

import java.util.ArrayList;

import peers.Pair;
import send.Client;
import utils.Block;
import utils.BlockChain;
import utils.Strings;
import utils.Transaction;

public class BlockListener {

	public static ArrayList<Block> altChain;
	
	public ArrayList<Block> requestChain(Pair p){
		Client c = new Client(p.IP,Strings.ServerPort);
		c.sendMessage("#BLR");
		while(true){
			if(altChain.isEmpty()){
				
			}
			else{
				return altChain;
			}
		}
	}
	public static void blockReceive(String s){
		Transaction T = null;
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
		
		headStart = s.indexOf(Strings.HeadDelim);
		headFin = s.indexOf(Strings.HeadDelim, headStart +1);
		metaStart = s.indexOf(Strings.MetaDelim);
		metaFin = s.indexOf(Strings.MetaDelim, metaStart + 1);
		genStart = s.indexOf(Strings.GenDelim);
		genFin = s.indexOf(Strings.GenDelim, genStart + 1);
		txStart = s.indexOf(Strings.TxDelim);
		txFin = s.indexOf(Strings.TxDelim, txStart + 1);
		System.out.println(String.valueOf(headStart + " " + headFin));
		
		h = s.substring(headStart+1, headFin-1);
		m = s.substring(metaStart+1, metaFin-1);
		g = s.substring(genStart+1, genFin-1);
		t = s.substring(txStart+1, txFin-1);
		
		System.out.println("hmgt vals");
		System.out.println(h);
		System.out.println(m);
		System.out.println(g);
		System.out.println(t);
		
		Header = h.split(" ");
		Meta = m.split(" ");
		Gen = g.split(" ");
		Transactions = t.split(" ");
		
		System.out.println();
		int i = 0;
		for(String str : Meta){
			System.out.println(String.valueOf(i) + " " + str);
			i++;
		}
		//%Transaction Number, From, To, Token value, Reference Transaction 
		System.out.println();
		for(int ii = 0; i < Transactions.length;i++){
			if(Transactions[ii].equals(null)) ii++;
			String str = Transactions[ii];
			switch(ii%5){
			case 0 : if(ii!=0){
				T = new Transaction();
				T.TxNumber = str;
				System.out.println("Txnumber: " + str);
			}
			break;
			case 1 : T.From = str;
			break;
			case 2 : T.To = str;
			break;
			case 3 : T.Token = str;
			break;
			case 4 : T.RefTx = str;
			txArr.add(T);
			break;
			}
		}
		GenTx = new Transaction(Gen);
		b = new Block(Header, Meta, txArr);
		b.gen = GenTx;
		if(BlockChain.MainChain.get(BlockChain.MainChain.size()-1).hashHeader.equals(b.hashPrevBlock)){
			BlockChain.MainChain.add(b);
		}
		else{
			altChain.add(b);
			System.out.println("Block Added to AltChain");
		}
		
		
	}
	public static void printChain(){
		for(int i = 0; i < altChain.size(); i++){
			System.out.println(altChain.get(i).allValues());
		}
	}
	public static boolean containsLetters(String s) {
	    char[] chars = s.toCharArray();

	    for (char c : chars) {
	        if(Character.isLetter(c)) {
	            return false;
	        }
	    }

	    return true;
	}
}
