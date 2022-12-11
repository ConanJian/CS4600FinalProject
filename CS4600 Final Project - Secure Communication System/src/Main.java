import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import security.AES;
import security.FileOperations;
import security.RSA;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		//Sender and Receiver must generate there own public key.
		
		//Get Each others Public Key from a Shared File1
		
		//Sender Gets Message from File2
		//Sender Generates AES Key/Gets AES Key from File2
		//Sender sends Message Encrypted by AES Key, sends AES Key encrypted by RSA public key of receiver, and sends MAC
		
		//Receiver receives the message, based on protocol, checking order is determined.
		
		//For protocol, probably have metadata that tells receiver when each message ends. Or could just use newline to differentiate
	
		try
		{
			Sender s = new Sender();
			
			String aesKey = AES.genKey();
			
			s.sendEncryptedMessageToReceiver("Message.txt",
					SystemConstants.RECEIVER_PUBLIC_KEY_FILE,
					aesKey);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		System.out.println("Sender has sent Message");
	}

}

//try
//{
//	RSA r = new RSA();
//	AES a = new AES();
//	FileOperations op1 = new FileOperations("Test1.txt");
//	
//	KeyPair senderPair = r.genKeys(2048);
//	KeyPair receiverPair = r.genKeys(2048);
//	
//	String aesKey = "RandomKeyLol21073";
//	String message = "Hello, this should be encrypted";
//	
//	r.storePublicKey("PublicKeyFile.txt", receiverPair.getPublic());
//	PublicKey pk = r.getPublicKey("PublicKeyFile.txt");
//	
//	String encryptedMessage = a.encrypt(message, aesKey);
//	String encryptedAESKey = r.encryptMessage(aesKey, pk);
//	
//	op1.writeFile(encryptedMessage+ "\n" + encryptedAESKey);
//	
//	String[] values = op1.readFile();
//	
//	String aesKey2 = r.decryptMessage(values[1], receiverPair.getPrivate());
//	
//	String message2 = a.decrypt(values[0], aesKey2);
//	
//	System.out.println(message2);
//}
//catch(Exception e)
//{
//	e.printStackTrace();
//}
//
//
//System.out.println("Done");
