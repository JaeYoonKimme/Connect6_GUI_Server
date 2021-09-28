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

class Board extends JFrame implements ActionListener, MouseListener{
	private int[][] board = new int[19][19];
	private String redStones ; 
	private int color; 
	private volatile int turn;
	private volatile int count;
	
	private int[] xd = {0,1,-1,1};
	private int[] yd = {1,0,1,1};
	
	private volatile  int[] point = {0, 0, 0, 0};

	private Button startButton;
	private Button settingButton;
	private JTextArea logTextArea;
	private JTextField portField;
	private JComboBox<String> colorBox;

	private volatile int gameStart = 0;
	private volatile int settingDone = 0;
	
	Board(){
		super();

		for(int i = 0; i < 19; i++){
			for(int j = 0; j < 19; j++){
				board[i][j] = 0;
			}
		}
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new GridLayout(1,2));
		setSize(1400,630);
		setTitle("Connect 6");


		JPanel leftPanel = new JPanel();
		leftPanel.setSize(600,600);
		
		JPanel rightPanel = new JPanel();
		rightPanel.setSize(800,600);
		rightPanel.setLayout(new FlowLayout());


		JLabel portLabel = new JLabel("Select Port Number : ");
		portField = new JTextField("8080");

		JLabel colorLabel = new JLabel("Select Server's Color : ");
		colorBox = new JComboBox<>(new String[] {"BLACK","WHITE"});

		logTextArea = new JTextArea("*********************** Log History ***********************", 30, 30);
		logTextArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(logTextArea);

		settingButton = new Button("OPEN SERVER");
		settingButton.addActionListener(new ActionListener() {
			public void actionPerformed ( ActionEvent e ) {
				int port = Integer.parseInt(portField.getText());
				logTextArea.append("port : " + port);
				settingDone = 1;
			}
		});

		startButton = new Button("START");
		startButton.addActionListener(new ActionListener() {
		    public void actionPerformed ( ActionEvent e ) {
				gameStart = 1;
		    }
		});
		startButton.setSize(50,50);


		rightPanel.add(portLabel);
		rightPanel.add(portField);
		rightPanel.add(colorLabel);
		rightPanel.add(colorBox);
		rightPanel.add(settingButton);
		rightPanel.add(startButton);
		rightPanel.add(scrollPane);	

		this.add(leftPanel);
		this.add(rightPanel);
		setVisible(true);
		
		addMouseListener(this);
        count = 0;
		redStoneGenerater();
	}

    public int getGameStart(){
        return gameStart;
    }

	public int getSettingDone(){
		return settingDone;
	}

	public void resetSettingDone(){
		settingDone = 0;
	}

    public void disableStartButton(){
        startButton.setEnabled(false);
    }

	public void disableSettingButton(){
		settingButton.setEnabled(false);
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
	}

	public void setPoint(int x, int y) {
		System.out.println("count " + count);
		point[(count) * 2] = x;
		point[(count) *2 + 1] = y;
		System.out.println(point[(count) * 2]);
		System.out.println(point[(count) *2 + 1]);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		int x = (e.getX() - 15)/30;
		int y = (e.getY() - 30)/30; 
		System.out.println("X: "+x+" Y: "+y);
		if (checkValid(x,y) == false || turn == 0 ) { 
			return ;
		}

		setPoint(x, 18 - y);
		board[y][x] = color;
		
        	if( x == 9 && y == 9 ){ // start as black
           		count = 2;
        	} else
            		count = count + 1; 

		repaint();

		if (checkWin(x, y) == true){
			System.out.println("Server Win! Game end");

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
		if(x < 0 || x > 18){
			return false;
		}
		if(y < 0 || y > 18){
			return false;
		}
		if(board[18 - y][x] != 0){
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
		if(checkValid(x, y) == false) {
			System.out.println("Wrong input");
			return ;
		}
		board[18 - y][x] = color;
		repaint();
        	if(checkWin(x, 18 - y) == true) {
            		System.out.println("Client Win! Game end");
            		return;
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

	private void redStoneGenerater(){
		int x, y, storedX, storedY;
		int numberOfRedstones;
		numberOfRedstones = (int)((Math.random() * 5) + 1);
		redStones = "";
		for(int i = 0; i < numberOfRedstones; i++){
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
