package send;

import core.Block;
import core.Strings;
import peers.Node;
import peers.Peers;

public class BroadcastBlock {
//send new block to all connections
	Client c;
	public BroadcastBlock(Block b){
//		System.out.println("Broadcasting Block");
		for(int i = 0; i < Peers.arr.size();i++){
			Node p = Peers.arr.get(i);//sends Block to every connected peer
			this.c = new Client(p.IP, Strings.ServerPort);
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try{
				c.sendBlock(b);
			}catch(NullPointerException e){
				System.out.println("Could not connect to : " + p.IP);
			}
		}
	}
}
