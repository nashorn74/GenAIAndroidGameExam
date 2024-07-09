package com.omworldgame.genaiandroidgameexam.common;

import java.util.Arrays;

public class GameEngine {
    private String name;
    private int curPlace;
    private int curMonth;
    private int curDay;
    private int[] charLove;
    private int[] variable;
    private boolean[][] event;

    public GameEngine() {
        this.name = "강철민";
        this.curPlace = 3;
        this.curMonth = 7;
        this.curDay = 1;

        this.charLove = new int[Constant.LOVE_COUNT];
        this.variable = new int[Constant.VARIABLE_COUNT];
        this.event = new boolean[Constant.CHARACTER_COUNT][Constant.EVENT_COUNT];

        Arrays.fill(this.charLove, 0);
        Arrays.fill(this.variable, 0);
        for (int i = 0; i < Constant.CHARACTER_COUNT; i++) {
            Arrays.fill(this.event[i], false);
        }
    }

    public void setEvent(int num, int event, boolean flag)
    {
        this.event[num][event] = flag;
    }

    public boolean getEvent(int num, int event)
    {
        return this.event[num][event];
    }

    public void setVariable(int num, int data)
    {
        variable[num] = data;
    }

    public int getVariable(int num)
    {
        return variable[num];
    }

    public void setCharLove(int num, int love) {
        this.charLove[num] = Math.max(0, Math.min(100, love));
    }

    public int getCharLove(int num)
    {
        return charLove[num];
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public void setCurPlace(int place)
    {
        curPlace = place;
    }

    public int getCurPlace()
    {
        return curPlace;
    }

    public void setCurMonth(int month)
    {
        curMonth = month;
    }

    public int getCurMonth()
    {
        return curMonth;
    }

    public void setCurDay(int day)
    {
        curDay = day;
    }

    public int getCurDay()
    {
        return curDay;
    }
}
