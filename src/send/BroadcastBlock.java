package send;

import peers.Node;
import peers.Peers;
import utils.Block;
import utils.Strings;

public class BroadcastBlock {
//send new block to all connections
	public static void Broadcast(Block b){
		for(Node p : Peers.arr){
			Client c = new Client(p.IP, Strings.ServerPort);
			c.sendMessage("#BLK" +b.allValues());
		}
	}
}
