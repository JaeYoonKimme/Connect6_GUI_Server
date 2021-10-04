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


class Gui extends JFrame implements ActionListener , MouseListener{ 


	private Button startButton;
	private Button randomButton;
	private Button settingButton;
	private JTextArea logTextArea;
	private JComboBox<String> portBox;
	private JRadioButton whiteBox;
	private JRadioButton blackBox;
	private ButtonGroup colorGroup;	
	
	private Board b;
	
	Gui(Board b){
		super();
		this.b = b;	
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
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
				b.redStoneGenerater();
				repaint();
			}
		});
		
		randomButton.setBounds(50, 110, 70, 20);


		startButton = new Button("START");
		startButton.addActionListener(new ActionListener() {
		    public void actionPerformed ( ActionEvent e ) {
			b.port = Integer.parseInt((String)portBox.getSelectedItem());

			if(whiteBox.isSelected() == true){
				b.color = 1;
			}
			else {
				b.color = 2;
			}

			disableButton();
			b.gameStart = 1;	

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
		rightPanel.add(buttonPanel);


		JPanel logPanel = new JPanel();
		logPanel.setBounds(0, 200, 180, 350);
		logPanel.setLayout(null);
		logPanel.add(scrollPane);



		rightPanel.add(buttonPanel);
		rightPanel.add(logPanel);
		
		addMouseListener(this);
		this.add(leftPanel);
		this.add(rightPanel);
		setBounds(100,100,800,600);
		setVisible(true);
		
		
	
	}
	public void printLog(String message){

		logTextArea.append(/*"["+now.format(formatter)+"] "+*/message+"\n");
		logTextArea.setCaretPosition(logTextArea.getText().length());
		logTextArea.requestFocus();
	}

	public void disableButton(){
        	startButton.setEnabled(false);
		randomButton.setEnabled(false);
		whiteBox.setEnabled(false);
		blackBox.setEnabled(false);
		portBox.setEnabled(false);
    }

	public void paint(Graphics g0) {
		Graphics2D g = (Graphics2D)g0;
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
				if(b.board[i][j] != -2 ) {
					if(b.board[i][j] == 1) {
						g.setColor(new Color(255,255,255));
						g.fillOval(j*30+20, i*30+30, 20, 20);
					}
					else if(b.board[i][j] == -1) {
						g.setColor(new Color(255,0,0));
						g.fillOval(j*30+20, i*30+30, 20, 20);
					}
					else if(b.board[i][j] == 2)  {
						g.setColor(new Color(0,0,0));
						g.fillOval(j*30+20, i*30+30, 20, 20);}
				}
			}
		}

		g.setStroke(new BasicStroke(2));
		for(int i = 0; i < 2; i++){
			g.setColor(new Color(0,0,255));
			g.drawOval(b.point[i * 2] * 30 + 20,(18 - b.point[i * 2 + 1]) * 30 + 30, 20, 20);
			if(b.point[i * 2] == 9 && b.point[i * 2 + 1] == 9 || b.getCount() == 1){
				break;
			}
		}
	
		
	}	

	@Override
	public void mouseClicked(MouseEvent e) {
		int x = (e.getX() - 15)/30;
		int y = (e.getY() - 30)/30; 
		System.out.println("X : " +x + "Y : "+y);	
		b.clickEvent(x,y);
		
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
	
}
