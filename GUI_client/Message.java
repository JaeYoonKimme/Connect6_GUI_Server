import java.util.*;

class Message{
	public static int byteToInt(byte[] bytes) {
		return ((bytes[3] & 0xFF) << 24) | 
			((bytes[2] & 0xFF) << 16) | 
			((bytes[1] & 0xFF) << 8 ) | 
			((bytes[0] & 0xFF) << 0 );
	}

	public static byte[] intToByte(int intValue) {
		byte[] byteArray = new byte[4];
		byteArray[3] = (byte)(intValue >> 24);
		byteArray[2] = (byte)(intValue >> 16);
		byteArray[1] = (byte)(intValue >> 8);
		byteArray[0] = (byte)(intValue);
		return byteArray;
	}
}

