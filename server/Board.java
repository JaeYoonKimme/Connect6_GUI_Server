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
	private int color, turn;
    private volatile int count;

	private int[] xd = {0,1,-1,1};
	private int[] yd = {1,0,1,1};

    private int[] point = {0, 0, 0, 0};
	
    Board(){
		super();
		for(int i = 0; i < 19; i++){
			for(int j = 0; j < 19; j++){
				board[i][j] = 0;
			}
		}

		super.setLayout(new FlowLayout());
		setBounds(100,100,600,600);
		JPanel p = new JPanel();

		p.setSize(600,600);
		add(p);
		addMouseListener(this);
		super.setVisible(true);
        count = 0;
		redStoneGenerater();
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
        point[(count - 1) * 2] = x;
        point[(count-1) *2 + 1] = y;
    }

    public boolean myTurn(){
        if (turn == 1)
            return true;
        else
            return false;
    }
	
    @Override
	public void mouseClicked(MouseEvent e) {
		int x= (e.getX() - 15)/30;
		int y = (e.getY() - 30)/30;

       
		if (checkValid(x,y) == false || myTurn() == false) { 
			return ;
		}
		
        count = count + 1; 
        setPoint(x, y);
		board[y][x] = color;
		repaint();

		if (checkWin(x, y) == true){
			System.out.println("Game end");
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
		if(x < 0 && x > 18){
			return false;
		}
		if(y < 0 && y > 18){
			return false;
		}
		if(board[y][x] != 0){
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

        for( int i = 0 ; i < 2 ; i++ ){
            if( point[2*i] < 8 ){
                alphabet = (char) (point[2*i] + 65);        
            } else {
                alphabet = (char) (point[2*i] + 66);
            }

            number = point[2*i+1] + 1;
            stones = stones + String.valueOf(alphabet);
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
			if (x >8) storedX = x-1;
			else storedX = x;
			storedY = y;
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
