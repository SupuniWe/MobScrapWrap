package com.db.cdap.scrapwrap.chatbot;

public class ChatMessage {

    private String msgText;
    private String msgUser;

    public ChatMessage(){}

    public ChatMessage(String msgText, String msgUser)
    {
        this.setMsgText(msgText);
        this.setMsgUser(msgUser);
    }


    public String getMsgText() {
        return msgText;
    }

    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }

    public String getMsgUser() {
        return msgUser;
    }

    public void setMsgUser(String msgUser) {
        this.msgUser = msgUser;
    }
}
