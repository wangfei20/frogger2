package game;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
public class BankServer {

	public static void main(String args[]) throws IOException {
		
		BankAccount accounts[] = new BankAccount[10];
		for(int i=0; i<accounts.length; i++) {
			accounts[i] = new BankAccount();
		}
		
		//server commands
		final int SOCKET_PORT = 5556;
		ServerSocket server = new ServerSocket(SOCKET_PORT);
		System.out.println("Server is listening for connection");
		
		while(true) {
			Socket s = server.accept();
			System.out.println("Client Connected");
			
			//process the command received on socket
			//BankService
			//Also need to pass in any variable that needs
			//to be updated within the thread
			BankService myService = new BankService(s, accounts);
			Thread t = new Thread(myService);
			t.start();			
		}
		
		
		
	}
}
