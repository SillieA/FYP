package Main;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
		}
		else{
			Genesis.checkBlockChain();
		}
	}
	
	public static void printChain(){
		for(int i = 0; i < MainChain.size(); i++){
			System.out.println(MainChain.get(i).allValues());
		}
	}
	public static void verifyChain(){
		
	}
	public static String latestBlockHeader(){
		//will not work if there is no origin block
		Block b = MainChain.get(MainChain.size()-1);
		return b.hashHeader;
	}
	public static void saveBlockChain(){
		try{
			FileWriter writer = new FileWriter(Strings.FileBlockChain); 
			writer.write("%Hash of Previous Block header, Merkle Root, Block Creation Unix Time, Nonce \r\n");
			writer.write("%Hash of Current Block header, Transaction Count in Block, Difficulty of Solution, Public Key of miner \r\n");
			writer.write("%Transaction Number, From, To, Token value, Reference Transaction \r\n");
			for(int i = 0; i < MainChain.size(); i++){
			  writer.write(MainChain.get(i).allValues());
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
			String[] Tx;
			String[] Temp;
			ArrayList<Transaction> TxList = new ArrayList<Transaction>();
			Transaction T;
			
			while (loop) {
				if(!line.startsWith("%")){
					if (line.startsWith("@HED")){
						Temp = line.split(Pattern.quote(" "));
						Header = Arrays.copyOfRange(Temp, 1, Temp.length);
						line = br.readLine();
						if (line.startsWith("@MET")){
							Temp = line.split(Pattern.quote(" "));
							Meta = Arrays.copyOfRange(Temp, 1, Temp.length);
							line = br.readLine();
							if (line.startsWith("@TXS")){
								TxList = new ArrayList<Transaction>();
								line = br.readLine();
								while(!line.isEmpty()){
									Tx = line.split(Pattern.quote(" "));
									T = new Transaction(Tx);
									System.out.println(T.values());
									TxList.add(T);
									line = br.readLine();
								}
								Block b = new Block(Header, Meta, TxList);
//								System.out.println(b.allValues());
								MainChain.add(b);
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
					loop = false;
				}
			}
		}
	}
}