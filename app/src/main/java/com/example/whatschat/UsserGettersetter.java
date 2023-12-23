package com.example.whatschat;

public class UsserGettersetter {

    String name;
    String userId;
    String email;
    String password;
    String rePassword;
    String imageuri;
    String status;
    String messages;

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public String getImageuri() {
        return imageuri;
    }

    public void setImageuri(String imageuri) {
        this.imageuri = imageuri;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public UsserGettersetter(String name, String email, String password, String userId, String imageuri, String status) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.userId=userId;
        this.imageuri = imageuri;
        this.status = status;

    }

    public UsserGettersetter() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRePassword() {
        return rePassword;
    }

    public void setRePassword(String rePassword) {
        this.rePassword = rePassword;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
