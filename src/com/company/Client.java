package com.company;

import com.company.Messages.Message;

import javax.imageio.stream.FileCacheImageOutputStream;
import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Client implements Runnable {

    private ArrayList<User> users = new ArrayList<>();
    private HashMap<User, ObjectOutputStream> oos = new HashMap<>();
    private Message message = new Message();
    private List<Group> groups = new ArrayList<>();

    public void run() {
        try (Socket socket = new Socket("localhost", 1337)) {

            socket.setSoTimeout(5000);
            BufferedReader echoes = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter stringToEcho = new PrintWriter(socket.getOutputStream(), true);

            Scanner scanner = new Scanner(System.in);
            String echoString;
            String response;

            do {
                System.out.println("Enter string to be echoed: ");
                echoString = scanner.nextLine();

                stringToEcho.println(echoString);
                if (!echoString.equals("exit")) {
                    response = echoes.readLine();
                    System.out.println(response);
                }
            } while (!echoString.equals("exit"));
        } catch (SocketTimeoutException e) {
            System.out.println("Client Error " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Client error: " + e.getMessage());
        }
    }

//    private void askListUsers() {
//
//    }

    private List<User> createGroup() {
        List<User> group = new ArrayList<>();

        return group;
    }

//    private List<Group> getGroups() {
//        List<Group> groups = new ArrayList<>();
//
//    }

    private void addUserToGroup(User userToAdd, List<User> group) throws Exception {
        for (User user: group) {
            if (userToAdd.getUserName().equals(user.getUserName())) {
                throw new Exception("User already exists in group");
            } else {
                group.add(user);
                System.out.println("User added to group.");
            }
        }
    }

//    private void sendMessageTo(User user, Message msg) throws IOException {
//        boolean found = false;
//
//        for (user: oos.keySet()) {
//            if (user.getUserName().equals(message.getRecipient())) {
//                Message newMsg = new Message("");
//                newMsg.setUser(new User("server"));
//                newMsg.setMsg("Message");
////                oos.get(user).writeObect(newMsg);
//                oos.get(user).writeObject(message);
//                found = true;
//                break;
//            }
//        }
//        if (!found) {
//            message.setMsg("Recipient not online");
//        }
//    }

    private void joinGroup(Group group, User user) {
        for (Group g: groups) {
            if (!g.equals(group)) {
                throw new IndexOutOfBoundsException("Group does not exist");
            } else {
                group.addUser(user.getUserName());
            }
        }
    }

    public void leaveGroup(Group group, User user) {
        for (Group g: groups) {
            if (!g.equals(group)) {
                throw new IndexOutOfBoundsException("Group does not exist");
            } else {

            }
        }
    }
}
