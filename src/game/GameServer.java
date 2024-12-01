package game;
import java.awt.Color;
import java.awt.Container;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

public class GameServer {
	

	public static void main(String[] args) throws IOException {

		int score;
		String name;
		
		final Character frog;
		final Obstacle[] cars;
		final Obstacle[] logs;
		Connection conn;

		frog = new Character(GameProperties.SCREEN_WIDTH / 2 - GameProperties.FROG_WIDTH, GameProperties.SCREEN_HEIGHT - GameProperties.FROG_HEIGHT - 60, 
				GameProperties.FROG_HEIGHT, GameProperties.FROG_WIDTH, GameProperties.FROG_IMAGE);
		
		cars = new Obstacle[] {
				new Obstacle(0, 560, 50, 50, 5, "car.png", true, true, frog, true),
				new Obstacle(80, 560, 50, 50, 5, "car.png", false, true, frog, true),
				new Obstacle(160, 560, 50, 50, 5, "car.png", false, true, frog, true),
				new Obstacle(300, 560, 50, 50, 5, "car.png", false, true, frog, true),

				new Obstacle(0, 500, 50, 50, 5, "car.png", false, true, frog, true),
				new Obstacle(80, 500, 50, 50, 5, "car.png", false, true, frog, true),
				new Obstacle(160, 500, 50, 50, 5, "car.png", false, true, frog, true),
				new Obstacle(300, 500, 50, 50, 5, "car.png", false, true, frog, true),

				new Obstacle(0, 450, 50, 50, 5, "car.png", false, true, frog, true),
				new Obstacle(80, 450, 50, 50, 5, "car.png", false, true, frog, true),
				new Obstacle(160, 450, 50, 50, 5, "car.png", false, true, frog, true),
				new Obstacle(300, 450, 50, 50, 10, "car.png", false, true, frog, true),
		};
		
		logs = new Obstacle[] {
				new Obstacle(0, 130, 64,64, 10, "wood.png", true, false, frog, true),
				new Obstacle(80, 130, 64,64, 5, "wood.png", true, false, frog, true),
				new Obstacle(160, 130, 64,64, 10, "wood.png", true, false, frog, true),
				new Obstacle(400, 130, 64,64, 5, "wood.png", true, false, frog, true),
				new Obstacle(0, 200, 64,64, 20, "wood.png", false, false, frog, true),
				new Obstacle(80, 200, 64,64, 5, "wood.png", false, false, frog, true),
				new Obstacle(160, 200, 64,64, 10, "wood.png", false, false, frog, true),
				new Obstacle(400, 200, 64,64, 5, "wood.png", false, false, frog, true),
				new Obstacle(0, 270, 64,64, 10, "wood.png", true, false, frog, true),
				new Obstacle(80, 270, 64,64, 5, "wood.png", true, false, frog, true),
				new Obstacle(160, 270, 64,64, 10, "wood.png", true, false, frog, true),
				new Obstacle(400, 270, 64,64, 10, "wood.png", true, false, frog, true),
		};
		
		try {
			
			Class.forName("org.sqlite.JDBC");
			System.out.println("Driver Loaded");
			
			String dbURL = "jdbc:sqlite:products.db";
			conn = DriverManager.getConnection(dbURL);
			
			if (conn != null) {
				System.out.println("connected to database");
				
				DatabaseMetaData db = (DatabaseMetaData) conn.getMetaData();
				
                String sqlCreateTable = "CREATE TABLE IF NOT EXISTS PLAYERS " +
                        "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        " NAME TEXT NOT NULL, " +
                        " SCORE INT NOT NULL)";

                try (PreparedStatement pstmtCreateTable = conn.prepareStatement(sqlCreateTable)) {
                	pstmtCreateTable.executeUpdate();
                	System.out.println("Table Successfully Created");
                }
            }
		

		final int SERVER_PORT = 5556;

		//game variables go here, pass to service
		

		//run server in own thread
		Thread t1 = new Thread ( new Runnable () {
			public void run ( ) {
				synchronized(this) {

					ServerSocket server;
					try {
						
						server = new ServerSocket(SERVER_PORT);
						System.out.println("Waiting for clients to connect...");
						while(true) {
							Socket s = server.accept();
							//System.out.println("client connected");
							
							ServerService myService = new ServerService(s, frog, cars, logs, conn);
							Thread t2 = new Thread(myService);
							t2.start();
						}
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
		
				}
			}
		});
		t1.start( );
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	


}
