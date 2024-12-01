package game;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//server app
public class BServer {

	public static void main(String[] args) throws IOException {

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
							System.out.println("client connected");
							
							BService myService = new BService (s);
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

	}

}
