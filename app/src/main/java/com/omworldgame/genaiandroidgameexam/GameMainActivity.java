package com.omworldgame.genaiandroidgameexam;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.omworldgame.genaiandroidgameexam.common.Constant;
import com.omworldgame.genaiandroidgameexam.common.GameEngine;
import com.omworldgame.genaiandroidgameexam.common.Utility;
import com.omworldgame.genaiandroidgameexam.entity.EventData;
import com.omworldgame.genaiandroidgameexam.entity.PlaceData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameMainActivity extends AppCompatActivity {

    private GameEngine gameEngine = new GameEngine();

    private boolean placeEvent = false;

    private static MediaPlayer mediaPlayer = null;
    private SoundPool soundPool;
    SharedPreferences preferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_main);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } catch (IllegalStateException ignore) {
            //nothing
            //안드로이드 8.0에서 투명배경과 화면고정을 함께 사용하면 에러가 나는 경우에 대한 예외 사항
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Intent intent = getIntent();
        int loadNum = intent.getIntExtra("load_num", -1);
        String name = intent.getStringExtra("name");

        if (loadNum != -1) {
            gameEngine = Utility.loadGame(loadNum);
        }
        if (name != null && !name.isEmpty()) {
            gameEngine.setName(name);
        }
        performMovePlace();

        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT && loadNum == -1) {
            Intent i = new Intent(GameMainActivity.this, GameScriptActivity.class);
            i.putExtra("etNameValue", gameEngine.getName());
            i.putExtra("etScriptValue", "intro");
            i.putExtra("etScriptIndexValue", "[INTRO]");
            startActivityForResult(i, Constant.GM_INTRO);
        }
    }

    @Override
    protected void onUserLeaveHint() {
        if (preferences.getBoolean("BGM", true)) {
            mediaPlayer.pause();
        }
        super.onUserLeaveHint();
    }

    @Override
    protected void onPostResume() {
        if (preferences.getBoolean("BGM", true)) {
            mediaPlayer.start();
        }

        super.onPostResume();
    }

    @Override
    protected void onDestroy() {
        if (preferences.getBoolean("BGM", true)) {
            mediaPlayer.stop();
        }

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
    }

    void performMovePlace() {
        updateDateTextView();
        setBackgroundImage();
        playBackgroundMusic();
    }

    private void updateDateTextView() {
        TextView dateTextView = findViewById(R.id.date);
        String dateText = String.format("%d월 %d일", gameEngine.getCurMonth(), gameEngine.getCurDay());
        dateTextView.setText(dateText);
    }

    private void setBackgroundImage() {
        ImageView bgImage = findViewById(R.id.bg);
        String filename = String.format("bg/bg%03d.jpg", gameEngine.getCurPlace());
        try {
            Drawable image = Drawable.createFromStream(getAssets().open(filename), null);
            bgImage.setBackgroundDrawable(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playBackgroundMusic() {
        if (preferences.getBoolean("BGM", true)) {
            if (mediaPlayer != null) {
                mediaPlayer.release();
            }
            mediaPlayer = MediaPlayer.create(this, R.raw.bgm06_serene_starlight);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
    }

    public void onMove(View view) {
        Utility.playWaveFromDAT(R.raw.select);

        List<EventData> locations = Utility.getEventLocationList(gameEngine);
        for (int i = 0; i < locations.size(); i++) {
            EventData place = locations.get(i);
            Log.e("Test", place.charNum+"/"+place.placeNum);
        }

        int curMonth = gameEngine.getCurMonth();
        int curDay = gameEngine.getCurDay();

        ArrayList<PlaceData> placeDataArrayList = new ArrayList<PlaceData>();

        placeDataArrayList.add(new PlaceData(1, getResources().getString(R.string.place_1_name), 0, -1));
        placeDataArrayList.add(new PlaceData(2, getResources().getString(R.string.place_2_name), 0, -1));
        placeDataArrayList.add(new PlaceData(3, getResources().getString(R.string.place_3_name), 0, -1));
        placeDataArrayList.add(new PlaceData(4, getResources().getString(R.string.place_4_name), 0, -1));
        placeDataArrayList.add(new PlaceData(5, getResources().getString(R.string.place_5_name), 0, -1));
        placeDataArrayList.add(new PlaceData(6, getResources().getString(R.string.place_6_name), 0, -1));
        placeDataArrayList.add(new PlaceData(7, getResources().getString(R.string.place_7_name), 0, -1));
        placeDataArrayList.add(new PlaceData(8, getResources().getString(R.string.place_8_name), 0, -1));
        placeDataArrayList.add(new PlaceData(9, getResources().getString(R.string.place_9_name), 0, -1));
        placeDataArrayList.add(new PlaceData(10, getResources().getString(R.string.place_10_name), 0, -1));

        Log.e("Test", "locations.size()="+locations.size());
        for (int i = 0; i < placeDataArrayList.size(); i++) {
            for (int j = 0; j < locations.size(); j++) {
                Log.e("Test", placeDataArrayList.get(i).getPlaceNumber()+"/"+locations.get(j).placeNum);
                if (placeDataArrayList.get(i).getPlaceNumber() == locations.get(j).placeNum) {
                    placeDataArrayList.get(i).setEventCharNum(locations.get(j).charNum);
                    break;
                }
            }
        }

        Intent intent = new Intent(GameMainActivity.this, MoveActivity.class);
        intent.putExtra("place_list", placeDataArrayList);
        intent.putExtra("cur_month", curMonth);
        intent.putExtra("cur_day", curDay);
        startActivityForResult(intent, Constant.GM_MOVE);
    }

    public void onSave(View view) {
        Utility.playWaveFromDAT(R.raw.select);

        /*Intent intent = new Intent(GameMainActivity.this, SaveGameActivity.class);
        startActivityForResult(intent, Constant.GM_SAVE);*/
    }

    public void onQuit(View view) {
        Utility.playWaveFromDAT(R.raw.cancel);

        AlertDialog.Builder builder = new AlertDialog.Builder(GameMainActivity.this);
        builder.setTitle("종료");
        builder.setMessage("게임을 종료하시겠습니까?")
                .setCancelable(true).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Utility.saveEventData();
                        finish();
                    }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //nothing
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    void performEvent() {
        String[] filenames = {"char1", "char2", "char3", "common"};
        String outFileName = getFilesDir().getAbsolutePath() + "/" + Constant.DB_NAME;

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

                            if (nmonth == gameEngine.getCurMonth() && nday == gameEngine.getCurDay() && nplace == gameEngine.getCurPlace() && !gameEngine.getEvent(i, nscene - 1)) {
                                Utility.playWaveFromDAT(R.raw.vibra);
                                gameEngine.setEvent(i,  nscene-1, true);
                                int lovePoint = -1;
                                if (i < Constant.LOVE_COUNT) {
                                    gameEngine.setCharLove(i, gameEngine.getCharLove(i)+5);
                                    lovePoint = gameEngine.getCharLove(i);
                                }

                                Intent intent = new Intent(GameMainActivity.this, GameScriptActivity.class);
                                intent.putExtra("etNameValue", gameEngine.getName());
                                intent.putExtra("etScriptValue", filenames[i]);
                                intent.putExtra("etScriptIndexValue", cursor.getString(0));
                                intent.putExtra("love", lovePoint);
                                startActivityForResult(intent, Constant.GM_PLAY);
                                break;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                Toast.makeText(this, "ERROR IN CODE:" + e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.GM_PLAY) {
            if (resultCode == RESULT_OK) {
                performMovePlace();
            } else {
                finish();
            }
        } else if (requestCode == Constant.GM_INTRO) {
            if (resultCode == RESULT_OK) {
                performMovePlace();
            } else {
                finish();
            }
        } else if (requestCode == Constant.GM_ENDING) {
            finish();
        } else if (requestCode == Constant.GM_MOVE) {
            if (resultCode == RESULT_OK) {
                int placeNum = data.getIntExtra("place_num", 0);

                if (gameEngine.getCurPlace() == placeNum) {
                    performMovePlace();
                    performEvent();
                } else if (placeNum == 9998) {//이동 종료
                    //Utility.playWaveFromDAT(R.raw.select);
                    //마지막날인가
                    if (gameEngine.getCurMonth() == 7 && gameEngine.getCurDay() == 31) {
                        int maxlove = 0;
                        int maxlovegirl = -1;
                        for (int i = 0; i < Constant.LOVE_COUNT; i++) {
                            if (gameEngine.getCharLove(i) > maxlove) {
                                maxlove = gameEngine.getCharLove(i);
                                maxlovegirl = i;
                            }
                        }

                        if (maxlove >= 90) {//해피엔딩
                            String filename[] = {
                                    "char1", "char2", "char3"
                            };
                            Intent i = new Intent(GameMainActivity.this, GameScriptActivity.class);
                            i.putExtra("etNameValue", gameEngine.getName());
                            i.putExtra("etScriptValue", filename[maxlovegirl]);
                            i.putExtra("etScriptIndexValue", "[HAPPYEND]");
                            startActivityForResult(i, Constant.GM_ENDING);
                        } else {//배드엔딩
                            Intent i = new Intent(GameMainActivity.this, GameScriptActivity.class);
                            i.putExtra("etNameValue", gameEngine.getName());
                            i.putExtra("etScriptValue", "bad_ending");
                            i.putExtra("etScriptIndexValue", "[BADEND]");
                            startActivityForResult(i, Constant.GM_ENDING);
                        }
                    } else {
                        gameEngine.setCurDay(gameEngine.getCurDay() + 1);
                        gameEngine.setCurPlace(3);
                        performMovePlace();
                    }
                } else {
                    //Utility.playWaveFromDAT(R.raw.walk);

                    gameEngine.setCurPlace(placeNum);
                    performMovePlace();
                    performEvent();
                }
            }
        } else if (requestCode == Constant.GM_SAVE) {
            if (resultCode == RESULT_OK) {
                int saveNum = data.getIntExtra("save_num", 0);
                Utility.saveGame(gameEngine, saveNum);
                Toast.makeText(getBaseContext(), "게임을 저장했습니다.", Toast.LENGTH_LONG).show();
            }
        }
    }
}