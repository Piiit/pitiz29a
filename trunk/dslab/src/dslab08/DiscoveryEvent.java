package dslab08;


import java.util.Enumeration;
import java.util.EventObject;
import net.jxta.document.Advertisement;

public class DiscoveryEvent extends EventObject{
	private static final long serialVersionUID = 4185128995329571583L;
	
private Enumeration<Advertisement> advertisements;

    public DiscoveryEvent(Object source) {
        super(source);
    }

    public void setAdvertisements(Enumeration<Advertisement> advertisements){
        this.advertisements=advertisements;
    }

    public Enumeration<Advertisement> getAdvertisements(){
        return this.advertisements;
    }
}
