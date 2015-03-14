package com.hatemcode.chatter.client;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import com.hatemcode.chatter.client.frame.ClientFrame;
import com.hatemcode.chatter.responder.MessageResponder;
import com.hatemcode.chatter.responder.imp.ServerMessageResponder;

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
		while(!getClient().isClosed()){
			
			// search about messages if socket is alive
			if(!getClient().isClosed()){
				DataInputStream inputStream;
				try {
					
					inputStream = new DataInputStream(getClient().getInputStream());
					String message = inputStream.readUTF();
					
					if(!message.startsWith("/")){
						
						publicAnnouncementReceived(message);
						
					}else if(message.startsWith("/message/")){
						
						publicMessageReceived(message);

					}else if(message.startsWith("/list")){
						
						listReceived(message);
						
					}else if(message.startsWith("/stopped/")){
						
						serverStopped(message);
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
	
	/**
	 * When server is stopped.
	 * @param message
	 */
	public void serverStopped(String message){
		JOptionPane.showMessageDialog(null, "Server is stopped !", "Warning",JOptionPane.WARNING_MESSAGE);
		getClientFrame().closeFrame();
	}
	
	/**
	 * When serve sending the clients list.
	 * @param message
	 */
	public void listReceived(String message){
		
		String clients = message.replace("/list:","");
		String[] clientsArray = clients.split(",");
		getClientFrame().getClientsList().setListData(clientsArray);
		getClientFrame().getClientsList().setVisibleRowCount(getClientFrame().getClientsList().getVisibleRowCount() -1);
		getClientFrame().highLightCurrentNickname();
	}

	/**
	 * When public message is received.
	 * @param message
	 */
	public void publicMessageReceived(String message){
		
		message = message.replace("/message/","");
		getClientFrame().getPublicChatTextArea().append("\n" + message);

	}
	

	/**
	 * When public announcement is received.
	 * @param message
	 */
	public void publicAnnouncementReceived(String message){
		
		getClientFrame().getPublicChatTextArea().append("\n" + message);

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
