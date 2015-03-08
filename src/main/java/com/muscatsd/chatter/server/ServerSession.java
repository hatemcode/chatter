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

import com.muscatsd.chatter.server.frame.ServerMainFrame;

/**
 * Represent the server in working mode.
 * @author Hatem Al Amri
 */
public class ServerSession extends Thread {
	
	private final Logger logger = Logger.getLogger(getClass().getName());
	
	private ServerSocket serverSocket;
	private List<Client> clients = new ArrayList<Client>();
	private ServerMainFrame serverMainFrame;
	

	/**
	 * Run server session thread.
	 */
	public void run(){
		
		frameLog("\n Server session started ..");
		try {
			frameLog("\n Server Host/IP is: " + InetAddress.getLocalHost());
		} catch (UnknownHostException e) {

			e.printStackTrace();
		}
		
		handlingClients();

	}
	
	/**
	 * Searching about new clients and assign for each of them message handler in the server session.
	 */
	public void handlingClients(){
		
		serverMainFrame.getLogsTextArea().append("\n Handling clients ..");
		
		// keep searching about clients
		while (true) {
			 try {
				
				 // if server socket is not closed
				if(!getServerSocket().isClosed()){
					
					// accept new client
					Socket clientSocket = getServerSocket().accept();
					serverMainFrame.getLogsTextArea().append("\n new client joned ..");
					
					// establish message handler for the new client
					ClientMessagesHandler messagesHandler = new ClientMessagesHandler(this,clientSocket,serverMainFrame);
					messagesHandler.start();
					serverMainFrame.getLogsTextArea().append("\n new client message handler ..");
					
				}else{
					
					// if server socket is closed stop handling clients
					serverMainFrame.getLogsTextArea().append("\n Server session is closed ..");
					break;
				}
				
			} catch (IOException e) {
				serverMainFrame.getLogsTextArea().append("\n " + e.getMessage());
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
			client.sendMessage("/stopped/");
			
			
			try {
				// close client socket
				client.getSocket().close();
				
				// remove client
				getClients().remove(index);

			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "Error",JOptionPane.ERROR_MESSAGE);
			}
			
		}
	}

	/**
	 * Search clients.
	 * @param nickname Nickname
	 * @return true if found , false if not
	 */
	public Boolean searchClients(String nickname){
		
		for(Client client : getClients()){
			if(client.getNickname().equals(nickname)){
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Broadcast message to all clients.
	 * @param message Message
	 */
	public void broadcast(String message){
		
		for(Client client : getClients()){
			OutputStream broadcast;
			try {
				broadcast = client.getSocket().getOutputStream();
				DataOutputStream out = new DataOutputStream(broadcast);
				out.writeUTF(message);
				
			} catch (IOException e) {

				e.printStackTrace();
			}
			
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
	 * Log to server frame.
	 * @param message Log message
	 */
	public void frameLog(String message){
		serverMainFrame.getLogsTextArea().append(message);
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

	public ServerMainFrame getServerMainFrame() {
		return serverMainFrame;
	}

	public void setServerMainFrame(ServerMainFrame serverMainFrame) {
		this.serverMainFrame = serverMainFrame;
	}

	public List<Client> getClients() {
		return clients;
	}

	public void setClients(List<Client> clients) {
		this.clients = clients;
	}
}
