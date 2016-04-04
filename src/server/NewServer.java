package server;
//source : http://cs.lmu.edu/~ray/notes/javanetexamples/#capitalize
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import miner.UnconfirmedTx;
import utils.Block;
import utils.BlockChain;
import utils.Strings;
import utils.Transaction;

public class NewServer {

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
			System.out.println(clientIP);
			System.out.println("New connection with client at " + socket);
		}

		public void run() {
			try {

				BufferedReader in = new BufferedReader(
						new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);


				while (true) {
					String input = in.readLine();
					System.out.println(input);
					if (input == null || input.equals(".")) {
						break;
					}
					else if(input.contains("#")){
						int typeDelimiter = input.indexOf("#")+1;
						String type = input.substring(typeDelimiter,typeDelimiter + 3);
						try{
							String message = input.substring(typeDelimiter + 4);
							caller(type,message);//executes code depending on hashtag
						}catch(StringIndexOutOfBoundsException e){
							caller(type,null);
						}
					}
					out.println(input.toUpperCase());
				}
			} catch (IOException e) {
				System.out.println("Error handling client# " + clientIP + ": " + e);
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
					System.out.println("Couldn't close a socket, what's going on?");
				}
				System.out.println("Connection with client# " + clientIP + " closed");
			}
		}
		private void caller(String code, String message){
			switch(code){
			case Strings.clientSendBlockChain : blockchainReceive(message);//block transmission received
			break;
			case "SBC" : sendBlockChain();//send blockchain
			break;
			case Strings.clientSendBlock : blockReceive(message);
			break;
			case Strings.clientSendDifficulty : difficultyReceive(message);
			break;
			case Strings.clientSendTx : txPoolReceive(message);//transaction transmission received
			break;

			//Debug
			case "DEB" : BlockChain.printChain();//debug: prints chain in terminal
			break;
			case "ADB" : BlockHandler.printChains();
			break;
			case "SAV" : BlockChain.saveBlockChain();//save chain to file
			break;
			}
		}
		
		private void sendBlockChain(){//send blockchain
			String s = "";
			for(Block b : BlockChain.MainChain){
				s += Strings.BlockDelim + Strings.HeadDelim + " " + b.headerValues() + Strings.HeadDelim + " " + Strings.MetaDelim + " " + b.metaValues() + Strings.MetaDelim + " " + Strings.GenDelim + " " + b.gen.values() + Strings.GenDelim + Strings.TxDelim + " " + b.txValuesNoNewLine() + Strings.TxDelim + " ";
			}
			s += Strings.BlockDelim;
			out.println("#BLR " + s);
		}
		private void blockchainReceive(String m) {//process for receiving blockchain
			String[] Blocks = m.split(Strings.BlockDelim);
			for(String str : Blocks){
				System.out.println(str);
			}
			for(String str : Blocks){
				if(!BlockHandler.containsLetters(str)){
					BlockHandler.blockReceive(str);
				}
			}
			BlockHandler.printChains();
		}
		private void txPoolReceive(String message) {//process for receiving transaction
			System.out.println("TxpoolReceive " + message);
			Transaction T = new Transaction();
			try{

				String[] txVal = message.split(" ");//split at the spaces
				//				for(String asdf : txVal){
				//					System.out.println(asdf);
				//				}
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
					System.out.println("Error with Tx parameters from ");
				}

			}catch(Exception e){
				System.out.println("error : empty TxPool message");
				e.printStackTrace();
			}

		}
		private void blockReceive(String message){//process for receiving a block
			
		}
		private void difficultyReceive(String message){
			
		}
	}
}

