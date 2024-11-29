package demo;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ServerService implements Runnable {

	//declare but not initialize the passed variables from
	// BankServer (we  need to use the originals)
	private Socket s;
	//s
	// for the variables we are passing in 
	private Character1 character1;
	private Character2 character2;
	
	//variables to process our incoming socket data
	private Scanner in;
	private PrintWriter out;
	
	public ServerService() {
		
	}
	
	public ServerService(Socket s ,  Character1 c1, Character2 c2) {
		//32:00 in the video for passing the score : pass by value and passing by referencing 
		
		this.s = s;
		this.character1 = c1;
		this.character2 = c2;
		
		
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
		if(command.equals("MOVEDOCTER")) {
			
			//extract the string passed through socket 
//			String direction = in.next();
//			if(direction.equals("UP")) {
//				int y = character1.getY();
//				y-= GameProperties.CHARACTER_STEP;
//				character1.setY(y);
//			}
			
			
			return ;
			
			
			
		}else if(command.equals("GETDOCTER")) {
			//open a socket to the client
			// DOCTERPOSITION + character1.getX() +   + character2.getY()  \n
			
			
			
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

