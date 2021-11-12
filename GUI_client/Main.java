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
		gui.waitSetting();
		System.out.println("RedStone : " + gui.b.redStones);
		System.out.println("TimeInterval: " + gui.time);


		gui.b.updateBoard(9, 9, 2);
		gui.repaint();
/*		TcpAgent tcpAgent = new TcpAgent(ip,port);


		//Send Setting Info
		gui.waitSetting();
		tcpAgent.sendMessage(gui.b.redStones); //RedStone
		tcpAgent.sendInt(50001); //Port
		tcpAgent.sendInt(50002); //Port
		tcpAgent.sendInt(10); //Interval
		
		//Send Start
		if (tcpAgent.recvMessage().equals("READY")){
			gui.waitStart();
			tcpAgent.sendMessage("START");
		}

		while(true) {
			String msg = tcpAgent.recvMessage();
			int color = 0;
			if(msg.charAt(0) == 'W')
				color = 1;
			else 
				color = 0;

			if(msg.charAt(1) == '$'){
				//gui.log(color, msg.substring(2));
			}
			else {
				msg = msg.substring(1);
				//gui.stone(color, msg.split(":"));
			}

		}
*/


	}
}
