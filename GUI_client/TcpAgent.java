import java.io.*;
import java.net.*;
import java.net.Socket;
import java.net.InetAddress;


class TcpAgent {
    private int port ;
    private String ipAddr;
    private InputStream inputStream;
    private OutputStream outputStream;
	private Socket socket;

    TcpAgent(String ipAddr, int port) {
        this.port = port;
        this.ipAddr = ipAddr;
		connect();
    }

    public void connect() {
        try {
			System.out.println("Connect to :"+ipAddr+":"+port);
            socket = new Socket(ipAddr, port);
            socket.setTcpNoDelay(true);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
        } catch (IOException e) {
			System.err.println("[connect] " + e);
			System.exit(1);
		}
		System.out.println("Connected!");
    }

    public void sendMessage(String msg) {
		try {
			outputStream.write(Message.intToByte(msg.length()), 0, 4);
			outputStream.write(msg.getBytes());
		} catch (IOException e) {
			System.err.println("[sendMessage] " + e);
			System.exit(1);
		}
	}

    public String recvMessage() {
		String msg = "";
		try {
	        byte[] byteOfSize = new byte[4];
			inputStream.read(byteOfSize, 0, 4);
			int sizeOfMsg = Message.byteToInt(byteOfSize);
			byte[] msgByte = new byte[sizeOfMsg];
			inputStream.read(msgByte, 0, sizeOfMsg);

			msg = new String(msgByte);
		} catch (IOException e) {
			System.err.println("[recvMessage] " + e);
			System.exit(1);
		}
		return msg;
    }

	public void sendInt(int val) {
		try {
			outputStream.write(Message.intToByte(val), 0, 4);
		} catch (IOException e) {
			System.err.println("[sendInt] " + e);
			System.exit(1);
		}
	}

    public void sendSettingInfo(){
        String redStone;
        int interval, blackPort, whitePort;
    }
    
	public void getRedStones() {
    }
}
