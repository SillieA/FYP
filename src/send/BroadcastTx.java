package send;

import peers.Node;
import peers.Peers;
import utils.Transaction;

public class BroadcastTx {
//sends Tx to every peer on the network
	public BroadcastTx(Transaction T){
		String TxString = T.values();
		for(Node p : Peers.arr){
			try{
				Client C = new Client(p.IP, 19996);
				C.sendMessage("#TPO " + TxString);
				C.sendMessage("TERMINATE");
			}
			catch(Exception e){
				System.out.println(e.toString());
			}
		}
	}
}
