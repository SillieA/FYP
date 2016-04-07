package devices;

import miner.UnconfirmedTx;
import send.BroadcastTx;
import utils.Main;
import utils.Transaction;

public class Device1 {
	public Device1(){
		Thread t = new Thread(){
			public void run(){
				for(int i = 0; i>10000;i++){
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Transaction T = new Transaction(null, Main.keyClass.returnPublicKey(Main.keyP),null,null,null);
					T.generateReference();
					new BroadcastTx(T);
					UnconfirmedTx.push(T);
				}
			}

		};
		t.start();


	}

}
