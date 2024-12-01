package game;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


//processing routine on server (B)
public class BService implements Runnable {
	final int CLIENT_PORT = 5656;

	private Socket s;
	private Scanner in;

	public BService (Socket aSocket) {
		this.s = aSocket;
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
	
		
		if ( command.equals("PLAYER")) {
			int playerNo = in.nextInt();
			String playerAction = in.next();
			System.out.println("Player "+playerNo+" moves "+playerAction);
			
			sendResponse("PLAYER "+playerNo+" POSTION 500 400\n");

		}
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
