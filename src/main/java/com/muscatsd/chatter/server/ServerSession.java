package com.muscatsd.chatter.server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import com.muscatsd.chatter.server.frame.ServerFrame;

/**
 * Represent the server in working mode.
 * @author Hatem Al Amri
 */
public class ServerSession extends Thread {
	
	private final Logger logger = Logger.getLogger(getClass().getName());
	
	private ServerSocket serverSocket;
	private List<Client> clients = new ArrayList<Client>();
	
	// server frame
	private ServerFrame serverFrame;
	

	/**
	 * Run server session thread.
	 */
	public void run(){
		
		getServerFrame().logToFrame("Server session is started ..");
		try {
			getServerFrame().logToFrame("Server Host/IP is: " + InetAddress.getLocalHost());
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null, e.getMessage() , "Error",JOptionPane.ERROR_MESSAGE); e.printStackTrace();

		}
		
		handlingClients();

	}
	
	/**
	 * Searching about new clients and assign for each of them message handler in the server session.
	 */
	public void handlingClients(){
		
		getServerFrame().logToFrame("Handling clients ..");
		
		// keep searching about clients
		while (!getServerSocket().isClosed()) {
		 try {	
				// accept new client
				Socket clientSocket = getServerSocket().accept();
				getServerFrame().logToFrame("new client joined ..");
				
				// establish message handler for the new client
				ClientMessagesHandler messagesHandler = new ClientMessagesHandler(this,clientSocket,serverFrame);
				messagesHandler.start();
			
			
		} catch (IOException e) {
			break;
		}
         }
	}
	
	/**
	 * Stop server session and client message handlers.
	 */
	public void stopSession(){
		
		Integer index = -1;

		for(Client client :getClients()){
			index++;
			// send message to client to notify him about closing the session
			sendMessage(client, "/stopped/");			
			
			try {
				// close client socket
				client.getSocket().close();
				
				// remove client
				getClients().remove(index);	


			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, e.getMessage() , "Error",JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();

			}
			
		}
		
		getServerFrame().logToFrame("Server session is closed ..");
	}

	/**
	 * Check if certain client is exist.
	 * @param nickname Nickname
	 * @return true if found , false if not
	 */
	public Boolean clientIsExist(String nickname){
		
		for(Client client : getClients()){
			if(client.getNickname().equals(nickname)){
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Search about certain client.
	 * @param nickname client nickname
	 * @return client object if found , null if not
	 */
	public Client searchClients(String nickname){
		
		for(Client client : getClients()){
			if(client.getNickname().equals(nickname)){
				return client;
			}
		}
		
		return null;
	}
	
	/**
	 * Add new client to server session.
	 * @param client
	 */
	public void addClient(Client client){
		getClients().add(client);
		getServerFrame().logToFrame("Clients number: " + getClients().size());

	}

	/**
	 * Remove client from server session.
	 * @param nickname
	 */
	public void removeClient(String nickname){
		
		Client client = searchClients(nickname);
		if(client != null)
		{
			// remove client from clients list
			getClients().remove(client.getId().intValue());
			
			getServerFrame().logToFrame("Client(" + client.getId() + "): " + client.getNickname() + " leaves");
			getServerFrame().logToFrame("Clients number: " + getClients().size());
			// announce about client leaving
			broadcast("\n" + nickname + " leaves chat.");
			
			// send the new updated client list
			broadcastList();
			
			try {
				// close client socket
				client.getClientMessagesHandler().getClient().close();
				
			} catch (IOException e) {
				
				JOptionPane.showMessageDialog(null, e.getMessage() , "Error",JOptionPane.ERROR_MESSAGE); e.printStackTrace();


			}			
		}
	}
	
	/**
	 * Send message to specific client.
	 * @param client
	 * @param message
	 */
	public void sendMessage(Client client,String message){
		OutputStream stream;
		try {
			stream = client.getSocket().getOutputStream();
			DataOutputStream dataStream = new DataOutputStream(stream);
			dataStream.writeUTF(message);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage() , "Error",JOptionPane.ERROR_MESSAGE); e.printStackTrace();


		}			
	}
	

	/**
	 * Send message to specific client socket.
	 * @param socket client socket
	 * @param message
	 */
	public void sendMessage(Socket socket,String message){
		OutputStream stream;
		try {
			stream = socket.getOutputStream();
			DataOutputStream dataStream = new DataOutputStream(stream);
			dataStream.writeUTF(message);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage() , "Error",JOptionPane.ERROR_MESSAGE); e.printStackTrace();


		}			
	}
	
	/**
	 * Broadcast message to all clients.
	 * @param message Message
	 */
	public void broadcast(String message){
		
		for(Client client : getClients()){			
			sendMessage(client,message);			
		}
	}
	
	/**
	 * Broadcast clients list to all clients.
	 */
	public void broadcastList(){
		String message = "/list:";
		Integer counter = 0;
		
		for(Client client : getClients()){
			counter++;
			
			if(counter == 1){
				message = message + client.getNickname();
			}else{
				message = message + "," + client.getNickname();
			}
		}
		
		broadcast(message);
	}
	
	
	/**
	 * Get number of clients.
	 * @return number of clients
	 */
	public Integer clientsNumber(){
		return getClients().size();
	}

	/*** Getters & Setters ***/
	public Logger getLogger() {
		return logger;
	}

	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	public void setServerSocket(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	public ServerFrame getServerFrame() {
		return serverFrame;
	}

	public void setServerFrame(ServerFrame serverFrame) {
		this.serverFrame = serverFrame;
	}

	public List<Client> getClients() {
		return clients;
	}

	public void setClients(List<Client> clients) {
		this.clients = clients;
	}
}
