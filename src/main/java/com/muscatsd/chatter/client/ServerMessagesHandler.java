package com.muscatsd.chatter.client;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import com.muscatsd.chatter.client.frame.ClientMainFrame;

/**
 * Handler thread for messages coming from the server to specific client.
 * @author Hatem Al Amri
 */
public class ServerMessagesHandler extends Thread {
	
	private final Logger logger = Logger.getLogger(getClass().getName());
	
	private Socket client;
	private ClientMainFrame clientMainFrame;



	public void run(){
		handleMessages();
	}
	
	/**
	 * Handle messages/responses coming from server.
	 */
	public void handleMessages(){
		
		// keep search about incoming messages
		while(true){
			
			// search about messages if socket is alive
			if(!getClient().isClosed()){
				DataInputStream in;
				try {
					
					in = new DataInputStream(client.getInputStream());
					String message = in.readUTF();
					
					if(!message.startsWith("/")){
						clientMainFrame.getPublicChatTextArea().append(message);
					}else if(message.startsWith("/message/")){
						String[] command = message.split("/");
						clientMainFrame.getPublicChatTextArea().append("\n" + command[2]);
	
					}else if(message.startsWith("/list")){
						String[] command = message.split(":");
						String[] users = command[1].split(",");
						getClientMainFrame().getClientsList().setListData(users);
						getClientMainFrame().getClientsList().setVisibleRowCount(getClientMainFrame().getClientsList().getVisibleRowCount() -1);
					}else if(message.startsWith("/stopped/")){
						JOptionPane.showMessageDialog(null, "Server is stopped !", "Error",JOptionPane.WARNING_MESSAGE);
						getClientMainFrame().closeFrame();
	
					}
					
					
				} catch (IOException e) {
	
				}
			}else{
				
				// is socket is closed don't search about incoming messages anymore for this client
				break;
			}
		}		
	}

	public Logger getLogger() {
		return logger;
	}



	public Socket getClient() {
		return client;
	}

	public void setClient(Socket client) {
		this.client = client;
	}

	public ClientMainFrame getClientMainFrame() {
		return clientMainFrame;
	}

	public void setClientMainFrame(ClientMainFrame clientMainFrame) {
		this.clientMainFrame = clientMainFrame;
	}

}
