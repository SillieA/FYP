package send;

import peers.Node;
import peers.Peers;
import utils.Block;
import utils.Strings;

public class BroadcastBlock {
//send new block to all connections
	public static void Broadcast(Block b){
		for(Node p : Peers.arr){
			NewClient c = new NewClient(p.IP, Strings.ServerPort);
			c.sendMessage(Strings.clientSendBlock +b.allValues());
		}
	}
}
