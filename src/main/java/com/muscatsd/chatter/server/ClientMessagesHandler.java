package com.muscatsd.chatter.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Logger;

import com.hatemcode.chatter.responder.MessageResponder;
import com.hatemcode.chatter.responder.imp.ClientMessageResponder;
import com.muscatsd.chatter.server.frame.ServerFrame;

public class ClientMessagesHandler extends Thread {
	
	private final Logger logger = Logger.getLogger(getClass().getName());
	
	private Socket client;
	private ServerSession serverSession;
	private ServerFrame serverMainFrame;
	

	public ClientMessagesHandler(ServerSession serverSession,Socket client,ServerFrame serverMainFrame){
		setServerSession(serverSession);
		setClient(client);
		setServerMainFrame(serverMainFrame);
	}

	public void run(){
		serverMainFrame.getLogsTextArea().append("\n new client message handler ..");
		MessageResponder messageResponder = new ClientMessageResponder(getServerSession(),this,getClient());
		 while (true) {
			 
			 try {
				if(!getClient().isClosed()){ 
					DataInputStream in = new DataInputStream(client.getInputStream());
					String message = in.readUTF();
					serverMainFrame.getLogsTextArea().append("\n " + message);
					
					if(message.startsWith("/user/")){
						String[] command = message.split(" ");
						if(getServerSession().getClients().size() == 0){
							getServerSession().getClients().add(new Client(getServerSession().getClients().size()-1,command[1],client,this));
							serverMainFrame.getLogsTextArea().append("\n user with nickname: " + command[1] + " joined chat ..");
							
							OutputStream response = getClient().getOutputStream();
							DataOutputStream out = new DataOutputStream(response);
							out.writeUTF("/user accepted/");
							getServerSession().broadcastList();
							getServerSession().broadcast(command[1] + " joied chat\n");
						}else{
							if(!getServerSession().searchClients(command[1])){
								getServerSession().getClients().add(new Client(getServerSession().getClients().size()-1,command[1],client,this));
								serverMainFrame.getLogsTextArea().append("\n user with nickname: " + command[1] + " joined chat ..");
								
								OutputStream response = getClient().getOutputStream();
								DataOutputStream out = new DataOutputStream(response);
								out.writeUTF("/user accepted/");
								getServerSession().broadcastList();
								getServerSession().broadcast(command[1] + " joied chat\n");
							}else{
								OutputStream response = getClient().getOutputStream();
								DataOutputStream out = new DataOutputStream(response);
								out.writeUTF("/user rejected/");
							}
						}
					}else if(message.startsWith("/message/")){
						getServerSession().broadcast(message);
					}else{
						serverMainFrame.getLogsTextArea().append("\n " + message);
					}
				}else{
					
					break;
				}
				
			} catch (IOException e) {
				
			}

		 }
	}
	
	private void respond(String message){
		if(message.startsWith("/user/")){
			newUser(message);
		}
	}

	public void newUser(String message){
		String nickname = message.replace("/user/", "");
		
		if(getServerSession().clientsNumber() == 0){
			
			Integer id = getServerSession().clientsNumber() - 1;
			Client client = new Client(id,nickname,getClient(),this);
			getServerSession().addClient(client);
			getServerSession().frameLog(client.getId() + ": " + client.getNickname() + " joined chat ..");
			
		}else if(getServerSession().clientsNumber() == 1){
			
			if(!getServerSession().searchClients(nickname)){
				
				Integer id = getServerSession().clientsNumber() - 1;
				Client client = new Client(id,nickname,getClient(),this);
				getServerSession().addClient(client);
				getServerSession().frameLog(client.getId() + ": " + client.getNickname() + " joined chat ..");
			}
		}
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

	public ServerFrame getServerMainFrame() {
		return serverMainFrame;
	}

	public void setServerMainFrame(ServerFrame serverMainFrame) {
		this.serverMainFrame = serverMainFrame;
	}

	public ServerSession getServerSession() {
		return serverSession;
	}

	public void setServerSession(ServerSession serverSession) {
		this.serverSession = serverSession;
	}


}
