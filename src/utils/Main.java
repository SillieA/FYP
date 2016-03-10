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
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import miner.UnconfirmedTx;
import peers.Peers;
import server.BlockHandler;
import server.NewServer;
//loads and saves Tx chain, contains printTxArr method, start server and keyCheck
public class Main {
	//the transaction list
//	public static ArrayList<Transaction> TxList = new ArrayList<Transaction>();
	//this nodes keypair
	public static Keys keyClass;
	public static KeyPair keyP;
	//this is the list of peers that this node is aware of
	public static Peers P;
	
	//main method
	public static void main(String args[]) throws FileNotFoundException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchProviderException, SignatureException{
		
		keyCheck();//see if private and public keys exist. if not, make some
		
		P = new Peers();//initialises the list of peers TO BE REPLACED WITH CENTRAL SERVER
		
//		String x = "catface";
//		String modx = keyClass.privateKeySign(x);
//		String pub = keyClass.returnPublicKey(keyP);
//		System.out.println(x + " " +  modx + " " + " " + pub);
//		boolean b = keyClass.verifySig(modx, pub, x);
//		System.out.println(String.valueOf(b));
		
//		String s[] = keyClass.returnKeyPair(keyP);
//		System.out.println(s[0] +"\n" + s[1]);
		
		
		new BlockChain();
		new UnconfirmedTx();
		BlockChain.initialiseChain();
//		new BlockHandler();
		startServer();//starts server which contains the initialiser for UnconfirmedTxList


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


