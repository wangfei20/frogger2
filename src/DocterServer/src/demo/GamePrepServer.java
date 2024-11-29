package demo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GamePrepServer {

	
	
	
	
	

	public static void main(String[] args) throws IOException {
		

		
		//declare copies of our character
		 Character1 character1;          //docter
		 Character2 character2;          // tardis
		
		character1 = new Character1(100, 200, 120, 200, "dw12.png");
		character2 = new Character2(0, 0, 120, 200, "tardis.png");
		
		//set up screen
		
		//character1 setup
		character1.setX(100);
		character1.setY(250);
		character1.setWidth(100);
		character1.setHeight(200);
		character1.setImage("dw12.png");

		//character2 setup
		character2.setX(0);
		character2.setY(0);
		character2.setWidth(120);
		character2.setHeight(200);
		character2.setImage("tardis.png");
		character2.setCharacter1(character1);
		
		final int SOCKET_PORT = 5556;
		ServerSocket server = new ServerSocket(SOCKET_PORT);
		
	    System.out.println("Server is listening for connection");
	    
	    Thread t1 = new Thread(new Runnable() {
	    	public void run() {
	    		synchronized(this) {
	    			while (true) {
	    				try {
	    		    	Socket s = server.accept();
	    		    	System.out.println("Client Connected");
	    		    	// pass each and everything in the server service as we are going to manipulate everything (24:00 in the video)
	    		    	ServerService myService = new ServerService(s,character1,character2);
	    		    	Thread t = new Thread(myService);
	    		    	t.start();
	    		    	
	    				}catch(Exception e) {
	    					e.printStackTrace();
	    				}
	    		    	
	    		}
	    	}
	    	}
	    	
	    });
	    
	    	t1.start();
	    	
	    	
	    
		
	}

}

