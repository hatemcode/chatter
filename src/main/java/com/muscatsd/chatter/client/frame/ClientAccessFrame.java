package com.muscatsd.chatter.client.frame;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.muscatsd.chatter.client.ServerMessagesHandler;

@SuppressWarnings("serial")
public class ClientAccessFrame extends JFrame implements ActionListener {

	private final Logger logger = Logger.getLogger(getClass().getName());

	private final String frameTitle = "Chatter Client";
	private final Dimension frameSize = new Dimension(350,130);

	private JPanel mainPanel = new JPanel();
	private JLabel nicknameLabel = new JLabel("Nickname:");
	private JLabel serverHostLabel = new JLabel("Host:");
	private JLabel serverPortLabel = new JLabel("Port:");
	private JTextField nicknameText = new JTextField();
	private JTextField serverHostText = new JTextField();
	private JTextField serverPortText = new JTextField();
	private JButton enterChatButton = new JButton("Enter Chat");
	
	private String nickname;
	private String serverHost;
	private Integer serverPort;
	
	private Socket socket;
	

	public ClientAccessFrame(){
		setNickname("Client");
		setServerHost("127.0.0.1");
		setServerPort(5555);
		
		constructFrame();
		constructControllers();
		constructListners();
	}
	


	public static void main(String[] args) {
		
		ClientAccessFrame clientAccessFrame = new ClientAccessFrame();
		clientAccessFrame.showFrame();
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
		
		getMainPanel().add(getNicknameLabel());
		getNicknameText().setText(getNickname());
		getMainPanel().add(getNicknameText());
		
		getMainPanel().add(getServerHostLabel());
		getServerHostText().setText(getServerHost());
		getMainPanel().add(getServerHostText());
		
		getMainPanel().add(getServerPortLabel());
		getServerPortText().setText(getServerPort().toString());
		getMainPanel().add(getServerPortText());
		
		getMainPanel().add(getEnterChatButton());
		
		add(getMainPanel());
	}

	public void constructListners(){

		getEnterChatButton().addActionListener(this);
	}

	
	public void showFrame(){
		getNicknameText().setFocusable(true);
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == enterChatButton){
			enterChat();
		}

	}
	
	public void enterChat(){
		
		setServerHost(getServerHostText().getText());
		setServerPort(Integer.parseInt(getServerPortText().getText()));
		setNickname(getNicknameText().getText());
		
		 Runnable run = new Runnable() {
			    public void run() {
					try {
						
						setSocket(new Socket(getServerHost(),getServerPort()));
						
						OutputStream message = getSocket().getOutputStream();
						DataOutputStream out = new DataOutputStream(message);
						
						out.writeUTF("/user/" + getNicknameText().getText());
						
						InputStream response = getSocket().getInputStream();
						DataInputStream in = new DataInputStream(response);

						String command = in.readUTF();
						System.out.print(command);
						if(command.startsWith("/user rejected/")){
							JOptionPane.showMessageDialog(null, "Nickname is exist", "Error",JOptionPane.ERROR_MESSAGE);
						}else if(command.startsWith("/user accepted/")){
							
							ClientFrame clientFrame = new ClientFrame(getSocket(),getNicknameText().getText());
							clientFrame.showFrame();
							ServerMessagesHandler serverMessagesHandler = new ServerMessagesHandler();
							serverMessagesHandler.setClient(getSocket());
							serverMessagesHandler.setClientFrame(clientFrame);
							serverMessagesHandler.start();
							dispose();
							
						}
						
					} catch (IOException e) {
						JOptionPane.showMessageDialog(null, "Make sure about host and port." + e.getMessage(), "Error",JOptionPane.ERROR_MESSAGE);
						

					}
			    }
			 };
			 
			 new Thread(run).start();

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


	public JPanel getMainPanel() {
		return mainPanel;
	}

	public void setMainPanel(JPanel mainPanel) {
		this.mainPanel = mainPanel;
	}

	public JButton getEnterChatButton() {
		return enterChatButton;
	}

	public void setEnterChatButton(JButton enterChatButton) {
		this.enterChatButton = enterChatButton;
	}

	public JLabel getNicknameLabel() {
		return nicknameLabel;
	}

	public void setNicknameLabel(JLabel nicknameLabel) {
		this.nicknameLabel = nicknameLabel;
	}

	public JLabel getServerPortLabel() {
		return serverPortLabel;
	}

	public void setServerPortLabel(JLabel serverPortLabel) {
		this.serverPortLabel = serverPortLabel;
	}

	public JLabel getServerHostLabel() {
		return serverHostLabel;
	}

	public void setServerHostLabel(JLabel serverHostLabel) {
		this.serverHostLabel = serverHostLabel;
	}


	public JTextField getNicknameText() {
		return nicknameText;
	}

	public void setNicknameText(JTextField nicknameText) {
		this.nicknameText = nicknameText;
	}

	public JTextField getServerHostText() {
		return serverHostText;
	}

	public void setServerHostText(JTextField serverHostText) {
		this.serverHostText = serverHostText;
	}

	public JTextField getServerPortText() {
		return serverPortText;
	}

	public void setServerPortText(JTextField serverPortText) {
		this.serverPortText = serverPortText;
	}
	
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getServerHost() {
		return serverHost;
	}

	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}

	public Integer getServerPort() {
		return serverPort;
	}

	public void setServerPort(Integer serverPort) {
		this.serverPort = serverPort;
	}



	public Socket getSocket() {
		return socket;
	}



	public void setSocket(Socket socket) {
		this.socket = socket;
	}





}
