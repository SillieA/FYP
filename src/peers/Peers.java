package peers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import miner.ProofOfWork;
import send.NewClient;
import utils.Main;
import utils.Strings;

public class Peers {
	public static List<Node> arr;
	public NewClient c;

	public Peers(){
		arr = new ArrayList<Node>();

		Runnable r = new Runnable(){
			public void run(){
				try{
					while(true){
						syncPeers();
						try {
							Thread.sleep(1500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						c.sendMessage("#PER " + Main.keyClass.returnPublicKey(Main.keyP) + " " + Strings.Role);
						try {
							Thread.sleep(1500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						c.sendMessage(".");
	
						Thread.sleep(60000);
					}
				}catch(Exception e){
					System.out.println("Could not connect to Peer server. Ensure server is running and restart program.");
					e.printStackTrace();
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
			printPeers();
		}
		else{
			System.out.println("ERROR: Peers node info contains incorrect number of elements");
		}
	}
	public void syncPeers(){
		Runnable r = new Runnable(){
			public void run(){
				c = new NewClient(Strings.PeerServerIP, 16789);
			}
		};
		Thread thr = new Thread(r);
		thr.start();
	}
	public static void printPeers(){
		for(Node n : arr){
			System.out.println(n.IP + "|" + ProofOfWork.sha256(n.PK) + "|" + n.Type );
		}
	}
	public static void clear(){
		arr.clear();
	}
}
