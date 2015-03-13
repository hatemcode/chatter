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
public class ServerFrame extends JFrame implements Runnable,ActionListener {
	
	// logger
	private final Logger logger = Logger.getLogger(getClass().getName());
	
	// server object
	private Server server;
	
	// frame specifications
	private final String frameTitle = "Chatter Server";
	private final Dimension frameSize = new Dimension(500,400);
	
	// frame controllers
	private JPanel mainPanel = new JPanel();
	private JLabel serverStatusLabel = new JLabel();
	private JLabel serverNameLabel = new JLabel("Server Name:");
	private JLabel serverPortLabel = new JLabel("Server Port:");;
	private JTextField serverNameText = new JTextField();
	private JTextField serverPortText = new JTextField();
	private JButton statusToggleButton = new JButton();
	private JTextArea logsTextArea = new JTextArea();
	
	public ServerFrame(){
		constructFrame();
		constructControllers();
	}
	
	public ServerFrame(Server server){
		setServer(server);
		constructFrame();
		constructControllers();
	}
	
	@Override
	public void run() {
		refreshControllersBasedOnStatus();
		constructListners();
		showFrame();
	}
	
	/**
	 * Build the frame.
	 */
	private void constructFrame(){

		// set frame title
		setTitle(getFrameTitle());
		
		// set frame size
		setSize(getFrameSize());
		
		// make it not resizable
		setResizable(false);
		
		// make the frame on center
		setLocationRelativeTo(null);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	/**
	 * Build frame controllers.
	 */
	private void constructControllers(){
		
		// set main panel layout
		getMainPanel().setLayout(new FlowLayout());
		
		// server status
		getMainPanel().add(getServerStatusLabel());
		
		// server name
		getMainPanel().add(getServerNameLabel());
		getMainPanel().add(getServerNameText());
		
		// server port
		getMainPanel().add(getServerPortLabel());
		getMainPanel().add(getServerPortText());
		
		// server status toggle button
		getMainPanel().add(getStatusToggleButton());
		
		// server logs
		getLogsTextArea().setColumns(40);
		getLogsTextArea().setRows(20);
		getMainPanel().add(new JScrollPane(getLogsTextArea()));
		
		// add main panel to the frame
		add(getMainPanel());
	}

	/**
	 * Add listeners to the frame controllers. 
	 */
	private void constructListners(){
		
		// status toggle button
		getStatusToggleButton().addActionListener(this);
	}
	
	/**
	 * Refresh controllers based on server status.
	 */
	public void refreshControllersBasedOnStatus(){
		
		if(server.getServerStatus() == ServerStatus.STOPPED){
			
			// enable fields of server name and port
			getServerNameText().setEnabled(true);
			getServerPortText().setEnabled(true);
			
			getServerStatusLabel().setForeground(Color.RED);
			getServerStatusLabel().setText("Server is Stopped");
			getStatusToggleButton().setText("Start");
			
		}else if(server.getServerStatus() == ServerStatus.STARTED){
			
			getServerNameText().setEnabled(false);
			getServerPortText().setEnabled(false);
			
			getServerStatusLabel().setForeground(Color.GREEN);
			getServerStatusLabel().setText("Server is Started");
			getStatusToggleButton().setText("Stop");
		}
		
		getServerNameText().setText(server.getServerName());
		getServerPortText().setText(server.getServerPort().toString());

	}
	

	/**
	 * Action listener manager.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		// on click status toggle button
		if(e.getSource() == statusToggleButton){
			toggleStatus();
		}
		
	}
	
	/**
	 * Toggle status.
	 */
	public void toggleStatus(){
		
		// if server is stopped start it
		if(server.getServerStatus() == ServerStatus.STOPPED){
			startServer();	
		
		// if server is started stop it
		}else if(server.getServerStatus() == ServerStatus.STARTED){
			stopServer();

		}
		
	}

	/**
	 * Start server.
	 */
	public void startServer(){
		
		String serverName = getServerNameText().getText();
		getServer().setServerName(serverName);
		
		Integer serverPort = Integer.parseInt(getServerPortText().getText());
		getServer().setServerPort(serverPort);
		
		// if server started
		if(getServer().start()){
			logToFrame("Server ("+ getServerNameText().getText() +") is started ..");
			refreshControllersBasedOnStatus();		
		}	

	}

	/**
	 * Stop server.
	 */
	public void stopServer(){
		
		// if server stopped
		if(getServer().stop()){
			
			refreshControllersBasedOnStatus();			
		}
		
		
	}
	
	/**
	 * Log to the frame.
	 * @param message
	 */
	public void logToFrame(String message){
		getLogsTextArea().append("\n" + message);
	}
	
	/**
	 * Show frame.
	 */
	public void showFrame(){
		
		// make it visible
		setVisible(true);
	}

	/*** Getters & Setters ***/
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
