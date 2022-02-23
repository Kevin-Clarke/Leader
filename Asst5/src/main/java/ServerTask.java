import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.io.PrintWriter;

import org.json.*;

/**
 * This is the class that handles communication with a peer/client that has connected to use
 * and wants something from us
 * 
 */

public class ServerTask extends Thread {
	private BufferedReader bufferedReader;
	private Peer peer = null; // so we have access to the peer that belongs to that thread
	private PrintWriter out = null;
	private Socket socket = null;
	
	// Init with socket that is opened and the peer
	public ServerTask(Socket socket, Peer peer) throws IOException {
		bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);
		this.peer = peer;
		this.socket = socket;
	}
	
	// basically wait for an input, right now we can only handle a join request
	// and a message
	// More requests will be needed to make everything work
	// You can enhance this or totally change it, up to you. 
	// I used simple JSON here, you can use your own protocol, use protobuf, anything you want
	// in here this is not done especially pretty, I just use a PrintWriter and BufferedReader for simplicity
	public void run() {
		while (true) {
			try {
			    JSONObject json = new JSONObject(bufferedReader.readLine());

				if (peer.isLeader()){
					System.out.println("  JSON Received: " + json); // just to show the json
				}

			    if (json.getString("type").equals("join")){
			    	peer.updateListenToPeers(json.getString("ip") + ":" + json.getInt("port"));
			    	out.println(("{'type': 'join', 'list': '"+ peer.getPeers() +"'}"));

			    	if (peer.isLeader()){
			    		if (json.getString("peerType").equals("client")){
							peer.pushMessage("{'type': 'idRequest'}");
						}
						System.out.println(json.getString("username") + " joined the network");
			    	}
			    	// TODO: should make sure that all peers that the leader knows about also get the info about the new peer joining
			    	// so they can add that peer to the list
			    }else if(json.getString("type").equals("idRequest")){

					if (peer.isClient()){
						System.out.println("Please type 'id' to begin entering your Client ID.");
					}else if (peer.isLeader()){
						System.out.println("Sent ID request");
					}else{
						peer.updateClientBalance(json.getString("id"),0);
					}
				}else if(json.getString("type").equals("id")){
					if (peer.isLeader() && peer.clientID == null){
						peer.clientID = (json.getString("data"));
						System.out.println("Client ID set to " + json.getString("data"));
						peer.pushMessage("{'type': 'firstMenu', 'id': '" + json.getString("data") + "'}");
					}else{
						Thread.sleep(1000);
					}
				}else if(json.getString("type").equals("firstMenu")){
					if (peer.isClient()){
						System.out.println("Please enter 'credit' to request a loan, or enter 'payment' to pay back an existing loan.");
					}else if (peer.isLeader()){
						;
					}else{
						peer.updateClientBalance(json.getString("id"), 0);
						System.out.println("Registered user " + json.getString("id") + " with starting balance " + peer.readClientBalance(json.getString("id")));
					}
				}else if(json.getString("type").equals("balance")){

					if (peer.isLeader()){
						;
					}else if (peer.isClient()){
						;
					}else{
						peer.pushMessage("{'type': 'balance', 'username': '" + peer.getUsername() + "', 'data': '" + peer.getMoney() + "'}");
					}
				}else if(json.getString("type").equals("credit")){
			    	if (peer.isLeader()){
			    		System.out.println("Credit requested");
					}
				}else if(json.getString("type").equals("payment")){
					System.out.println("Payment requested");;
				}else {
			    	System.out.println("[" + json.getString("username")+"]: " + json.getString("message"));
			    }
			    
			    
			} catch (Exception e) {
				interrupt();
				break;
			}
		}
	}

}
