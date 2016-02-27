package Network;

import java.util.ArrayList;

import Main.Block;
import Main.Strings;

public class BroadcastBlock {
//send new block to all connections
	public static void Broadcast(Block b){
		for(Pair p : Peers.arr){
			Client c = new Client(p.IP, Strings.ServerPort);
			c.sendMessage("#BLK" +b.allValues());
		}
	}
}
