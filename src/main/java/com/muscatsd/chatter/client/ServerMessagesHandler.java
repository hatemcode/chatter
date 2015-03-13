package com.muscatsd.chatter.client;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import com.hatemcode.chatter.responder.MessageResponder;
import com.hatemcode.chatter.responder.imp.ServerMessageResponder;
import com.muscatsd.chatter.client.frame.ClientFrame;

/**
 * Handler thread for messages coming from the server to specific client.
 * @author Hatem Al Amri
 */
public class ServerMessagesHandler extends Thread {
	
	private final Logger logger = Logger.getLogger(getClass().getName());
	
	private Socket client;
	private ClientFrame clientFrame;
	private MessageResponder messageResponder;

	public ServerMessagesHandler(){
		setMessageResponder(new ServerMessageResponder());

	}

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
						clientFrame.getPublicChatTextArea().append(message);
					}else if(message.startsWith("/message/")){
						String[] command = message.split("/");
						clientFrame.getPublicChatTextArea().append("\n" + command[2]);
	
					}else if(message.startsWith("/list")){
						String[] command = message.split(":");
						String[] users = command[1].split(",");
						getClientFrame().getClientsList().setListData(users);
						getClientFrame().getClientsList().setVisibleRowCount(getClientFrame().getClientsList().getVisibleRowCount() -1);
					}else if(message.startsWith("/stopped/")){
						JOptionPane.showMessageDialog(null, "Server is stopped !", "Error",JOptionPane.WARNING_MESSAGE);
						getClientFrame().closeFrame();
	
					}
					
					
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, e.getMessage() , "Error",JOptionPane.ERROR_MESSAGE); e.printStackTrace();
	
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

	public ClientFrame getClientFrame() {
		return clientFrame;
	}

	public void setClientFrame(ClientFrame clientFrame) {
		this.clientFrame = clientFrame;
	}

	public MessageResponder getMessageResponder() {
		return messageResponder;
	}

	public void setMessageResponder(MessageResponder messageResponder) {
		this.messageResponder = messageResponder;
	}


}
