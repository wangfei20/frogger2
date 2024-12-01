package game;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import javax.swing.JOptionPane;

//processing routine on server (B)
public class ServerService implements Runnable {

	final int CLIENT_PORT = 5656;

	private Socket s;
	private Scanner in;
	private Connection conn;
	
	Character frog;
	Obstacle[] cars;
	Obstacle[] logs;

	public ServerService (Socket aSocket, Character frog, Obstacle[] cars,Obstacle[] logs,Connection conn) {
		this.s = aSocket;
		this.frog = frog;
		this.cars = cars;
		this.logs = logs;
		this.conn = conn;
	}
	public void run() {
		
		try {
			in = new Scanner(s.getInputStream());
			processRequest( );
		} catch (IOException e){
			e.printStackTrace();
		} finally {
			try {
				s.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	//processing the requests
	public void processRequest () throws IOException {
		//if next request is empty then return
		while(true) {
			if(!in.hasNext( )){
				return;
			}
			String command = in.next();
			if (command.equals("Quit")) {
				return;
			} else {
				
				executeCommand(command);
			}
		}
	}
	
	public void executeCommand(String command) throws IOException{
	
		
		if ( command.equals("STARTGAME")) {
			String inputName = in.next();
			String sqlSelectFilter = 
					"SELECT * FROM PLAYERS WHERE name = ?";
			System.out.println(inputName);
	        try (PreparedStatement pstmtSelectLike = 
	        		      conn.prepareStatement(sqlSelectFilter)) {
	        	pstmtSelectLike.setString(1, inputName);
	            ResultSet rs = pstmtSelectLike.executeQuery();
	            if(rs.next()) {
	            	int id = rs.getInt("id");
	            	frog.setName(rs.getString("name"));
	            	frog.setScore(rs.getInt("score"));
	            } else {
	            	String sqlInsert = "INSERT INTO PLAYERS (NAME, SCORE) VALUES (?, ?)";
	                try (PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsert)) {

	                  	//execute calls to prepared statement
	                  	pstmtInsert.setString(1, inputName);
	                  	pstmtInsert.setInt(2, 0);
	                  	pstmtInsert.executeUpdate();
	                  	
	                  	System.out.println("1 records inserted");
	                }
	                frog.setName(inputName);
	            	frog.setScore(0);
	            }
	            
	            
	            System.out.println("Name:"+ frog.getName());
	            System.out.println("Score:"+ frog.getScore());
	            
	            rs.close();
	         	
	         } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        startGame();
		} else if ( command.equals("GETFROG")) {
			sendResponse("FROG " + frog.x + " " + frog.y);
		/*} else if ( command.equals("SETFROG")) {
			int x = in.nextInt();
			int y = in.nextInt();
			System.out.println("SET Frog: " + x + " " + y);
			
			frog.setX(x);
			frog.setY(y);
			
			sendResponse("FROG " + frog.getX() + " " + frog.getY());
			*/
		}  else if ( command.equals("GETOBSTACLES")) {
			try {
				//send a response
					Socket s2 = new Socket("localhost", CLIENT_PORT);
					
					//Initialize data stream to send data out
					OutputStream outstream = s2.getOutputStream();
					PrintWriter out = new PrintWriter(outstream);
					
					for(int i = 0; i<cars.length; i++) {
						String data = "CARS " + i + " " + cars[i].x + " " + cars[i].y;
						//System.out.println("Sending: " + data);
						out.println(data);
						out.flush();
					}
					for(int i = 0; i<logs.length; i++) {
						String data = "LOGS " + i + " " + logs[i].x + " " + logs[i].y;
						//System.out.println("Sending: " + data);
						out.println(data);
						out.flush();
					}
					
						
					s2.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			
		}  else if ( command.equals("RESTART")) {
			startGame();
		} else if ( command.equals("SETFROG")) {
			int x = in.nextInt();
			int y = in.nextInt();
			
			frog.setX(x);
			frog.setY(y);
			
			
			if(frog.getLanded() && frog.getLog() != null) {
				frog.getLog().setMoveFrog(false);
				frog.setLog(null);
				

				System.out.println("Left Log!");
			}
			

			System.out.println("update Frog: " + y);
			if(y < 90) {
				gameOver(true);
			} else if(y < 310) {
				System.out.println("In Water Area!");
				frog.setLanded(false);
				for(int i = 0; i < logs.length; i++) {
					if(logs[i].getRectangle().intersects(frog.getRectangle())){
						frog.setLog(logs[i]);

						System.out.println("Set log! " + i);
						break;
					}
				}
				
				if(!frog.getLanded()) {
					System.out.println("In Water!");
					gameOver(false);
				}
			}
			

			sendResponse("FROG " + frog.getX() + " " + frog.getY());
			
		} 
	}
	

	private void startGame() {
		frog.init();
		frog.setX(GameProperties.SCREEN_WIDTH / 2 - GameProperties.FROG_WIDTH);
		frog.setY(GameProperties.SCREEN_HEIGHT - GameProperties.FROG_HEIGHT - 60);
        
        for(int i = 0;i<cars.length;i++) {
        	cars[i].setGame(this);
        	cars[i].startThread();
        	logs[i].setGame(this);
        	logs[i].startThread();
        }
        
        sendResponse("STARTGAME " + frog.getX() + " " + frog.getY() + " " + frog.getName() + " " + frog.getScore() + "\n");
	}
	
	public void handleCollision(Obstacle obstacle) {
		if(obstacle.isCar()) {
			System.out.println("car crash!");
			gameOver(false);
		} else {
			int x = obstacle.getX();
			int y = obstacle.getY();
			if(frog.getLog() == obstacle) {
				System.out.println("on log!");
//				// Frog gets off the log if it gets carried to the border
				if( x >= GameProperties.SCREEN_WIDTH - frog.getWidth() || x <= 0) {
					gameOver(false);
				} else {
					frog.setX(x);
					frog.setY(y);
				}
				
				sendResponse("FROG " + frog.getX() + " " + frog.getY());
			}
		}
	}
	
	private void gameOver(boolean won){
		System.out.println("Game Over: " + won);
        for(int i = 0;i<cars.length;i++) {
        	cars[i].setMoving(false);

        	logs[i].setMoving(false);
        	cars[i].stopThread();
        	logs[i].stopThread();
        }
        int score = frog.getScore();
        if(won){
    		score+=50;
        } else {
        	if(score >= 50)
    			score -= 50;
        }
        
        frog.setScore(score);

		String sqlUpdate = "UPDATE PLAYERS SET score = ? WHERE name = ?";
        try (PreparedStatement pstmtUpdate = this.conn.prepareStatement(sqlUpdate)) {

        	pstmtUpdate.setInt(1, score);
        	pstmtUpdate.setString(2, frog.getName());
        	pstmtUpdate.executeUpdate();
        	
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        sendResponse("GAMEOVER " + (won ? 1 : 0));
        //startGame();
	}
	


	public void sendResponse(String response) {

		try {
		//send a response
			Socket s2 = new Socket("localhost", CLIENT_PORT);
			
			//Initialize data stream to send data out
			OutputStream outstream = s2.getOutputStream();
			PrintWriter out = new PrintWriter(outstream);
	
			System.out.println("Sending: " + response);
			out.println(response);
			out.flush();
				
			s2.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
