package com.example.demo;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * TODO: A [what] that does [what]
 *
 * Created by Bryn Koldewyn U1081891 on 2020/10/29
 */
public class WSHandler extends TextWebSocketHandler {

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        // Checks to see if the session is already in a room
        Room room = RoomManager.whichRoom(session);
        String command = message.getPayload().split(" ")[1];
        // if the session is not in a room, join room, else send message to all sessions in that room
        if (command.equals("join")) {
            if (room != null) {
                room.leaveRoom(session);
            }
        }
        if (room == null) {
            RoomManager.joinRoom(command, session);
        } else
            room.sendMessage(message.getPayload());
    }
}



