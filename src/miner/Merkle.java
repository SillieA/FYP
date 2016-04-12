package miner;

import java.util.ArrayList;

import core.Transaction;

public class Merkle {
	public static String root(ArrayList<Transaction> Tx){
		ArrayList<String>Hashes = new ArrayList<String>();
		Hashes = StringifyTxArrList(Tx);
//		print(Hashes);
		do{
			Hashes = reHash(Hashes);
		}while(Hashes.size()>1);
//		print(Hashes);
		return Hashes.get(0);

	}
	//never touch again!*******************
	private static ArrayList<String> reHash(ArrayList<String> Hashes){
		String s;
		int i;
		ArrayList<String> arr = new ArrayList<String>();
		if(Hashes.size()%2 == 1){
			arr.add(Hashes.get(Hashes.size()-1));
		}
		
//		print(Hashes);
//		System.out.print("hashes array");
		
		for(i = 1; i<Hashes.size();i = i + 2){
			if((i>Hashes.size())!=true){
				s = ProofOfWork.sha256(Hashes.get(i-1) + Hashes.get(i));
				arr.add(s);
			}
			else{
				s = ProofOfWork.sha256(Hashes.get(i) + Hashes.get(i));
				arr.add(s);
			}
		}
		return arr;
	}

	public static void print(ArrayList<String> arr){
		for(int i = 0; i<arr.size();i++){
			System.out.println(arr.get(i));
		}
	}
	public static ArrayList<String> StringifyTxArrList(ArrayList<Transaction> Tx){
		ArrayList<String> arr = new ArrayList<String>();
		for(int i=0;i<Tx.size();i++){
			arr.add(Tx.get(i).values());
		}
		return arr;
	}

}
