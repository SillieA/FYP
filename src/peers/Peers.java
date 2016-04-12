package peers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import miner.ProofOfWork;
import send.Client;
import utils.Main;
import utils.Strings;

public class Peers {
	public static List<Node> arr;
	public Client c;

	public Peers(){
		arr = new ArrayList<Node>();

		Runnable r = new Runnable(){
			public void run(){
				try{
					while(true){
						syncPeers();

						Thread.sleep(1500);

						c.requestPeers();
						
						Thread.sleep(1500);
						
						printPeers();
						
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
			if(!p.PK.equals(Main.keyClass.returnPublicKey(Main.keyP))){
				arr.add(p);
			}
			
		}
		else{
			System.out.println("ERROR: Peers node info contains incorrect number of elements");
		}
	}
	public void syncPeers(){
		Runnable r = new Runnable(){
			public void run(){
				c = new Client(Strings.PeerServerIP, 16789);
			}
		};
		Thread thr = new Thread(r);
		thr.start();
	}
	public static void printPeers(){
		System.out.println("Connected Peers:");
		for(Node n : arr){
			System.out.println(n.IP + "|" + ProofOfWork.sha256(n.PK) + "|" + n.Type );
		}
		System.out.println();
	}
	public static void clear(){
		arr.clear();
	}
	public static Node getRandomNode(){
		Random r = new Random();
		while(true){
			int rand = r.nextInt(arr.size());
			if(arr.get(rand).Type.equals("Node")){
				return arr.get(rand);
			}
		}
	}
}
