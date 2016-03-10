package peers;

import java.util.HashSet;
//replace with centralised server
import java.util.Set;

import send.Client;
import utils.Keys;
import utils.Main;
import utils.Strings;

public class Peers {
	public static Set<Node> arr;

	public Peers(){
		arr = new HashSet<Node>();

		Runnable r = new Runnable(){
			public void run(){
				try{
					while(true){
						Client c = new Client("25.47.33.74", 16789);
						try {
							Thread.sleep(1500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						c.sendMessage(Main.keyClass.returnPublicKey(Main.keyP));
						c.sendMessage("TERMINATE");
						Thread.sleep(60000);
					}
				}catch(Exception e){
					System.out.println("Could not connect to Peer server. Ensure server is running and restart program.");
				}
			}
		};
		Thread thr = new Thread(r);
		thr.start();
	}
	//returns all the peers in the array
	public static Set<String[]> getPeers(){
		Set<String[]> sArr = new HashSet<String[]>();
		String[] s = {"","",""};
		for(Node p : arr){
			s[0] = p.IP;
			s[1] = p.PK;
			s[2] = p.Type;
			sArr.add(s);
		}
		return sArr;
	}
	public static Set<String> getIPs(){
		Set<String> sArr = new HashSet<String>();
		String s; 
		for(Node p : arr){
			s = p.IP;
			sArr.add(s);
		}
		return sArr;
	}
	public static void addPeers(String[] IPPK){
		if(IPPK.length == 3){
		Node p = new Node(IPPK[0],IPPK[1],IPPK[2]);
		arr.add(p);
		}
		else{
			System.out.println("ERROR: node info contains incorrect number of elements");
		}
	}
}
