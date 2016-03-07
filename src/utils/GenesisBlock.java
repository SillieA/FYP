package utils;

import java.util.ArrayList;
import javax.swing.JOptionPane;

public class GenesisBlock {
	public static void checkBlockChain(){
		//if peer server = empty

		int input = JOptionPane.showConfirmDialog(
				null,
				Strings.NoteMissingBlockchain,
				Strings.NoteMissingBlockchainTitle,
				JOptionPane.YES_NO_OPTION);
		if(input == JOptionPane.YES_OPTION){
			createGenesisBlock();
		}
		else if(input == JOptionPane.NO_OPTION){
			//get some blocks from other nodes.
			//if no nodes are found, print error
		}
	}
	
	private static void createGenesisBlock(){
		ArrayList<Transaction> TxList = new ArrayList<Transaction>();
		Transaction T = new Transaction();
		T.write("0", Strings.Genesis , Keys.returnPublicKey(Main.keyP), Strings.rewards(), Strings.Genesis);
		T.generateReference();
		TxList.add(T);
		Block Gen = new Block(TxList,Strings.Genesis,0,Strings.Genesis, 0);
		Gen.rewardTx(Strings.rewards());
		BlockChain.MainChain.add(Gen);
	}

}
