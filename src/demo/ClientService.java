package demo;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JLabel;

public class ClientService implements Runnable {

	//declare but not initialize the passed variables from
	// BankServer (we  need to use the originals)
	private Socket s;

	// for the variables we are passing in 
	private Character1 character1;
	private Character2 character2;
	private JLabel character1Label, character2Label;
	private JButton startButton, visibilityButton;
	
	//variables to process our incoming socket data
	private Scanner in;
	private PrintWriter out;
	
	public ClientService() {
		
	}
	
	public ClientService(Socket s ,  Character1 c1, Character2 c2,JLabel l1,JLabel l2, JButton b1, JButton b2) {
		//32:00 in the video for passing the score : pass by value and passing by referencing 
		
		this.s = s;
		this.character1 = c1;
		this.character2 = c2;
		
		this.character1Label = l1;
		
		
		
	}
	
	
	@Override
	public void run() {
		
		try {
			in = new Scanner(s.getInputStream());
			
			out = new PrintWriter(s.getOutputStream());
			
			processRequest();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				s.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		
		
	}
	
	private void processRequest() {
		//need a loop to process the command tokens as they are 
		// parsed one at a time 
		while (true) {
			if(!in.hasNext()) return; // this is the line which stops us if there is not data left to parse
			
			// extract the first token (command)
			
			String command = in.next();    //in.next() gets String
			
			executeCommand(command);
			
			
			
		}
	}
	
	private void executeCommand(String command) {
		if(command.equals("DOCTERPOSITON")) {
			
			//extract the string passed through socket 
			int x = in.nextInt();
			int y = in.nextInt();
			character1.setX(x);
			character1.setY(y);
			
			character1Label.setLocation(character1.getX(),character2.getY());
			
			
			
			
			return ;
			
			
			
		}else if(command.equals("CARDATA")) {
			//open a socket to the client
			// DOCTERPOSITION + character1.getX() +   + character2.getY()  \n
			int x = in.nextInt();  // first car's x
			int y = in.nextInt();  // first car's y
			String moving = "";
			
			String moving = in.next();    // first car's moving
			
			cars[i].setX(x);
			cars[i].setY(y);
			cars[i].setMoving(moving);
			carsLabels[i].setLocation(cars[i])
			
			
			// 2nsd car
			int x = in.nextInt();  // second car's x
			int y = in.nextInt();  // second  car's y
			String moving = "";
			
			String moving = in.next();    // first car's moving
			
			cars[i].setX(x);
			cars[i].setY(y);
			cars[i].setMoving(moving);
			carsLabels[i].setLocation(cars[i])
			
			
			
			return ;
			
		}else if(command.equals("STARTGAME")) {
			//loop through and start tardis moving
			
			
			
			return ;
			
		}else if(command.equals("GETTARDIS")) {
			
			//open a socket to the client , send tardis coordinates
			
			
			
			return ;
		
		}
		
		else {
			// not a valid command
			System.out.println(command + "received");
			String outCommand = "INVALID";
			out.println(outCommand);
			out.flush();
			
			return ; 
		}
	}
	
	

}


