package security;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class MAC {

	private Key _key;
	private Mac _mac;
	
	public MAC() throws NoSuchAlgorithmException, InvalidKeyException, IllegalArgumentException, IOException
	{
		FileOperations op = new FileOperations("MacKey.txt");
		
		byte[] desKey = op.readByteStream();
		if(desKey.length == 0)
		{
			KeyGenerator keyGen = KeyGenerator.getInstance("DES");
			SecureRandom secRandom = new SecureRandom();
			keyGen.init(secRandom);
			_key = keyGen.generateKey();
			op.writeByteStream(_key.getEncoded());
		}
		else
		{
			_key = new SecretKeySpec(desKey, 0, desKey.length, "DES");
		}
		
		
		_mac = Mac.getInstance("HmacSHA256");
		_mac.init(_key);
	}
	
	public byte[] genMac(String message)
	{
		byte[] bytes = message.getBytes();
		byte[] macResult = _mac.doFinal(bytes);
		return macResult;
	}
	
	public boolean match(String message, byte[] mac)
	{
		byte[] otherMac = genMac(message);
		
		if(otherMac.length != mac.length)
			return false;
		
		try
		{
			for(int i = 0; i < mac.length; i++)
			{
				if(mac[i] != otherMac[i])
				{
					return false;
				}
			}
			return true;
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			return false;
		}
		
	}
}
