import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.zip.CRC32;

public final class Ex2Client 
{

	public static void main(String[] args) throws Exception 
	{
		// Create socket
    	try (Socket socket = new Socket("18.221.102.182", 38102)) 
        {
    		
    		System.out.println("Connected to server.");
			
    		
    		InputStream is = socket.getInputStream();
    		DataInputStream dis = new DataInputStream(is);
    		OutputStream out = socket.getOutputStream(); 
    	    DataOutputStream dos = new DataOutputStream(out);
    	    
    		byte[] byteArrayIn1 = new byte[100];
    		byte[] byteArrayIn2 = new byte[100];
    		byte[] byteArrayOut = new byte[100];
    		
    		
    		//Reads in the two parts of the byte, then "concats" them togeather. 
    		byte tempByte;
    		for(int i = 0; i <= byteArrayOut.length - 1; i++)
    		{
    			byteArrayIn1[i] = (byte) dis.readUnsignedByte();
    			byteArrayIn2[i] = (byte) dis.readUnsignedByte();
    			
    			tempByte = twoBytesToShort(byteArrayIn1[i], byteArrayIn2[i]);
    			byteArrayOut[i] = tempByte;
    		}
    		
    		System.out.println("Recived byted: " + (bytesToHex(byteArrayOut)));
			
    		
    		
    		
    		//Using java's CRC32.
    		CRC32 crc32 = new CRC32();
    		crc32.update(byteArrayOut);
    		System.out.printf("Generated CRC32: %X\n", crc32.getValue());
    		long bytee = crc32.getValue();
    		
    		
    		//converting long to bytes, then only taking non 0 values to new array.
    		byte [] bytes = ByteBuffer.allocate(8).putLong(bytee).array();
     	    byte [] bytess = new byte[4];
     	    bytess[0] = bytes[4];
     	    bytess[1] = bytes[5];
     	   	bytess[2] = bytes[6];
     	  	bytess[3] = bytes[7];
    	    
     	    
    		// send crc as sequence back to server
    	    int temp;
    	    for(int i = 0; i < 4; i++)
    	    {
    	    	temp = unsigned(bytess[i]);
    	    	bytess[i] = (byte)temp;
    	    	dos.writeByte(bytess[i]);
    	    }

    		
    		//Reads reply from server,if server gets same crc value then it sends byte = 1 else = 0.
    		int correct = 0;
    		correct = dis.readByte();
    		
    		if(correct == 1)
    			System.out.println("Response Good."); 
    		else
    			System.out.println("Response Bad."); 
        }
    	
    	catch(IOException e)
    	{
    		return;
    	}
	}
	

	
	
	
	
	/**
	 * This function applies a mask to a byte.
	 * @param b
	 * @return masked byte
	 */
	public static int unsigned(byte b) 
	{
		return b & 0xff;
		
	}
	
	
	
	/**
	 * This function takes in 2 bytes and concatenates them together.
	 * @param b1 first byte input
	 * @param b2 second byte input
	 * @return The concatenation of the two on binary level as a byte.
	 */
	public static byte twoBytesToShort(byte b1, byte b2) 
	{
		return (byte) (((b1 & 0xF) << 4) | (b2 & 0xF));
	}
	
	
	

	
	
	/**
	 * Converts bytes to Hex. 
	 * Returns string with Hex values.
	 */
	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
	public static String bytesToHex(byte[] bytes) 
	{
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	
	
	
	
	
	

}






