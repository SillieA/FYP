package utils;
import java.util.ArrayList;

import miner.UnconfirmedTx;
import miner.ProofOfWork;

public class Block {
	
	//these are the parameters to be stored in the header of a block
	public String hashPrevBlock = "";
	public String hashMerkleRoot = "";
	public long time;
	public int Nonce;
	//txlist
	public ArrayList<Transaction> TxList;
	//metadata
	public String hashHeader;
	public int TxCount;
	public int difficulty;
	public String key;
	public Transaction gen;
	
	public Block(String[] Header, String[] Meta, ArrayList<Transaction> Tx){
		//for the header
		hashPrevBlock = Header[0];
		hashMerkleRoot = Header[1];
		System.out.println(String.valueOf(Header[2]));
		time = Long.valueOf(Header[2]);
		Nonce = Integer.valueOf(Header[3]);
		//for the txs
		TxList = new ArrayList<Transaction>(Tx);
		//for the metadata
		hashHeader = Meta[0];
		TxCount = Integer.valueOf(Meta[1]);
		difficulty = Integer.valueOf(Meta[2]);
		key = Meta[3];
	}
	
	public Block(ArrayList<Transaction> Tx, String hashMerkleRoot, int Nonce, String hashPrevBlock,int difficulty){
		//set all the values for the header
		this.hashPrevBlock = hashPrevBlock;
		this.hashMerkleRoot = hashMerkleRoot;
		this.time = System.currentTimeMillis();
		this.Nonce = Nonce;
		//txlist
		this.TxList = Tx;
		//metadata
		hashHeader = ProofOfWork.sha256(headerValues());
		this.TxCount = TxList.size();
		this.difficulty = difficulty;
		key = Main.keyClass.returnPublicKey(Main.keyP);
		rewardTx(Strings.rewards());
	}
	//creates reward tokens for miner
	public void rewardTx(String token){
		//number
		//from
		//to
		//token
		//reference
		gen = new Transaction("", Strings.Genesis,this.key,token, hashHeader);
		gen.generateReference();
	}
	//returns all of the values in theory
	public String allValues(){
		String Header = "\r\n@HED " +  headerValues();//returns the values of the header
		String Meta = "\r\n@MET " + metaValues();
		String Gen = "\r\n@GEN " + generatedTxValues();
		String Transactions = "\r\n@TXS\r\n" + txValues(); 
		
		
		return Header + Meta + Gen + Transactions ;
	}
	public String valuesForSending(){
		String Header = " + " +  headerValues() +  " + ";//returns the values of the header
		String Meta = " - " + metaValues() + " - ";
		String Gen = " * " + generatedTxValues() + " * ";
		String Transactions = " ~ " + txValues() + " ~ ";
		
		return Header + Meta + Gen + Transactions;
	}
	public ArrayList<Transaction> Transactions(){
		return TxList;
	}
	//add an individual transaction to this blocks list
	public void add(Transaction T){
		TxList.add(T);
	}
	//called whenever a block is deemed invalid, or race is lost.
	public void destroy(){
		for(Transaction T : TxList){
			UnconfirmedTx.push(T);
		}
		//maybe get rid of pbh?
	}
	//returns the values of the header other than the hash
	public String headerValues(){
		return hashPrevBlock + " " + hashMerkleRoot + " " + String.valueOf(time) + " " + Nonce;
	}
	//returns the meta values
	public String metaValues(){
		return hashHeader + " " + String.valueOf(TxCount) + " " + String.valueOf(difficulty) + " " + key;
	}
	public String generatedTxValues(){
		String gen = this.gen.values();
		return gen;
	}
	public String txValues(){
		String ans = "";
		if(TxList==null){
			System.out.println("Empty Tx arraylist for block");
		}
		else{
			for(int i = 0; i < TxList.size();i++){
				ans += TxList.get(i).values() + "\r\n";
			}
		}
//		for(Transaction T : TxList){
//			ans += T.values() + "\r\n";
//		}
		return ans;
	}
	public String txValuesNoNewLine(){
		String ans = "";
		if(TxList==null){
			System.out.println("Empty Tx arraylist for block");
		}
		else{
			for(int i = 0; i < TxList.size();i++){
				ans += TxList.get(i).values() + " ";
			}
		}
//		for(Transaction T : TxList){
//			ans += T.values() + "\r\n";
//		}
		return ans;
	}
	//returns the hash of the header
	public String headerHash(){
		return hashHeader;
	}	
}
