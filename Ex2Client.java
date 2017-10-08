import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.zip.CRC32;

public final class Ex2Client 
{

	public static void main(String[] args) throws Exception 
	{
		// Create socket
    	try (Socket socket = new Socket("18.221.102.182", 38002)) 
        {
    		// Read from INPUTSTREAM twice then concat bytes.
    		InputStream is = socket.getInputStream();
//    		byte[] byteArrayIn = new byte[200];
//    		byte[] byteArrayOut = new byte[100];
    		byte[] byteArrayIn = new byte[2];
    		byte[] byteArrayOut = new byte[1];
    		
    		
//    		is.read(byteArrayIn);
    		
    		byteArrayIn[0] = 0x05;
    		byteArrayIn[1] = 0x0A;
    		
    		
    		int n = 0;
    		byte tempByte;
    		for(int i = 0; i <= byteArrayOut.length - 1; n++)
    		{
    			
    			
    			tempByte = twoBytesToShort(byteArrayIn[n], byteArrayIn[n+1]);
    			byteArrayOut[n] = tempByte;
    			
    			//System.out.println(byteArrayOut.toString() + " " + byteArrayOut);
    			
    			System.out.println("Hex-form: " + (bytesToHex(byteArrayOut)));
    			n++;
    			i++;
    			
    		}
    		
    		// use java crc32
    		crc32Sum(is);
    		
    		// send crc as sequence back to server
    		// if server gets crc then sends byte = 1 else 0
    		
    		
        }
    	catch(IOException e)
    	{
    		return;
    	}
	}
	
	protected static long crc32Sum(InputStream is) throws Exception {
		CRC32 crc = new CRC32();
		byte[] ba = new byte[(int) is.available()];
		String crcString = crc.toString();
		
		is.read(ba);
		crc.update(ba);
		is.close();
		System.out.println("Generated CRC32: " + crcString.substring(crcString.lastIndexOf("@") + 1));
		
		return crc.getValue();
	}
	
	
	public static byte twoBytesToShort(byte b1, byte b2) 
	{
		return (byte) ((b1 << 4) | (b2));
//		return (byte) ((b1 & 0xff00 << 8) | (b2 & 0xFF));
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






