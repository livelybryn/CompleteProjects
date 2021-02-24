package com.example.demo;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;

/**
 * TODO: A [what] that does [what]
 *
 * Created by Bryn Koldewyn U1081891 on 2020/10/29
 */
public class Room {

    private ArrayList<WebSocketSession> allSessions = new ArrayList<>(); // all sessions in a room
    private ArrayList<Message> oldMessages = new ArrayList<>();

    public Room() {
    }

    public void addSession(WebSocketSession session) {
        if (!allSessions.contains(session)) {
            allSessions.add(session);
        }
    }

    public ArrayList<Message> oldMessages() {
        return oldMessages;
    }

    public void leaveRoom(WebSocketSession session) {
        allSessions.remove(session);
    }

    public void sendMessage(String rawMessage) {
        String[] splitString = rawMessage.split(" ", 2);
        Message newMessage = new Message(splitString[0], splitString[1]);
        String message = jsonMessage(newMessage);
        System.out.println("Send message: " + message);

        allSessions.forEach((session) -> {
            try {
                // do thing here
                session.sendMessage(new TextMessage(message));
            } catch (IOException ioException) {
                // handle ioException (maybe send a websocket error, log it, or send a regular HTTP error)
            }
        });
        oldMessages.add(newMessage);
    }

    public String jsonMessage(Message message) {
        return "{ \"user\" " + ":\"" + message.user + '"' + ", \"message\" " + ":\"" +
            message.messageContent + '"' + " }";
    }

    class Message {

        String user;
        String messageContent;

        public Message(String user, String message) {
            this.user = user;
            messageContent = message;
        }
    }
}

