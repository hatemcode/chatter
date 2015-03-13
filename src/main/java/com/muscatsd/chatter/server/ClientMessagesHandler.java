package com.muscatsd.chatter.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import com.hatemcode.chatter.responder.MessageResponder;
import com.hatemcode.chatter.responder.imp.ClientMessageResponder;
import com.muscatsd.chatter.server.frame.ServerFrame;

public class ClientMessagesHandler extends Thread {
	
	private final Logger logger = Logger.getLogger(getClass().getName());
	
	private Socket client;
	private ServerSession serverSession;
	private ServerFrame serverFrame;
	

	public ClientMessagesHandler(ServerSession serverSession,Socket client,ServerFrame serverFrame){
		setServerSession(serverSession);
		setClient(client);
		setServerFrame(serverFrame);
	}

	public void run(){
		
		getServerFrame().logToFrame("new client message handler ..");
		
		 while (true) {
			 
			 try {
				if(!getClient().isClosed()){ 
					
					DataInputStream inputStream = new DataInputStream(getClient().getInputStream());
					String message = inputStream.readUTF();
					
					if(message.startsWith("/user/")){
						
						newUser(message);
						
					}else if(message.startsWith("/message/")){
						
						publicMessage(message);
						
					}else if(message.startsWith("/leave/")){
						
						leaveChat(message);
						
					}else{
						getServerFrame().logToFrame(message);
					}
				}else{
					
					break;
				}
				
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, e.getMessage() , "Error",JOptionPane.ERROR_MESSAGE); e.printStackTrace();


			}

		 }
	}
	
	/**
	 * New user.
	 * @param message incoming message from client
	 */
	public void newUser(String message){
		String nickname = message.replace("/user/", "");
		
		if(!getServerSession().clientIsExist(nickname)){
			
			Integer id = getServerSession().getClients().size();
			Client client = new Client(id,nickname,getClient(),this);
			getServerSession().addClient(client);
			
			getServerSession().sendMessage(getClient(),"/user accepted/");
			getServerSession().broadcastList();
			getServerSession().broadcast(nickname + " joined chat\n");
			
		}else{
			getServerSession().sendMessage(getClient(), "/user rejected/");
		}
	}
	
	/**
	 * Send public message.
	 * @param message incoming message from client
	 */
	public void publicMessage(String message){
		getServerSession().broadcast(message);
	}
	/**
	 * Client leave chat.
	 * @param message incoming message from client
	 */
	public void leaveChat(String message){
		String nickname = message.replace("/leave/", "");
		getServerSession().removeClient(nickname);
	}
	
	
	/*** Getters & Setters ***/
	public Logger getLogger() {
		return logger;
	}


	public Socket getClient() {
		return client;
	}

	public void setClient(Socket client) {
		this.client = client;
	}

	public ServerFrame getServerFrame() {
		return serverFrame;
	}

	public void setServerFrame(ServerFrame serverFrame) {
		this.serverFrame = serverFrame;
	}

	public ServerSession getServerSession() {
		return serverSession;
	}

	public void setServerSession(ServerSession serverSession) {
		this.serverSession = serverSession;
	}


}
