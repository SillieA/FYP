package peers;

public class Node {//stores information about connected peers
	public String IP;
	public String PK;
	public String Type;
	
	public Node(String IP, String PK, String type){
		this.IP = IP;
		this.PK = PK;
		this.Type = type;
	}

	public Node() {
	}

}
