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

class Gui extends JFrame implements ActionListener , MouseListener { 
	public Button startButton , randomButton , settingButton, sendButton;
	private JPanel leftPanel, rightPanel, settingPanel, portPanel, colorPanel, buttonPanel, logPanel;
	private JLabel portLabel, colorLabel;
	private JScrollPane scrollPane;
	private JTextPane logTextPane;
	private StyledDocument doc;
	private Style logTextStyle;
	private ImageIcon redStoneIcon;
	public Board b;
	private timerThread t;


	private String whiteName, blackName;
	private JPanel timerPanel, whitePanel, blackPanel;
  	private Timer timer;
  	private int timerCnt;
  	private JLabel timerLabel, turnLabel;

	private JPanel titlePanel;
	private JLabel titleLabel, blackTeamLabel, whiteTeamLabel;


	private volatile boolean setFlag;
	private volatile boolean startFlag;

	public int whitePort, blackPort, time;

	private int  boardSize = 680,  rectSize = boardSize/20 , ovalSize=(int)(rectSize*0.65), xMargin = 20, yMargin = 25, winX=960, winY = 720;

	private int turn;
	private JPanel turnPanel;

	Gui(){
		super();
		b =  new Board();
		t = new timerThread(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/icon.png"));
//		redStoneIcon = new ImageIcon(getClass().getResource("/redstone.png"));
//        Image icon = imageIcon.getImage();
//		setIconImage(icon);
		setLayout(null);
		setTitle("CONNSIX");

		leftPanelInit();
		rightPanelInit();

		buttonActionInit();
		buttonPanelInit();
		sendButton.setEnabled(false);
		startButton.setEnabled(false);

		logPanelInit();
		logAreaInit();
		settingPanelInit();		
		timerPanelInit();
		turnPanelInit();

		titlePanelInit();
		rightPanel.add(titlePanel);


		logPanel.add(scrollPane);
		rightPanel.add(logPanel);
		rightPanel.add(buttonPanel);
		addMouseListener(this);
		rightPanel.add(turnPanel);
		rightPanel.add(timerPanel);
		
		this.add(leftPanel);
		this.add(rightPanel);
		setBounds(50,50,winX,winY);
		setVisible(true);

		setFlag = false;
		startFlag = false;
		//startTimer();
	}
	
	private void turnPanelInit(){
      int xLen = (int)(winX - (xMargin + boardSize));
      turnPanel = new JPanel();
      turnPanel.setBounds(0,(int)(winY*0.4)  , winX-(xMargin+boardSize)  , (int)(winY*0.12));
      TitledBorder tb = new TitledBorder(new LineBorder(Color.black), "TURN");
      tb.setTitleColor(Color.black);
      turnPanel.setBorder(tb);
      turnLabel = new JLabel("-");
      turnLabel.setFont(new Font("SansSerif",Font.BOLD,35));
      turnPanel.add(turnLabel);
  }

	private void timerPanelInit(){
		int xLen = (int)(winX - (xMargin + boardSize));
		timerPanel = new JPanel();
		timerPanel.setBounds(0,(int)(winY*0.22)  , winX-(xMargin+boardSize)  , (int)(winY*0.12));
		TitledBorder tb = new TitledBorder(new LineBorder(Color.black), "TIMER");
		tb.setTitleColor(Color.black);
		timerPanel.setBorder(tb);
		timerLabel = new JLabel("-");
		timerLabel.setFont(new Font("SansSerif",Font.BOLD,35));
		timerCnt = 30;
		timerPanel.add(timerLabel);
     }
	  private void titlePanelInit(){
        int xLen = (int)(winX - (xMargin + boardSize));
        titlePanel = new JPanel();
        titlePanel.setBounds(0,(int)(winY*0.05)  , winX-(xMargin+boardSize)  , (int)(winY*0.12));
        titlePanel.setLayout(new GridLayout(3,1));
        titleLabel = new JLabel("BY");
        titleLabel.setFont(new Font("SansSerif",Font.BOLD,25));
        blackTeamLabel = new JLabel("CONNSIX");
        blackTeamLabel.setFont(new Font("SansSerif",Font.BOLD,25));
        whiteTeamLabel = new JLabel("TEAM EVERUST");
        whiteTeamLabel.setFont(new Font("SansSerif",Font.BOLD,25));
		whiteTeamLabel.setForeground(Color.cyan);
        blackTeamLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        whiteTeamLabel.setHorizontalAlignment(JLabel.CENTER);
        titlePanel.add(blackTeamLabel);
        titlePanel.add(titleLabel);
        titlePanel.add(whiteTeamLabel);
    }
	private void logPanelInit(){
		logPanel = new JPanel();
		logPanel.setBounds(0,(int)( winY*0.7), (int)(winX-(boardSize+xMargin)), (int)(winY*0.25));
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
		int xlen = (int)(winX-(xMargin+boardSize));
		int ylen =100; 
		randomButton = new Button("REDSTONE");
		randomButton.setBounds((int)(xlen*0.08) , (int)(ylen*0.20) , (int)(xlen*0.35),30);
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
				b.redStoneGenerater(redStoneCount);
				repaint();
	    		}   
		});

		startButton = new Button("START");
		startButton.setBounds((int)(xlen*0.58),(int)(ylen*0.20)  ,(int)(xlen*0.35),30);
		startButton.setEnabled(false);
		startButton.addActionListener(new ActionListener() {
				public void actionPerformed (ActionEvent e) {
					startFlag = true;
					startButton.setEnabled(false);
					t.start();
				}
				
		});
		
		settingButton = new Button("SETTING");
		settingButton.setBounds((int)(xlen*0.08) ,(int) (ylen*0.6) , (int)(xlen*0.35),30);
	        settingButton.addActionListener(new ActionListener() {
			public void actionPerformed ( ActionEvent e ) {
							JTextField blackNameField = new JTextField("Black");
                    JTextField whiteNameField = new JTextField("White");
                    JTextField whiteField = new JTextField("8082");
                    JTextField blackField = new JTextField("8081");
                    JTextField timeField = new JTextField("10");
                    Object[] message = {
                    "BLACK TEAM NAME", blackNameField,
                    "BLACK PORT", blackField,
                    "WHITE TEAM NAME", whiteNameField,
                    "WHITE PORT", whiteField,
                    "TIME INTERVAL", timeField,
			};
        	        int option = JOptionPane.showConfirmDialog(null, message, "Options", JOptionPane.OK_CANCEL_OPTION);
       			if (option == JOptionPane.OK_OPTION){
	    			try {
					whitePort = Integer.parseInt(whiteField.getText());
                    blackPort = Integer.parseInt(blackField.getText());
                    time = Integer.parseInt(timeField.getText());
                    whiteName = whiteNameField.getText();
                    blackName = blackNameField.getText();
                    printLog("[SETTING]");
                    printLog("White Port : " + whitePort);
                    printLog("Black Port : " + blackPort);
                    printLog("Time Interval : " + time);
                    blackTeamLabel.setText(blackName + " (BLACK)");
                    titleLabel.setText(" VS ");
                    whiteTeamLabel.setText(whiteName + " (WHITE)");
					whiteTeamLabel.setForeground(Color.black);

					sendButton.setEnabled(true);
	    			} catch (NumberFormatException er) {
					printLog("[ERROR] Invalid Setting Value");
					return;
	    			}
	 		}
			repaint();
            }
        });

		sendButton = new Button("READY");
		sendButton.setBounds((int)(xlen*0.58) , (int)(ylen*0.6) , (int)(xlen*0.35),30);
		sendButton.addActionListener(new ActionListener() {
	    		public void actionPerformed ( ActionEvent e ) {
	    			setFlag = true;
	    			randomButton.setEnabled(false);
	    			sendButton.setEnabled(false);
	    			settingButton.setEnabled(false);
	    		}
		});
	}
	
   private void leftPanelInit(){
		leftPanel = new JPanel();
		leftPanel.setBounds(0, 0, xMargin+boardSize, winY);
	}	
	private void rightPanelInit(){
		rightPanel = new JPanel();
		rightPanel.setBounds(xMargin+boardSize, 0, winX-(xMargin+ boardSize), winY);
		rightPanel.setLayout(null);
	}
	private void settingPanelInit(){
		settingPanel = new JPanel();
		settingPanel.setBounds(0, (int)(winY*0.5), winX-(xMargin+boardSize), 100 );
		settingPanel.setLayout(null);
		TitledBorder tb = new TitledBorder(new LineBorder(Color.black), "SETTING");
		tb.setTitleColor(Color.black);
		settingPanel.setBorder(tb);
	}

	private void buttonPanelInit(){
		buttonPanel = new JPanel();
		buttonPanel.setBounds(0, (int)(winY*0.55), winX- (xMargin+boardSize),  100  );
		buttonPanel.setLayout(null);
		
		TitledBorder tb = new TitledBorder(new LineBorder(Color.black), "SETTING");
		tb.setTitleColor(Color.black);
		buttonPanel.setBorder(tb);

		buttonPanel.add(randomButton);
		buttonPanel.add(settingButton);
		buttonPanel.add(sendButton);
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
	public void printLog(String message, int color){
		String name = "";
		if(color == 1)
			name = whiteName;
		else 
			name = blackName;

		LocalTime now = LocalTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

		if(message.contains("Win")){
			StyleConstants.setForeground(logTextStyle, Color.red);
		}
		else{
			StyleConstants.setForeground(logTextStyle, Color.black);
		}		

		try {
			doc.insertString(doc.getLength(),"["+now.format(formatter)+"] "+name + " "+message+"\n", logTextStyle);
		} catch (Exception e){}

		logTextPane.setCaretPosition(logTextPane.getDocument().getLength());
		logTextPane.requestFocus();
	}
	public void printWinner(int color) {
		timer.stop();
		turnLabel.setFont(new Font("SansSerif",Font.BOLD,20));
		turnLabel.setForeground(Color.red);
		if (color == 1){
			StyleConstants.setForeground(logTextStyle, Color.red);
			turnLabel.setText(whiteName + "(WHITE) WIN!");
		}
		else if(color == 2){
			turnLabel.setText(blackName + "(BLACK) WIN!");
		}
		else {
			turnLabel.setText("TIE");
		}
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
		g.fillRect(xMargin -5, yMargin + 5,boardSize, boardSize);
		g.setStroke(new BasicStroke(2));
		g.setColor(new Color(0,0,0));
		g.drawRect(xMargin-5 , yMargin +5,boardSize, boardSize);

	}
	public void drawGrid(Graphics2D g , int xMargin, int yMargin){
		for(int i=1; i<19; i++) {
			for(int j=1; j<19; j++) {
				g.setColor(new Color(0,0,0));
			g.drawRect( xMargin + rectSize*i , yMargin + rectSize*j  ,rectSize,rectSize);
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
		g.setColor(new Color(0,0,255));
		for(int i = 0; i < 2; i++){
			if(b.lastStone[0]==-500){
				break;
			}
			g.drawOval(b.lastStone[i * 2] * rectSize + xMargin + ovalSize ,(18 - b.lastStone[i * 2 + 1]) * rectSize + yMargin+ ovalSize, ovalSize, ovalSize);
		}
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
		if(startFlag == true)
			return;
		int x = (e.getX() - (rectSize/2) - xMargin )/rectSize;
		int y = (e.getY() - (rectSize/2) - yMargin )/rectSize; 
		if(!b.redStoneClickEvent(x,y))
			return;
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

	public void setTurn (int color) {
		this.turn = color;
	}

	public void setTimerCnt(int n){
		timerCnt = n;
	}

	public void setTurnWait(){
			if ( turn == 2 ) 
				turnLabel.setText("WHITE");
			else
				turnLabel.setText("BLACK") ;
	}
	public void startTimer(){
   	ActionListener actListener = new ActionListener() {
     		public void actionPerformed(ActionEvent event){
      		if(timerCnt != 0){
        			timerCnt -= 1;
      		}
      		timerLabel.setText("" + timerCnt);
	  		}
   	};
   	timer = new Timer(1000, actListener);
   	timer.start();
  	}
}
