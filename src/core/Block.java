package core;
import java.util.ArrayList;
import java.util.List;

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
//		System.out.println(String.valueOf(Header[2]));
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
	//this method is used by the table GUI class
	public String[][] valuesArr(){
		List<String> values = new ArrayList<String>();
		List<String> type = new ArrayList<String>();
		String[] txVals;
		String[][] ret;
		values.add(this.hashPrevBlock);
		type.add("PBH");
		values.add(this.hashMerkleRoot);
		type.add("MRH");
		values.add(String.valueOf(this.time));
		type.add("TIME");
		values.add(String.valueOf(this.Nonce));
		type.add("NONCE");
		values.add(this.hashHeader);
		type.add("HEAD");
		values.add(String.valueOf(this.TxCount));
		type.add("TXCNT");
		values.add(String.valueOf(this.difficulty));
		type.add("DIF");
		values.add(ProofOfWork.sha256(this.key));
		type.add("KEY");
		for(Transaction T : this.TxList){
			txVals = T.valuesArr();
			int i = 0;
			for(String s : txVals){
				values.add(s);
				type.add("Tx" + String.valueOf(i));
				i++;
			}
			i = 0;
		}
		int size = type.size();
		ret = new String[size][2];
		for(int j = 0; j < size; j++){
			ret[j][0] = type.get(j);
			ret[j][1] = values.get(j);
		}
		return ret;
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
	//returns all of the values of the block for saving to txt file
	public String allValues(){
		String Header = "\r\n@HED " +  headerValues();//returns the values of the header
		String Meta = "\r\n@MET " + metaValues();
		String Gen = "\r\n@GEN " + generatedTxValues();
		String Transactions = "\r\n@TXS\r\n" + txValues(); 
		
		
		return Header + Meta + Gen + Transactions ;
	}
	//values for sending block
	public String valuesDelimited(){
		String Header = " " + Strings.HeadDelim + " " + headerValues() +  " " + Strings.HeadDelim + " ";//returns the values of the header
		String Meta = " " + Strings.MetaDelim + " " + metaValues() + " " + Strings.MetaDelim + " ";
		String Gen = " " + Strings.GenDelim + " " + generatedTxValues() + " " + Strings.GenDelim + " ";
		String Transactions = " " + Strings.TxDelim + " " + txValuesNoNewLine() + " " + Strings.TxDelim + " ";
		
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
	}
	//returns the values of the header other than the hash
	public String headerValues(){
		return hashPrevBlock + " " + hashMerkleRoot + " " + String.valueOf(time) + " " + Nonce;
	}
	//returns the meta values
	public String metaValues(){
		return hashHeader + " " + String.valueOf(TxCount) + " " + String.valueOf(difficulty) + " " + key;
	}
	//values of the reward transaction
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
