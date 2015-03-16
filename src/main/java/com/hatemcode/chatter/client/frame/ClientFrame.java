package com.hatemcode.chatter.client.frame;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
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

/**
 * Main client frame.
 * @author Hatem Al Amri
 *
 */
@SuppressWarnings("serial")
public class ClientFrame extends JFrame implements ActionListener,WindowListener,KeyListener{

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
	
	public ClientFrame(Socket socket,String nickname){
		setNickname(nickname);
		setClient(socket);
		constructFrame();
		constructControllers();
		constructListeners();
	}
	
	public ClientFrame(){
		constructFrame();
		constructControllers();
		constructListeners();
	}
	
	/**
	 * Driver of Client Frame.
	 * @param args
	 */
	public static void main(String[] args) {
		
		ClientFrame clientMainFrame = new ClientFrame();
		clientMainFrame.showFrame();
	}

	/**
	 * Construct client frame.
	 */
	public void constructFrame(){
		setTitle(getFrameTitle() + " - " + getNickname());
		setSize(getFrameSize());
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	/**
	 * Construct client frame controllers.
	 */
	public void constructControllers(){
		getMainPanel().setLayout(new FlowLayout());

		
		getClientsList().setVisibleRowCount(20);
		getClientsList().setFixedCellWidth(100);
		getMainPanel().add( new JScrollPane(getClientsList()));
	
		
		getPublicChatTextArea().setEditable(false);
		getPublicChatTextArea().setColumns(40);
		getPublicChatTextArea().setRows(20);
		getMainPanel().add(new JScrollPane(getPublicChatTextArea()));

		getMessageTextArea().setColumns(42);
		getMessageTextArea().setRows(3);
		getMainPanel().add(new JScrollPane(getMessageTextArea()));
		
		getMainPanel().add(getSendButton());
		
		add(getMainPanel());
	}
	
	/**
	 * Construct event listeners.
	 */
	public void constructListeners(){
		addWindowListener(this);
		getMessageTextArea().addKeyListener(this);
		getSendButton().addActionListener(this);
	}
	
	/**
	 * Show client frame.
	 */
	public void showFrame(){
		setVisible(true);
	}
	
	/**
	 * Close the client frame.
	 */
	public void closeFrame(){
		System.exit(0);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == sendButton){
			sendMessage();
		}
		
	}

	/**
	 * Highlight current nickname.
	 */
	public void highLightCurrentNickname(){
		for (int i = 0; i < getClientsList().getModel().getSize(); i++) {
			
			Object item = getClientsList().getModel().getElementAt(i);
			
			if(item.equals(getNickname())){
				getClientsList().setSelectedIndex(i);
				break;
			}
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
	
	public void sendMessage(String message){
		OutputStream broadcast;
		try {
			broadcast = getClient().getOutputStream();
			DataOutputStream out = new DataOutputStream(broadcast);
			out.writeUTF(message);
		} catch (IOException e) {

			e.printStackTrace();
		}	
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		sendMessage("/leave/" + getNickname());	
		
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		if(arg0.getKeyCode() == KeyEvent.VK_ENTER){
			arg0.consume();
			sendMessage();
		}
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
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
