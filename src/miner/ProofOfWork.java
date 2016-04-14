package miner;

import java.security.MessageDigest;

import core.Strings;

public class ProofOfWork {
	public static int[] find(String input){
		String x =sha256(input);
		//difficulty,nonce
		int[] solution = solver(x);
		
		return solution;
	}
	//hashes a String with sha 256
	public static String sha256(String base) {
		try{
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(base.getBytes("UTF-8"));
			StringBuffer hexString = new StringBuffer();

			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if(hex.length() == 1) hexString.append('0');
				hexString.append(hex);
			}

			return hexString.toString();
		} catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}
	//******PROOF OF WORK********
	private static int[] solver(String b){
		String t;
		String solution;
		for(int i = 1; i<999999999;i++){
			//hash nonce with input string
			t = sha256(b + String.valueOf(i));
			
//			System.out.println(String.valueOf(i) + ": " + t);
			//checks whether the first 5 characters are 0's
			solution = t.substring(0,5);
			
//			System.out.println(String.valueOf(i) + ": " + solution);
			if(solution.equals(Strings.Difficulty)){
//				System.out.println(String.valueOf(t));
				//return nonce and difficulty
				int ans[] = {difficulty(t), i};
				return ans;	
				//effectively H(H(Merkle + PBH) + Nonce))
			}
		}
		//if hash cannot be solved
		System.out.println(Strings.ErrorNonceSolve);
		return null;
	}
	//finds total difficulty of solution
	private static int difficulty(String s){
		char[] arr = s.toCharArray();
		int i = 0;
		for(char c : arr){
			if(c == '0'){
				i++;
			}
			else{
				break;
			}
		}
		return i;
	}
}