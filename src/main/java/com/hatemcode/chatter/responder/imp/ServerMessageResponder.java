package com.hatemcode.chatter.responder.imp;

import java.io.DataInputStream;
import java.io.IOException;

import javax.swing.JOptionPane;

import com.hatemcode.chatter.client.ServerMessagesHandler;
import com.hatemcode.chatter.responder.MessageResponder;

public class ServerMessageResponder implements MessageResponder {
	
	private ServerMessagesHandler serverMessagesHandler;
	
	public ServerMessageResponder(ServerMessagesHandler serverMessagesHandler){
		setServerMessagesHandler(serverMessagesHandler);
	}
	
	@Override
	public void respond() {
		DataInputStream inputStream;
		try {
			
			inputStream = new DataInputStream(getServerMessagesHandler().getSocket().getInputStream());
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

	}

	
	/**
	 * When server is stopped.
	 * @param message
	 */
	public void serverStopped(String message){
		JOptionPane.showMessageDialog(null, "Server is stopped !", "Warning",JOptionPane.WARNING_MESSAGE);
		getServerMessagesHandler().getClientFrame().closeFrame();
	}
	
	/**
	 * When serve sending the clients list.
	 * @param message
	 */
	@SuppressWarnings("unchecked")
	public void listReceived(String message){
		
		String clients = message.replace("/list:","");
		String[] clientsArray = clients.split(",");
		getServerMessagesHandler().getClientFrame().getClientsList().setListData(clientsArray);
		getServerMessagesHandler().getClientFrame().getClientsList().setVisibleRowCount(getServerMessagesHandler().getClientFrame().getClientsList().getVisibleRowCount() -1);
		getServerMessagesHandler().getClientFrame().highLightCurrentNickname();
	}

	/**
	 * When public message is received.
	 * @param message
	 */
	public void publicMessageReceived(String message){
		
		message = message.replace("/message/","");
		getServerMessagesHandler().getClientFrame().getPublicChatTextArea().append("\n" + message);

	}
	

	/**
	 * When public announcement is received.
	 * @param message
	 */
	public void publicAnnouncementReceived(String message){
		
		getServerMessagesHandler().getClientFrame().getPublicChatTextArea().append("\n" + message);

	}
	
	public ServerMessagesHandler getServerMessagesHandler() {
		return serverMessagesHandler;
	}

	public void setServerMessagesHandler(ServerMessagesHandler serverMessagesHandler) {
		this.serverMessagesHandler = serverMessagesHandler;
	}


}
