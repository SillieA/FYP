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
	private String s;
	private NewClient C;
	public BroadcastTx(Transaction T){
		try{
			for(String str : Peers.getIPs()){
				this.s = str;
				C = new NewClient(s, 19996);
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				C.sendMessage("#" + Strings.clientSendTx + " " + T.values());
				C.sendMessage("TERMINATE");
				System.out.println("Message sent");
			}
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("Error connecting to " + s);
		}
	}
}
