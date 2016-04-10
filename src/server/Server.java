package server;
//source : http://cs.lmu.edu/~ray/notes/javanetexamples/#capitalize
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import miner.UnconfirmedTx;
import utils.Block;
import utils.BlockChain;
import utils.Strings;
import utils.Transaction;

public class Server {
	public static void initialise(int port) throws Exception {
		System.out.println("Server is running.");
		ServerSocket listener = new ServerSocket(port);
		try {
			while (true) {
				new Connector(listener.accept()).start();
			}
		} finally {
			listener.close();
		}
	}

	private static class Connector extends Thread {
		private Socket socket;
		private String clientIP;
		private PrintWriter out;

		public Connector(Socket socket) {
			this.socket = socket;
			this.clientIP = socket.getInetAddress().toString().substring(1);
//			System.out.println(clientIP);
//			System.out.println("New connection with client at " + socket);
		}
		//this method listens for the input, and processes it for the caller method
		public void run() {
			try {
				
				BufferedReader in = new BufferedReader(
						new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);
				String input = "";
				
				do {
					input = in.readLine();
					System.out.println("Server In: " + input);
					if (input == null || input.equals(".")) {
						break;
					}
					else if(input.equals("debug")){
						for(int i =0; i<100000;i++){
							caller(Strings.clientSendTx," 0 test test " + String.valueOf(i) + " test");
						}
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
				}while(!input.contains("TERMINATE"));
			} catch (IOException e) {
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
					System.out.println("Couldn't close a socket, what's going on?");
				}
				System.out.println("Connection: " + clientIP + " closed");
			}
		}
		public void caller(String code, String message){
			switch(code){
			
			case Strings.clientSendBlock : blockReceive(message);
			break;
			case Strings.clientSendTx : if((Strings.Role).equals("Miner")) txPoolReceive(message);//transaction transmission received
			break;
			case Strings.clientSendBlockChain : blockchainReceive(message);//block transmission received
			break;
			case Strings.clientSendDifficulty : difficultyReceive(message);
			break;
			

			//Debug
			case "DEB" : BlockChain.printChain();//debug: prints chain in terminal
			break;
			case "SAV" : BlockChain.saveBlockChain();//save chain to file
			break;
			case "SBC" : sendBlockChain();//send blockchain
			break;
			}
		}
		//inputs
		//CO1
		private void blockReceive(String message){//process for receiving a block
			System.out.println("CO1 called");
			int difficulty;
			BlockHandler bh = new BlockHandler();
			difficulty = bh.blockReceive(message,false);
			if(difficulty == -1){
				terminateConnection();
			}
			else{
				sendDifficulty(difficulty);
			}
			
		}
		//CO2
		private void txPoolReceive(String message) {//process for receiving transaction
			System.out.println("CO2 called");
			System.out.println("CO2: TxpoolReceive " + message);
			
			Transaction T = new Transaction();
			try{

				String[] txVal = message.split(" ");//split at the spaces

				try{
					T.write(txVal[0],txVal[1],txVal[2],txVal[3],txVal[4]);//make 5 words into new tx
					T.generateReference();
//					if(Validation.checkTx(T) == true){
						UnconfirmedTx.push(T);
//					}
//					else{
//						System.out.println("TX not valid");
//					}
				}catch(ArrayIndexOutOfBoundsException e){
					System.out.println("Error with Tx parameters");
				}

			}catch(Exception e){
				System.out.println("error : empty TxPool message");
				e.printStackTrace();
			}
			terminateConnection();
			

		}
	
		//CO3
		private void blockchainReceive(String message) {//process for receiving blockchain
			System.out.println("CO3 called");
			BlockHandler bh = new BlockHandler();
			int difficulty = 0;
			String[] Blocks = message.split(Strings.BlockDelim);
			for(String str : Blocks){
				System.out.println(str);
			}
			for(String str : Blocks){
				if(!bh.containsLetters(str)){
					difficulty += bh.blockReceive(str,true);
				}
				
			}
			if(difficulty > BlockChain.chainDifficulty()){
				BlockChain.MainChain = new ArrayList<Block>(bh.altChain);
				bh.altChain.clear();
			}
			bh.printChain();
			terminateConnection();
		}
		
		//CO4
		private void difficultyReceive(String message){
			System.out.println("CO4 called");
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
		//SO1
		private void sendDifficulty(int currentDifficulty){
			sendMessage("#" + Strings.serverSendDifficulty + " " + String.valueOf(currentDifficulty));
		}
		//SO2
		private void sendBlockChain(){//send blockchain
			String s = "";
			
			for(Block b : BlockChain.MainChain){
				s += Strings.BlockDelim + " " + Strings.HeadDelim + " " + b.headerValues() + Strings.HeadDelim + " " + Strings.MetaDelim + " " + b.metaValues() + Strings.MetaDelim + " " + Strings.GenDelim + " " + b.gen.values() + Strings.GenDelim + Strings.TxDelim + " " + b.txValuesNoNewLine() + Strings.TxDelim + " ";
			}
			s += Strings.BlockDelim;
			sendMessage("#" + Strings.serverSendBlockChain + " " + s);
			terminateConnection();
		}
		//utils
		public void sendMessage(String message){
			out.println(message);
			out.flush();
			System.out.println("Server Out: " + message);
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
}

