//This class initialises the threads and performs some preliminary checks

package utils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import miner.UnconfirmedTx;
import peers.Peers;
import server.NewServer;
import server.Server;
//loads and saves Tx chain, contains printTxArr method, start server and keyCheck
public class Main {
	//the transaction list
//	public static ArrayList<Transaction> TxList = new ArrayList<Transaction>();
	//this nodes keypair
	public static KeyPair keyP;
	//this is the list of peers that this node is aware of
	public static Peers P;
	
	//main method
	@SuppressWarnings("unused")
	public static void main(String args[]) throws FileNotFoundException, IOException, NoSuchAlgorithmException, InvalidKeySpecException{
		
		keyCheck();//see if private and public keys exist. if not, make some
		
		P = new Peers();//initialises the list of peers TO BE REPLACED WITH CENTRAL SERVER
		
		BlockChain BLKCHN = new BlockChain();
		BlockChain.initialiseChain();
		startServer();//starts server which contains the initialiser for UnconfirmedTxList
		new UnconfirmedTx();

	}
	//starts the server to listen for connections
	public static void startServer(){
		Thread t = new Thread() {
		    public void run() {
//		    	these is for the old server class
//		        Server host = new Server();
//				host.startRunning(Strings.ServerPort);
				try {
					NewServer.initialise(Strings.ServerPort);
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
		    Keys keyPair = new Keys();
		    keyP = keyPair.LoadKeyPair(Strings.FileDirectory, "DSA");
		}
		//if not make some key files
		else{
			Keys.createKeys();
			System.out.println(Strings.NoteMissingKeys);
		}
	}
}


