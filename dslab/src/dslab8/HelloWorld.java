package dslab8;

import java.util.Random;

import net.jxta.peergroup.PeerGroup;
import net.jxta.peergroup.PeerGroupFactory;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.discovery.DiscoveryService;
import net.jxta.exception.PeerGroupException;

@SuppressWarnings("deprecation")
public class HelloWorld implements Runnable, DiscoveryListener {

	static PeerGroup netPeerGroup = null;
	private DiscoveryService discovery;

	public static void main(String args[]) {
		System.out.println("Starting JXTA ....");
		HelloWorld myapp = new HelloWorld();
		myapp.startJxta();
		System.out.println("Hello from JXTA group "
				+ netPeerGroup.getPeerGroupName());
		System.out.println(" Group ID = "
				+ netPeerGroup.getPeerGroupID().toString());
		System.out.println(" Peer name = " + netPeerGroup.getPeerName());
		System.out.println(" Peer ID = " + netPeerGroup.getPeerID().toString());
		System.out.println("Good Bye ....");

		myapp.run();

		// HelloWorld.netPeerGroup.stopApp();
		// System.exit(0);
	}

	private void startJxta() {
		try { 
			netPeerGroup = PeerGroupFactory.newNetPeerGroup();
		} catch (PeerGroupException e) { 
			System.out.println("fatal error : group creation failure");
			e.printStackTrace();
			System.exit(1);
		}
		discovery = netPeerGroup.getDiscoveryService();
	}

	@Override
	public void discoveryEvent(net.jxta.discovery.DiscoveryEvent event) {
		System.out.println("Discover !");
		try {
			dslab8.DiscoveryEvent d_event = new dslab8.DiscoveryEvent(this);
			d_event.setAdvertisements(event.getSearchResults());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {

		Random r = new Random();
		long waitTime = 0;
		do {
			try {
				System.out.println("Discovering ...");
				discovery.getRemoteAdvertisements(null, DiscoveryService.ADV,
						null, null, 1000, null);
				discovery.getRemoteAdvertisements(null, DiscoveryService.GROUP,
						null, null, 1000, null);
				discovery.getRemoteAdvertisements(null, DiscoveryService.PEER,
						null, null, 1000, this);
				waitTime = r.nextInt(5) * 1000L;
				Thread.sleep(waitTime);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} while (true);

	}

}