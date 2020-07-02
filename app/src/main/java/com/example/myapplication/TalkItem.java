package com.example.myapplication;

public class TalkItem {
    private boolean bot; // 챗봇이 말할 때
    private String talk;
    private String[][] items =null;

    public TalkItem(Boolean bot, String talk, String[][] items){
        this.bot = bot;
        this.talk = talk;
        this.items=items;
    }
    public TalkItem(Boolean bot, String talk){
        this.bot = bot;
        this.talk = talk;
    }
    public String[][] getItems() {
        return items;
    }
    public void setItems(String[][] items) {
        this.items = items;
    }
    public boolean isBot() {
        return bot;
    }
    public void setBot(boolean bot) {
        this.bot = bot;
    }
    public String getTalk() {
        return talk;
    }
    public void setTalk(String talk) {
        this.talk = talk;
    }
}
