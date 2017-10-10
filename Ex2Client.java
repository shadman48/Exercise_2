import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.zip.CRC32;

public final class Ex2Client 
{

	public static void main(String[] args) throws Exception 
	{
		// Create socket
    	try (Socket socket = new Socket("18.221.102.182", 38102)) 
        {
    		// Read from INPUTSTREAM twice then concat bytes.
    		InputStream is = socket.getInputStream();
    		DataInputStream dis = new DataInputStream(is);
    		byte[] byteArrayIn = new byte[200];
    		byte[] byteArrayOut = new byte[100];
    		byte[] byteArrayReturn = new byte[1];
    		
    		
    		dis.readFully(byteArrayIn);
    		
    		int n = 0;
    		byte tempByte;
    		
    		
    		for(int i = 0; i <= byteArrayOut.length - 1; n++)
    		{
    			tempByte = twoBytesToShort(byteArrayIn[n], byteArrayIn[n+1]);
    			byteArrayOut[i] = tempByte;
    			i++;
    		}
    		
    		System.out.println("Hex-form: " + (bytesToHex(byteArrayOut)));
			
    		
    		// use java crc32
    		CRC32 crc32 = new CRC32();
    		crc32.update(byteArrayOut);
    		System.out.printf("%X\n", crc32.getValue());
    		long bytee = crc32.getValue();
    		
    		byte [] bytes = ByteBuffer.allocate(8).putLong(bytee).array();
     	    System.out.println(Arrays.toString(bytes));
     	    byte [] bob = Arrays.copyOfRange(bytes, 4, 8);
     	    System.out.println(Arrays.toString(bob));
     	    
    		
    		// send crc as sequence back to server
    		OutputStream out = socket.getOutputStream(); 
    	    DataOutputStream dos = new DataOutputStream(out);
    	    
    	    dos.write(bytes, 4, 4);
    	    
    		
    		
    		// if server gets same crc then sends byte = 1 else 0
    		dis.readFully(byteArrayReturn);
    		
    		System.out.println("~~~~" + (bytesToHex(byteArrayReturn))); 
        }
    	catch(IOException e)
    	{
    		return;
    	}
	}
	

	
	
	
	
	
	public static byte twoBytesToShort(byte b1, byte b2) 
	{
		return (byte) ((b1 << 4) | (b2));
	}
	
	
	

	
	
	
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






