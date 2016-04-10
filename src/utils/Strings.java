package utils;

import java.io.File;
import java.nio.file.Paths;
import java.util.Random;
import miner.ProofOfWork;

public class Strings {
	
	//words used by terminal, human facing
	final public static String NoteServerUp = "Server Started Successfully";
	final public static String NoteUnconfirmedTxUp = "Unconfirmed Transaction pool list and allocater have started successfully";
	final public static String NoteCurrentBlockUp = "BlockChain mining process started successfully";
	final public static String NoteTxPoolPopulated = "Currently Transactions in Unconfirmed Transaction pool : ";
	final public static String NoteMissingBlockchain = "BlockChain missing from C:\\FYP folder. Create new BlockChain? \n ***SELECT NO IF YOU ARE LOOKING TO JOIN AN EXISTING NETWORK***";
	final public static String NoteMissingBlockchainTitle = "Warning: Blockchain.txt not found";
	final public static String NoteMissingKeys = "WARNING: No Key Pair detected in file directory. New keys will be generated";
	final public static String NoteKeyPairGenerated = "Key Pair Successfully Generated";
	final public static String NoteKeyPairLoaded = "Key Pair Successfully Loaded";
	final public static String NoteMiningNonce = "Trying to find nonce from Merkle Root and Previous Block Hash:\n";
	final public static String NoteFalseBlock = "Block Rejected due to incorrect Nonce";
	final public static String NoteBlockChainLoaded = "Block Chain successfully loaded"; 
	
	//ERRORS, human facing
	final public static String Error = "Error";
	final public static String ErrorThreadWait = "CRITICAL ERROR: Thread wait failed!";
	final public static String ErrorPop = "CRITICAL ERROR: pop invoked on empty unconfirmed Transaction Pool!";
	final public static String ErrorNonceSolve = "CRITICAL ERROR: Nonce not found!";
	
	//words used in logic
	public static String Role = "Miner";
	final public static String Genesis = "genesis";
	final public static String PeerServerIP = "25.38.219.89";
	final public static String Difficulty = "00000";
	final public static String BlockDelim = "###";
	final public static String HeadDelim = "+++";
	final public static String MetaDelim = "---";
	final public static String GenDelim = ":::";
	final public static String TxDelim = "~~~";
	final public static int ServerPort = 19996;
	
	//server outputs/client inputs
	final public static String serverSendDifficulty = "SO1";
	final public static String serverSendBlockChain = "SO2";

	//client outputs/server inputs
	final public static String clientSendBlock = "CO1";
	final public static String clientSendTx = "CO2";
	final public static String clientSendBlockChain = "CO3";
	final public static String clientSendDifficulty = "CO4";
	
	//debug client outputs
	final public static String clientDebug1 = "CD1";
	final public static String clientDebug2 = "CD2";
	final public static String clientDebug3 = "CD3";
	final public static String clientDebug4 = "CD4";
	final public static String clientDebug5 = "CD5";
	
	//debus server outputs
	final public static String serverDebug1 = "SD1";
	final public static String serverDebug2 = "SD2";
	final public static String serverDebug3 = "SD3";
	final public static String serverDebug4 = "SD4";
	final public static String serverDebug5 = "SD5";
	
	//File names
	final public static String FileDirectory = Paths.get(".").toAbsolutePath().normalize().toString() + File.separator;
	final public static String FileBlockChain = FileDirectory + "BlockChain.txt";
	final public static String FilePublicKey = FileDirectory + "public.key";
	final public static String FilePrivateKey = FileDirectory + "private.key";
	final public static String FileAltChains = FileDirectory + "AltChain";
	final public static String FileLogger = FileDirectory + "Log.txt";
	final public static String FileTimeStamp = FileDirectory + "Tokens.txt";
	
//	final public static String
//	final public static String
	
	
	//returns a random hash (possibly requested by nodes) for token generation
	public static String rewards(){
		Random r = new Random();
		int rand = r.nextInt((8-1)+1)+1;
		
		final String reward1 = ProofOfWork.sha256("Client 1" + "Password 1");
		final String reward2 = ProofOfWork.sha256("Client 2" + "Password 2");
		final String reward3 = ProofOfWork.sha256("Client 3" + "Password 3");
		final String reward4 = ProofOfWork.sha256("Client 4" + "Password 4");
		final String reward5 = ProofOfWork.sha256("Client 5" + "Password 5");
		final String reward6 = ProofOfWork.sha256("Client 6" + "Password 6");
		final String reward7 = ProofOfWork.sha256("Client 7" + "Password 7");
		final String reward8 = ProofOfWork.sha256("Client 8" + "Password 8");

		switch(rand){
		case 1 : return reward1;
		case 2 : return reward2;
		case 3 : return reward3;
		case 4 : return reward4;
		case 5 : return reward5;
		case 6 : return reward6;
		case 7 : return reward7;
		case 8 : return reward8;
		}
		return reward1;

	}

}
