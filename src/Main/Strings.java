package Main;

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
	
	//ERRORS, human facing
	final public static String Error = "Error";
	final public static String ErrorThreadWait = "CRITICAL ERROR: Thread wait failed!";
	final public static String ErrorPop = "CRITICAL ERROR: pop invoked on empty unconfirmed Transaction Pool!";
	final public static String ErrorNonceSolve = "CRITICAL ERROR: Nonce not found!";
	
	//words used in logic
	final public static String Genesis = "genesis";
	final public static String PeerServerIP = "25.47.33.74";
	final public static String Difficulty = "00000";
	final public static int ServerPort = 19996;
	
	//File names
	final public static String FileBlockChain = "C:\\FYP\\BlockChain.txt";
	final public static String FilePublicKey = "C:\\FYP\\public.key";
	final public static String FilePrivateKey = "C:\\FYP\\private.key";
	final public static String FileDirectory = "C:\\FYP\\";
//	final public static String
//	final public static String
//	final public static String

}
