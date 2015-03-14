package com.hatemcode.chatter.server;

import java.net.Socket;

/**
 * Represent client.
 * @author Hatem Al Amri
 *
 */
public class Client {
	
	private Integer id;
	private String nickname;
	private Socket socket;
	private ClientMessagesHandler clientMessagesHandler;
	
	public Client(){
	}
	
	public Client(Integer id,String nickname,Socket socket,ClientMessagesHandler clientMessagesHandler){
		setId(id);
		setNickname(nickname);
		setSocket(socket);
		setClientMessagesHandler(clientMessagesHandler);
	}
	

	
	/*** Getters & Setters ***/
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public ClientMessagesHandler getClientMessagesHandler() {
		return clientMessagesHandler;
	}

	public void setClientMessagesHandler(ClientMessagesHandler clientMessagesHandler) {
		this.clientMessagesHandler = clientMessagesHandler;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
