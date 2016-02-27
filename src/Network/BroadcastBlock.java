package Network;

import java.util.ArrayList;

import Main.Block;
import Main.Strings;

public class BroadcastBlock {
//send new block to all connections
	public BroadcastBlock(Block b){
		for(Pair p : Peers.arr){
			Client c = new Client(p.IP, Strings.ServerPort);
			c.sendMessage(b.allValues());
		}
	}
}
