package miner;

import java.util.ArrayList;

import send.BroadcastBlock;
import utils.Block;
import utils.BlockChain;
import utils.Strings;
import utils.Transaction;

public class BlockBuilder {
	
	public static ArrayList<Transaction> TxList;
	
	public BlockBuilder(){
		TxList = new ArrayList<Transaction>();

		Runnable r = new Runnable(){
			public void run(){
				try {
					Thread.sleep(10000);;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
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

						String prevBlockHash = BlockChain.latestBlockHeader();
						System.out.println("Started building block after " + prevBlockHash);
						String merkle = Merkle.root(txLista);
                                               
						int[] difficultyNonce = ProofOfWork.find(merkle + prevBlockHash);
						Block b = new Block(txLista, merkle,difficultyNonce[1] , prevBlockHash,difficultyNonce[0]);

						System.out.println(b.hashHeader + "Created!");
						new BroadcastBlock(b);
						//take used tx out of pool
						if(b.hashPrevBlock.equals(BlockChain.latestBlockHeader())){
							BlockChain.MainChain.add(b);
							for(int i = txLista.size()-1; i >= 0 ;i--){
								TxList.remove(i);
							}
						}
						else{
							System.out.println("Lost Block Race");
							b.destroy();
						}


					}
				}
			}
		};
		Thread thr = new Thread(r);
		thr.start();
	}

	public void add(Transaction Tx){
		TxList.add(Tx);
	}
	public static void dump(){
		for(Transaction T:TxList){
			miner.UnconfirmedTx.push(T);
		}
	}
}
