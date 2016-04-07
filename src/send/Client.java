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
import server.BlockHandler;
import utils.Block;
import utils.BlockChain;
import utils.Strings;
import utils.Transaction;

public class Client {

	private BufferedReader in;
	private PrintWriter out;
	private Socket socket;
	private String IP;


	public Client(String IP, int port) {
		this.IP = IP;
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

						System.out.println(IP + ": " + input);
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
					} catch (Exception e1) {
						e1.printStackTrace();
					}

				}while(!input.contains("TERMINATE"));
				System.out.println("Connection Terminated with " + IP);
				try {
					socket.close();
				} catch (IOException e) {
				}
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
	
	//provides an interface. 
	private void caller(String code, String message){
		switch(code){
		case Strings.serverSendDifficulty : receiveDifficulty(message);
		break;
		case Strings.serverSendBlockChain : receiveBlockChain(message);
		break;
		case "BLS" : sendBlockChain();
		break;
		case "BLR" : receiveBlockChain(message);
		break;
		case "PER" : receivePeers(message);
		}
	}
	//inputs
	private void receiveBlockChain(String message) {
		BlockHandler bh = new BlockHandler();
		int difficulty = 0;
		String[] Blocks = message.split(Strings.BlockDelim);
		for(String str : Blocks){
			System.out.println(str);
		}
		for(String str : Blocks){
			if(!bh.containsLetters(str)){
				difficulty += bh.blockReceive(str, true);
			}
		}
		if(difficulty > BlockChain.chainDifficulty()){
			BlockChain.MainChain = new ArrayList<Block>(bh.altChain);
			bh.altChain.clear();
		}
		
		bh.printChain();
		terminateConnection();
	}
	private void receiveDifficulty(String message) {
		String m[] = message.split(" ");
		int inputNumber = 0;
		int currentDifficulty;
		boolean containsNumber = false;
		for(String s : m){
			if(s.matches(".*\\d+.*") && containsNumber == false){//checks for integers. if there is more than one section with integers, some sort of error has occurred
				containsNumber = true;
				inputNumber = Integer.parseInt(s);
			}
			else if (containsNumber == true){
				System.out.println("Error : difficulty input not recognised!");
			}
		}
		currentDifficulty = BlockChain.chainDifficulty();
		if(currentDifficulty > inputNumber){
			sendBlockChain();
		}
		else if(currentDifficulty < inputNumber){
			sendDifficulty(currentDifficulty);
		}
	}
	//outputs
	//CO1
	public void sendBlock(Block b){
		String message = b.valuesDelimited();
		sendMessage("#" + Strings.clientSendBlock + " " + message);
	}
	//CO2
	public void sendTx(Transaction T){
		String message = T.values();
		sendMessage("#" + Strings.clientSendTx + " " + message); 
	}
	//CO3
	private void sendBlockChain(){//sends current blockchain
		String s = "";
		for(Block b : BlockChain.MainChain){
			s += Strings.BlockDelim + Strings.HeadDelim + " " + b.headerValues() + Strings.HeadDelim + " " + Strings.MetaDelim + " " + b.metaValues() + Strings.MetaDelim + " " + Strings.GenDelim + " " + b.gen.values() + Strings.GenDelim + Strings.TxDelim + " " + b.txValuesNoNewLine() + Strings.TxDelim + " ";
		}
		s += Strings.BlockDelim;
		out.println("#BLR " + s);
	}
	//CO4
	private void sendDifficulty(int difficulty){//sends local difficulty to server
		sendMessage("#" + Strings.clientSendDifficulty + " " + String.valueOf(difficulty));
	}
	//peers
	private static void receivePeers(String message){//receives a list of peers, adds them to the peer list
		String[] s = message.split(",");
		System.out.println(String.valueOf(s.length));
		Peers.clear();
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
	//utility
	static String[] removeBlanks(String[] input){//removes all blank elements of the array
		List<String> list = new ArrayList<String>(Arrays.asList(input));
		list.removeAll(Arrays.asList("", null));
		return list.toArray(new String[list.size()]);
	}
	public void sendMessage(String message){
		out.println(message);
		out.flush();
		System.out.println("To: " + this.IP + "|");
	}
	public void terminateConnection(){
		sendMessage("TERMINATE");
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
