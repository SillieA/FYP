package miner;

import java.util.ArrayList;

import core.Block;
import core.BlockChain;
import core.Strings;
import core.Transaction;
import send.BroadcastBlock;

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
					//if tx list is too small..
					if(TxList.size()<2){
						try {//wait.
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
						//PROOF OF WORK - WHERE BLOCKS ARE MADE
						int[] difficultyNonce = ProofOfWork.find(merkle + prevBlockHash);
						Block b = new Block(txLista, merkle,difficultyNonce[1] , prevBlockHash,difficultyNonce[0]);

						System.out.println("Block number " + String.valueOf(BlockChain.MainChain.size() + 1) + " Created!");
						//broadcast block
						
						
						if(b.hashPrevBlock.equals(BlockChain.latestBlockHeader())){
							BlockChain.MainChain.add(b);
							new BroadcastBlock(b);
							//cut off txs used in this block
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
	//pushes tx's from block back into unconfirmedTx list
	public static void dump(){
		for(Transaction T:TxList){
			miner.UnconfirmedTx.push(T);
		}
	}
}
