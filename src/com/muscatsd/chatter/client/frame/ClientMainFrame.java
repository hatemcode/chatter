package com.muscatsd.chatter.client.frame;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class ClientMainFrame extends JFrame implements ActionListener {

	private final Logger logger = Logger.getLogger(getClass().getName());

	private final String frameTitle = "Chatter Client";
	private final Dimension frameSize = new Dimension(600,420);

	private JPanel mainPanel = new JPanel();
	private JList clientsList = new JList();
	private JTextArea publicChatTextArea = new JTextArea();
	private JTextArea messageTextArea = new JTextArea();
	private JButton sendButton = new JButton("Send");
	
	private Socket client;
	private String nickname;
	
	public ClientMainFrame(){
		constructFrame();
		constructControllers();
		constructListners();
	}
	
	public static void main(String[] args) {
		
		ClientMainFrame clientMainFrame = new ClientMainFrame();
		clientMainFrame.showFrame();
	}

	public void constructFrame(){
		setTitle(getFrameTitle());
		setSize(getFrameSize());
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public void constructControllers(){
		getMainPanel().setLayout(new FlowLayout());

		
		getClientsList().setVisibleRowCount(20);
		getClientsList().setFixedCellWidth(100);
		getMainPanel().add( new JScrollPane(getClientsList()));
	
		
		getPublicChatTextArea().setColumns(40);
		getPublicChatTextArea().setRows(20);
		getMainPanel().add(new JScrollPane(getPublicChatTextArea()));

		getMessageTextArea().setColumns(42);
		getMessageTextArea().setRows(2);
		getMainPanel().add(new JScrollPane(getMessageTextArea()));
		
		getMainPanel().add(getSendButton());
		
		add(getMainPanel());
	}
	
	public void constructListners(){

		getSendButton().addActionListener(this);
	}
	
	public void showFrame(){
		setVisible(true);
	}
	
	public void closeFrame(){
		System.exit(0);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == sendButton){
			sendMessage();
		}
		
	}
	
	public void sendMessage(){
		OutputStream broadcast;
		try {
			broadcast = getClient().getOutputStream();
			DataOutputStream out = new DataOutputStream(broadcast);
			out.writeUTF("/message/" + getNickname() + ": " + getMessageTextArea().getText());
			getMessageTextArea().setText("");
		} catch (IOException e) {

			e.printStackTrace();
		}	
	}
	
	public Dimension getFrameSize() {
		return frameSize;
	}

	public String getFrameTitle() {
		return frameTitle;
	}

	public Logger getLogger() {
		return logger;
	}

	public JTextArea getPublicChatTextArea() {
		return publicChatTextArea;
	}

	public void setPublicChatTextArea(JTextArea publicChatTextArea) {
		this.publicChatTextArea = publicChatTextArea;
	}

	public JPanel getMainPanel() {
		return mainPanel;
	}

	public void setMainPanel(JPanel mainPanel) {
		this.mainPanel = mainPanel;
	}

	public JList getClientsList() {
		return clientsList;
	}

	public void setClientsList(JList clientsList) {
		this.clientsList = clientsList;
	}

	public JTextArea getMessageTextArea() {
		return messageTextArea;
	}

	public void setMessageTextArea(JTextArea messageTextArea) {
		this.messageTextArea = messageTextArea;
	}

	public JButton getSendButton() {
		return sendButton;
	}

	public void setSendButton(JButton sendButton) {
		this.sendButton = sendButton;
	}

	public Socket getClient() {
		return client;
	}

	public void setClient(Socket client) {
		this.client = client;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}



}
