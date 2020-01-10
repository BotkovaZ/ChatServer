package com.company;

import com.company.Messages.Message;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.net.Socket;
import java.net.ServerSocket;

public class Server {

    private ServerSocket serverSocket;
    private Set<ClientThread> threads;
    private ServerConfiguration conf;
    private final int PING_TIMEOUT = 3000;
    private static ArrayList<User> users = new ArrayList<>();
    private static HashMap<User, ObjectOutputStream> oos = new HashMap<>();

    public Server(ServerConfiguration conf) {
        this.conf = conf;
    }

    public void run() {
        try {
            this.serverSocket = new ServerSocket(this.conf.getServerPort());
            this.threads = new HashSet();

            while(true) {
                Server.ClientThread ct;
                do {
                    Socket socket = this.serverSocket.accept();
                    ct = new Server.ClientThread(socket);
                    this.threads.add(ct);
                    (new Thread(ct)).start();
                    System.out.println("Num clients: " + this.threads.size());
                } while(!this.conf.isSendPong());

                Server.PingClientThread dct = new Server.PingClientThread(ct);
                (new Thread(dct)).start();
            }
        } catch (IOException var4) {
            var4.printStackTrace();
        }
    }

    private class ClientThread implements Runnable {
        private DataInputStream is;
        private OutputStream os;
        private Socket socket;
        private ServerState state;
        private String username;
        private boolean pongReceived = false;

        public ClientThread(Socket socket) {
            this.state = ServerState.INIT;
            this.socket = socket;
        }

        public String getUsername() {
            return this.username;
        }

        public void run() {
            try {
                this.os = this.socket.getOutputStream();
                this.is = new DataInputStream(this.socket.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(this.is));
                this.state = ServerState.CONNECTING;
                StringBuilder var10000 = (new StringBuilder()).append("HELO ");
                Server.this.conf.getClass();
                String welcomeMessage = var10000.append("Welkom to WhatsUpp!").toString();
                this.writeToClient(welcomeMessage);

                while(true) {
                    while(true) {
                        String line;
                        do {
                            if (this.state.equals(ServerState.FINISHED)) {
                                Server.this.threads.remove(this);
                                this.socket.close();
                                return;
                            }

                            line = reader.readLine();
                        } while(line == null);

                        boolean isIncomingMessage = true;
                        this.logMessage(isIncomingMessage, line);
                        Message message = new Message(line);
                        switch(message.getMessageType()) {
                            case HELO:
                                boolean isValidUsername = message.getPayload().matches("[a-zA-Z0-9_]{3,14}");
                                if (!isValidUsername) {
                                    this.state = ServerState.FINISHED;
                                    this.writeToClient("-ERR username has an invalid format (only characters, numbers and underscores are allowed)");
                                } else {
                                    boolean userExists = false;
                                    Iterator var12 = Server.this.threads.iterator();

                                    while(var12.hasNext()) {
                                        Server.ClientThread ctx = (Server.ClientThread)var12.next();
                                        if (ctx != this && message.getPayload().equals(ctx.getUsername())) {
                                            userExists = true;
                                            break;
                                        }
                                    }

                                    if (userExists) {
                                        this.writeToClient("-ERR user already logged in");
                                    } else {
                                        this.state = ServerState.CONNECTED;
                                        this.username = message.getPayload();
                                        this.writeToClient("+OK " + message.getLine());
                                    }
                                }
                                break;
                            case BCST:
                                Iterator var7 = Server.this.threads.iterator();

                                while(var7.hasNext()) {
                                    Server.ClientThread ct = (Server.ClientThread)var7.next();
                                    if (ct != this) {
                                        ct.writeToClient("BCST [" + this.getUsername() + "] " + message.getPayload());
                                    }
                                }

                                this.writeToClient("+OK " + message.getLine());
                                break;
                            case QUIT:
                                this.state = ServerState.FINISHED;
                                this.writeToClient("+OK Goodbye");
                                break;
                            case PONG:
                                this.pongReceived = true;
                                break;
                            case UNKOWN:
                                this.writeToClient("-ERR Unkown command");
                        }
                    }
                }
            } catch (IOException var10) {
                System.out.println("Server Exception: " + var10.getMessage());
            }
        }

        public void kill() {
            try {
                System.out.println("[DROP CONNECTION] " + this.getUsername());
                Server.this.threads.remove(this);
                this.socket.close();
            } catch (Exception var2) {
                System.out.println("Exception when closing outputstream: " + var2.getMessage());
            }

            this.state = ServerState.FINISHED;
        }

        private void writeToClient(String message) {
            PrintWriter writer = new PrintWriter(this.os);
            writer.println(message);
            writer.flush();
            boolean isIncomingMessage = false;
            this.logMessage(isIncomingMessage, message);
        }

        private void logMessage(boolean isIncoming, String message) {
            Server.this.conf.getClass();
            String colorCode = "\u001b[32m";
            String directionString = "<< ";
            if (isIncoming) {
                Server.this.conf.getClass();
                colorCode = "\u001b[31m";
                directionString = ">> ";
            }

            String logMessage;
            if (this.getUsername() == null) {
                logMessage = directionString + message;
            } else {
                logMessage = directionString + "[" + this.getUsername() + "] " + message;
            }

            if (Server.this.conf.isShowColors()) {
                PrintStream var10000 = System.out;
                StringBuilder var10001 = (new StringBuilder()).append(colorCode).append(logMessage);
                Server.this.conf.getClass();
                var10000.println(var10001.append("\u001b[0m").toString());
            } else {
                System.out.println(logMessage);
            }

        }
    }

    private class PingClientThread implements Runnable {
        Server.ClientThread ct;
        private boolean shouldPing = true;

        PingClientThread(Server.ClientThread ct) {
            this.ct = ct;
        }

        public void run() {
            while(this.shouldPing) {
                try {
                    int sleep = (10 + (new Random()).nextInt(10)) * 1000;
                    Thread.sleep((long)sleep);
                    this.ct.pongReceived = false;
                    this.ct.writeToClient("PING");
                    Thread.sleep(3000L);
                    if (!this.ct.pongReceived) {
                        this.shouldPing = false;
                        this.ct.writeToClient("DSCN Pong timeout");
                        this.ct.kill();
                    }
                } catch (InterruptedException var2) {
                    var2.printStackTrace();
                }
            }

        }
    }
}















