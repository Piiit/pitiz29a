package dslab03;

public class Main {
	
	private static final int NUMBEROFCLIENTS = 200;
	private static Office registryOffice = new Office(15);

	public static void main(String[] args) {
		
		for(int i = 0; i < NUMBEROFCLIENTS; i++) {
			Thread t = new Citizen(registryOffice);
			t.start();
		}
	}
}
