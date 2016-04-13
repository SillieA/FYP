package core;

import java.util.ArrayList;

public class GenesisBlock {
	public static void checkBlockChain(){
		//if blockchain is empty, create one.
		createGenesisBlock();
		BlockChain.saveBlockChain();


	}
	
	private static void createGenesisBlock(){
		ArrayList<Transaction> TxList = new ArrayList<Transaction>();
		Transaction T = new Transaction();
		T.write("0", Strings.Genesis , Main.keyClass.returnPublicKey(Main.keyP), Strings.rewards(), Strings.Genesis);
		T.generateReference();
		TxList.add(T);
		Block Gen = new Block(TxList,Strings.Genesis,0,Strings.Genesis, 0);
		Gen.rewardTx(Strings.rewards());
		BlockChain.MainChain.add(Gen);
	}

}
