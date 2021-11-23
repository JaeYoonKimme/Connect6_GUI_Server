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
		gui.printLog("Send Setting info");
		tcpAgent.sendMessage(gui.b.redStones); //RedStone
		tcpAgent.sendInt(gui.blackPort); //Port
		tcpAgent.sendInt(gui.whitePort); //Port
		tcpAgent.sendInt(gui.time); //Interval
		gui.printLog("Setting Done. Waiting AI Connection");

		//Send Start
		if (tcpAgent.recvMessage().equals("READY")){
			gui.printLog("AI Connected. Ready to start");
			gui.startButton.setEnabled(true);
			gui.waitStart();
			gui.printLog("Start Game");
			tcpAgent.sendMessage(new String("START"));
		}

		while(true) {
			String msg = "";
			msg = tcpAgent.recvMessage();
			if(msg.length() < 3){
				continue;	
			}

			int color = 0;

			if(msg.charAt(0) == 'W'){
				color = 1;
			}
			else{
				color = 2;
			}
			gui.setTurn(color);
			gui.setTurnWait();

			if(msg.length() > 8){
				gui.printLog(msg.substring(1), color);
			}
			else if(msg.contains("WIN")) {
				gui.printLog(msg.substring(1));
				gui.printWinner(color);
			}
			else if(msg.contains("TIE")) {
				gui.printLog(msg.substring(1));
				gui.printWinner(-1);
			}
			else {
				msg = msg.substring(1);
				gui.printLog(msg, color);
				int[] points = Message.parseString(msg);
				gui.b.updateBoard(points[0],points[1], points[2], points[3],color);
				gui.setTimerCnt(30);
				gui.repaint();
			}
		}



	}
}
