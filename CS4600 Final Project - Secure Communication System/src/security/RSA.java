package security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSA {

	public static KeyPair genKeys(int keySize) throws NoSuchAlgorithmException
	{
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		keyGen.initialize(keySize);
		KeyPair keys = keyGen.genKeyPair();
		return keys;
	}
	
	public static String encryptMessage(String message, PublicKey publicKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
	{
		Cipher encryptCipher = Cipher.getInstance("RSA");
		encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
		
		byte[] secretMessageBytes = message.getBytes(StandardCharsets.UTF_8);
		//This encrypted the message
		byte[] encyptedMessageBytes = encryptCipher.doFinal(secretMessageBytes);
		String encodedMessage = Base64.getEncoder().encodeToString(encyptedMessageBytes);
		return encodedMessage;
	}
	
	public static String decryptMessage(String encryptedMessage, PrivateKey privateKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
	{
		Cipher decryptCipher = Cipher.getInstance("RSA");
		decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
		
		byte[] encryptedMessageBytes = Base64.getDecoder().decode(encryptedMessage);
		
		//This decrypts the message
		byte[] decryptedMessageBytes = decryptCipher.doFinal(encryptedMessageBytes);
		String decryptedMessage = new String(decryptedMessageBytes, StandardCharsets.UTF_8);
		return decryptedMessage;
	}
	
	public static void storePublicKey(String fileName, PublicKey publicKey) throws IllegalArgumentException, IOException
	{
		FileOperations op = new FileOperations(fileName);
		op.writeByteStream(publicKey.getEncoded());
	}
	
	public static PublicKey getPublicKey(String fileName) throws IllegalArgumentException, IOException, NoSuchAlgorithmException, InvalidKeySpecException
	{
		FileOperations op = new FileOperations(fileName);
		
		byte[] outsidePublicKey = op.readByteStream();
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(outsidePublicKey);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		
		return keyFactory.generatePublic(keySpec);
	}
	
	public static void storePrivateKey(String fileName, PrivateKey privateKey) throws IllegalArgumentException, IOException
	{
		FileOperations op = new FileOperations(fileName);
		op.writeByteStream(privateKey.getEncoded());
	}
	
	public static PrivateKey getPrivateKey(String fileName) throws IllegalArgumentException, IOException, NoSuchAlgorithmException, InvalidKeySpecException
	{
		FileOperations op = new FileOperations(fileName);
		
		byte[] privateKey = op.readByteStream();
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		
		return keyFactory.generatePrivate(keySpec);
	}
}
