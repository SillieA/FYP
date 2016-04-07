package devices;

import miner.UnconfirmedTx;
import send.BroadcastTx;
import utils.Main;
import utils.Strings;
import utils.Transaction;

public class Device1 {
	public Device1(){
		Thread t = new Thread(){
			public void run(){
				System.out.println("Device 1 started");
				for(int i = 0; i<10000;i++){
					try {
						Thread.sleep(150);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Transaction T = new Transaction(null, Main.keyClass.returnPublicKey(Main.keyP),"0MIIBtzCCASwGByqGSM44BAEwggEfAoGBAP1%2FU4EddRIpUt9KnC7s5Of2EbdSPO9EAMMeP4C2USZpRV1AIlH7WT2NWPq%2FxfW6MPbLm1Vs14E7gB00b%2FJmYLdrmVClpJ%2Bf6AR7ECLCT7up1%2F63xhv4O1fnxqimFQ8E%2B4P208UewwI1VBNaFpEy9nXzrith1yrv8iIDGZ3RSAHHAhUAl2BQjxUjC8yykrmCouuEC%2FBYHPUCgYEA9%2BGghdabPd7LvKtcNrhXuXmUr7v6OuqC%2BVdMCz0HgmdRWVeOutRZT%2BZxBxCBgLRJFnEj6EwoFhO3zwkyjMim4TwWeotUfI0o4KOuHiuzpnWRbqN%2FC%2FohNWLx%2B2J6ASQ7zKTxvqhRkImog9%2FhWuWfBpKLZl6Ae1UlZAFMO%2F7PSSoDgYQAAoGAOpQFLirWwZgm0LyI5ptp9celwBIKvjB%2BhMUopvM4B4RsMr5Sw41sHoKsE4peQhqoiloowpJ2WCnAxBrxDOlTMWvv8aYsFISU8b%2BqiTfJc0M0BlyoUMl61jS6tLtO7RBnqNgy4bFPlniWQ1T77DXgfnldzbbCO9wDl4eeuowoVrM%3D","Test Token " + String.valueOf(i),Strings.Genesis);
					T.generateReference();
					System.out.println("Device Created Tx: " + T.TxNumber);
					new BroadcastTx(T);
					UnconfirmedTx.push(T);
				}
			}

		};
		t.start();


	}

}
