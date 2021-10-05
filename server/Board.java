import java.io.*;
import java.net.*;
import java.io.BufferedReader;
import java.util.Date;
import java.util.*;
import java.nio.ByteBuffer;
import javax.swing.* ;
import java.awt.Graphics ;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

class Board extends JFrame {
	Board(){
		count = 0;
		redStoneCount = 0;
		Gui gui = new Gui(this);
		this.g = gui;
	
	}
	public int[][] board = new int[19][19];
	private String redStones = ""; 
	public int color; 
	public int port;
	private int redStoneCount;
	private volatile int turn;
	private volatile int count;
	public Gui g;
	public String result;
	
	private int[] xd = {0,1,-1,1};
	private int[] yd = {1,0,1,1};
	public volatile  int[] point = {-10, -10, -10, -10};
	public volatile int gameStart = 0;
	private volatile int gameEnd = 0;


	

    public int getGameStart(){
        return gameStart;
    }

	public int getPort(){
		return port;
	}
	
	public void setGameEnd(int value){
		gameEnd = value;
	}
	public int getColor(){
		return color;
	}

	public int getGameEnd(){
		return gameEnd;
	}

   
	public void setPoint(int x, int y) {
		point[(count) * 2] = x;
		point[(count) * 2 + 1] = y;
		System.out.println(point[(count) * 2]);
		System.out.println(point[(count) *2 + 1]);
	}

	private boolean inBoardRange(int x, int y){
		if(x>18 || x<0 ||y>18 || y<0){
			return false;
		}
		return true;
	}
	private void deleteRedStone(int x , int y){
		String aRedString="";
		board[y][x]=0;
		if(x>7)
			x +=1;
		y = 19-y;
		char alphabet = (char)(65 + x);
		aRedString +=String.valueOf(alphabet);
		if(y < 10){
			aRedString += Integer.toString(0);
		}
		aRedString +=  Integer.toString(y);

		redStones = redStones.replaceAll(aRedString+":","");
		redStones = redStones.replaceAll(":"+aRedString,"");
		redStones = redStones.replaceAll(aRedString,"");
		redStoneCount -=1;
	
	}
	
	private boolean checkValid(int x, int y){
		if(x < 0 || x > 18){ // BADCOORD
        		g.printLog("[ERROR] BADCOORD : alphabet is incorrect");
        		return false;
        	}
        	if(y < 0 || y > 18){ // BADCOORD
            		g.printLog("[ERROR] BADCOORD : number is incorrect");
            		return false;
        	}
        	if(board[18 - y][x] != 0){ // NOTEMPTY
        		g.printLog("[ERROR] NOTEMPTY : invalid point");
        		return false;
        	}
        	return true;	
	}
		
	public boolean checkWin(int x, int y){
		//Check if game is end
		int isWin[] = new int[4];
		for(int i = 0; i < 4; i++) {
			isWin[i] = search(xd[i], yd[i], x, y);
			if(isWin[i] == 6) {
				gameEnd = 1;
				return true ;
			}
		}
		return false;
	}

	public int search(int xd, int yd, int x, int y) {
		int line = 1, value = board[y][x];
		int tmpx = x,tmpy = y;

		for(int i = 0; i < 2; i++) {
			while(true) {
				tmpx += xd;
				tmpy += yd;
				if(tmpx >= 0 && tmpx < 19 && tmpy >= 0 && tmpy < 19) {
					if(board[tmpy][tmpx] == value) {
						line++;
					}
					else {
							break;
					}
				}
				else {
					break;
				}
			}
			tmpx = x; tmpy = y;
			xd =- xd;
			yd =- yd;
		}
		return line;
	}
		
	public void clickEvent(int x, int y){
		if(x>18 || x<0 ||y>18 || y<0){
				return;
		}
		if(gameStart == 0){
			if(board[y][x] == -1){
				deleteRedStone(x,y);
			}
			else if(redStoneCount < 5 && storeRedStones(x, 18 - y)){
				redStoneCount += 1;
				redStonesString(x, y);
			}
			g.repaint();
			return ;
		}

		if(checkValid(x, 18 - y) == false || turn == 0 || gameEnd == 1){
			return ;
		}


		setPoint(x, 18 - y);
		board[y][x] = color;
		
		g.repaint();
        
		if (checkWin(x, y) == true){
			g.printLog("Single player Win Game end");
			result = "Single player Win Game end";
			gameEnd = 1;
			count = count + 1;
			return ;
		}

		if( x == 9 && y == 9 )
			count = 2;
		else 
            		count = count + 1;
					
	}
	public void updateBoard(int x, int y, int color){
		if(gameEnd == 1) {
			return ;
		}

		if(checkValid(x, y) == false) {
			gameEnd = 1;
            g.printLog("Single player Server WIN! Game end");
            result = "Single player Server WIN! Game end";
			return ;
		}

		setPoint(x, y);
		if( x == 9 && 18- y == 9 ){ // start as black
			count = 2;
        	} 
		else {
			count = count + 1;
		}
		board[18 - y][x] = color;

		if(count == 2) {
			g.repaint();
    		}
		if(checkWin(x, 18 - y) == true) {
			gameEnd = 1;
            g.printLog("Client Win! Game end");
            result = "Client Win! Game end";
			return;
		}
		if(count == 2) {
			count = 0;
		}		
	}

	public void setColor(int color){
		this.color = color;	
	}
	
	public void setTurn(int turn){
		this.turn = turn;
	}
	
	public void setCount(int count){
		this.count = count;
	}

	public int getCount() {
		return this.count;
	}

	public String stoneGenerator() {
		String stones = "";
		int number = 0 ;
		char alphabet = 0;
		
        	if(point[0] == 9 && point[1] == 9){
            		stones = "K10";
            		return stones;
        	}

		for( int i = 0 ; i < 2 ; i++ ){
			if( point[2*i] < 8 ){
				alphabet = (char) (point[2*i] + 65);        
			} else {
				alphabet = (char) (point[2*i] + 66);
			}
			
			number = point[2*i+1] + 1;
			
			stones = stones + String.valueOf(alphabet);
			if(number < 10){
				stones = stones + "0";
			}
			stones = stones + Integer.toString(number);
			if( i == 0 ) 
				stones = stones + ":";
		}
		System.out.println("stone generator: "+ stones);
		return stones;
	}

	private void resetBoard(){
		for(int i = 0; i < 19; i++){
			for(int j = 0; j < 19; j++){
				board[i][j] = 0;
				}
		}
	}

	public void redStoneGenerater(){
		int x, y, storedX, storedY;
		resetBoard();
		redStoneCount = (int)((Math.random() * 5) + 1);
		redStones = "";
		for(int i = 0; i < redStoneCount; i++){
			while(true) {
				x = (int)((Math.random() * 20));
				if(x != 8) {
					break;
				}
			}
			y = (int)((Math.random() * 19) + 1);
			char alphabet = (char)(65 + x);
			System.out.println("Generated redstones : "+alphabet + " " + y);
			if (x > 8) storedX = x-1;
			else storedX = x;
			storedY = y - 1;
			if(storeRedStones(storedX, storedY)){
				if(i != 0) {
					redStones = redStones + ":";
				}
				redStones = redStones+String.valueOf(alphabet);
				if(y < 10){
					redStones = redStones + Integer.toString(0);
				}
				redStones = redStones + Integer.toString(y);
			}
		}
	}
	private void redStonesString(int x, int y){
		if(x>7)
			x +=1;
		y = 19-y;
		char alphabet = (char)(65 + x);
		if(redStoneCount >1)
			redStones = redStones + ":";

		else
			redStones = "";
		redStones = redStones+String.valueOf(alphabet);
		if(y < 10){
			redStones = redStones + Integer.toString(0);
		}
		redStones = redStones + Integer.toString(y);

		System.out.println("Redstoned:: "+redStones);
	}

	private boolean storeRedStones(int x, int y){
		if(x == 9 && y == 9){
			return false;
		}
		if(this.board[18 - y][x] != -1){
			board[18 - y][x] = -1;
			return true;
		}
		else{
			return false;
		}
	}

	public String getRedStones(){
		return redStones;
	}
}
