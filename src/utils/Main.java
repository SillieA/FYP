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

import devices.TxGenerator;
import devices.TokenFinder;
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
		//args[0] = role
		//args[1] = number of communications to send
		//args[2] = token Name
		if(args[0].equals("Miner") || args[0].equals("Node")){
			Strings.Role = args[0];
		}
		keyCheck();
		System.out.println(keyClass.returnPublicKey(keyP));
		P = new Peers();

		new BlockChain();
		if(Strings.Role.equals("Miner")){
			new UnconfirmedTx();
		}
		BlockChain.initialiseChain();
		new TokenFinder();
//		GUITest();
		new TxGenerator(Integer.parseInt(args[1]),args[2]);
		startServer();//starts server
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


