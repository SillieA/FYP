package send;

import java.io.*;
import java.net.*;


public class Client{
	
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String message = "";
	private String serverIP;
	private String name = "NA";
	private Socket connection;
	private int port = 19996;
	
	//constructor
	public Client(String host, int port){
		serverIP = host;
		this.port = port;
		try {
			name = String.valueOf(InetAddress.getLocalHost());
			System.out.println(name);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		startRunning();
	}
	
	//connect to server
	public void startRunning(){
		try{
			connectToServer();
			setupStreams();
			whileChatting();
		}catch(EOFException eofException){
			System.out.println("Server Terminated connection");
		}catch(IOException ioException){
			ioException.toString();
		}finally{
			closeCrap();
		}
	}
	//connect to server
	private void connectToServer() throws IOException{
		System.out.println("Attempting connection...");
		connection  = new Socket(InetAddress.getByName(serverIP),port);
		
		System.out.println("connected to: " + connection.getRemoteSocketAddress().toString());
	}
	// setup streams to send and receive messages
	private void setupStreams() throws IOException{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		System.out.println("Streams connected successfully!");
	}
	//while chatting with server
	private void whileChatting() throws IOException{
		do{
			try{
				message = (String) input.readObject();
				System.out.println(message);
				taskAllocator(message);
			}catch(ClassNotFoundException classNotFoundException){
				System.out.println("unknown object type (Client)");
			}
		}while(!message.contains("TERMINATE"));
	}
	private void taskAllocator(String message) {
		
		
	}

	//close streams and sockets
	private void closeCrap(){
		System.out.println("shutting down");
		try{
			output.close();
			input.close();
			connection.close();
		}catch(IOException ioException){
			ioException.printStackTrace();
		}
		
	}
	//send message to server
	public void sendMessage(String message){
		try{
			output.writeObject(name + " - " + message);
			output.flush();
			System.out.println(message);
			
		}catch(IOException ioException){
			System.out.println("Error sending message(Client)");
		}
	}

}








