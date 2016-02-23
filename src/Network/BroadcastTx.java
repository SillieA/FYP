package Network;

import Main.Transaction;

public class BroadcastTx {
//sends Tx to every peer on the network
	public BroadcastTx(Transaction T){
		String TxString = T.values();
		for(Pair p : Peers.arr){
			try{
				Client C = new Client(p.IP, 19996);
				C.sendMessage("#TPO " + TxString);
				C.sendMessage("TERMINATE");
			}
			catch(Exception e){
				//remove peers?
			}
		}
	}
}
