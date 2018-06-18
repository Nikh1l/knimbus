package com.knimbus.elib;

public class ChatMessage {
    private String text, email, name;
    private long timestamp;
    private int typeId;

    public ChatMessage(String text, String email, long timestamp, int typeId, String name){
        this.text = text;
        this.email = email;
        this.timestamp = timestamp;
        this.typeId = typeId;
        this.name = name;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }
}
