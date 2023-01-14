package com.example.springboot.message;

public class PingMessage extends Message {
    @Override
    public int getMessageType() {
        return PingMessage;
    }
}
