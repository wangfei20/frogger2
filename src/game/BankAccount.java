package game;

public class BankAccount {
	
	private double balance;

	public BankAccount() {
		super();
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	public void deposit (double amount) {
		balance += amount;
	}

	public void withdraw (double amount) {
		balance -= amount;
	}
}
