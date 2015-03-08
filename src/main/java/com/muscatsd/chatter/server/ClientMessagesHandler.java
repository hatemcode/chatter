package com.muscatsd.chatter.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Logger;

import com.muscatsd.chatter.server.frame.ServerMainFrame;

public class ClientMessagesHandler extends Thread {
	
	private final Logger logger = Logger.getLogger(getClass().getName());
	
	private Socket client;
	private ServerSession serverSession;
	private ServerMainFrame serverMainFrame;

	public ClientMessagesHandler(ServerSession serverSession,Socket client,ServerMainFrame serverMainFrame){
		setServerSession(serverSession);
		setClient(client);
		setServerMainFrame(serverMainFrame);
	}

	public void run(){
		serverMainFrame.getLogsTextArea().append("\n new client message handler ..");
		 while (true) {
			 
			 try {
				if(!getClient().isClosed()){ 
					DataInputStream in = new DataInputStream(client.getInputStream());
					String message = in.readUTF();
					serverMainFrame.getLogsTextArea().append("\n " + message);
					
					if(message.startsWith("/user/")){
						String[] command = message.split(" ");
						if(getServerSession().getClients().size() == 0){
							getServerSession().getClients().add(new Client(command[1],client,this));
							serverMainFrame.getLogsTextArea().append("\n user with nickname: " + command[1] + " joined chat ..");
							
							OutputStream response = getClient().getOutputStream();
							DataOutputStream out = new DataOutputStream(response);
							out.writeUTF("/user accepted/");
							getServerSession().broadcastList();
							getServerSession().broadcast(command[1] + " joied chat\n");
						}else{
							if(!getServerSession().searchClients(command[1])){
								getServerSession().getClients().add(new Client(command[1],client,this));
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

	public ServerMainFrame getServerMainFrame() {
		return serverMainFrame;
	}

	public void setServerMainFrame(ServerMainFrame serverMainFrame) {
		this.serverMainFrame = serverMainFrame;
	}

	public ServerSession getServerSession() {
		return serverSession;
	}

	public void setServerSession(ServerSession serverSession) {
		this.serverSession = serverSession;
	}
}
