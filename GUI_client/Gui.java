
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

@SuppressWarnings("serial")
class Gui extends JFrame implements ActionListener , MouseListener{ 
	private Button startButton , randomButton , settingDoneButton;
	private JTextField blackPortBox, whitePortBox, IntervalBox;
	private JLabel whiteLabel, blackLabel, timeLabel;
	private ButtonGroup colorGroup;	
	private JPanel leftPanel, rightPanel, settingPanel, portPanel, colorPanel, buttonPanel, logPanel;
	private JLabel portLabel, colorLabel;
	private JScrollPane scrollPane;
	private JTextPane logTextPane;
	private StyledDocument doc;
	private Style logTextStyle;
	private ImageIcon redStoneIcon;
	public Board b;

	private volatile boolean setFlag;
	private volatile boolean startFlag;

	public int whitePort, blackPort, time;

	private int rectSize = 20, boardSize = 400, ovalSize=13, xMargin = 20, yMargin = 30;
	
	Gui(){
		super();
		b =  new Board();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/icon.png"));
//		redStoneIcon = new ImageIcon(getClass().getResource("/redstone.png"));
//        Image icon = imageIcon.getImage();
//		setIconImage(icon);
		setLayout(null);
		setTitle("CONNSIX");
		leftPanelInit();
		rightPanelInit();
		settingPanelInit();		
		portPanelInit();
		buttonActionInit();
		buttonPanelInit();

		logPanelInit();
		logAreaInit();
		portPanelInit();
	
		settingPanel.add(portPanel);
		settingPanel.add(buttonPanel);
		rightPanel.add(settingPanel);
		logPanel.add(scrollPane);
		rightPanel.add(logPanel);
		addMouseListener(this);
		this.add(leftPanel);
		this.add(rightPanel);
		setBounds(100,100,810,450);
		setVisible(true);



		setFlag = false;
		startFlag = false;
	}

	private void logPanelInit(){
		logPanel = new JPanel();
		logPanel.setBounds(0, 150, 370, 260);
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
				Object[] options = {"1", "2", "3", "4", "5"};
             	String input = (String)JOptionPane.showInputDialog(null,"Select a number of red stones","",JOptionPane.INFORMATION_MESSAGE,redStoneIcon,options, options[0]);
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
		


		startButton = new Button("START");
		startButton.setEnabled(false);
		settingDoneButton = new Button("SETTING");
		
		settingDoneButton.addActionListener(new ActionListener() {
		    public void actionPerformed ( ActionEvent e ) {
				try { 

					whitePort =	Integer.parseInt(whitePortBox.getText());
					blackPort = Integer.parseInt(blackPortBox.getText());
					time = Integer.parseInt(IntervalBox.getText());
				} catch (NumberFormatException er) {
					printLog("[ERROR] Invalid Setting Value");
					return;
				}
					setFlag = true;
					randomButton.setEnabled(false);
	   	   			settingDoneButton.setEnabled(false);
					startButton.setEnabled(true);
		    }
		});

		startButton.addActionListener(new ActionListener() {
		    public void actionPerformed ( ActionEvent e ) {
				startFlag = true;
				startButton.setEnabled(false);
		    }
		});

	}
	
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
		settingPanel.setBounds(0, 15, 370, 130);
		settingPanel.setLayout(null);
		TitledBorder tb = new TitledBorder(new LineBorder(Color.black), "SETTING");
		tb.setTitleColor(Color.black);
		settingPanel.setBorder(tb);
	}

	private void portPanelInit(){
		int size =25;
		int xmargin = 10;
		portPanel = new JPanel();
		portPanel.setLayout(null);
		portPanel.setBounds(10, 20, 180, 100);
		
		whiteLabel = new JLabel("WHITE Port");
		whiteLabel.setBounds(xmargin, 0 , size*3 + 15, size);
		whitePortBox = new JTextField("");
		whitePortBox.setBounds(xmargin+ size*3 + 15, 0 , size*2+ 15, size);

		blackLabel = new JLabel("BLACK Port");
		blackLabel.setBounds(xmargin, size+ 5, size*3+ 15, size);
		blackPortBox = new JTextField("");
		blackPortBox.setBounds(xmargin+ size*3 +15 , size + 5, size*2 + 15 , size);

		
		timeLabel = new JLabel("Time");
		timeLabel.setBounds(xmargin, size*2 + 10, size*2+ 15, size);
		IntervalBox = new JTextField("");
		IntervalBox.setBounds(xmargin+ size*3 + 15, size*2 + 10 , size*2+ 15, size);
		
		portPanel.add(timeLabel);
		portPanel.add(IntervalBox);
		portPanel.add(whiteLabel);
		portPanel.add(blackLabel);
		portPanel.add(blackPortBox);
		portPanel.add(whitePortBox);
	}
	private void colorPanelInit(){
		colorPanel = new JPanel();
		colorPanel.setLayout(null);
		colorPanel.setBounds(120, 20, 115, 50);

		Font font = new Font("SansSerif", Font.BOLD, 10);	
	}


	private void buttonPanelInit(){
		buttonPanel = new JPanel();
		portPanel.setLayout(null);
		buttonPanel.setBounds(230, 17, 100, 90);
		buttonPanel.setLayout(new GridLayout(3,1));
		
		buttonPanel.add(randomButton);
		buttonPanel.add(settingDoneButton);
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
		} catch (Exception e){}

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
	
	public void writeCoordinate(Graphics2D g, int xMargin, int yMargin){
		for (int i = 1; i < 20 ; i++){
			String num=String.valueOf(20 - i);
			if((20-i)<10){
				num  = "0"+num;
			}
			g.drawString(num, xMargin-4   , yMargin + 5  +  rectSize * (i));
			
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
	}
	public void entireBoard(Graphics2D g , int xMargin, int yMargin){
		g.setColor(new Color(240,170,40));
		g.fillRect(xMargin - 5, yMargin+ 5,400, 400);
		g.setStroke(new BasicStroke(2));
		g.setColor(new Color(0,0,0));
		g.drawRect(xMargin - 5, yMargin + 5,400, 400);

	}
	public void drawGrid(Graphics2D g , int xMargin, int yMargin){
		for(int i=1; i<19; i++) {
			for(int j=1; j<19; j++) {
				g.setColor(new Color(0,0,0));
				g.drawRect( xMargin + rectSize*i, yMargin + rectSize*j,rectSize,rectSize);
			}
		}
	}

	public void drawStones(Graphics2D g , int xMargin, int yMargin){
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
/*
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
		*/
	}

	public void paint(Graphics g0) {
		Graphics2D g = (Graphics2D)g0;
		super.paint(g);
		entireBoard(g,xMargin,yMargin);
		g.setStroke(new BasicStroke(1));
		
		writeCoordinate(g,xMargin, yMargin);
		drawGrid(g, xMargin,yMargin);	
		drawStones(g, xMargin, yMargin);	
			
	}	

	@Override
	public void mouseClicked(MouseEvent e) {
		int x = (e.getX() - 10 - xMargin )/rectSize;
		int y = (e.getY() - 10 - yMargin )/rectSize; 
		b.redStoneClickEvent(x,y);
		repaint();		
       }

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void actionPerformed(ActionEvent e) {}

	public void waitSetting(){
		while(setFlag != true) {
			
		}
	}

	public void waitStart(){
		while(startFlag != true) {

		}
	}
}
