import java.io.*;
import java.net.*;
import java.io.BufferedReader;
import java.util.Date;
import java.util.*;
import java.nio.ByteBuffer;
import javax.swing.* ;
import java.awt.Graphics ;

class Server {
	private int color, clientColor, port;
	private Socket socket;
	private InputStream inputStream;
	private OutputStream outputStream;
	private Board board;

	Server(){
		board = new Board();
	}

	public void getArgument() {
		port = board.getPort();
		color = board.getColor();
		
		board.printLog("Setting Done");
		board.printLog("PORT : "+port);

		if(color == 1){
			clientColor = 2;
			board.printLog("COLOR : WHITE");
		}
		else {
			board.printLog("COLOR : BLACK");
			clientColor = 1;
		}

		System.out.println("Stone Color : " + color);
		System.out.println("Port Numver : " + port);
	}

	public void connect() {
		while(true){
		    if(board.getGameStart() == 1) 
			break;
		}
		this.getArgument();

		try {
			ServerSocket serverSocket = new ServerSocket(port);
			board.printLog("Connect Your AI With " + port + " Port");

			socket = serverSocket.accept();
			socket.setTcpNoDelay(true);

			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();
		} catch(SocketTimeoutException e){ /* Timeout occured on a socket read or accept*/
			board.printLog("Socket Timeout " + e);
		} catch(ConnectException e){ /* An error occurred while attempting to connect a socket */
			board.printLog("[ERROR] Couldn't get I/O for the connection " + e);
		} catch(SocketException e){ /* TcpNoDelay set error*/
			board.printLog("Set tcp_nodelay" + e);
		} catch(IOException e){
			board.printLog("catch" + e);
		}

		board.printLog("Connected!");
	}

	public void sendRedStones(){
		String redStones = board.getRedStones();
		int sizeOfRedStones = redStones.length();

		try {
			outputStream.write(intToByte(sizeOfRedStones), 0, 4);
			outputStream.write(redStones.getBytes(), 0, sizeOfRedStones);

			if(redStones.equals("")){
				board.printLog("Sent redStones : No redstone selected");
			}
			else {
				board.printLog("Sent RedStones : "+redStones);
			}
		} catch (IOException e) {
			System.err.println("Send redStone" + e);
		}
    }

	public int byteToInt(byte[] bytes) {
		return ((bytes[3] & 0xFF) << 24) | 
			((bytes[2] & 0xFF) << 16) | 
			((bytes[1] & 0xFF) << 8 ) | 
			((bytes[0] & 0xFF) << 0 );
	}

	public byte[] intToByte(int intValue) {
		byte[] byteArray = new byte[4];
		byteArray[3] = (byte)(intValue >> 24);
		byteArray[2] = (byte)(intValue >> 16);
		byteArray[1] = (byte)(intValue >> 8);
		byteArray[0] = (byte)(intValue);
		return byteArray;
	}

	private int[] parseString(String stones){
		int start = 0, end=0;
		int[] pointArray = new int[4]; //0,1 : firstDol jwapyo   1,2 : secondDol jwapyo
		char firstAlphabet='0', secondAlphabet='0';
		String firstPoint="", secondPoint="";
		if(stones.equals("K10")){
			Arrays.fill(pointArray, 9);
			return pointArray;
		}
		for(int i=0; i<stones.length(); i++){
			if(stones.charAt(i)==':'){
				end = i;
				firstPoint = stones.substring(start , end);
				System.out.println(firstPoint);
				start = end + 1;
			}
		}
		secondPoint = stones.substring(end+1);
		System.out.println(secondPoint);
		firstAlphabet = firstPoint.charAt(0);
		if(firstAlphabet - 65 > 8){
			pointArray[0] = firstAlphabet - 66;
		}
		else{
			pointArray[0] = firstAlphabet - 65;
		}
		pointArray[1] = Integer.parseInt(firstPoint.substring(1))-1;
		secondAlphabet = secondPoint.charAt(0);
		if(secondAlphabet - 65 > 8){
			pointArray[2] = secondAlphabet - 66;
		}
		else{
			pointArray[2] = secondAlphabet - 65;
		}
		pointArray[3] = Integer.parseInt(secondPoint.substring(1))-1;

		System.out.println("points of stone: " + pointArray[0]+" "+ pointArray[1]+" "+ pointArray[2]+" "+ pointArray[3]+" ");
		return pointArray;
	}

	public void sendStones(){
		try {
			while(board.getCount() != 2) {	
				if(board.getGameEnd() == 1){
					while(true){
					}
				}
			}
			String stones = board.stoneGenerator();
			System.out.println("stone Generator() : " + stones);
			int sizeOfStones = stones.length();
			outputStream.write(intToByte(sizeOfStones), 0, 4);
			System.out.println("sizeOfStones : " + sizeOfStones);
			byte[] bytesOfStones = stones.getBytes();
			outputStream.write(bytesOfStones);
			board.setCount(0);

			board.printLog("Sent "+stones);
		} catch (IOException e) {}
	}

	public void recvStones(){
		String stones = "";
		try{
			byte[] byteOfSize = new byte[4];
			inputStream.read(byteOfSize, 0, 4);
			int sizeOfStones = byteToInt(byteOfSize);
			byte[] stonesByte = new byte[sizeOfStones];
			inputStream.read(stonesByte, 0, sizeOfStones);
			stones = new String(stonesByte);
			System.out.println("Recved Stone from Server -> " + stones);
			board.printLog("Recved : " + stones);
		} catch (IOException e) {
			System.err.println("Recv Stone" + e);
		}
        
		if(stones.equals("K10")){
		    board.updateBoard(9, 9, clientColor);
		    return;
		}

		int[] pointArray = parseString(stones);

		for(int i = 0; i< 4; i++){
			System.out.println(pointArray[i]);
		}
		for(int i = 0; i < 2; i++){
			board.updateBoard(pointArray[2 * i], pointArray[2 * i + 1], clientColor); 
		}

		if(board.getGameEnd() == 1){
			while(true){
			}
		}
	}

	public void start(){

		System.out.println("STONE");		
		if(color == 1){ // server is white
			System.out.println("SERVER IS WHITE");
			board.setTurn(0);
			board.printLog("TURN : CLIENT");
			recvStones();
		}
		else { // server is black
			System.out.println("SERVER IS BLACK");
			board.printLog("TURN : SERVER");
			board.setTurn(1);
			sendStones();
            board.setTurn(0);
			board.printLog("TURN : CLIENT");
			recvStones();
		}

		while(true) {
			System.out.println("DRAW AND WAIT");
			board.printLog("TURN : SERVER");
			board.setTurn(1);
			sendStones();
			board.setTurn(0);
			board.printLog("TURN : CLIENT");
			recvStones();
		}
        
	}

	public static void main(String[] args){
		Server server = new Server();
		server.connect();		
		server.sendRedStones();	
		server.start();
		//server.echo();
	}
}
