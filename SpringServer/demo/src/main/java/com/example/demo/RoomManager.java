package com.example.demo;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashMap;

/**
 * TODO: A [what] that does [what]
 *
 * Created by Bryn Koldewyn U1081891 on 2020/10/29
 */
public class RoomManager {

    private static HashMap<String, Room> roomMap = new HashMap<>(); // holds room name as key and room object as value.
    private static HashMap<WebSocketSession, Room> roomsByUser = new HashMap<>();

    // Possibly needs to be moved to different class (RoomManager class). ArrayList<Socket> needs to be Room, String is room name

    public static synchronized Room getRoom(String roomName) { // RoomManager class
        return roomMap.computeIfAbsent(roomName, (ignored) -> new Room());
    }

    public static Room whichRoom(WebSocketSession session) {
        return roomsByUser.get(session);
    }

    public static synchronized Room joinRoom(String roomName, WebSocketSession session) {
        // returns the room from the roomName or a new room if the roomName is not found
        Room room = getRoom(roomName);

        // adds the session to the room session list
        room.addSession(session);

        // adds the key session and the value room to the hashmap roomsByUser
        roomsByUser.put(session, room);

        // sends a text message to all sessions in a room
        room.oldMessages().forEach((message) -> {
            try {
                String jsonMessage = room.jsonMessage(message);
                session.sendMessage(new TextMessage(jsonMessage));
            } catch (IOException ioException) {
                // maybe say "unable to send messages"
            }
        });
        return room;
    }


}
