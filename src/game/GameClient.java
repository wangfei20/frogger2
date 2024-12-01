package game;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import game.Character;
import game.GameProperties;
import game.Obstacle;


// Open app, prompt for user name
// Search database for user data, load score
// Set up frog and cars, game starts
// 


public class GameClient extends JFrame implements KeyListener, ActionListener {
	
	final static int CLIENT_PORT = 5656;
	final static int SERVER_PORT = 5556;

	private Connection conn;

	private int score;
	private String name;
	
	
	

	//Frog
	private Character frog;
	//Cars
	private Obstacle[] cars;
	//Logs
	private Obstacle[] logs;
	//GUI variables
	private JLabel nameLabel;
	private JLabel scoreLabel;
	private JLabel frogLabel;
	
	private Container content;
	private JLabel backgroundLabel;
	private ImageIcon backgroundIcon;

	
	private JButton startButton;
	
	
	//GUI setup
	public GameClient() {
		super("Frogger");

		nameLabel = new JLabel();
		scoreLabel = new JLabel();

		
		backgroundIcon = new ImageIcon(getClass().getResource("images/background.jpg"));
		backgroundLabel = new JLabel();
		
		scoreLabel.setSize(200,30);
		nameLabel.setLocation(50, 0);
		scoreLabel.setLocation(300, 0);
		nameLabel.setSize(200,30);
		
		add(nameLabel);
		add(scoreLabel);
		
		frog = new Character(GameProperties.SCREEN_WIDTH / 2 - GameProperties.FROG_WIDTH, GameProperties.SCREEN_HEIGHT - GameProperties.FROG_HEIGHT - 60, 
				GameProperties.FROG_HEIGHT, GameProperties.FROG_WIDTH, GameProperties.FROG_IMAGE);
		
		cars = new Obstacle[] {
				new Obstacle(0, 560, 50, 50, 5, "car.png", true, true),
				new Obstacle(80, 560, 50, 50, 5, "car.png", false, true),
				new Obstacle(160, 560, 50, 50, 5, "car.png", false, true),
				new Obstacle(300, 560, 50, 50, 5, "car.png", false, true),

				new Obstacle(0, 500, 50, 50, 5, "car.png", false, true),
				new Obstacle(80, 500, 50, 50, 5, "car.png", false, true),
				new Obstacle(160, 500, 50, 50, 5, "car.png", false, true),
				new Obstacle(300, 500, 50, 50, 5, "car.png", false, true),

				new Obstacle(0, 450, 50, 50, 5, "car.png", false, true),
				new Obstacle(80, 450, 50, 50, 5, "car.png", false, true),
				new Obstacle(160, 450, 50, 50, 5, "car.png", false, true),
				new Obstacle(300, 450, 50, 50, 10, "car.png", false, true),
		};
		
		logs = new Obstacle[] {
				new Obstacle(0, 130, 64,64, 10, "wood.png", true, false),
				new Obstacle(80, 130, 64,64, 5, "wood.png", true, false),
				new Obstacle(160, 130, 64,64, 10, "wood.png", true, false),
				new Obstacle(400, 130, 64,64, 5, "wood.png", true, false),
				new Obstacle(0, 200, 64,64, 20, "wood.png", false, false),
				new Obstacle(80, 200, 64,64, 5, "wood.png", false, false),
				new Obstacle(160, 200, 64,64, 10, "wood.png", false, false),
				new Obstacle(400, 200, 64,64, 5, "wood.png", false, false),
				new Obstacle(0, 270, 64,64, 10, "wood.png", true, false),
				new Obstacle(80, 270, 64,64, 5, "wood.png", true, false),
				new Obstacle(160, 270, 64,64, 10, "wood.png", true, false),
				new Obstacle(400, 270, 64,64, 10, "wood.png", true, false),
		};
		
		//set up screen
		setSize(GameProperties.SCREEN_WIDTH, 
				     GameProperties.SCREEN_HEIGHT);
		content = getContentPane();
		content.setBackground(Color.gray);
		setLayout(null);

		
		backgroundLabel.setIcon(backgroundIcon);
		backgroundLabel.setSize(
				GameProperties.SCREEN_WIDTH, 
			     GameProperties.SCREEN_HEIGHT
		);
		backgroundLabel.setLocation(0,0);
		
		
		frogLabel= new JLabel();
		
		frogLabel.setIcon(new ImageIcon(
				getClass().getResource("images/" + frog.getImage()
						)));
		frogLabel.setSize(
				frog.getWidth(),
				frog.getHeight()
		);
		frogLabel.setLocation(
				frog.getX(), frog.getY() );

		add(frogLabel);

		for(int i = 0;i<cars.length;i++) {

			addObstacle(cars[i], true);
		
		}
		for(int i = 0;i<logs.length;i++) {
			addObstacle(logs[i], false);
		}

		add(backgroundLabel);
		
		
		content.addKeyListener(this);
		content.setFocusable(true);

		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
/*
		String sqlSelectFilter = 
				"SELECT * FROM PLAYERS WHERE name = ?";
		System.out.println(inputName);
        try (PreparedStatement pstmtSelectLike = 
        		      conn.prepareStatement(sqlSelectFilter)) {
        	pstmtSelectLike.setString(1, inputName);
            ResultSet rs = pstmtSelectLike.executeQuery();
            if(rs.next()) {
            	int id = rs.getInt("id");
            	name = rs.getString("name");
            	score = rs.getInt("score");
            } else {
            	String sqlInsert = "INSERT INTO PLAYERS (NAME, SCORE) VALUES (?, ?)";
                try (PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsert)) {

                  	//execute calls to prepared statement
                  	pstmtInsert.setString(1, inputName);
                  	pstmtInsert.setInt(2, 0);
                  	pstmtInsert.executeUpdate();
                  	
                  	System.out.println("1 records inserted");
                }
                score = 0;
                name = inputName;
            }
            
            
            System.out.println("Name:"+name);
            System.out.println("Score:"+score);
            
            rs.close();
         	
         } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	private void addObstacle(Obstacle obstacle, Boolean isCar) {
		Obstacle car = obstacle;
		JLabel carLabel = new JLabel();
		carLabel.setIcon(new ImageIcon(
				getClass().getResource("images/" + car.getImage()
						)));
		carLabel.setSize(
				car.getWidth(),
				car.getHeight()
				);
		carLabel.setLocation(
				car.getX(), car.getY());
		

		car.setFrog(frog);
		car.setFrogLabel(frogLabel);
		car.setLabel(carLabel);
		car.setCar(isCar);
		
		
		//car.startThread();
		
		add(carLabel);
		
	}
	
	private void startGame() {
	
		/*frog.init();
		frog.setX(GameProperties.SCREEN_WIDTH / 2 - GameProperties.FROG_WIDTH);
		frog.setY(GameProperties.SCREEN_HEIGHT - GameProperties.FROG_HEIGHT - 60);
		
		//move label
		frogLabel.setLocation(
				frog.getX(), frog.getY() );
        nameLabel.setText("Name:"+name);
        scoreLabel.setText("Score:"+score);
        
        for(int i = 0;i<cars.length;i++) {
        	cars[i].startThread();
        	logs[i].startThread();
        }*/
	}
	
	public static void main(String[] args) {

		GameClient myGame = new GameClient();
		myGame.setVisible(true);
		
		Thread t1 = new Thread ( new Runnable () {
			public void run ( ) {
				synchronized(this) {
					
					ServerSocket client;
					
					try {
						
						client = new ServerSocket(CLIENT_PORT);
						while(true) {
							Socket s2;
							try {
								s2 = client.accept();
								ClientService myService = new ClientService(s2, myGame.frog, myGame.cars, myGame.logs,myGame.frogLabel,myGame.nameLabel, myGame.scoreLabel);
								//
								Thread t2 = new Thread(myService);
								t2.start();
									
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							//System.out.println("client connected");
							
						}
					
					
					
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("Waiting for server responses...");

					
				}
			}
		});
		t1.start( );
		
		
		String inputName = javax.swing.JOptionPane.showInputDialog("Input your name:");
		String command = "STARTGAME " + inputName + "\n";
		
		System.out.println("Sending: " + command);
	    myGame.sendMessage(command);
		/*try {
			Socket s;
			s = new Socket("localhost", SERVER_PORT);
			
			//Initialize data stream to send data out
			OutputStream outstream = s.getOutputStream();
			PrintWriter out = new PrintWriter(outstream);
	
			String command = "STARTGAME " + inputName + "\n";
			
			System.out.println("Sending: " + command);
			out.println(command);
			out.flush();
			
			s.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		Thread t2 = new Thread(new Runnable() {
	    	public void run() {
	    		synchronized(this) {
	    			while (true) {
	    				
	    				try {
	    		    	Socket s = new Socket("localhost",SERVER_PORT);
	    		    	
	    		    	//Initialize data stream to send data out
	    		    	OutputStream outstream = s.getOutputStream();
	    		    	PrintWriter out = new PrintWriter(outstream);
	    		    	
	    		    	String command = "GETOBSTACLES\n";
	    		    	//System.out.println("Sending : "+command);
	    		    	out.println(command);
	    		    	out.flush();
	    		    	s.close();
	    		    	Thread.sleep(100);
	    		    	
	    				} catch (UnknownHostException e) {
	    					// TODO Auto-generated catch block
	    					e.printStackTrace();
	    				} catch (IOException e) {
	    					// TODO Auto-generated catch block
	    					e.printStackTrace();
	    				} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	    				
	    		    	
	    			}
	    		}
	    	}
	    	
	    });
		t2.start();
	}
	
	private void sendMessage(String message) {
		try {
			Socket s;
			s = new Socket("localhost", SERVER_PORT);
			
			//Initialize data stream to send data out
			OutputStream outstream = s.getOutputStream();
			PrintWriter out = new PrintWriter(outstream);
	
			
			System.out.println("Sending: " + message);
			out.println(message);
			out.flush();
			
			s.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		//get current position
		int x = frog.getX();
		int y = frog.getY();
		
		//detect direction
		if ( e.getKeyCode() == KeyEvent.VK_UP ) {
			
			if ( y - GameProperties.CHARACTER_STEP >= 0) {
				y -= GameProperties.CHARACTER_STEP;
			}

			
		} else if ( e.getKeyCode() == KeyEvent.VK_DOWN ) {
			
			if ( y + GameProperties.CHARACTER_STEP <= (GameProperties.SCREEN_HEIGHT - frog.getHeight() - 30)) {
				y += GameProperties.CHARACTER_STEP;

			}
			
			
		} else if ( e.getKeyCode() == KeyEvent.VK_LEFT ) {
			
			if(x - GameProperties.CHARACTER_STEP >= 0) {
				x -= GameProperties.CHARACTER_STEP;
			}
			
		}  else if ( e.getKeyCode() == KeyEvent.VK_RIGHT ) {
			
			if(x + GameProperties.CHARACTER_STEP <= GameProperties.SCREEN_WIDTH - frog.getWidth()) {
				x += GameProperties.CHARACTER_STEP;
			}
			
		} 
		
		String message = "SETFROG " + x + " " + y + "\n";
		
		System.out.println("Sending: " + message);
	    sendMessage(message);
		
		//update frog
		/*frog.setX(x);
		frog.setY(y);
		
		
		
		System.out.println(y);
		
		//move label
		frogLabel.setLocation(
				frog.getX(), frog.getY() );
		*/
	    
	    /*
	    
		if(frog.getLanded() && frog.getLog() != null) {
			frog.getLog().setMoveFrog(false);
			frog.setLog(null);
			

			System.out.println("Left Log!");
		}
		
		if(y < 90) {
//			if(frog.getLanded()) {
//				gameWon();
//			} else {
//				gameLost();
//			}
			gameOver(true);
		} else if(y < 310) {
			frog.setLanded(false);
			for(int i = 0; i < logs.length; i++) {
				if(logs[i].getRectangle().intersects(frog.getRectangle())){
					frog.setLog(logs[i]);
					
					break;
				}
			}
			
			if(!frog.getLanded()) {
				gameOver(false);
			}
		}*/
		
	}
	
	public void handleCollision(Obstacle obstacle) {
		if(obstacle.isCar()) {

			gameOver(false);
		} else {
			int x = obstacle.getX();
			int y = obstacle.getY();
			if(frog.getLog() == obstacle) {
//				// Frog gets off the log if it gets carried to the border
				if( obstacle.getX() >= GameProperties.SCREEN_WIDTH - frog.getWidth() || x <= 0) {
					gameOver(false);
				} else {
					frog.setX(x);
					frog.setY(y);
				}
				//move label
				//frogLabel.setLocation(
				//
			}
		}
	}
	
	private void gameOver(boolean won){
		
        for(int i = 0;i<cars.length;i++) {
        	cars[i].stopThread();
        	logs[i].stopThread();
        }
        
        if(won){
    		score+=50;
        } else {
        	if(score >= 50)
    			score -= 50;
        }

    	JOptionPane.showMessageDialog(null, won ? "You won! Game restarted!" : "You lost! Game restarted!");
        
		String sqlUpdate = "UPDATE PLAYERS SET score = ? WHERE name = ?";
        try (PreparedStatement pstmtUpdate = this.conn.prepareStatement(sqlUpdate)) {

        	pstmtUpdate.setInt(1, score);
        	pstmtUpdate.setString(2, name);
        	pstmtUpdate.executeUpdate();
        	
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        

        startGame();
	}
	


	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		
		/*if ( e.getSource() == visibilityButton ) {
			
			System.out.println("visibilityButton pressed");	
			
			if ( character2.getVisible() ) {
				//character2.hide();
			} else {
				//character2.show();
			}
		
		}*/

		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
