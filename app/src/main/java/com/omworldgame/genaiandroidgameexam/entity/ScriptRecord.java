package com.omworldgame.genaiandroidgameexam.entity;

public class ScriptRecord {
    String character;
    String background;
    String speaker;
    String talk;

    public ScriptRecord() {
        this.character = "";
        this.background = "";
        this.speaker = "";
        this.talk = "";
    }

    public ScriptRecord(String character, String background, String speaker, String talk) {
        this.character = character;
        this.background = background;
        this.speaker = speaker;
        this.talk = talk;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getCharacter() {
        return character;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getBackground() {
        return background;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setTalk(String talk) {
        this.talk = talk;
    }

    public String getTalk() {
        return talk;
    }
}
