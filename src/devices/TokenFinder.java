package devices;

import utils.Block;
import utils.BlockChain;

public class TokenFinder {
	public TokenFinder(){
		
	}
	public TokenFinder(String token){
		for(Block B : BlockChain.MainChain){
			if(B.gen.Token.equals(token)){
				
			}
			else{
//				for
			}
		}
	}
}
