import java.util.*;

public class Main {

	public static void main(String[] args) {
		int port;
		String ip;

		if(args.length != 2) {
			System.err.println("Invalid Argument");
			System.exit(1);
		}

		ip = args[0];
		port = 0;
		try {
			port = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			System.err.println("ParseInt Exception(port)");
		}

		TcpAgent tcpAgent = new TcpAgent(ip,port);
	
		tcpAgent.sendMessage("A10:B12");
		tcpAgent.sendInt(50001);
		tcpAgent.sendInt(50002);
		tcpAgent.sendInt(10);

		if((tcpAgent.recvMessage()).equals("start")) {
			tcpAgent.sendMessage("start");
		}
	}
}
