package Main;

public class Transaction {
	public String TxNumber;//uid of Tx?
	public String From;
	public String To;
	public String Token;
	public String RefTx;
	
	//values can either be written via constructors or from 
	public Transaction(){
		
	}
	public Transaction(String iTxNumber, String iFrom, String iTo, String iToken, String iRefTx){
		write(iTxNumber, iFrom, iTo, iToken, iRefTx);
	}
	public Transaction(String[] arr){
		write(arr);
	}
	public void write(String iTxNumber, String iFrom, String iTo, String iToken, String iRefTx){
		TxNumber = iTxNumber;
		From = iFrom;
		To = iTo;
		Token = iToken;
		RefTx = iRefTx;

	}
	public void write(String[] s){
		TxNumber = s[0];
		From = s[1];
		To = s[2];
		Token = s[3];
		RefTx = s[4];
	}
	public String values(){
		return TxNumber + " " + From + " " + To + " " + Token + " " + RefTx;
	}
	public String[] valuesArr(){
		String[] s = {TxNumber, From, To,Token,RefTx};
		return s;
	}
	

}
