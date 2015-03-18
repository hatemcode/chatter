package com.hatemcode.chatter.server;

import java.io.IOException;
import java.net.ServerSocket;

import com.hatemcode.chatter.server.frame.ServerFrame;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represent the server application.
 *
 * @author Hatem Al Amri
 */
public class Server {

    private static final Logger LOG = Logger.getLogger(Server.class.getName());

    private final String DEFAULT_SERVER_NAME = "Chatter";
    private final Integer DEFAULT_SERVER_PORT = 5555;

    private String serverName;
    private Integer serverPort;
    private ServerStatus serverStatus = ServerStatus.STOPPED;
    private ServerSocket socket;
    private ServerSession serverSession;

    private ServerFrame serverFrame;

    public Server() {
        this.serverName = DEFAULT_SERVER_NAME;
        this.serverPort = DEFAULT_SERVER_PORT;
        this.serverFrame = new ServerFrame(this);
    }

    public Server(String serverName, Integer serverPort) {

        this.serverName = serverName;
        this.serverPort = serverPort;
        this.serverFrame = new ServerFrame(this);
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.getServerFrame().run();

    }

    /**
     * Start server.
     *
     * @return Boolean true if success.
     */
    public Boolean start() {
        try {
            // prepare server socket and establish it
            ServerSocket serverSocket = new ServerSocket(getServerPort());
            setSocket(serverSocket);

            // prepare server session and start it
            setServerSession(serverSession);
            getServerSession().setServerSocket(getSocket());
            getServerSession().setServerFrame(getServerFrame());
            getServerSession().start();

            // change server status to started
            setServerStatus(ServerStatus.STARTED);

            // success start of server
            return true;

        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Port {} is used by another application.", getServerPort());

        } catch (IllegalArgumentException e) {
            LOG.log(Level.SEVERE, "Port {} is not out of the range.", getServerPort());
        }
        // failed start of server
        return false;

    }

    /**
     * Stop server.
     *
     * @return Boolean if stop success
     */
    public Boolean stop() {
        if (!getSocket().isClosed()) {
            try {

                // stop session and socket
                getServerSession().stopSession();
                getSocket().close();

                // change server status to stopped
                setServerStatus(ServerStatus.STOPPED);

                getServerFrame().logToFrame("Server (" + getServerName() + ") is stopped ..\n");

                // success server stop
                return true;

            } catch (IOException e) {
                LOG.log(Level.SEVERE, e.getMessage());
            }
        }

        // fail server stop
        return false;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public Integer getServerPort() {
        return serverPort;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    public ServerFrame getServerFrame() {
        return serverFrame;
    }

    public void setServerFrame(ServerFrame serverFrame) {
        this.serverFrame = serverFrame;
    }

    public ServerStatus getServerStatus() {
        return serverStatus;
    }

    public void setServerStatus(ServerStatus serverStatus) {
        this.serverStatus = serverStatus;
    }

    public ServerSocket getSocket() {
        return socket;
    }

    public void setSocket(ServerSocket socket) {
        this.socket = socket;
    }

    public String getDEFAULT_SERVER_NAME() {
        return DEFAULT_SERVER_NAME;
    }

    public Integer getDEFAULT_SERVER_PORT() {
        return DEFAULT_SERVER_PORT;
    }

    public ServerSession getServerSession() {
        return serverSession;
    }

    public void setServerSession(ServerSession serverSession) {
        this.serverSession = serverSession;
    }

}
