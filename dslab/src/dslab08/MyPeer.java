package dslab08;

/*
* Copyright (c) 2006 Mark Petrovic <mspetrovic@gmail.com>
*
* Permission is hereby granted, free of charge, to any person obtaining
* a copy of this software and associated documentation files (the
* "Software"), to deal in the Software without restriction, including
* without limitation the rights to use, copy, modify, merge, publish,
* distribute, sublicense, and/or sell copies of the Software, and to
* permit persons to whom the Software is furnished to do so, subject to
* the following conditions:
*
* The above copyright notice and this permission notice shall be
* included in all copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
* EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
* MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
* NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
* LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
* OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
* WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*
* Original Author: Mark Petrovic <mspetrovic@gmail.com>
* */


import java.io.*;
import java.net.*;
import java.util.*;

import net.jxta.credential.*;
import net.jxta.discovery.*;
import net.jxta.endpoint.*;
import net.jxta.id.*;
import net.jxta.peergroup.*;
import net.jxta.pipe.*;
import net.jxta.protocol.*;

import net.jxta.membership.Authenticator;
import net.jxta.membership.MembershipService;

import net.jxta.document.*;
import net.jxta.platform.*;
import net.jxta.rendezvous.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MyPeer implements RendezvousListener, PipeMsgListener {

   private DiscoveryService netPGDiscoveryService;
   private RendezVousService appPGRdvService;
   private RendezVousService netPGRdvService;
   private NetworkConfigurator configurator;
   private OutputPipe outputPipe;
   private InputPipe inputPipe;
   private PeerGroup netPeerGroup;
   private PeerGroup appPeerGroup;
   private Random rand;

   private String NetPeerGroupID="urn:jxta:uuid-8B33E028B054497B8BF9A446A224B1FF02";
   private String NetPeerGroupName="My NetPG";
   private String NetPeerGroupDesc="A Private Net Peer Group";
   private String jxtaHome;
   private String rdvlock = new String("rocknroll");
   private String exitlock = new String("jazz");
   private String myPeerID;

   private boolean connected=false;

   // -------------------------------------

   public MyPeer() {
      //jxtaHome = System.getProperty("JXTA_HOME");
	   jxtaHome = ".";
      if ( null == jxtaHome ) {
         System.out.println("System property JXTA_HOME null. Exiting.");
         System.exit(1);
      }
      rand = new Random();
   }

   // -------------------------------------

   private void startJXTA() throws Throwable {

      clearCache(new File(jxtaHome,"cm"));

      NetPeerGroupFactory factory=null;
      try {
         factory = new NetPeerGroupFactory(
            (ConfigParams)configurator.getPlatformConfig(),
            new File(jxtaHome).toURI(),
            IDFactory.fromURI(new URI(NetPeerGroupID)),
            NetPeerGroupName,
            (XMLElement) StructuredDocumentFactory.newStructuredDocument(MimeMediaType.XMLUTF8,
                "desc", NetPeerGroupName)
         );
      }
      catch(URISyntaxException e) {
         e.printStackTrace();
         System.out.println("Exiting...");
         System.exit(1);
      }
      netPeerGroup = factory.getInterface();

      netPGDiscoveryService = netPeerGroup.getDiscoveryService();

      netPGRdvService = netPeerGroup.getRendezVousService();
      netPGRdvService.addListener(this);
   }

   // -------------------------------------

   public void createApplicationPeerGroup() {

      // key parameters for the new "appPeerGroup"
      String name = "MyAppGroup";
      String desc = "MyAppGroup Description goes here";
      String gid =  "urn:jxta:uuid-79B6A084D3264DF8B641867D926C48D902";
      String specID = "urn:jxta:uuid-309B33F10EDF48738183E3777A7C3DE9C5BFE5794E974DD99AC7D409F5686F3306";

      //  create the new application group, and publish its various advertisements
      try {
         ModuleImplAdvertisement implAdv = netPeerGroup.getAllPurposePeerGroupImplAdvertisement();
         ModuleSpecID modSpecID = (ModuleSpecID )IDFactory.fromURI(new URI(specID));
         implAdv.setModuleSpecID(modSpecID);
         PeerGroupID groupID = (PeerGroupID )IDFactory.fromURI(new URI(gid));
         appPeerGroup = netPeerGroup.newGroup(groupID, implAdv, name, desc);
         PeerGroupAdvertisement pgadv = appPeerGroup.getPeerGroupAdvertisement();

         appPGRdvService = appPeerGroup.getRendezVousService();

         myPeerID = appPeerGroup.getPeerID().toString();

         netPGDiscoveryService.publish(implAdv);
         netPGDiscoveryService.remotePublish(null,implAdv);
         netPGDiscoveryService.remotePublish(null,pgadv);
   
         // listen for app group rendezvous events
         appPeerGroup.getRendezVousService().addListener(this);
   
         // join the group
         if (appPeerGroup != null) {
            AuthenticationCredential cred = new AuthenticationCredential(appPeerGroup, null, null);
            MembershipService membershipService = appPeerGroup.getMembershipService();
            Authenticator authenticator = membershipService.apply(cred);
            if (authenticator.isReadyForJoin()) {
               membershipService.join(authenticator);
               System.out.println("Joined group: " + appPeerGroup);
            }
            else {
               System.out.println("Impossible to join the group");
            }
         }
      }
      catch(Exception e) {
         e.printStackTrace();
         System.out.println("Exiting.");
         System.exit(1);
      }

   }

   // -----------------------------------

   private void stop() {
      netPeerGroup.stopApp();
   }

   // -----------------------------------

   private void configureJXTA() {
      configurator = new NetworkConfigurator();
      configurator.setHome(new File(jxtaHome));
      configurator.setPeerID(IDFactory.newPeerID(PeerGroupID.defaultNetPeerGroupID));
      configurator.setName("My Peer Name");
      configurator.setPrincipal("ofno");
      configurator.setPassword("consequence");
      configurator.setDescription("I am a P2P Peer.");
      configurator.setUseMulticast(false);

      // fetch seeds from file, or alternately from network 
      URI seedingURI = new File("seeds.txt").toURI();  
      configurator.addRdvSeedingURI(seedingURI);
      configurator.addRelaySeedingURI(seedingURI);
      configurator.setUseOnlyRelaySeeds(true);
      configurator.setUseOnlyRendezvousSeeds(true);

      configurator.setTcpIncoming(false);

      try {
         configurator.save();
      }
      catch(IOException e) {
         e.printStackTrace();
         System.exit(1);
      }
      System.out.println("Platform configured and saved");
   }

   // ---------------------------------

   // the Rendezvous service callback
   public void rendezvousEvent(RendezvousEvent event) {
      String eventDescription;
      int eventType = event.getType();
      switch( eventType ) {
         case RendezvousEvent.RDVCONNECT:
               eventDescription = "RDVCONNECT";
               connected=true;
               break;
            case RendezvousEvent.RDVRECONNECT:
               eventDescription = "RDVRECONNECT";
               connected=true;
               break;
            case RendezvousEvent.RDVDISCONNECT:
               eventDescription = "RDVDISCONNECT";
               break;
            case RendezvousEvent.RDVFAILED:
               eventDescription = "RDVFAILED";
               break;
            case RendezvousEvent.CLIENTCONNECT:
               eventDescription = "CLIENTCONNECT";
               break;
            case RendezvousEvent.CLIENTRECONNECT:
               eventDescription = "CLIENTRECONNECT";
               break;
            case RendezvousEvent.CLIENTDISCONNECT:
               eventDescription = "CLIENTDISCONNECT";
               break;
            case RendezvousEvent.CLIENTFAILED:
               eventDescription = "CLIENTFAILED";
               break;
            case RendezvousEvent.BECAMERDV:
               eventDescription = "BECAMERDV";
               connected=true;
               break;
            case RendezvousEvent.BECAMEEDGE:
               eventDescription = "BECAMEEDGE";
               break;
            default:
               eventDescription = "UNKNOWN RENDEZVOUS EVENT";
      }
      System.out.println(new Date().toString() + "  Rdv: event=" + eventDescription + " from peer = " + event.getPeer());

      synchronized(rdvlock) {
         if( connected ) {
            rdvlock.notify();
         }
      }
   }

   // ---------------------------------

   public void waitForRdv() {
      synchronized (rdvlock) {
         while (! appPGRdvService.isConnectedToRendezVous() ) {
            System.out.println("Awaiting rendezvous conx...");
            try {
               if (! appPGRdvService.isConnectedToRendezVous() ) {
                  rdvlock.wait();
               }
            }
            catch (InterruptedException e) {
               ;
            }
         }
      }
   }

   // ---------------------------------

   private void waitForQuit() {
      synchronized(exitlock) {
         try {
            System.out.println("waiting for quit");
            exitlock.wait();
            System.out.println("Goodbye");
         }
         catch(InterruptedException e) {
            ;
         }
      }
   }

   // ---------------------------------

   private void setupPipe() {
      PipeAdvertisement propagatePipeAdv = (PipeAdvertisement )AdvertisementFactory.
         newAdvertisement(PipeAdvertisement.getAdvertisementType());

      try {
         byte[] bid  = MessageDigest.getInstance("MD5").digest("abcd".getBytes("ISO-8859-1"));
         PipeID pipeID = IDFactory.newPipeID(appPeerGroup.getPeerGroupID(), bid);
         propagatePipeAdv.setPipeID(pipeID);
         propagatePipeAdv.setType(PipeService.PropagateType);
         propagatePipeAdv.setName("A chattering propagate pipe");
         propagatePipeAdv.setDescription("verbose description");

	 PipeService pipeService = appPeerGroup.getPipeService();
         inputPipe  = pipeService.createInputPipe(propagatePipeAdv, this);
         outputPipe = pipeService.createOutputPipe(propagatePipeAdv, 1000);
         System.out.println("Propagate pipes and listeners created");
         System.out.println("Propagate PipeID: " + pipeID.toString());
      }
      catch (UnsupportedEncodingException e) {
         e.printStackTrace();
      }
      catch (NoSuchAlgorithmException e) {
         e.printStackTrace();
      }
      catch (IOException e) {
         e.printStackTrace();
      }

   }

   // ---------------------------------

   private void sendToPeers() {
      try {
         String data = new Integer(rand.nextInt()).toString();
         Message msg = new Message();

         MessageElement fromElem = new ByteArrayMessageElement(
            "From", null, myPeerID.toString().getBytes("ISO-8859-1"), null
         );
         MessageElement msgElem = new ByteArrayMessageElement(
            "Msg", null, data.getBytes("ISO-8859-1"), null
         );

         msg.addMessageElement(fromElem);
         msg.addMessageElement(msgElem);
         outputPipe.send(msg);
      }
      catch(IOException e) {
         e.printStackTrace();
      }

   }

   // ---------------------------------

   private void doSomething() {
      setupPipe();
      new Thread("AppGroup Send Thread") {
         public void run() {
            int sleepy=10000;
            while(true) {
               sendToPeers();
               try {
                  sleep(sleepy);
               }
               catch(InterruptedException e) {}
            }
         }
      }.start();
   }

   // ---------------------------------

   // the InputPipe callback
   public void pipeMsgEvent(PipeMsgEvent event) {
     try {
       Message msg = event.getMessage();
       byte[] msgBytes = msg.getMessageElement("Msg").getBytes(true);  
       byte[] fromBytes = msg.getMessageElement("From").getBytes(true);  

       String fromPeerID = new String(fromBytes);
       if( fromPeerID.equals(myPeerID)) {
          System.out.print("(from self): ");
       }
       else {
          System.out.print("(from other): ");
       }
       System.out.print(new Date());
       System.out.println(" " + fromPeerID + " says " + new String(msgBytes));
     }
     catch (Exception e) {
       e.printStackTrace();
       return;
     }

   }

   // ---------------------------------

   private static void clearCache(final File rootDir) {
      try {
         if (rootDir.exists()) {
            File[] list = rootDir.listFiles();
            for (File aList : list) {
               if (aList.isDirectory()) {
                  clearCache(aList);
               } else {
                  aList.delete();
               }
            }
         }
         rootDir.delete();
         System.out.println("Cache component " + rootDir.toString() + " cleared.");
      }
      catch (Throwable t) {
         System.out.println("Unable to clear " + rootDir.toString());
         t.printStackTrace();
      }
   }

   // ---------------------------------

   public static void main(String[] args) throws Throwable {
      MyPeer peer = new MyPeer();
      peer.configureJXTA();
      try {
         peer.startJXTA();
         peer.createApplicationPeerGroup();
         peer.waitForRdv();
         peer.doSomething();
         peer.waitForQuit();
      }
      catch(Exception e) {
         e.printStackTrace();
         System.out.println("Exiting.");
         System.exit(1);
      }
   }
}
