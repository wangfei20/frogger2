package game;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class BankService implements Runnable  {
	
	//declare but not initialize the passed variables from 
	//BankServer (we need to use the originals)
	private Socket s;
	private BankAccount accounts[];
	
	//variables to process our incoming socket data
	private Scanner in;
	private PrintWriter out;
	
	public BankService() {}
	
	public BankService(Socket s, BankAccount accounts[]) {
		this.s = s;
		this.accounts = accounts;
	}

	@Override
	public void run() {
		
		try {
			in = new Scanner( s.getInputStream() );
			out = new PrintWriter( s.getOutputStream() );
			processRequest();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				s.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
		
	}
	
	private void processRequest() {
		//need a loop to process the command tokens as they are
		//parsed one at a time
		while (true) {
			if ( !in.hasNext() ) return;
			
			//extract the first token (command)
			String command = in.next(); //in.next() gets String
			
			executeCommand(command);
		}
		
	}
	
	private void executeCommand(String command) {
		if ( command.equals("DEPOSIT") ) {
			
			//DEPOSIT 3 1000.00
			
			//parse the rest of the tokens from the 'in'
			System.out.println(command + " received");
			
			int account_number = in.nextInt();
			double amount = in.nextDouble();
			
			accounts[account_number].deposit(amount);
			System.out.println(amount + 
					" deposited into account " + account_number);
			
			//UPDATEBALANCE 3 1000.00
			String outCommand = "UPDATEBALANCE " + account_number;
			outCommand += " " + accounts[account_number].getBalance();
			outCommand += "\n";
			out.println(outCommand);
			out.flush();
			
			return;			
				
		} else if ( command.equals("WITHDRAW") ) {

			//WITHDRAW 3 300.00
			
			//parse the rest of the tokens from the 'in'
			System.out.println(command + " received");
			
			int account_number = in.nextInt();
			double amount = in.nextDouble();
			
			accounts[account_number].withdraw(amount);
			System.out.println(amount + 
					" withdrawn from account " + account_number);
			
			//UPDATEBALANCE 3 1000.00
			String outCommand = "UPDATEBALANCE " + account_number;
			outCommand += " " + accounts[account_number].getBalance();
			outCommand += "\n";
			out.println(outCommand);
			out.flush();
			
			return;
			
		} else if ( command.equals("GETBALANCE") ) {
			
			//GETBALANCE 3
			
			//parse the rest of the tokens from the 'in'
			System.out.println(command + " received");
			
			int account_number = in.nextInt();
			
			String outCommand = "BALANCE " + account_number;
			outCommand += " " + accounts[account_number].getBalance();
			outCommand += "\n";
			out.println(outCommand);
			out.flush();
			
			return;
			
		}if ( command.equals("GETBALANCES") ) {
			
			//GETBALANCES
			
			//parse the rest of the tokens from the 'in'
			System.out.println(command + " received");
			
			String outCommand = "ALLBALANCES";
			for (int i=0; i<10; i++) {
				outCommand += " " + i;
				outCommand += " " + accounts[i].getBalance();
			}			
			outCommand += "\n";
			out.println(outCommand);
			out.flush();
			
			return;
			
		} else {
			//not a valid command
			System.out.println(command + " received");
			String outCommand = "INVALID";
			outCommand += "\n";
			out.println(outCommand);
			out.flush();
			
			return;
		}
	}

}
