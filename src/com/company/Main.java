package com.company;

import java.io.PrintStream;

public class Main {

    public Main() {
    }

    public static void main(String[] args) {
        System.out.println("Usage:");
        System.out.println("\t--no-colors: log debug messages without colors in the console.");
        System.out.println("\t--no-pong: client does not send pong messages.");
        System.out.println("\t--port: the port on which the server should listen (default is 1337).");
        System.out.println("");
        if (args.length == 0) {
            System.out.println("Starting the client with the default configuration.");
        } else {
            System.out.println("Starting the client with:");
        }

        ServerConfiguration config = new ServerConfiguration();
        boolean shouldParsePort = false;
        String[] var3 = args;
        int var4 = args.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String arg = var3[var5];
            if (!shouldParsePort) {
                if (arg.equals("--no-colors")) {
                    config.setShowColors(false);
                } else if (arg.equals("--no-pong")) {
                    config.setSendPong(false);
                } else if (arg.equals("--port")) {
                    shouldParsePort = true;
                }
            } else {
                if (tryParseInt(arg)) {
                    int port = Integer.parseInt(arg);
                    if (port > 1024 && port < 65535) {
                        config.setServerPort(port);
                        System.out.println(" * Port has been configured");
                    } else {
                        System.out.println(" ERROR: Invalid port number (should be between 1024 and 65535)");
                    }
                } else {
                    System.out.println(" ERROR: Port is not a valid number.");
                }

                shouldParsePort = false;
            }
        }

        if (config.isShowColors()) {
            System.out.println(" * Colors in debug message enabled");
        } else {
            System.out.println(" * Colors in debug message disabled");
        }

        if (config.isSendPong()) {
            System.out.println(" * Sending of PONG messages enabled");
        } else {
            System.out.println(" * Sending of PONG messages disabled");
        }

        System.out.println("Starting the server:");
        System.out.println("-------------------------------");
        PrintStream var10000 = System.out;
        StringBuilder var10001 = (new StringBuilder()).append("\tversion:\t");
        config.getClass();
        var10000.println(var10001.append("1.3").toString());
        System.out.println("\thost:\t\t127.0.0.1");
        System.out.println("\tport:\t\t" + config.getServerPort());
        System.out.println("-------------------------------");
        (new Server(config)).run();
    }

    static boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException var2) {
            return false;
        }
    }
}
