package send;

import peers.Node;
import peers.Peers;
import utils.Block;
import utils.Strings;

public class BroadcastBlock {
//send new block to all connections
	Client c;
	public BroadcastBlock(Block b){
		System.out.println("Broadcasting Block");
		for(Node p : Peers.arr){
			this.c = new Client(p.IP, Strings.ServerPort);
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			c.sendMessage("#" + Strings.clientSendBlock + " " + b.allValues());
		}
	}
}
