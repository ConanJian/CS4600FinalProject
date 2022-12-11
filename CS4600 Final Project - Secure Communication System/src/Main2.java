
public class Main2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try
		{
			Receiver r = new Receiver();
			
			r.decryptMessageFromSender();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		System.out.println("Receiver has received the message");
	}

}
