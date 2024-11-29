package game;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class BankClient {

	public static void main(String[] args) throws IOException {
		
		//local copy of account (keep in sync with server)
		BankAccount accounts[] = new BankAccount[10];
		for(int i=0; i<accounts.length; i++) {
			accounts[i] = new BankAccount();
		}
		
		
		
		//DEPOSIT 1000 for account 3
		//DEPOSIT 3 1000.00 <= REST-LIKE

		//WITHDRAW 300 for account 3
		//WITHDRAW 3 300.00
		
		//get balance for account 3
		//GETBALANCE 3
		
		//get all balances
		//GETBALANCES
		
		//set up socket request to send to the server
		final int SOCKET_PORT = 5556;
		Socket s = new Socket("localhost", SOCKET_PORT);
		
		//initialize a data stream to send data out to server
		OutputStream outStream = s.getOutputStream();
		PrintWriter out = new PrintWriter(outStream);
		
		//initialize an data stream to receive data 
		//sent back via the socket
		InputStream inStream = s.getInputStream();
		Scanner in = new Scanner(inStream);
		
		//send deposit to server
		String command = "DEPOSIT 3 1000.00\n";
		System.out.println("Sending: " + command);
		out.println(command); //command is sent to server via socket
		out.flush();
		
		//receive a response from the server
		String response = in.nextLine();
		System.out.println("RECEIVED: " + response);
		
		//UPDATEBALANCE 3 1000.00
		String[] myResponses = response.split(" ");
		String serverCommand = myResponses[0]; //UPDATEBALANCE
		System.out.println("serverCommand: " + serverCommand);
		
		int serverAccountNumber = Integer.parseInt(myResponses[1]); //3
		System.out.println("serverAccountNumber: " + serverAccountNumber);
		double serverAccountBalance = Double.parseDouble(myResponses[2]); //1000.00
		System.out.println("serverAccountBalance: " + serverAccountBalance);			
		
		//send next command to server
		command = "WITHDRAW 3 300.00\n";
		System.out.println("Sending: " + command);
		out.println(command); //command is sent to server via socket
		out.flush();
		response = in.nextLine();
		System.out.println("RECEIVED: " + response);

		//send next command to server
		command = "GETBALANCE 3\n";
		System.out.println("Sending: " + command);
		out.println(command); //command is sent to server via socket
		out.flush();
		response = in.nextLine();
		System.out.println("RECEIVED: " + response);

		//send next command to server
		command = "GETBALANCES\n";
		System.out.println("Sending: " + command);
		out.println(command); //command is sent to server via socket
		out.flush();
		response = in.nextLine();
		System.out.println("RECEIVED: " + response);

		s.close();		
		

		//server commands
				
		final int RECEIVING_SOCKET_PORT = 5557;
		ServerSocket server = new ServerSocket(RECEIVING_SOCKET_PORT);
		System.out.println("Client is listening for connection");

		while(true) {
			Socket ss = server.accept();
			System.out.println("Client Connected");
			
			//process the command received on socket
			//BankService
			//Also need to pass in any variable that needs
			//to be updated within the thread
			BankService myService = new BankService(ss, accounts);
			Thread t = new Thread(myService);
			t.start();			
		}
	}

}
