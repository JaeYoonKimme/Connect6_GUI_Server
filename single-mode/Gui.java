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
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
class Gui extends JFrame implements ActionListener , MouseListener{ 

	private Button startButton , randomButton, settingButton;
	private JTextField portBox;
	private JRadioButton whiteBox, blackBox;
	private ButtonGroup colorGroup;	
	private JPanel leftPanel, rightPanel, settingPanel, portPanel, colorPanel, buttonPanel, logPanel;
	private JLabel portLabel, colorLabel;
	private JScrollPane scrollPane;
	private JTextPane logTextPane;
	private StyledDocument doc;
	private Style logTextStyle;

	private Board b;

	private int rectSize = 20, boardSize = 400, ovalSize=13, xMargin = 20, yMargin = 30;
	
	Gui(Board b){
		super();
		this.b = b;	
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Image icon = Toolkit.getDefaultToolkit().getImage("../icon.png");
		setIconImage(icon);
		setLayout(null);
		setTitle("Connect 6");
		leftPanelInit();
		rightPanelInit();
		settingPanelInit();		
		portPanelInit();
		colorPanelInit();	
		buttonActionInit();
		buttonPanelInit();

		logPanelInit();
		logAreaInit();
		portPanelInit();
		colorPanelInit();


		//addIntoButtonPanel();
	
		settingPanel.add(portPanel);
		settingPanel.add(colorPanel);
		settingPanel.add(buttonPanel);
		rightPanel.add(settingPanel);
		logPanel.add(scrollPane);
		rightPanel.add(logPanel);
		addMouseListener(this);
		this.add(leftPanel);
		this.add(rightPanel);
		setBounds(100,100,810,450);
		setVisible(true);
	
	}

	private void logPanelInit(){
		logPanel = new JPanel();
		logPanel.setBounds(0, 100, 370, 310);
		logPanel.setLayout(new GridLayout(1,1));
		TitledBorder tb = new TitledBorder(new LineBorder(Color.black), "LOG");
		tb.setTitleColor(Color.black);
		logPanel.setBorder(tb);
	}
	private void logAreaInit(){
		logTextPane = new JTextPane();
		logTextPane.setEditable(false);

		doc = logTextPane.getStyledDocument();
		logTextStyle = logTextPane.addStyle("",null);

		Font logFont = new Font("SansSerif", Font.BOLD, 12);
		logTextPane.setFont(logFont);
		scrollPane = new JScrollPane(logTextPane);
	}
	private void buttonActionInit(){	
		randomButton = new Button("REDSTONE");
		randomButton.addActionListener(new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				int redStoneCount = 0;
             	String input = JOptionPane.showInputDialog(null,"Enter a number of red stones", "", JOptionPane.INFORMATION_MESSAGE);
				if(input == null){
					return ;
				}
				try {
					redStoneCount = Integer.parseInt(input);
	
				} catch (NumberFormatException er) {
					printLog("[ERROR] Input should be an integer number 1~5");
						return;
					}	
                    		if( redStoneCount < 1 || redStoneCount > 5){	
					printLog("[ERROR] Input should be an integer number 1~5");	
					return;
				}
					
             
				b.redStoneGenerater(redStoneCount);
				repaint();
			}	
	
		});
		
		randomButton.setBounds(300, 10, 60, 20);


		startButton = new Button("START");
		startButton.addActionListener(new ActionListener() {
		    public void actionPerformed ( ActionEvent e ) {

			try {
				b.port = Integer.parseInt(portBox.getText());
			} catch (NumberFormatException er) {
				printLog("[ERROR] : Invalid Port Number");
				return ;
			}

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
		startButton.setBounds(300, 50, 60, 20);
	}
	/*
	private void addIntoButtonPanel(){
		buttonPanel.add(portLabel);
		buttonPanel.add(portBox);
		buttonPanel.add(colorLabel);
		buttonPanel.add(whiteBox);
		buttonPanel.add(blackBox);
		buttonPanel.add(randomButton);
		buttonPanel.add(startButton);
	}
	*/
	private void leftPanelInit(){
		leftPanel = new JPanel();
		leftPanel.setBounds(0, 0, 400, 420);
	}	
	private void rightPanelInit(){
		rightPanel = new JPanel();
		rightPanel.setBounds(420, 0, 400, 420);
		rightPanel.setLayout(null);
	}
	private void settingPanelInit(){
		settingPanel = new JPanel();
		settingPanel.setBounds(0, 15, 370, 80);
		settingPanel.setLayout(null);
		TitledBorder tb = new TitledBorder(new LineBorder(Color.black), "SETTING");
		tb.setTitleColor(Color.black);
		settingPanel.setBorder(tb);
	}

	private void portPanelInit(){
		portPanel = new JPanel();
		portPanel.setLayout(null);
		portPanel.setBounds(10, 20, 110, 50);

		portLabel = new JLabel("PORT");
		portLabel.setBounds(0, 0, 40, 50);  //10 50
		portBox = new JTextField("");
		portBox.setBounds(40, 10, 65, 30);

		portPanel.add(portLabel);
		portPanel.add(portBox);
	}
	private void colorPanelInit(){
		colorPanel = new JPanel();
		colorPanel.setLayout(null);
		colorPanel.setBounds(120, 20, 115, 50);

		Font font = new Font("SansSerif", Font.BOLD, 10);
		
		colorLabel = new JLabel("COLOR");
		colorLabel.setBounds(0, 0, 60, 50);  //140 190 -> 
		whiteBox = new JRadioButton("WHITE");
		whiteBox.setBounds(55, 0, 80, 20);
		whiteBox.setFont(font);
		blackBox = new JRadioButton("BLACK");
		blackBox.setBounds(55, 25, 80, 20);
		blackBox.setFont(font);
		whiteBox.setSelected(true);
		colorGroup = new ButtonGroup();
		colorGroup.add(whiteBox);
		colorGroup.add(blackBox);

		colorPanel.add(colorLabel);
		colorPanel.add(whiteBox);
		colorPanel.add(blackBox);
	}


	private void buttonPanelInit(){
		buttonPanel = new JPanel();
		portPanel.setLayout(null);
		buttonPanel.setBounds(260, 17, 100, 50);
		buttonPanel.setLayout(new GridLayout(2,1));

		buttonPanel.add(randomButton);
		buttonPanel.add(startButton);
	}


	public void printLog(String message){
		LocalTime now = LocalTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

		if(message.contains("Win")){
			StyleConstants.setForeground(logTextStyle, Color.red);
		}
		else{
			StyleConstants.setForeground(logTextStyle, Color.black);
		}		

		try {
			doc.insertString(doc.getLength(),"["+now.format(formatter)+"] "+message+"\n", logTextStyle);
		} catch (Exception e){
		}

		logTextPane.setCaretPosition(logTextPane.getDocument().getLength());
		logTextPane.requestFocus();
	}

	public void printNewLine(int n){
		try {
			for (int i = 0; i < n; i++){
				doc.insertString(doc.getLength(),"\n", logTextStyle);
			}
		} catch (Exception e){}
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
		g.setColor(new Color(240,170,40));
		g.fillRect(xMargin - 5, yMargin+ 5,400, 400);
		g.setStroke(new BasicStroke(2));
		g.setColor(new Color(0,0,0));
		g.drawRect(xMargin - 5, yMargin + 5,400, 400);
		g.setStroke(new BasicStroke(1));
		for (int i = 1; i < 20 ; i++){
			String num=String.valueOf(20 - i);
			g.drawString(num, xMargin-5   , yMargin + 5  +  rectSize * (i));
			
			char alphabet = '0';
			if(i < 9){
				alphabet = (char)(i+64);
			}
			else {
				alphabet = (char)(i+65);
			}
			String alphabetString = String.valueOf(alphabet);
			g.drawString(alphabetString,xMargin - 3 + i*(rectSize), yMargin + boardSize);
		}
		
		for(int i=1; i<19; i++) {
			for(int j=1; j<19; j++) {
				
				g.setColor(new Color(0,0,0));
				g.drawRect( xMargin + rectSize*i, yMargin + rectSize*j,rectSize,rectSize);
			}
		}
		for(int i=0; i<19; i++) {
			for(int j=0; j<19; j++) {
				if(b.board[i][j] != -2 ) {
					if(b.board[i][j] == 1) {
						g.setColor(new Color(255,255,255));
						g.fillOval(j*rectSize+ovalSize + xMargin, i*rectSize+ ovalSize + yMargin, ovalSize, ovalSize);
					}
					else if(b.board[i][j] == -1) {
						g.setColor(new Color(255,0,0));
						g.fillOval(j*rectSize+ovalSize + xMargin, i*rectSize+ ovalSize + yMargin, ovalSize, ovalSize);
					}
					else if(b.board[i][j] == 2)  {
						g.setColor(new Color(0,0,0));
						g.fillOval(j*rectSize+ovalSize + xMargin, i*rectSize+ ovalSize + yMargin, ovalSize, ovalSize);
					}
				}
			}
		}	

		g.setStroke(new BasicStroke(2));
		for(int i = 0; i < 2; i++){
			if(b.point[0]==-1){
				break;
			}

			g.setColor(new Color(0,0,255));
			g.drawOval(b.point[i * 2] * rectSize + xMargin + ovalSize ,(18 - b.point[i * 2 + 1]) * rectSize + yMargin+ ovalSize, 13, 13);
			if((i==0 &&(b.board[18-b.point[1]][b.point[0]]!= b.board[18-b.point[3]][b.point[2]]) ) ){
				break;
			}
		
	
		}	
	}	

	@Override
	public void mouseClicked(MouseEvent e) {
		int x = (e.getX() - 10 - xMargin )/rectSize;
		int y = (e.getY() - 10 - yMargin )/rectSize; 
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
