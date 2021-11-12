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

		Gui gui = new Gui();
		TcpAgent tcpAgent = new TcpAgent(ip,port);


		//Send Setting Info
		gui.waitSetting();
		System.out.println("Setting Done. Send info");
		tcpAgent.sendMessage(gui.b.redStones); //RedStone
		tcpAgent.sendInt(gui.blackPort); //Port
		tcpAgent.sendInt(gui.whitePort); //Port
		tcpAgent.sendInt(gui.time); //Interval
			
		//Send Start
		if (tcpAgent.recvMessage().equals("READY")){
			gui.waitStart();
			System.out.println("Send START message");
			tcpAgent.sendMessage(new String("START"));
		}

		while(true) {
			String msg = tcpAgent.recvMessage();
			
			int color = 0;
			if(msg.charAt(0) == 'W')
				color = 1;
			else 
				color = 2;

			if(msg.length() > 8){
				gui.printLog(msg.substring(1));
			}
			else {
				msg = msg.substring(1);
				int[] points = Message.parseString(msg);
				gui.b.updateBoard(points[0],points[1],color);
				gui.repaint();
				gui.b.updateBoard(points[2],points[3],color);
				gui.repaint();
			}
		}



	}
}
