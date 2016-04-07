package utils;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.*;
import java.security.spec.*;

import javax.xml.bind.DatatypeConverter;
 //source: http://snipplr.com/view/18368/
public class Keys {
 
	public static void createKeys() {
		Keys keyz = new Keys();
		try {
			String path = Strings.FileDirectory;
 
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA");
 
			keyGen.initialize(1024);
			KeyPair generatedKeyPair = keyGen.genKeyPair();
 
			System.out.println(Strings.NoteKeyPairGenerated);
			keyz.dumpKeyPair(generatedKeyPair);
			keyz.SaveKeyPair(path, generatedKeyPair);
 
			KeyPair loadedKeyPair = keyz.LoadKeyPair(path, "DSA");
			System.out.println(Strings.NoteKeyPairLoaded);
			keyz.dumpKeyPair(loadedKeyPair);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	//written by me, using information from java tutorial at https://docs.oracle.com/javase/tutorial/security/apisign/step3.html
	public String privateKeySign(String message) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException, IOException{
		PrivateKey priv = Main.keyP.getPrivate();
		
        Signature dsa = Signature.getInstance("SHA1withDSA", "SUN"); 
        dsa.initSign(priv);
        
        InputStream in = new ByteArrayInputStream(message.getBytes());
        BufferedInputStream bufin = new BufferedInputStream(in);
        byte[] buffer = new byte[1024];
        int len;
        while (bufin.available() != 0) {
            len = bufin.read(buffer);
            dsa.update(buffer, 0, len);
            };
        bufin.close();
        in.close();
        byte[] realSig = dsa.sign();
        //from http://stackoverflow.com/questions/27201847/error-decoding-signature-bytes-java-security-signatureexception-error-decodi
        String signTostring = DatatypeConverter.printBase64Binary(realSig);
        signTostring = URLEncoder.encode(signTostring, "UTF-8");
        return signTostring;
	}
	public boolean verifySig(String signature, String key, String data) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException, InvalidKeyException, SignatureException{
//		InputStream in = new ByteArrayInputStream(key.getBytes());
//        byte[] encKey = new byte[in.available()];  
//        in.read(encKey);
//
//        in.close();
//        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encKey);
//
//        KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");
//        PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);
		PublicKey pubKey = Main.keyP.getPublic();
		 //from http://stackoverflow.com/questions/27201847/error-decoding-signature-bytes-java-security-signatureexception-error-decodi
		String st = URLDecoder.decode(signature, "UTF-8");
		byte[] sign_byte = DatatypeConverter.parseBase64Binary(st); 
        
        Signature sig = Signature.getInstance("SHA1withDSA", "SUN");
        sig.initVerify(pubKey);

        
        InputStream datais = new ByteArrayInputStream(data.getBytes());
        BufferedInputStream bufin = new BufferedInputStream(datais);

        byte[] buffer = new byte[1024];
        int len;
        while (bufin.available() != 0) {
            len = bufin.read(buffer);
            sig.update(buffer, 0, len);
            };

        bufin.close();

        boolean verifies = sig.verify(sign_byte);
        return verifies;
	}
	
	private void dumpKeyPair(KeyPair keyPair) {
		PublicKey pub = keyPair.getPublic();
		System.out.println("Public Key: " + getHexString(pub.getEncoded()));
 
		PrivateKey priv = keyPair.getPrivate();
		System.out.println("Private Key: " + getHexString(priv.getEncoded()));
	}
//	public static String[] returnKeyPair(KeyPair keyPair) {
//		PublicKey pub = keyPair.getPublic();
//		
//		String s1 = getHexStrings(pub.getEncoded());
// 
//		PrivateKey priv = keyPair.getPrivate();
//		String s2 = getHexStrings(priv.getEncoded());
//		String[] arr = {s1,s2};
//		return arr;
//	}
	public String[] returnKeyPair(KeyPair keyPair) throws UnsupportedEncodingException{
		PublicKey pub = keyPair.getPublic();
        String s1 = DatatypeConverter.printBase64Binary(pub.getEncoded());
        s1 = URLEncoder.encode(s1, "UTF-8");
        
        PrivateKey priv = keyPair.getPrivate();
        String s2 = DatatypeConverter.printBase64Binary(priv.getEncoded());
        s2 = URLEncoder.encode(s2, "UTF-8");
        
        String[] arr = {s1,s2};
        
        return arr;
	}
	public String returnPublicKey(KeyPair keyPair){
		String[] s;
		try {
			s = returnKeyPair(keyPair);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			s = null;
		}
		return s[0];
	}

 
	private static String getHexString(byte[] b) {
		String result = "";
		for (int i = 0; i < b.length; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result;
	}
 
	public void SaveKeyPair(String path, KeyPair keyPair) throws IOException {
		PrivateKey privateKey = keyPair.getPrivate();
		PublicKey publicKey = keyPair.getPublic();
 
		// Store Public Key.
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(
				publicKey.getEncoded());
		FileOutputStream fos = new FileOutputStream(path + "/public.key");
		fos.write(x509EncodedKeySpec.getEncoded());
		fos.close();
 
		// Store Private Key.
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
				privateKey.getEncoded());
		fos = new FileOutputStream(path + "/private.key");
		fos.write(pkcs8EncodedKeySpec.getEncoded());
		fos.close();
	}
 
	public KeyPair LoadKeyPair(String path, String algorithm)
			throws IOException, NoSuchAlgorithmException,
			InvalidKeySpecException {
		// Read Public Key.
		File filePublicKey = new File(path + "/public.key");
		FileInputStream fis = new FileInputStream(path + "/public.key");
		byte[] encodedPublicKey = new byte[(int) filePublicKey.length()];
		fis.read(encodedPublicKey);
		fis.close();
 
		// Read Private Key.
		File filePrivateKey = new File(path + "/private.key");
		fis = new FileInputStream(path + "/private.key");
		byte[] encodedPrivateKey = new byte[(int) filePrivateKey.length()];
		fis.read(encodedPrivateKey);
		fis.close();
 
		// Generate KeyPair.
		KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(
				encodedPublicKey);
		PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
 
		PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(
				encodedPrivateKey);
		PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
 
		return new KeyPair(publicKey, privateKey);
	}
}