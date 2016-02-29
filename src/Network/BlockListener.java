package Network;

import java.util.ArrayList;

import Main.Block;
import Main.Strings;

public class BlockListener {

	public ArrayList<Block> altChain;
	
	public ArrayList<Block> requestChain(Pair p){
		Client c = new Client(p.IP,Strings.ServerPort);
		c.sendMessage("#BLR");
		while(true){
			if(altChain.isEmpty()){
				
			}
			else{
				return altChain;
			}
		}
	}
}
