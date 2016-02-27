package Mine;

import java.util.ArrayList;

import Main.Block;
import Main.BlockChain;
import Main.Strings;
import Main.Transaction;
import Network.BroadcastBlock;

public class currentBlock {
	
	public static ArrayList<Transaction> TxList;
	
	public currentBlock(){
		TxList = new ArrayList<Transaction>();
		Runnable r = new Runnable(){
			public void run(){
				System.out.println(Strings.NoteCurrentBlockUp);
				while(true){
					if(TxList.size()<2){
						try {
							Thread.sleep(1500);;
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					else{
						ArrayList<Transaction> txLista = new ArrayList<Transaction>(TxList);
						
						//DEBUG
						System.out.println();
						System.out.println("TXLISTA VALUES: ");
						for(Transaction Tr : txLista){
							System.out.println(Tr.values());
						}
						System.out.println();
						//
						String prevBlockHash = BlockChain.latestBlockHeader();
						String merkle = Merkle.root(txLista);
//						System.out.println(Strings.NoteMiningNonce + merkle + "\n" + prevBlockHash);
						System.out.println("mined block, now broadcasting");
						int[] difficultyNonce = findNonce.find(merkle + prevBlockHash);
						Block b = new Block(txLista, merkle,difficultyNonce[1] , prevBlockHash,difficultyNonce[0]);
						generateRewardTx(b);
						BroadcastBlock.Broadcast(b);
						//take used tx out of pool
						BlockChain.MainChain.add(b);
						for(int i = txLista.size()-1; i >= 0 ;i--){
							TxList.remove(i);
						}
					}
				}
			}
		};
		Thread thr = new Thread(r);
		thr.start();
	}
	public void generateRewardTx(Block b){
		
	}
	public void add(Transaction Tx){
		TxList.add(Tx);
	}
	public static void dump(){
		for(Transaction T:TxList){
			Network.UnconfirmedTx.push(T);
		}
	}
}
