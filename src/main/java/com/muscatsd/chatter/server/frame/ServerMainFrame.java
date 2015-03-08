package com.muscatsd.chatter.server.frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.muscatsd.chatter.server.Server;
import com.muscatsd.chatter.server.enumeration.ServerStatus;

@SuppressWarnings("serial")
public class ServerMainFrame extends JFrame implements Runnable,ActionListener {
	
	private final Logger logger = Logger.getLogger(getClass().getName());
	
	private Server server;
	
	private final String frameTitle = "Chatter Server";
	private final Dimension frameSize = new Dimension(500,400);
	
	private JPanel mainPanel = new JPanel();
	private JLabel serverStatusLabel = new JLabel();
	private JLabel serverNameLabel = new JLabel("Server Name:");
	private JLabel serverPortLabel = new JLabel("Server Port:");;
	private JTextField serverNameText = new JTextField();
	private JTextField serverPortText = new JTextField();
	private JButton statusToggleButton = new JButton();
	private JTextArea logsTextArea = new JTextArea();
	
	public ServerMainFrame(){
		constructFrame();
		constructControllers();
	}
	
	public ServerMainFrame(Server server){
		setServer(server);
		constructFrame();
		constructControllers();
	}
	
	@Override
	public void run() {
		refreshFillControls();
		constructListners();
		showFrame();
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
		
		getMainPanel().add(getServerStatusLabel());
		
		getMainPanel().add(getServerNameLabel());
		getMainPanel().add(getServerNameText());
		
		getMainPanel().add(getServerPortLabel());
		getMainPanel().add(getServerPortText());
		
		getMainPanel().add(getStatusToggleButton());
		
		getLogsTextArea().setColumns(40);
		getLogsTextArea().setRows(20);
		getMainPanel().add(new JScrollPane(getLogsTextArea()));
		
		add(getMainPanel());
	}
	
	public void refreshFillControls(){
		
		if(server.getServerStatus() == ServerStatus.STOPPED){
			
			getServerStatusLabel().setForeground(Color.RED);
			getServerStatusLabel().setText("Server is Stopped");
			getStatusToggleButton().setText("Start");
			
		}else if(server.getServerStatus() == ServerStatus.STARTED){
			
			getServerStatusLabel().setForeground(Color.GREEN);
			getServerStatusLabel().setText("Server is Started");
			getStatusToggleButton().setText("Stop");
		}
		
		getServerNameText().setText(server.getServerName());
		getServerPortText().setText(server.getServerPort().toString());

	}
	
	public void constructListners(){

		getStatusToggleButton().addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == statusToggleButton){
			StatusToggleButtonToggled();
		}
		
	}
	
	public void StatusToggleButtonToggled(){
		
		if(server.getServerStatus() == ServerStatus.STOPPED){
			startServer();	
			
		}else if(server.getServerStatus() == ServerStatus.STARTED){
			stopServer();

		}
		
	}
	
	public void startServer(){

		
		getServer().setServerName(getServerNameText().getText());
		getServer().setServerPort(Integer.parseInt(getServerPortText().getText()));
		
		if(getServer().start()){
			getStatusToggleButton().setText("Stop");
			
			getServerNameText().setEnabled(false);
			getServerPortText().setEnabled(false);
			
			
					
		}
		refreshFillControls();	

	}
	
	public void stopServer(){

		
		if(getServer().stop()){
	
			getStatusToggleButton().setText("Start");
			
			getServerNameText().setEnabled(true);
			getServerPortText().setEnabled(true);
			
			getLogsTextArea().append("\n Server stopped ..");
			
		}
		refreshFillControls();
	}
	
	public void showFrame(){
		setVisible(true);
	}

	public String getFrameTitle() {
		return frameTitle;
	}

	public Dimension getFrameSize() {
		return frameSize;
	}

	public JPanel getMainPanel() {
		return mainPanel;
	}

	public void setMainPanel(JPanel mainPanel) {
		this.mainPanel = mainPanel;
	}

	public JLabel getServerStatusLabel() {
		return serverStatusLabel;
	}

	public void setServerStatusLabel(JLabel serverStatusLabel) {
		this.serverStatusLabel = serverStatusLabel;
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public Logger getLogger() {
		return logger;
	}

	public JLabel getServerNameLabel() {
		return serverNameLabel;
	}

	public void setServerNameLabel(JLabel serverNameLabel) {
		this.serverNameLabel = serverNameLabel;
	}

	public JLabel getServerPortLabel() {
		return serverPortLabel;
	}

	public void setServerPortLabel(JLabel serverPortLabel) {
		this.serverPortLabel = serverPortLabel;
	}

	public JTextField getServerNameText() {
		return serverNameText;
	}

	public void setServerNameText(JTextField serverNameText) {
		this.serverNameText = serverNameText;
	}

	public JTextField getServerPortText() {
		return serverPortText;
	}

	public void setServerPortText(JTextField serverPortText) {
		this.serverPortText = serverPortText;
	}

	public JButton getStatusToggleButton() {
		return statusToggleButton;
	}

	public void setStatusToggleButton(JButton statusToggleButton) {
		this.statusToggleButton = statusToggleButton;
	}

	public JTextArea getLogsTextArea() {
		return logsTextArea;
	}

	public void setLogsTextArea(JTextArea logsTextArea) {
		this.logsTextArea = logsTextArea;
	}





}
