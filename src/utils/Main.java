//This class initialises the threads and performs some preliminary checks

package utils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import devices.Device1;
import devices.table;
import miner.UnconfirmedTx;
import peers.Peers;
import server.Server;

//loads and saves Tx chain, contains printTxArr method, start server and keyCheck
public class Main {

	//this nodes keypair
	public static Keys keyClass;
	public static KeyPair keyP;
	//this is the list of peers that this node is aware of
	public static Peers P;
	
	//main method
	public static void main(String args[]) throws FileNotFoundException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchProviderException, SignatureException{

		keyCheck();
		P = new Peers();
		
		boolean b = true;
		while(b){
			Strings.Role = JOptionPane.showInputDialog("Type 'Miner' or 'Node' to select network role");
			if(Strings.Role.equals("Miner")|| Strings.Role.equals("Node")){
				b = false;
			}
		}
		new BlockChain();
		if(Strings.Role.equals("Miner")){
			new UnconfirmedTx();
		}
		BlockChain.initialiseChain();
		GUITest();
		new Device1();
		startServer();//starts server which contains the initialiser for UnconfirmedTxList
		

//		String x = "catface";
//		String modx = keyClass.privateKeySign(x);
//		String pub = keyClass.returnPublicKey(keyP);
//		System.out.println(x + " " +  modx + " " + " " + pub);
//		boolean b = keyClass.verifySig(modx, pub, x);
//		System.out.println(String.valueOf(b));
		
		//		String s[] = keyClass.returnKeyPair(keyP);
		//		System.out.println(s[0] +"\n" + s[1]);
	}
	//starts the server to listen for connections
	public static void GUITest(){

		System.out.println("table thread started");
		table gui = new table();
		gui.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		gui.setSize(800,400);
		gui.setVisible(true);
		gui.setTitle("table");
	}
	public static void startServer(){
		Thread t = new Thread() {
		    public void run() {
				try {
					Server.initialise(Strings.ServerPort);
				} catch (Exception e) {
					e.printStackTrace();
				}
		    }  
		};
		t.start();

		System.out.println(Strings.NoteServerUp);
	}
	
	//this is for checking whether a keypair has been generated. if not, then 
	public static void keyCheck() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException{
		File f1 = new File(Strings.FilePrivateKey);
		File f2 = new File(Strings.FilePublicKey);
		//if keys are present on the host machine...
		if(f1.exists() && f2.exists()) { 
			//load them into the program
		    keyClass = new Keys();
		    keyP = keyClass.LoadKeyPair(Strings.FileDirectory, "DSA");
		}
		//if not make some key files
		else{
			Keys.createKeys();
			System.out.println(Strings.NoteMissingKeys);
		}
	}
}


