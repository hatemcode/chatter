package com.hatemcode.chatter.server;

import java.net.Socket;
import java.util.logging.Logger;

import com.hatemcode.chatter.responder.MessageResponder;
import com.hatemcode.chatter.responder.imp.ClientMessageResponder;
import com.hatemcode.chatter.server.frame.ServerFrame;

public class ClientMessagesHandler extends Thread {

    private final Logger logger = Logger.getLogger(getClass().getName());

    private Socket socket;
    private ServerSession serverSession;
    private ServerFrame serverFrame;
    private MessageResponder messageResponder;

    public ClientMessagesHandler(ServerSession serverSession, Socket client, ServerFrame serverFrame) {
        setServerSession(serverSession);
        setSocket(client);
        setServerFrame(serverFrame);
        setMessageResponder(new ClientMessageResponder(this));
    }

    public void run() {

        getServerFrame().logToFrame("new client message handler ..");

        handleMessages();

    }

    private void handleMessages() {

        while (!getSocket().isClosed()) {

            getMessageResponder().respond();

        }
    }

    /**
     * * Getters & Setters **
     */
    public Logger getLogger() {
        return logger;
    }

    public ServerFrame getServerFrame() {
        return serverFrame;
    }

    public void setServerFrame(ServerFrame serverFrame) {
        this.serverFrame = serverFrame;
    }

    public ServerSession getServerSession() {
        return serverSession;
    }

    public void setServerSession(ServerSession serverSession) {
        this.serverSession = serverSession;
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
