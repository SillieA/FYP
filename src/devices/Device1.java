package devices;

import peers.Peers;
import send.BroadcastTx;
import send.NewClient;
import utils.Main;
import utils.Transaction;

public class Device1 {
	public Device1(){
		for(int i = 0; i>10000;i++){
			Transaction T = new Transaction(null, Main.keyClass.returnPublicKey(Main.keyP),null,null,null);
			new BroadcastTx(T);
			
		}
	}

}
