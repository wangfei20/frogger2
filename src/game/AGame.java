package game;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class AGame {

	final static int CLIENT_PORT = 5656;
	final static int SERVER_PORT = 5556;

	public static void main(String[] args) throws IOException {		
		
		
		//set up listening server (you would need to put this code in your GamePrep constructor)
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
								AService myService = new AService (s2);
								Thread t2 = new Thread(myService);
								t2.start();
									
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							System.out.println("client connected");
							
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

		
		//sample code (like in keypressed)

		//set up a communication socket
		Socket s = new Socket("localhost", SERVER_PORT);
		
		//Initialize data stream to send data out
		OutputStream outstream = s.getOutputStream();
		PrintWriter out = new PrintWriter(outstream);

		String command = "PLAYER 2 UP\n";
		System.out.println("Sending: " + command);
		out.println(command);
		out.flush();
		
		command = "PLAYER 1 DOWN\n";
		System.out.println("Sending: " + command);
		out.println(command);
		out.flush();
		
		s.close();

		
	}

}
