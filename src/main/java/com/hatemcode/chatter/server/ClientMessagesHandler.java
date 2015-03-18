package com.hatemcode.chatter.server;

import java.net.Socket;
import java.util.logging.Logger;

import com.hatemcode.chatter.responder.MessageResponder;
import com.hatemcode.chatter.responder.imp.ClientMessageResponder;
import com.hatemcode.chatter.server.frame.ServerFrame;

public class ClientMessagesHandler extends Thread {

    private static final Logger LOG = Logger.getLogger(ClientMessagesHandler.class.getName());

    private Socket socket;
    private ServerSession serverSession;
    private ServerFrame serverFrame;
    private MessageResponder messageResponder;

    public ClientMessagesHandler(ServerSession serverSession, Socket client, ServerFrame serverFrame) {
        this.serverSession = serverSession;
        this.socket = client;
        this.serverFrame = serverFrame;
        this.messageResponder = new ClientMessageResponder(this);
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
