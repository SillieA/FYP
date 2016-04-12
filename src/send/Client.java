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
import utils.Main;
import utils.Strings;
import utils.Transaction;

public class Client {

	private BufferedReader in;
	private PrintWriter out;
	private Socket socket;


	public Client(final String IP, final int port) {
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

//						System.out.println("Client In: " + input);
						if(input.isEmpty()|| input.equals(null)||input.equals("")){
							
						}
						else if(input.startsWith("#")){
							String type = input.substring(1,4);
							try{
								String message = input.substring(4);
			
								caller(type,message);//executes code depending on hashtag
							}catch(StringIndexOutOfBoundsException e){
								System.out.println("Server Error : null input");
							}
						}
					} catch (Exception e1) {
						break;
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
		
//		System.out.println("Connected to " + serverAddress + ":" + String.valueOf(serverPort));
		
	}
	
	//sorts input to correct methods
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
		int currentDifficulty;
		String[] Blocks = message.split(Strings.BlockDelim);
		for(String str : Blocks){
			if(!bh.containsLetters(str)){
				difficulty += bh.blockReceive(str, true);
			}
		}
		currentDifficulty = BlockChain.chainDifficulty();
		System.out.println("DIFF------ NEW: " + String.valueOf(difficulty) + "OLD: " + String.valueOf(currentDifficulty));
		if(difficulty > currentDifficulty){
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
		sendMessage("#" + Strings.clientSendBlock + " " + message,"SendBlock");
	}
	//CO2
	public void sendTx(Transaction T){
		String message = T.values();
		sendMessage("#" + Strings.clientSendTx + " " + message,"SendTx"); 
		terminateConnection();
	}
	//CO3
	private void sendBlockChain(){//sends current blockchain
		String s = "";
		for(Block b : BlockChain.MainChain){
			s += Strings.BlockDelim + Strings.HeadDelim + " " + b.headerValues() + Strings.HeadDelim + " " + Strings.MetaDelim + " " + b.metaValues() + Strings.MetaDelim + " " + Strings.GenDelim + " " + b.gen.values() + Strings.GenDelim + Strings.TxDelim + " " + b.txValuesNoNewLine() + Strings.TxDelim + " ";
		}
		s += Strings.BlockDelim;
		sendMessage("#" + Strings.clientSendBlockChain + " " + s,"sendChain");
	}
	//CO4
	private void sendDifficulty(int difficulty){//sends local difficulty to server
		sendMessage("#" + Strings.clientSendDifficulty + " " + String.valueOf(difficulty),"SendDif");
	}
	//peers
	private void receivePeers(String message){//receives a list of peers, adds them to the peer list
		String[] s = message.split(",");
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
		sendMessage(".","RecPeers");
	}
	public void requestPeers(){
		sendMessage("#PER " + Main.keyClass.returnPublicKey(Main.keyP) + " " + Strings.Role, "ReqPeers");
	}
	//utility
	static String[] removeBlanks(String[] input){//removes all blank elements of the array
		List<String> list = new ArrayList<String>(Arrays.asList(input));
		list.removeAll(Arrays.asList("", null));
		return list.toArray(new String[list.size()]);
	}
	private void sendMessage(String message, String source){
		out.println(message);
		out.flush();
//		System.out.println("Client Out(" + source + "): ");
	}
	public void terminateConnection(){
		sendMessage("TERMINATE","terminate");
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
