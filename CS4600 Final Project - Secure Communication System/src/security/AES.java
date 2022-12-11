package security;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class AES {
	
	private static SecretKeySpec secretKey;
	private static byte[] key;
	
	
	private static void setKey(final String myKey) throws NoSuchAlgorithmException, UnsupportedEncodingException 
	{
		//Hash my randomly generated key to get the final key
		MessageDigest sha = null;
		key = myKey.getBytes("UTF-8");
	    sha = MessageDigest.getInstance("SHA-1");
	    key = sha.digest(key);
	    key = Arrays.copyOf(key, 16);
	    secretKey = new SecretKeySpec(key, "AES");
    }
	
	public static String genKey()
	{
		//Create a random 16 character key
		String aesKey = "";
		String map = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
		         + "0123456789"
		         + "abcdefghijklmnopqrstuvxyz";
		
		for(int i = 0; i < 16; i++)
		{
			int temp = (int)(Math.random()*(map.length()));
			aesKey += map.charAt(temp);
		}
		return aesKey;
	}
	
	public static String encrypt(final String strToEncrypt, final String secret) throws NoSuchAlgorithmException, UnsupportedEncodingException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException 
	{
		setKey(secret);
    	Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    	cipher.init(Cipher.ENCRYPT_MODE, secretKey);
    	return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
    }
	
	public static String decrypt(final String strToDecrypt, final String secret) throws NoSuchAlgorithmException, UnsupportedEncodingException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException 
	{
		setKey(secret);
	    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
	    cipher.init(Cipher.DECRYPT_MODE, secretKey);
	    return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)), StandardCharsets.UTF_8);
    }
}
