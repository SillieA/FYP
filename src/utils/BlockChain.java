package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class BlockChain {
	public static ArrayList<Block> MainChain;
	public BlockChain(){
		MainChain = new ArrayList<Block>();
	}
	public static void initialiseChain() throws IOException{
		File f = new File(Strings.FileBlockChain);
		if(f.exists() && !f.isDirectory()) { 
		    loadBlockChain();
		    System.out.println(Strings.NoteBlockChainLoaded);
		}
		else{
			GenesisBlock.checkBlockChain();
		}
	}
	public static int chainDifficulty(){
		int dif = 0;
		int temp;
		for(int i = 0; i < MainChain.size(); i++){
			temp = MainChain.get(i).difficulty;
			dif += temp;
		}
		return dif;
	}
	
	public static void printChain(){
		for(int i = 0; i < MainChain.size(); i++){
			System.out.println(MainChain.get(i).allValues());
		}
	}
	public static String chainAsString(){
		String s = "";
		for(int i = 0; i < MainChain.size(); i++){
			s += "##" + MainChain.get(i).allValues();
		}
		return s;
	}
	public static String[] getHeaders(){
		List<String> headers = new ArrayList<String>(MainChain.size());
		String[] ret;
		for(Block b : MainChain){
			headers.add(b.hashHeader);
		}
		ret = new String[headers.size()];
		ret = headers.toArray(ret);
		return ret;
	}

	public static String latestBlockHeader(){
		//will not work if there is no origin block
		Block b;
		try{
			b = MainChain.get(MainChain.size()-1);
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("blockchain not loaded, attempting to load again");
			try{
				initialiseChain();
			}catch(IOException IOE){
				System.out.println(IOE.toString());
			}
			try{
				b = MainChain.get(MainChain.size()-1);
			}catch(ArrayIndexOutOfBoundsException e1){
				return Strings.Genesis;
			}
		}
		return b.hashHeader;
	}
	public static void saveBlockChain(){
		ArrayList<Block> chainCopy = new ArrayList<Block>(MainChain);
		try{
			FileWriter writer = new FileWriter(Strings.FileBlockChain); 
			writer.write("%Hash of Previous Block header, Merkle Root, Block Creation Unix Time, Nonce \r\n");
			writer.write("%Hash of Current Block header, Transaction Count in Block, Difficulty of Solution, Public Key of miner \r\n");
			writer.write("%Transaction Number, From, To, Token value, Reference Transaction \r\n");
			for(int i = 0; i < chainCopy.size(); i++){
			  writer.write(chainCopy.get(i).allValues());
			  writer.write("\r\n");
			}
			writer.close();
			}catch(IOException ioException){
				ioException.printStackTrace();
			}
	}
	private static void loadBlockChain() throws IOException{
		try(BufferedReader br = new BufferedReader(new FileReader(Strings.FileBlockChain))) {
			String line = br.readLine();
			boolean loop = true;
			String[] Header;
			String[] Meta;
			String[] Gen;
			String[] Tx;
			String[] Temp;
			ArrayList<Transaction> TxList = new ArrayList<Transaction>();
			Transaction T;
			Transaction G;
			
			while (loop) {
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
//									System.out.println("TXS");
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
									MainChain.add(b);
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
								loop = false;
							}
						}
					}
				}catch(NullPointerException e){
//					printChain();

					loop = false;
				}
				
			}
		}
	}
}