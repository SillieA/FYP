package core;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;

import miner.ProofOfWork;

public class Transaction {
	public String TxNumber;//hash of other values, signed by priv key of 'From'
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
	public String valuesForSignature(){
		return From + To + Token + RefTx;
	}
	public String[] valuesArr(){
		String[] s = {TxNumber,From,To,Token,RefTx};
		return s;
	}
	//generates a reference for the transaction by signing a hash of data in transaction with private key
	public void generateReference(){
		String hash = ProofOfWork.sha256(valuesForSignature());
		try {
			TxNumber = Main.keyClass.privateKeySign(hash);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
