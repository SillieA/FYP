package Network;

import java.io.*;
import java.net.*;
import java.util.HashSet;
import java.util.Set;

import Main.BlockChain;

public class Server{

	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ServerSocket server;
	private Socket connection;
	public UnconfirmedTx unconfirmedTxThread;
	private String ClientIP;

	//set up and run server - called once gui is made
	public void startRunning(int port){
		//initialise the txpool
		@SuppressWarnings("unused")
		UnconfirmedTx unconfirmedTxThread = new Network.UnconfirmedTx();
		try{
			server = new ServerSocket(port, 100);
			while(true){
				try{
					//wait for connection
					waitForConnection();
					//sets up connection
					setupStreams();
					//allows communication in streams
					whileChatting();
				}catch(EOFException eofException){
					System.out.println("Client ended the connection!");
				}finally{
					closeConnection();
				}
			}
		}catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	
	//wait for connection, then display connection information
	private void waitForConnection() throws IOException{
		System.out.println("Waiting for someone to connect...");
		connection = server.accept();//creates socket when connection is made
		ClientIP = connection.getRemoteSocketAddress().toString();
		System.out.println("Now connected to " + ClientIP);//other persons IP address
	}
	
	//get stream to send and receive data
	private void setupStreams() throws IOException{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		System.out.println("Streams setup Success!");
	}
	
	//during the chat conversation
	private void whileChatting() throws IOException{
		String message = "Connection successful";
		String type;
		int typeDelimiter = 0;
		sendMessage(message);
		do{
			try{
				message = (String) input.readObject();
				System.out.println(message);
				if(message.contains("#")){
					typeDelimiter = message.indexOf("#")+1;
					type = message.substring(typeDelimiter,typeDelimiter + 3);
					caller(type,message);//executes code depending on hashtag
				}
				else if(message.contains("SHUTDOWN SERVER")){
					closeConnection();
					message = "TERMINATE";
				}
			}catch(ClassNotFoundException classNotFoundException){
				System.out.println("unknown class (Server) ");
			}
			
		}while(!message.contains("TERMINATE"));
	}
	
	//close streams and sockets after connection is terminated
	private void closeConnection(){
		try{
			output.close();
			input.close();
			connection.close();//closes socket
		}catch(IOException ioException){
			ioException.printStackTrace();
		}
	}	
	
	//send a message to client
	private void sendMessage(String message){
		try{
			output.writeObject(message);
			output.flush();
			System.out.println(message);
		}catch(IOException ioException){
			System.out.println("Message not sert (Server)");
		}
	}
	//sorts out the different 'input' requests. 
	//TRE = client informing that they are sending you money so you can send public key. returns public key.
	//BLK = Receiving new block from client UNFINISHED
	//TPO = new transaction for pool. takes 5 inputs. NULL FROMpubkey TOpubkey TOKEN REFTX
	//DEB = Displays blockchain in console
	//SAV = saves blockchain to file
	private void caller(String code, String message){
		switch(code){
		case "TRE" : txReqReceive(message);
		break;
		case "BLK" : blockReceive(message);
		break;
		case "TPO" : txPoolReceive(message);
		break;
		case "PRQ" : peerRequest();
		break;
		case "DEB" : BlockChain.printChain();
		break;
		case "SAV" : BlockChain.saveBlockChain();
		break;
		}
	}
	private void blockReceive(String message) {
		System.out.println("blockReceive" + message);
		
	}
	//new tx for txpool
	private void txPoolReceive(String message) {
		System.out.println("TxpoolReceive " + message);
		Main.Transaction T = new Main.Transaction();
		try{
			int delimiter = message.indexOf("#")+5;//gets position immediately after #REQ
			String tx = message.substring(delimiter);

			String[] txVal = tx.split(" ");//split at the spaces
			try{
				T.write(txVal[0],txVal[1],txVal[2],txVal[3],txVal[4]);//make 5 words into new tx
				UnconfirmedTx.push(T);
			}catch(ArrayIndexOutOfBoundsException e){
				System.out.println("Error with Tx parameters from " + ClientIP);
			}

		}catch(Exception e){
			System.out.println("error : empty TxPool message");
			e.printStackTrace();
		}

	}
	//returns public key when informed of inbound transaction
	private void txReqReceive(String message){
		System.out.println("txReqReceive" + message);
		String[] kp = Main.Keys.returnKeyPair(Main.Main.keyP);
		
		sendMessage(kp[0]);
		
	}
	private void peerRequest(){
		Set<String[]> sArr = new HashSet<String[]>();
		String message = "";
		for(String[] s : sArr){
			message += s[0] + " " + s[1];
		}
		sendMessage(message);
	}
}

