package froggerClient;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

//processing routine on server (B)
public class ClientService implements Runnable {

	final static int SERVER_PORT = 5556;
	private Socket s;
	private Scanner in;
	
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

	public ClientService (Socket aSocket, Character frog, Obstacle[] cars,Obstacle[] logs, JLabel frogLabel,JLabel nameLabel,JLabel scoreLabel) {
		this.s = aSocket;
		this.frog = frog;
		this.cars = cars;
		this.logs = logs;
		this.frogLabel = frogLabel;
		this.scoreLabel = scoreLabel;
		this.nameLabel = nameLabel;
				
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
		
		
		if ( command.equals("FROG")) {
			int x = in.nextInt();
			int y = in.nextInt();
			frog.setX(x);
			frog.setY(y);
			
			//move label
			frogLabel.setLocation(
					frog.getX(), frog.getY() );
			
			/*int playerNo = in.nextInt();
			String playerAction = in.next();
			int playerX = in.nextInt();
			int playerY = in.nextInt();
			System.out.println("Player "+playerNo+" "+playerAction + " "+playerX+", "+playerY);*/
		} else if ( command.equals("STARTGAME")) {
			int x = in.nextInt();
			int y = in.nextInt();
			String name = in.next();
			int score = in.nextInt();
			frog.setX(x);
			frog.setY(y);
			frog.setName(name);
			frog.setScore(score);
			
			
			//move label
			frogLabel.setLocation(
					frog.getX(), frog.getY() );
			nameLabel.setText("Name:"+name);
			scoreLabel.setText("Score:"+score);
			
		} else if(command.equals("CARS") || command.equals("LOGS")) {
			int index = in.nextInt();
			int x = in.nextInt();
			int y = in.nextInt();

			Obstacle car = command.equals("CARS") ? cars[index] : logs[index];
			car.setX(x);
			car.setY(y);
			car.getLabel().setLocation(
				car.getX(), car.getY());
			
			//car.detectCollision();

			//System.out.println(command+ index + ":" + x + "  " + y);
		} else if(command.equals("LOG")) {
			int index = in.nextInt();
			int x = in.nextInt();
			int y = in.nextInt();

			Obstacle car = logs[index];
			car.setX(x);
			car.setY(y);
			car.getLabel().setLocation(
				car.getX(), car.getY());

			car.detectCollision();
			//System.out.println(command+ index + ":" + x + "  " + y);
		} else if(command.equals("GAMEOVER")) {
			int won = in.nextInt();
			JOptionPane.showMessageDialog(null, won > 0 ? "You won! Game restarted!" : "You lost! Game restarted!");
			try {
				String message = "RESTART\n";
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
	}
}
