import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;

import security.AES;
import security.FileOperations;
import security.MAC;
import security.RSA;

public class Sender 
{
	
	private static String SENDER_PRIVATE_KEY_FILE = "SenderPrivateKey.txt";
	
	private static FileOperations _comChannel; 
	
	public Sender() throws NoSuchAlgorithmException, IllegalArgumentException, IOException
	{
		_comChannel = new FileOperations(SystemConstants.COMMUNICATION_CHANNEL);
		File file = new File(SystemConstants.SENDER_PUBLIC_KEY_FILE);
		if(!file.isFile())
		{
			KeyPair senderPair = RSA.genKeys(SystemConstants.KEY_SIZE);
			RSA.storePublicKey(SystemConstants.SENDER_PUBLIC_KEY_FILE, senderPair.getPublic());
			RSA.storePrivateKey(SENDER_PRIVATE_KEY_FILE, senderPair.getPrivate());
		}
	}
	
	public void sendEncryptedMessageToReceiver(String messageFileName, String receiverPublicKeyFile, String aesKey) throws IllegalArgumentException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException
	{	
		FileOperations messageFile = new FileOperations(messageFileName);
		String message = messageFile.readFile();
		
		PublicKey pk = RSA.getPublicKey(receiverPublicKeyFile);
		
		String encryptedMessage = AES.encrypt(message, aesKey);
		String encryptedAESKey = RSA.encryptMessage(aesKey, pk);
		
		String completeEncryptedMessage = encryptedMessage + encryptedAESKey;
		
		MAC mac = new MAC();
		byte[] macResult = mac.genMac(completeEncryptedMessage);
		String encodedMac = Base64.getEncoder().encodeToString(macResult);
		
		_comChannel.writeFile(encryptedMessage+ "\n" + encryptedAESKey + "\n" + encodedMac);
	}
	
//	public void decryptMessageFromReceiver() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IllegalArgumentException, InvalidKeySpecException, IOException
//	{
//		String[] values = _comChannel.readFileAsArray();
//		
//		String aesKey = RSA.decryptMessage(values[1], RSA.getPrivateKey(SENDER_PRIVATE_KEY_FILE));
//		
//		String message = AES.decrypt(values[0], aesKey);
//		
//		System.out.println(message);
//	}
}
