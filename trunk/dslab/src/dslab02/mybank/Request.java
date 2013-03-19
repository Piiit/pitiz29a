package dslab02.mybank;

public class Request {
	int clientId;
	double amount;
	
	public Request(int clientId, double amount) {
		super();
		this.clientId = clientId;
		this.amount = amount;
	}

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

}
