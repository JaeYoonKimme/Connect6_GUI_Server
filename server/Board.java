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

class Board extends JFrame implements ActionListener, MouseListener{
	private int[][] board = new int[19][19];
	private String redStones = ""; 
	private int color; 
	private int port;
	private int redStoneCount;
	private volatile int turn;
	private volatile int count;

	private int[] xd = {0,1,-1,1};
	private int[] yd = {1,0,1,1};
	

	private volatile  int[] point = {-10, -10, -10, -10};

	private Button startButton;
	private Button randomButton;
	private Button settingButton;
	private JTextArea logTextArea;
	private JComboBox<String> portBox;
	private JRadioButton whiteBox;
	private JRadioButton blackBox;
	private ButtonGroup colorGroup;

	private volatile int gameStart = 0;
	private volatile int gameEnd = 0;
	public String result;

	Board(){
		super();

		for(int i = 0; i < 19; i++){
			for(int j = 0; j < 19; j++){
				board[i][j] = 0;
			}
		}
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		setSize(800,600);
		setTitle("Connect 6");

		JPanel leftPanel = new JPanel();
		leftPanel.setBounds(0, 0, 600, 600);
		
		JPanel rightPanel = new JPanel();
		rightPanel.setBounds(600, 0, 200, 600);
		rightPanel.setLayout(null);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBounds(0, 0, 200, 200);
		buttonPanel.setLayout(null);


		JLabel portLabel = new JLabel("PORT");
		portLabel.setBounds(10, 10, 70, 50);
		portBox = new JComboBox<>(new String[] {"8080", "8081", "8082"});
		portBox.setBounds(80, 20, 85, 30);

		JLabel colorLabel = new JLabel("COLOR");
		colorLabel.setBounds(10, 55, 70, 50);
		whiteBox = new JRadioButton("WHITE");
		whiteBox.setBounds(80, 60, 100, 20);
		blackBox = new JRadioButton("BLACK");
		blackBox.setBounds(80, 80, 100, 20);
		whiteBox.setSelected(true);
		colorGroup = new ButtonGroup();
		colorGroup.add(whiteBox);
		colorGroup.add(blackBox);

		logTextArea = new JTextArea("***********Log History***********\n");
		logTextArea.setEditable(false);
		Font logFont = new Font("SansSerif", Font.BOLD, 10);
		logTextArea.setFont(logFont);
		JScrollPane scrollPane = new JScrollPane(logTextArea);
		scrollPane.setBounds(0,0,180,350);

		randomButton = new Button("적돌생성");
		randomButton.addActionListener(new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				redStoneGenerater();
				repaint();
			}
		});
		randomButton.setBounds(50, 110, 70, 20);


		startButton = new Button("START");
		startButton.addActionListener(new ActionListener() {
		    public void actionPerformed ( ActionEvent e ) {
				port = Integer.parseInt((String)portBox.getSelectedItem());

				if(whiteBox.isSelected() == true){
					color = 1;
				}
				else {
					color = 2;
				}

				disableButton();
				gameStart = 1;
		    }
		});
		startButton.setBounds(50, 135, 70, 20);


		buttonPanel.add(portLabel);
		buttonPanel.add(portBox);
		buttonPanel.add(colorLabel);
		buttonPanel.add(whiteBox);
		buttonPanel.add(blackBox);
		buttonPanel.add(randomButton);
		buttonPanel.add(startButton);
		buttonPanel.setBounds(0, 15, 180, 170);
		TitledBorder tb = new TitledBorder(new LineBorder(Color.black), "SETTING");
		tb.setTitleColor(Color.black);
		buttonPanel.setBorder(tb);
		//buttonPanel.setBackground(Color.white);
		rightPanel.add(buttonPanel);


		JPanel logPanel = new JPanel();
		logPanel.setBounds(0, 200, 180, 350);
		logPanel.setLayout(null);
		logPanel.add(scrollPane);



		rightPanel.add(buttonPanel);
		rightPanel.add(logPanel);

		this.add(leftPanel);
		this.add(rightPanel);
		setVisible(true);
		
		addMouseListener(this);
        count = 0;
		redStoneCount =0;
	}

	public void printLog(String message){
		//LocalTime now = LocalTime.now();
		//DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

		logTextArea.append(/*"["+now.format(formatter)+"] "+*/message+"\n");
		logTextArea.setCaretPosition(logTextArea.getText().length());
		logTextArea.requestFocus();
	}

    public int getGameStart(){
        return gameStart;
    }

	public int getPort(){
		return port;
	}

	public int getColor(){
		return color;
	}

	public int getGameEnd(){
		return gameEnd;
	}

	public void setGameEnd(int val){
		gameEnd = val;
	}

    public void disableButton(){
        startButton.setEnabled(false);
		randomButton.setEnabled(false);
		whiteBox.setEnabled(false);
		blackBox.setEnabled(false);
		portBox.setEnabled(false);
    }
	
	public void paint(Graphics g0) {
		Graphics2D g= (Graphics2D)g0;
		super.paint(g);

		for (int i = 1; i < 20 ; i++){
			String num=String.valueOf(20 - i);
			g.drawString(num,0, 15 + 30 * (i));
			
			char alphabet = '0';
			if(i < 9){
				alphabet = (char)(i+64);
			}
			else {
				alphabet = (char)(i+65);
			}
			String alphabetString = String.valueOf(alphabet);
			g.drawString(alphabetString, 30*(i), 600);
		}
		
		for(int i=1; i<19; i++) {
			for(int j=1; j<19; j++) {
				g.setColor(new Color(240,170,40));
				g.fillRect(30*i,30*j+10,30, 30);
				g.setColor(new Color(0,0,0));
				g.drawRect(30*i,30*j+10,30, 30);
			}
		}

		for(int i=0; i<19; i++) {
			for(int j=0; j<19; j++) {
				if(board[i][j] != -2 ) {
					if(board[i][j] == 1) {
						g.setColor(new Color(255,255,255));
						g.fillOval(j*30+20, i*30+30, 20, 20);
					}
					else if(board[i][j] == -1) {
						g.setColor(new Color(255,0,0));
						g.fillOval(j*30+20, i*30+30, 20, 20);
					}
					else if(board[i][j] == 2)  {
						g.setColor(new Color(0,0,0));
						g.fillOval(j*30+20, i*30+30, 20, 20);}
				}
			}
		}

		g.setStroke(new BasicStroke(2));
		for(int i = 0; i < 2; i++){
			g.setColor(new Color(0,0,255));
			g.drawOval(point[i * 2] * 30 + 20,(18 - point[i * 2 + 1]) * 30 + 30, 20, 20);
			if(point[i * 2] == 9 && point[i * 2 + 1] == 9 || count == 1){
				break;
			}
		}
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
	
	@Override
	public void mouseClicked(MouseEvent e) {
		

		int x = (e.getX() - 15)/30;
		int y = (e.getY() - 30)/30; 
	
		if(!inBoardRange(x,y))
			return ;

		if(gameStart == 0 && board[y][x]==-1){
			deleteRedStone(x,y);
			repaint();
			return ;
		}

		else if(gameStart == 0 && redStoneCount<5 && storeRedStones(x,18-y)){
			redStoneCount +=1;
			repaint();
			redStonesString(x,y);
		}

		if(turn == 0 || gameEnd == 1){
			return ;
		}

		if(checkValid(x, 18 - y) == false) {
			printLog("Client Win! Game end");
			result = "Client Win! Game end";
			gameEnd = 1;
			return ;
		}

		setPoint(x, 18 - y);
		board[y][x] = color;
		
        if( x == 9 && y == 9 ){ // start as black
           	count = 2;
        }
		else {
            count = count + 1;
		}
		repaint();
		
		if (checkWin(x, y) == true){
			printLog("Single player Win Game end");
			printLog("Single player Win Game end");
			gameEnd = 1;
			return ;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void actionPerformed(ActionEvent e) {
	}
	
	private boolean checkValid(int x, int y){
        if(x < 0 || x > 18){ // BADCOORD
            printLog("[ERROR] BADCOORD : alphabet is incorrect");
            return false;
        }
        if(y < 0 || y > 18){ // BADCOORD
            printLog("[ERROR] BADCOORD : number is incorrect");
            return false;
        }
        if(board[18 - y][x] != 0){ // NOTEMPTY
            printLog("[ERROR] NOTEMPTY : invalid point");
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

	public void updateBoard(int x, int y, int color){
		if(gameEnd == 1) {
			return ;
		}

		if(checkValid(x, y) == false) {
			gameEnd = 1;
            printLog("Single player Server WIN! Game end");
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
			repaint();
    	}
		if(checkWin(x, 18 - y) == true) {
			gameEnd = 1;
            printLog("Client Win! Game end");
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

	private void redStoneGenerater(){
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
