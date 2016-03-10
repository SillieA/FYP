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
import send.Client;
import utils.Block;
import utils.BlockChain;
import utils.Strings;
import utils.Transaction;

public class BlockHandler {

//	public static ArrayList<Block> altChain;
	public static List<ArrayList<Block>> Chains;

	public BlockHandler(){
		Chains = new ArrayList<ArrayList<Block>>();
		try {
			loadChains();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Runnable r = new Runnable(){
			public void run(){
				System.out.println("BlockListener Up");
				while(true){
					try {
						Thread.sleep(60000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(!Chains.isEmpty()){
						for(int i = 0 ; i<Chains.size()-1; i++){
							ArrayList<Block> alt = Chains.get(i);
							if(totalDifficulty(alt)>totalDifficulty(BlockChain.MainChain)){
								ArrayList<Block> oldChain = new ArrayList<Block>(BlockChain.MainChain);
								Chains.add(oldChain);
								BlockChain.MainChain = new ArrayList<Block>(alt);
								
							}
						}
					}
				}
			}
		};

		Thread thr = new Thread(r);
		thr.start();
	}
	public int totalDifficulty(ArrayList<Block> blkChn){
		int difficulty = 0;
		for(Block b : blkChn){
			difficulty += b.difficulty;
		}
		return difficulty;
	}



	public ArrayList<Block> requestChain(Node p){
		Client c = new Client(p.IP,Strings.ServerPort);
		c.sendMessage("#BLR");
		while(true){

			return BlockChain.MainChain;

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

		h = s.substring(headStart+2, headFin);
		m = s.substring(metaStart+2, metaFin);
		g = s.substring(genStart+2, genFin);
		t = s.substring(txStart+2, txFin);

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
			System.out.println("Block added to MainChain");
			BlockChain.MainChain.add(b);
		}
		else{
			for(int jj = BlockChain.MainChain.size()-2;jj>0;jj--){//-2 to skip over the already checked block
				if(BlockChain.MainChain.get(jj).hashHeader.equals(b.hashPrevBlock)){
					ArrayList<Block> altChain = new ArrayList<Block>(BlockChain.MainChain.subList(0, jj));
					altChain.add(b);
					Chains.add(altChain);
					System.out.println("Block added to MainChain");
					break;

				}
				else{
					for(int ii = Chains.size()-1;ii > 0; ii-- ){
						for(int jjj = Chains.get(ii).size()-1;jjj>0;jjj--){//-2 to skip over the already checked block
							if(BlockChain.MainChain.get(jjj).hashHeader.equals(b.hashPrevBlock) && jjj != Chains.get(ii).size()-1){
								ArrayList<Block> altChain = new ArrayList<Block>(Chains.get(jjj).subList(0, jjj));
								altChain.add(b);
								Chains.add(altChain);
								System.out.println("Block added to new altChain");
								break;
							}
							else if(BlockChain.MainChain.get(jjj).hashHeader.equals(b.hashPrevBlock)){
								System.out.println(b.valuesForSending());
								Chains.get(ii).add(b);
								System.out.println("Block added to existing altChain");
								break;
							}
						}
					}
				}
			}
		}



	}
	public static void printChains(){
		if(Chains.isEmpty()) System.out.println("Chains Error");;
		for(ArrayList<Block> altChain : Chains){
			if(altChain.isEmpty()) continue;
			System.out.println(String.valueOf(altChain.size()));
			for(int i = 0; i < altChain.size()-1; i++){
				System.out.println(altChain.get(i).allValues());
			}
		}
	}
	public static void printChain(int jj){
		for(int i = 0; i < Chains.get(jj).size(); i++){
			System.out.println(Chains.get(jj).get(i).allValues());
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
	void loadChains() throws IOException{
		int z = 0;
		ArrayList<Block> altChain = null;
		while(true){//go through each file
			z++;
			BufferedReader br;
			
			try{
				br = new BufferedReader(new FileReader(Strings.FileAltChains + String.valueOf(z) + ".txt"));
			}catch(FileNotFoundException e){
				Chains.add(altChain);
				System.out.println(String.valueOf(z-1) + " altchains loaded");
				break;
			}
				String line = br.readLine();
				String[] Header;
				String[] Meta;
				String[] Gen;
				String[] Tx;
				String[] Temp;
				ArrayList<Transaction> TxList = new ArrayList<Transaction>();
				Transaction T;
				Transaction G;
				

				while (true) {
					altChain = new ArrayList<Block>();
					if(!line.startsWith("%")){
						if (line.startsWith("@HED")){
							Temp = line.split(Pattern.quote(" "));
							Header = Arrays.copyOfRange(Temp, 1, Temp.length);
							line = br.readLine();
							//						System.out.println("HED");
							if (line.startsWith("@MET")){
								Temp = line.split(Pattern.quote(" "));
								Meta = Arrays.copyOfRange(Temp, 1, Temp.length);
								line = br.readLine();
								//							System.out.println("MET");
								if(line.startsWith("@GEN")){
									Temp = line.split(Pattern.quote(" "));
									Gen = Arrays.copyOfRange(Temp, 1, Temp.length);
									G = new Transaction(Gen);
									//								System.out.println("GEN");
									line = br.readLine();
									if (line.startsWith("@TXS")){
//										System.out.println("TXS");
										TxList = new ArrayList<Transaction>();
										line = br.readLine();
										while(!line.isEmpty()){
											Tx = line.split(Pattern.quote(" "));
											T = new Transaction(Tx);
											//										System.out.println(T.values());
											TxList.add(T);
											line = br.readLine();
										}
										Block b = new Block(Header, Meta, TxList);
										b.gen = G;
										//									System.out.println("Block loaded");
										altChain.add(b);
									}
								}
							}
							else{
								line = br.readLine();
							}
						}
						else{
							line = br.readLine();
						}
					}		
					else{
						line = br.readLine();
					}
					try{
						if(line.isEmpty()){
							line = br.readLine();
							if(line.isEmpty()){
								line = br.readLine();
								if(line.isEmpty()){
									br.close();
									Chains.add(altChain);
									System.out.println("AltChain" + String.valueOf(z) + ".txt loaded");
									break;
								}
							}
						}
					}catch(NullPointerException e){
						//						printChain(z - 1);
						br.close();
						break;
					}

				}
		}
	}

	public void saveChains(){
		int f = 1;
		for(ArrayList<Block> b : Chains){
			try{
				FileWriter writer = new FileWriter(Strings.FileAltChains + String.valueOf(f) + ".txt"); 
				writer.write("%Hash of Previous Block header, Merkle Root, Block Creation Unix Time, Nonce \r\n");
				writer.write("%Hash of Current Block header, Transaction Count in Block, Difficulty of Solution, Public Key of miner \r\n");
				writer.write("%Transaction Number, From, To, Token value, Reference Transaction \r\n");
				for(int i = 0; i < b.size(); i++){
					writer.write(b.get(i).allValues());
					writer.write("\r\n");
				}
				writer.close();
			}catch(IOException ioException){
				ioException.printStackTrace();
			}
			f++;
		}

	}
}
