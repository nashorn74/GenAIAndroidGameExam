package com.omworldgame.genaiandroidgameexam.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.omworldgame.genaiandroidgameexam.entity.ScriptRecord;

import java.util.ArrayList;

public class Script {
    Context context;

    public static final int OP_EQ 	= 1;
    public static final int OP_MORE 	= 2;
    public static final int OP_LESS 	= 3;

    String scriptFilename = "";
    String scriptIndex = "";

    private boolean wait = false;
    private boolean select = false;
    private boolean skip = false;
    private boolean event = false;

    ArrayList<ScriptRecord> scriptList = new ArrayList<ScriptRecord>();

    public Script(Context context) {
        this.context = context;
    }

    public String getEventData(int num) {
        return Constant.EventData.getEventData(num);
    }

    public void setScriptFilename(String scriptFilename) {
        this.scriptFilename = scriptFilename;
    }

    public void setScriptIndex(String scriptIndex) {
        this.scriptIndex = scriptIndex;
    }

    public void setEvent(boolean event) {
        this.event = event;
    }

    public boolean isEvent() {
        return event;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    public boolean isSkip() {
        return skip;
    }

    public void setWait(boolean wait) {
        this.wait = wait;
    }

    public boolean isWait() {
        return wait;
    }

    boolean findSelectString(String string) {

        return true;
    }

    boolean findRandomString(String string) {

        return true;
    }

    void CheckCurrentTimeScript() {

    }

    public ArrayList<ScriptRecord> gotoScript(String indexString) {
        event = false;

        Log.i("gotoScript", indexString);
        scriptIndex = indexString;
        return performNextScript();
    }

    void increaseCurrentTime(int time) {

    }

    boolean findResult(int ifCount, boolean result) {
        return false;
    }

    @SuppressLint("Recycle")
    public static ArrayList<String> getIndexList(Context context, String filename) {
        ArrayList<String> indexList = new ArrayList<String>();
        try {
            Cursor cursor;
            String outFileName = context.getFilesDir().getAbsolutePath()+"/"+Constant.DB_NAME;
            SQLiteDatabase db = SQLiteDatabase.openDatabase(outFileName, null, SQLiteDatabase.OPEN_READONLY);

            String[] FROM = {	"*"  	};
            cursor = db.query(filename, FROM, null, null, null, null, null);

            while(cursor.moveToNext()) {
                String character = cursor.getString(0);

                if (character != null && !character.isEmpty()) {
                    if (character.charAt(0) == '[') {
                        indexList.add(character);
                    }
                }
            }

            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return indexList;
    }

    @SuppressLint("Recycle")
    public ArrayList<ScriptRecord> performNextScript() {

        try {
            Cursor cursor;
            String outFileName = context.getFilesDir().getAbsolutePath()+"/"+Constant.DB_NAME;
            SQLiteDatabase db = SQLiteDatabase.openDatabase(outFileName, null, SQLiteDatabase.OPEN_READONLY);

            String[] FROM = {	"*"  	};
            cursor = db.query(scriptFilename, FROM, null, null, null, null, null);

            scriptList.clear();

            boolean startScript = false;
            while(cursor.moveToNext()) {
                String character = cursor.getString(0);
                String background = cursor.getString(1);
                String speaker = cursor.getString(2);
                String talk = cursor.getString(3);

                if (!startScript) {
                    if (character != null) {
                        if (character.equals(scriptIndex)) {
                            startScript = true;
                        }
                    }
                }
                else {
                    ScriptRecord scriptRecord = new ScriptRecord();

                    if (character != null && !character.isEmpty()) {
                        if (character.charAt(0) == '[') {
                            //다음 이벤트 스크립트일 경우 종료
                            startScript = false;
                            break;
                        } else {
                            scriptRecord.setCharacter(character);
                        }
                    }
                    if (background != null && !background.isEmpty()) {
                        scriptRecord.setBackground(background);
                    }
                    if (speaker != null && !speaker.isEmpty()) {
                        scriptRecord.setSpeaker(speaker);
                    }
                    if (talk != null && !talk.isEmpty()) {
                        scriptRecord.setTalk(talk);
                    }
                    scriptList.add(scriptRecord);
                }

            }

            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        wait = false;
        select = false;
        skip = false;
        event = true;

        return scriptList;
    }
}
