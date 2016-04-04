package send;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import peers.Peers;
import utils.Block;
import utils.BlockChain;
import utils.Strings;


/**
 * A simple Swing-based client for the capitalization server.
 * It has a main frame window with a text field for entering
 * strings and a textarea to see the results of capitalizing
 * them.
 */
public class NewClient {

	private BufferedReader in;
	private PrintWriter out;
	//    private JFrame frame = new JFrame("Capitalize Client");
	//    private JTextField dataField = new JTextField(40);
	//    private JTextArea messageArea = new JTextArea(8, 60);
	private Socket socket;

	/**
	 * Constructs the client by laying out the GUI and registering a
	 * listener with the textfield so that pressing Enter in the
	 * listener sends the textfield contents to the server.
	 */
	public NewClient(String IP, int port) {
		Runnable r = new Runnable(){
			public void run(){
				try {
					connectToServer(IP, port);
				} catch (IOException e2) {
					e2.printStackTrace();
				}

				String input = "";
				do{
					try {

						input = in.readLine();

						System.out.println(input);
						if(input.startsWith("#")){
							int typeDelimiter = input.indexOf("#")+1;
							String type = input.substring(typeDelimiter,typeDelimiter + 3);
							try{
								String message = input.substring(typeDelimiter + 4);
								caller(type,message);//executes code depending on hashtag
							}catch(StringIndexOutOfBoundsException e){
								caller(type,null);
							}
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}

				}while(!input.contains("TERMINATE"));
			}
		};
		Thread thr = new Thread(r);
		thr.start();

	}
	public void connectToServer(String serverAddress, int serverPort) throws IOException {

		// Make connection and initialize streams
		System.out.println(serverAddress + String.valueOf(serverPort));
		socket = new Socket(serverAddress, serverPort);
		in = new BufferedReader(
				new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);
		
		System.out.println("Connected to " + serverAddress + ":" + String.valueOf(serverPort));
		
	}
	
	//provides an interface. need to replace the strings with those defined in the Strings class
	private void caller(String code, String message){
		switch(code){
		case "BLK" : readInBlock(message);; 
		break;
		case "BLS" : sendBlockChain();
		break;
		case "BLR" : receiveBlockChain(message);
		break;
		case "RDF" : compareDifficulty(message);
		break;
		case "PRE" : receivePeers(message);
		}
	}
	private void receiveBlockChain(String message) {
		
	}
	private void readInBlock(String message){//called when 

	}
	private void compareDifficulty(String message){//compares difficulty of local blockchain to server blockchain

	}
	private void sendBlockChain(){//sends current blockchain
		String s = "";
		for(Block b : BlockChain.MainChain){
			s += Strings.BlockDelim + Strings.HeadDelim + " " + b.headerValues() + Strings.HeadDelim + " " + Strings.MetaDelim + " " + b.metaValues() + Strings.MetaDelim + " " + Strings.GenDelim + " " + b.gen.values() + Strings.GenDelim + Strings.TxDelim + " " + b.txValuesNoNewLine() + Strings.TxDelim + " ";
		}
		s += Strings.BlockDelim;
		out.println("#BLR " + s);
	}
	private static void receivePeers(String message){//receives a list of peers, adds them to the peer list
		String[] s = message.split(",");
		System.out.println(String.valueOf(s.length));
		String[] k = null;
		for(String str : s){
			String[] ret = {"","",""};
			if(str.equals(null) == false && str.equals("") == false && str.isEmpty() == false){

				k = str.split(" ");
				ret = removeBlanks(k);

				if(ret.length == 3){
					Peers.addPeers(ret);
				}
				else{
					System.out.println("Message length exception : " + Arrays.toString(ret));
				}
			}
		}
	}
	static String[] removeBlanks(String[] input){//removes all blank elements of the array
		List<String> list = new ArrayList<String>(Arrays.asList(input));
		list.removeAll(Arrays.asList("", null));
		return list.toArray(new String[list.size()]);
	}
	public void sendMessage(String message){
		out.println(message);
		out.flush();
	}
}
