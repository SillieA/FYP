package server;

import java.util.ArrayList;

import peers.Pair;
import send.Client;
import utils.Block;
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
		Block b;
		ArrayList<Transaction> txArr = new ArrayList<Transaction>();
		String[] Header = null;
		String[] Meta = null;
		String[] Transactions = null;
		String h;
		String m;
		String t;
		int headStart;
		int headFin;
		int metaStart;
		int metaFin;
		int txStart;
		int txFin;
		
		headStart = s.indexOf("+");
		headFin = s.indexOf("+", headStart +1);
		metaStart = s.indexOf("*");
		metaFin = s.indexOf("-", metaStart + 1);
		txStart = s.indexOf("~");
		txFin = s.indexOf("~", txStart + 1);
		System.out.println(String.valueOf(headStart + " " + headFin));
		
		h = s.substring(headStart+1, headFin-1);
		m = s.substring(metaStart+1, metaFin-1);
		t = s.substring(txStart+1, txFin-1);
		
		System.out.println("hmt vals");
		System.out.println(h);
		System.out.println(m);
		System.out.println(t);
		
		Header = h.split(" ");
		Meta = m.split(" ");
		Transactions = t.split(" ");
		
		for(int i = 0;i<Header.length;i++){
			
		}
		System.out.println();
		int i = 0;
		for(String str : Meta){
			System.out.println(String.valueOf(i) + " " + str);
			i++;
		}
		//%Transaction Number, From, To, Token value, Reference Transaction 
		System.out.println();
		i = 0;
		for(String str : Transactions){
			switch(i%5){
			case 0 : if(i!=0){
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
			i++;
		}
		b = new Block(Transactions, Transactions, null);
		altChain.add(b);
		
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
