package send;

import peers.Node;
import peers.Peers;
import utils.Strings;
import utils.Transaction;

public class BroadcastTx {
//sends Tx to every peer on the network
//	public BroadcastTx(Transaction T){
//		String TxString = T.values();
//		for(Node p : Peers.arr){
//			try{
//				Client C = new Client(p.IP, 19996);
//				C.sendMessage(Strings.clientSendTx + " " + TxString);
//				C.sendMessage("TERMINATE");
//			}
//			catch(Exception e){
//				System.out.println(e.toString());
//			}
//		}
//	}
	public BroadcastTx(Transaction T){
		try{
			Client C = new Client("25.47.156.0", 19996);
			Thread.sleep(2000);
			C.sendMessage(Strings.clientSendTx + " " + T.values());
			C.sendMessage("TERMINATE");
			System.out.println("Message sent");
			
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}
}
