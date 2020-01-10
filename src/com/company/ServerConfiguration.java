package com.company;

public class ServerConfiguration {

    public final String WELCOME_MESSAGE = "Welkom to WhatsUpp!";
    public final String VERSION = "1.3";
    public final int DEFAULT_SERVER_PORT = 1337;
    public final String RESET_CLI_COLORS = "\u001b[0m";
    public final String CLI_COLOR_INCOMING = "\u001b[31m";
    public final String CLI_COLOR_OUTGOING = "\u001b[32m";
    private int serverPort = 1337;
    private boolean showColors = true;
    private boolean sendPong = true;

    public ServerConfiguration() {
    }

    public int getServerPort() {
        return this.serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public boolean isShowColors() {
        return this.showColors;
    }

    public void setShowColors(boolean showColors) {
        this.showColors = showColors;
    }

    public boolean isSendPong() {
        return this.sendPong;
    }

    public void setSendPong(boolean sendPong) {
        this.sendPong = sendPong;
    }
}
