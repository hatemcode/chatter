package com.hatemcode.chatter.responder.imp;

import java.io.DataInputStream;
import java.io.IOException;
import com.hatemcode.chatter.responder.MessageResponder;
import com.hatemcode.chatter.server.Client;
import com.hatemcode.chatter.server.ClientMessagesHandler;

public class ClientMessageResponder implements MessageResponder {

	private ClientMessagesHandler clientMessagesHandler;
	
	public ClientMessageResponder(ClientMessagesHandler clientMessagesHandler){
		setClientMessagesHandler(clientMessagesHandler);
	}
	
	@Override
	public void respond() {
		 try {
				
			DataInputStream inputStream = new DataInputStream(getClientMessagesHandler().getSocket().getInputStream());
			String message = inputStream.readUTF();
			
			if(message.startsWith("/user/")){
				
				newUser(message);
				
			}else if(message.startsWith("/message/")){
				
				publicMessage(message);
				
			}else if(message.startsWith("/leave/")){
				
				leaveChat(message);
				
			}else{
				getClientMessagesHandler().getServerFrame().logToFrame(message);
			}

			
		} catch (IOException e) {
			//break;

		}
		
	}
	/**
	 * New user.
	 * @param message incoming message from client
	 */
	public void newUser(String message){
		String nickname = message.replace("/user/", "");
		
		if(!getClientMessagesHandler().getServerSession().clientIsExist(nickname)){
			
			Integer id = getClientMessagesHandler().getServerSession().getClients().size();
			Client client = new Client(id,nickname,getClientMessagesHandler().getSocket(),getClientMessagesHandler());
			getClientMessagesHandler().getServerSession().addClient(client);
			
			getClientMessagesHandler().getServerSession().sendMessage(getClientMessagesHandler().getSocket(),"/user accepted/");
			getClientMessagesHandler().getServerSession().broadcastList();
			getClientMessagesHandler().getServerSession().broadcast(nickname + " joined chat");
			
		}else{
			getClientMessagesHandler().getServerSession().sendMessage(getClientMessagesHandler().getSocket(), "/user rejected/");
		}
	}
	
	/**
	 * Send public message.
	 * @param message incoming message from client
	 */
	public void publicMessage(String message){
		getClientMessagesHandler().getServerSession().broadcast(message);
	}
	/**
	 * Client leave chat.
	 * @param message incoming message from client
	 */
	public void leaveChat(String message){
		String nickname = message.replace("/leave/", "");
		getClientMessagesHandler().getServerSession().removeClient(nickname);
	}
	
	
	public ClientMessagesHandler getClientMessagesHandler() {
		return clientMessagesHandler;
	}

	public void setClientMessagesHandler(ClientMessagesHandler clientMessagesHandler) {
		this.clientMessagesHandler = clientMessagesHandler;
	}

}