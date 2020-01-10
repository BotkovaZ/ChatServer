package com.company.Messages;

import com.company.User;

public class Message {

    private String line;
    private String msg = null;
    private User user = null;
    private MessageType msgType = null;

    public Message() {

    }

    public Message(String msg, User user) {
        this.msg = msg;
        this.user = user;
    }

    public Message(String line) {
        this.line = line;
    }

    public String getLine() {
        return this.line;
    }

    public Message.MessageType getMessageType() {
        Message.MessageType result = Message.MessageType.UNKOWN;

        try {
            if (this.line != null && this.line.length() > 0) {
                String[] splits = this.line.split("\\s+");
                result = Message.MessageType.valueOf(splits[0]);
            }
        } catch (IllegalArgumentException var3) {
            System.out.println("[ERROR] Unknown command");
        }

        return result;
    }

    public String getPayload() {
        if (this.getMessageType().equals(Message.MessageType.UNKOWN)) {
            return this.line;
        } else {
            return this.line != null && this.line.length() >= this.getMessageType().name().length() + 1 ? this.line.substring(this.getMessageType().name().length() + 1) : "";
        }
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MessageType getMsgType() {
        return msgType;
    }

    public void setMsgType(MessageType msgType) {
        this.msgType = msgType;
    }

    public User getRecipient() {
        return user;
    }

    public static enum MessageType {
        HELO,
        BCST,
        QUIT,
        PONG,
        UNKOWN;

        private MessageType() {
        }
    }
}
