package Network;

import java.util.HashSet;
//replace with centralised server
import java.util.Set;
import Main.Keys;
import Main.Main;
import Main.Strings;

public class Peers {
	public static Set<Pair> arr;

	public Peers(){
		arr = new HashSet<Pair>();

		Runnable r = new Runnable(){
			public void run(){
				Client c = new Client(Strings.PeerServerIP, 16789);
				c.sendMessage(Keys.returnPublicKey(Main.keyP));
			}
		};
		Thread thr = new Thread(r);
		thr.start();
	}
	//returns all the peers in the array
	public static Set<String[]> getPeers(){
		Set<String[]> sArr = new HashSet<String[]>();
		String[] s = {"",""};
		for(Pair p : arr){
			s[0] = p.IP;
			s[1] = p.PK;
			sArr.add(s);
		}
		return sArr;
	}
	public static Set<String> getIPs(){
		Set<String> sArr = new HashSet<String>();
		String s; 
		for(Pair p : arr){
			s = p.IP;
			sArr.add(s);
		}
		return sArr;
	}
}
