package com.hatemcode.chatter.responder.imp;

import java.net.Socket;

import com.hatemcode.chatter.responder.MessageResponder;
import com.muscatsd.chatter.server.Client;
import com.muscatsd.chatter.server.ClientMessagesHandler;
import com.muscatsd.chatter.server.ServerSession;

public class ClientMessageResponder implements MessageResponder {

	private ServerSession serverSession;
	private Socket socket;
	private ClientMessagesHandler clientMessageHandler;
	
	private String message;
	
	public ClientMessageResponder(ServerSession serverSession,ClientMessagesHandler clientMessageHandler,Socket socket){
		setServerSession(serverSession);
		setClientMessageHandler(clientMessageHandler);
		setSocket(socket);
	}
	
	@Override
	public void respond() {
	
	}
	
	private void newUser(){
		
		String nickname = getMessage().replace("/user/", "");
		
		// if nickname is not exist
		if(!getServerSession().clientIsExist(nickname)){
			
			Integer id = getServerSession().getClients().size() - 1;
			Client client = new Client(id,nickname,getSocket(),getClientMessageHandler());
			getServerSession().sendMessage(client,"/user accepted/");
			
		}else{
			
		}
	}

	public ServerSession getServerSession() {
		return serverSession;
	}

	public void setServerSession(ServerSession serverSession) {
		this.serverSession = serverSession;
	}

	public ClientMessagesHandler getClientMessageHandler() {
		return clientMessageHandler;
	}

	public void setClientMessageHandler(ClientMessagesHandler clientMessageHandler) {
		this.clientMessageHandler = clientMessageHandler;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	

}
