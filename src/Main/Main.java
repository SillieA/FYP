//This class initialises the threads and performs some preliminary checks

package Main;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import Network.Peers;
import Network.Server;
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

	}
	

	//starts the server to listen for connections
	public static void startServer(){
		Thread t = new Thread() {
		    public void run() {
		        Server host = new Server();
				host.startRunning(19996);
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
	//load the transaction chain (currently txt)
//	public static void loadTxChain(String file) throws FileNotFoundException, IOException{
//		try(BufferedReader br = new BufferedReader(new FileReader(file))) {
//		    String line = br.readLine();
//
//		    while (line != null) {
//		    	if(!line.startsWith("%")){
//		    		//split up 
//		    		String[] parts = line.split(Pattern.quote(" "));
//		    		Transaction t = new Transaction();
//		    		t.write(parts);
//		    		TxList.add(t);
//		    	}
//		    	//go to next line
//		    	line = br.readLine();
//		    }
//		}
//	}
	
	//save the tx chain(for use when shutting down program)
//	public static void saveTxChain(){
//		try{
//		FileWriter writer = new FileWriter("C:\\FYP\\Transactions2.txt"); 
//		writer.write("%Transaction Number, From, To, Token value, Reference Transaction \n");
//		for(Transaction tx: TxList) {
//		  writer.write(tx.values());
//		}
//		writer.close();
//		}catch(IOException ioException){
//			ioException.printStackTrace();
//		}
//	}
//	//prints all tx's in the arraylist
//	public static void printTxArr(ArrayList<Transaction> arr){
//		for(Transaction i:arr){
//			System.out.println(i.values());
//		}
//	}
}


