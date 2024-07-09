package com.omworldgame.genaiandroidgameexam.common;

import static android.content.Context.VIBRATOR_SERVICE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.omworldgame.genaiandroidgameexam.entity.EventData;
import com.omworldgame.genaiandroidgameexam.R;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class Utility {
    @SuppressLint("StaticFieldLeak")
    private static Activity activity = null;

    public static void setActivity(Activity activity) { Utility.activity = activity; }

    public static void copyDatabase(Context context) {
        String databasePath = context.getFilesDir().getPath() + "/" + Constant.DB_NAME;
        try (InputStream input = context.getAssets().open(Constant.DB_NAME);
             OutputStream output = new FileOutputStream(databasePath)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            output.flush();
            Toast.makeText(activity, activity.getResources().getString(R.string.copied_dbfile), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("DatabaseUtils", "Error copying database", e);
        }
    }

    public static boolean saveGame(GameEngine gameEngine, int num) {
        @SuppressLint("DefaultLocale") String filename = String.format("game_%d.sav", num);

        try {
            File myDir = new File(activity.getFilesDir().getAbsolutePath()+"/save");
            try {
                myDir.mkdirs();
            } catch(Exception e) {
                Toast.makeText(activity, e.toString(), Toast.LENGTH_LONG).show();
            }

            File file = new File(myDir, filename);
            Log.e("filename", file.getAbsolutePath());

            FileOutputStream fileStream = new FileOutputStream(file);
            DataOutputStream dataStream = new DataOutputStream(fileStream);

            try {
                //////////////////////////////
                //파일 헤더 저장

                dataStream.writeUTF(Constant.GAME_VERSION);

                dataStream.writeUTF(gameEngine.getName());
                dataStream.writeInt(gameEngine.getCurPlace());
                dataStream.writeInt(gameEngine.getCurMonth());
                dataStream.writeInt(gameEngine.getCurDay());
                for (int i = 0; i < Constant.LOVE_COUNT; i++) {
                    dataStream.writeInt(gameEngine.getCharLove(i));
                }
                //이벤트 정보 저장
                for (int i = 0; i < Constant.CHARACTER_COUNT; i++)
                    for (int j = 0; j < Constant.EVENT_COUNT; j++) {
                        dataStream.writeBoolean(gameEngine.getEvent(i, j));
                    }
                //변수 정보 저장
                for (int i = 0; i < Constant.VARIABLE_COUNT; i++) {
                    dataStream.writeInt(gameEngine.getVariable(i));
                }
            } catch(IOException e) {
                Toast.makeText(activity, e.toString(), Toast.LENGTH_LONG).show();
            } finally {
                fileStream.close();
            }
        } catch(IOException e) {
            Toast.makeText(activity, e.toString(), Toast.LENGTH_LONG).show();
        }

        return true;
    }

    public static GameEngine loadGame(int num) {
        GameEngine gameEngine = new GameEngine();
        String filename = String.format(activity.getFilesDir().getAbsolutePath()+"/save/game_%d.sav", num);
        File file = new File(filename);
        if (file.isFile()) {
            try {
                FileInputStream fileStream = new FileInputStream(file);
                DataInputStream dataStream = new DataInputStream(fileStream);

                try {
                    ////////////////////////////////////////////////////////
                    // FILE HEADER
                    ////////////////////////////////////////////////////////

                    dataStream.readUTF();

                    gameEngine.setName(dataStream.readUTF());
                    gameEngine.setCurPlace(dataStream.readInt());
                    gameEngine.setCurMonth(dataStream.readInt());
                    gameEngine.setCurDay(dataStream.readInt());
                    for (int i = 0; i < Constant.LOVE_COUNT; i++) {
                        gameEngine.setCharLove(i, dataStream.readInt());
                    }
                    //이벤트 정보 로드
                    for (int i = 0; i < Constant.CHARACTER_COUNT; i++)
                        for (int j = 0; j < Constant.EVENT_COUNT; j++) {
                            gameEngine.setEvent(i, j, dataStream.readBoolean());
                        }
                    //변수 정보 로드
                    for (int i = 0; i < Constant.VARIABLE_COUNT; i++) {
                        gameEngine.setVariable(i, dataStream.readInt());
                    }
                } catch(IOException e) {
                    Toast.makeText(activity, e.toString(), Toast.LENGTH_LONG).show();
                } finally {
                    fileStream.close();
                }
            } catch(IOException e) {
                Toast.makeText(activity, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
        return gameEngine;
    }

    public static ArrayList<String> getSaveFileList() {
        ArrayList<String> fileList = new ArrayList<String>();

        for (int i = 0; i < Constant.SAVE_FILE_COUNT; i++) {
            String filename = String.format(activity.getFilesDir().getAbsolutePath()+"/save/game_%d.sav", i);
            Log.e("filename", filename);
            File file = new File(filename);
            if (file.isFile()) {
                try {
                    FileInputStream fileStream = new FileInputStream(file);
                    DataInputStream dataStream = new DataInputStream(fileStream);
                    //Reader in = new InputStreamReader(dataStream, "UTF-8");

                    try {
                        ////////////////////////////////////////////////////////
                        // FILE HEADER
                        ////////////////////////////////////////////////////////

                        dataStream.readUTF();

                        String name = dataStream.readUTF();
                        dataStream.readInt();
                        int curMonth = dataStream.readInt();
                        int curDay = dataStream.readInt();

                        @SuppressLint("DefaultLocale") String textString = String.format("%-16s %2d/%2d", name, curMonth, curDay);
                        fileList.add(textString);
                    } catch(IOException e) {
                        Toast.makeText(activity, e.toString(), Toast.LENGTH_LONG).show();
                    } finally {
                        fileStream.close();
                    }
                } catch(IOException e) {
                    Toast.makeText(activity, e.toString(), Toast.LENGTH_LONG).show();
                }
            } else {
                fileList.add("- - - - - NO SAVE - - - - -");
            }
        }
        return fileList;
    }

    private static boolean[] viewEventCG = new boolean[Constant.EVENT_CG_MAX];
    private static final String PREFS_NAME = "EventPrefs";
    public static void saveEventData() {
        Log.i("", "saveEventData()");
        SharedPreferences prefs = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        for (int i = 0; i < Constant.EVENT_CG_MAX; i++) {
            editor.putBoolean("evt"+String.valueOf(i), viewEventCG[i]);
        }

        editor.apply();
    }

    public static void loadEventData() {
        SharedPreferences prefs = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        for (int i = 0; i < Constant.EVENT_CG_MAX; i++) {
            viewEventCG[i] = prefs.getBoolean("evt"+String.valueOf(i), false);
        }
    }

    public static boolean checkEventData(String buffer) {
        for (int i = 0; i < Constant.EventData.getEventDataSize(); i++) {
            if (Constant.EventData.getEventData(i).equals(buffer)) {
                viewEventCG[i] = true;
                break;
            }
        }
        return true;
    }

    public static boolean isViewEvent(int num) {
        //return true; //testing
        return viewEventCG[num];
    }

    public static final long VIBRATE_DURATION = 50L;
    public static MediaPlayer player = null;
    public static void playWaveFromDAT(int resourceId) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity.getBaseContext());
        if (preferences.getBoolean("SOUND", true) == false) {
            return;
        }

        try {
            AudioManager audioManager = (AudioManager)activity.getSystemService(Context.AUDIO_SERVICE);
            int volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            int ringVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);

            //Toast.makeText(getBaseContext(), String.valueOf(volume)+"/"+String.valueOf(ringVolume), Toast.LENGTH_LONG).show();

            if (ringVolume == 0 || volume == 0) {
                Vibrator vibrator = (Vibrator) activity.getSystemService(VIBRATOR_SERVICE);
                //vibrator.vibrate(VIBRATE_DURATION);
            } else {
                if (player != null) {
                    if (player.isPlaying()) {
                        player.stop();
                    }
                }

                if (player != null) {
                    player.release();

                }

                //player = new MediaPlayer();
                try {
                    player = MediaPlayer.create(activity, resourceId);
                    //player.prepare();
                    player.start();
                } catch(Exception e) {
                    Toast.makeText(activity, e.toString(), Toast.LENGTH_LONG).show();
                }

	        		/*Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
	    	    	vibrator.vibrate(VIBRATE_DURATION);*/
            }
        } catch(Exception e) {
            Log.e("", e.toString());
            Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public static List<EventData> getEventLocationList(GameEngine gameEngine) {
        List<EventData> locations = new ArrayList<>();
        String[] filenames = {"char1", "char2", "char3", "common"};
        String outFileName = activity.getFilesDir().getAbsolutePath() + "/" + Constant.DB_NAME;

        for (int i = 0; i < filenames.length; i++) {
            try (SQLiteDatabase db = SQLiteDatabase.openDatabase(outFileName, null, SQLiteDatabase.OPEN_READONLY)) {
                Cursor cursor = db.query(filenames[i], new String[]{"*"}, null, null, null, null, null);

                while (cursor.moveToNext()) {
                    String character = cursor.getString(0);

                    if (character != null && character.startsWith("[") && character.length() > 1) {
                        character = character.substring(1, character.length() - 1);  // Remove the brackets
                        String[] array = character.split(":");
                        if (array.length == 3) {
                            int nscene = Integer.parseInt(array[0]);
                            int nmonth = Integer.parseInt(array[1].substring(0, 2));
                            int nday = Integer.parseInt(array[1].substring(2, 4));
                            int nplace = Integer.parseInt(array[2]);

                            if (nmonth == gameEngine.getCurMonth() && nday == gameEngine.getCurDay() && !gameEngine.getEvent(i, nscene - 1)) {
                                locations.add(new EventData(i, nplace));
                                break;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                Toast.makeText(activity, "ERROR IN CODE:" + e.toString(), Toast.LENGTH_LONG).show();
            }
        }

        return locations;
    }
}
