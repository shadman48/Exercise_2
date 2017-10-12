import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
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
    		OutputStream out = socket.getOutputStream(); 
    	    DataOutputStream dos = new DataOutputStream(out);
    	    
    		byte[] byteArrayIn1 = new byte[100];
    		byte[] byteArrayIn2 = new byte[100];
    		byte[] byteArrayOut = new byte[100];
    		byte[] byteArrayReturn = new byte[1];
    		
    		
//    		dis.readFully(byteArrayIn1);
//    		dis.readFully(byteArrayIn2);
    		
    		
    		byte tempByte;
    		
    		//byte[] test = Files.readAllBytes(new File("/fileToRead.txt").toPath());
//    		Path fileLocation = Paths.get("fileToRead.txt");
//    		byte[] test = Files.readAllBytes(fileLocation);
    		//String s="51E0908A32F6867BD5DF384AC3A040D75D699CCB9B137545AEA15BA078C7C0FAF205BA90E5A4C7C95DC2358DD7DC89CBBBA408933D13C39B3945716F153C1840E77C184D0A60661BBF20FC4C9F0BFB043C0990437604AE6BC60102483A127DBC7220BD84";
    		 
    		//byte[] test = new BigInteger(s,16).toByteArray();
    		
    		for(int i = 0; i <= byteArrayOut.length - 1; i++)
    		{
    			byteArrayIn1[i] = (byte) dis.readUnsignedByte();
    			byteArrayIn2[i] = (byte) dis.readUnsignedByte();
    			
    			tempByte = twoBytesToShort(byteArrayIn1[i], byteArrayIn2[i]);
    			byteArrayOut[i] = tempByte;
    		}
    		
    		System.out.println("Hex-form: " + (bytesToHex(byteArrayOut)));
			
    		
    		// use java crc32
    		CRC32 crc32 = new CRC32();
    		crc32.update(byteArrayOut);
    		System.out.printf("%X\n", crc32.getValue());
    		long bytee = crc32.getValue();
    		
    		
    		byte [] bytes = ByteBuffer.allocate(8).putLong(bytee).array();
     	    System.out.println(Arrays.toString(bytes));
     	    
     	    
     	    byte [] bytess = new byte[4];
     	    bytess[0] = bytes[4];
     	    bytess[1] = bytes[5];
     	   	bytess[2] = bytes[6];
     	  	bytess[3] = bytes[7];
    	    System.out.println(Arrays.toString(bytess) + "\n" + bytesToHex(bytess));
   		
     	    
     	    
    		// send crc as sequence back to server
    		
    	  //  dos.write(bytess);
    	    int temp;
    	    for(int i = 0; i < 4; i++)
    	    {
    	    	temp = unsigned(bytess[i]);
    	    	bytess[i] = (byte)temp;
    	    	dos.writeByte(bytess[i]);
    	    	System.out.println(bytess[i]);
    	   		
    	    }

    		
    		// if server gets same crc then sends byte = 1 else 0
    		//dis.readFully(byteArrayReturn);
    		
    		//String correct = (bytesToHex(byteArrayReturn)).substring(1);
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
	

	
	
	
	
	
	public static int unsigned(byte b) 
	{
		return b & 0xff;
		
	}
	
	
	
	
	public static byte twoBytesToShort(byte b1, byte b2) 
	{
		return (byte) (((b1 & 0xF) << 4) | (b2 & 0xF));
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






