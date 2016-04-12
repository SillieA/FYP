package send;

import core.Transaction;
import peers.Node;
import peers.Peers;

public class BroadcastTx {
	//sends Tx to every peer on the network
	private String str;
	private Client C;
	public BroadcastTx(Transaction T){
//		System.out.println("BroadcastTx: " + T.values());
		try{
			for(Node N : Peers.arr){
				if(N.Type.equals("Miner")){
					this.str = N.IP;

					C = new Client(str, 19996);
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					try{
						C.sendTx(T);
						//					System.out.println("Message sent");
					}catch(NullPointerException e){
//						System.out.println("Unable to connect to : " + str);
					}
				}

			}
		}
		catch(Exception e){
//			e.printStackTrace();
//			System.out.println("Error connecting to " + str);
		}
	}
}
