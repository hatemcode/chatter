package com.hatemcode.chatter.client;

import java.net.Socket;
import java.util.logging.Logger;

import com.hatemcode.chatter.client.frame.ClientFrame;
import com.hatemcode.chatter.responder.MessageResponder;
import com.hatemcode.chatter.responder.imp.ServerMessageResponder;

/**
 * Handler thread for messages coming from the server to specific client.
 *
 * @author Hatem Al Amri
 */
public class ServerMessagesHandler extends Thread {

    private final Logger logger = Logger.getLogger(getClass().getName());

    private Socket socket;
    private ClientFrame clientFrame;
    private MessageResponder messageResponder;

    public ServerMessagesHandler() {
        setMessageResponder(new ServerMessageResponder(this));

    }

    public void run() {
        handleMessages();

    }

    /**
     * Handle messages/responses coming from server.
     */
    public void handleMessages() {

        // keep search about incoming messages
        while (!getSocket().isClosed()) {

            // search about messages if socket is alive
            if (!getSocket().isClosed()) {
                getMessageResponder().respond();
            } else {

                // is socket is closed don't search about incoming messages anymore for this client
                break;
            }
        }
    }

    /**
     * When public announcement is received.
     *
     * @param message
     */
    public void publicAnnouncementReceived(String message) {

        getClientFrame().getPublicChatTextArea().append("\n" + message);

    }

    public Logger getLogger() {
        return logger;
    }

    public ClientFrame getClientFrame() {
        return clientFrame;
    }

    public void setClientFrame(ClientFrame clientFrame) {
        this.clientFrame = clientFrame;
    }

    public MessageResponder getMessageResponder() {
        return messageResponder;
    }

    public void setMessageResponder(MessageResponder messageResponder) {
        this.messageResponder = messageResponder;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

}
