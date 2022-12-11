package security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileOperations {
	
	private File _file;
	
	public FileOperations(String fileName) throws IOException, IllegalArgumentException
	{
		_file = new File(fileName);
		if(!_file.exists())
		{
			_file.createNewFile();
		}
		else
		{
			if(!_file.isFile())
			{
				throw new IllegalArgumentException("File Name is not a file");
			}
		}
	}
	
	/** 
	 * Read the entire file in separated by line breaks.
	 */
	public String[] readFileAsArray() throws FileNotFoundException
	{
		ArrayList<String> buffer = new ArrayList<String>();
		Scanner r = new Scanner(_file);
		while(r.hasNextLine())
		{
			buffer.add(r.nextLine());
		}
		r.close();
		return buffer.toArray(new String[buffer.size()]);
	}
	
	/**
	 * Just Read in the entire file
	 */
	public String readFile() throws FileNotFoundException
	{
		String message = "";
		Scanner r = new Scanner(_file);
		while(r.hasNextLine())
		{
			message += r.nextLine() + "\n";
		}
		r.close();
		return message.substring(0, message.length()-1);
	}
	
	public void writeFile(String fileBody) throws IOException
	{
		FileWriter fw = new FileWriter(_file);
		fw.write(fileBody);
		fw.close();
	}
	
	/**
	 * Append to the end of the file
	 * @param str
	 * @throws IOException
	 */
	public void appendFile(String str) throws IOException
	{
		FileWriter fw = new FileWriter(_file, true);
		fw.append(str);
		fw.close();
	}
	
	
	/**
	 * Utility Method to write a byte stream to a file
	 * @param bytes
	 * @throws IOException
	 */
	public void writeByteStream(byte[] bytes) throws IOException
	{
		FileOutputStream out = new FileOutputStream(_file);
		out.write(bytes);
		out.close();
	}
	
	/**
	 * Utility Method to append a byte stream to the end of a file
	 * @param bytes
	 * @throws IOException
	 */
	public void appendByteStream(byte[] bytes) throws IOException
	{
		FileOutputStream out = new FileOutputStream(_file, true);
		out.write(bytes);
		out.close();
	}
	
	/**
	 * Read the entire file in as a byte stream
	 * @return
	 * @throws IOException
	 */
	public byte[] readByteStream() throws IOException
	{
		FileInputStream in = new FileInputStream(_file);
		byte[] encKey = new byte[in.available()];  
		in.read(encKey);
		in.close();
		return encKey;
	}
}
