import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import security.AES;
import security.FileOperations;
import security.MAC;
import security.RSA;

public class Receiver {
private static String RECEIVER_PRIVATE_KEY_FILE = "ReceiverPrivateKey.txt";
	
	private static FileOperations _comChannel; 
	
	public Receiver() throws NoSuchAlgorithmException, IllegalArgumentException, IOException
	{
		_comChannel = new FileOperations(SystemConstants.COMMUNICATION_CHANNEL);
		File file = new File(SystemConstants.RECEIVER_PUBLIC_KEY_FILE);
		if(!file.isFile())
		{
			KeyPair receiverPair = RSA.genKeys(SystemConstants.KEY_SIZE);
			RSA.storePublicKey(SystemConstants.RECEIVER_PUBLIC_KEY_FILE, receiverPair.getPublic());
			RSA.storePrivateKey(RECEIVER_PRIVATE_KEY_FILE, receiverPair.getPrivate());
		}
	}
	
//	public void sendEncryptedMessageToSender(String messageFileName, String senderPublicKeyFile, String aesKey) throws IllegalArgumentException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException
//	{	
//		FileOperations messageFile = new FileOperations(messageFileName);
//		String message = messageFile.readFile();
//		
//		PublicKey pk = RSA.getPublicKey(senderPublicKeyFile);
//		
//		String encryptedMessage = AES.encrypt(message, aesKey);
//		String encryptedAESKey = RSA.encryptMessage(aesKey, pk);
//		
//		_comChannel.writeFile(encryptedMessage+ "\n" + encryptedAESKey);
//	}
	
	public void decryptMessageFromSender() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IllegalArgumentException, InvalidKeySpecException, IOException
	{
		String[] values = _comChannel.readFileAsArray();
		
		
		
		String encodedMac = values[2];
		byte[] mac = Base64.getDecoder().decode(encodedMac);
		
		MAC macOp = new MAC();
		boolean isMatch = macOp.match(values[0] + values[1], mac);
		
		if(isMatch)
		{
			System.out.println("Mac matches");
			System.out.println();
			
			String aesKey = RSA.decryptMessage(values[1], RSA.getPrivateKey(RECEIVER_PRIVATE_KEY_FILE));
			String message = AES.decrypt(values[0], aesKey);
			System.out.println(message);
			System.out.println();
		}
		else
		{
			System.out.println("Mac does not match, Data Integrity compromised");
		}
	}
}
