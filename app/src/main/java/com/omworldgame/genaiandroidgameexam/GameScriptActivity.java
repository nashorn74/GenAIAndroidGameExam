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
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.omworldgame.genaiandroidgameexam.common.Constant;
import com.omworldgame.genaiandroidgameexam.common.Utility;

import java.util.ArrayList;

public class GameScriptActivity extends AppCompatActivity {

    private ArrayList<String> scriptCharList = new ArrayList<String>();
    private ArrayList<String> scriptBgList = new ArrayList<String>();
    private ArrayList<String> scriptSpeakerList = new ArrayList<String>();
    private ArrayList<String> scriptTalkList = new ArrayList<String>();

    private boolean bWait = false;
    private boolean bEvent = false;

    private int currentScriptNum = -1;

    private String name = "";
    private String scriptFilename = "";
    private String scriptIndex = "";

    private static MediaPlayer mediaPlayer = null;
    private SoundPool soundPool;
    SharedPreferences preferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_script);

        try {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } catch (IllegalStateException ignore) {
            //nothing
            //안드로이드 8.0에서 투명배경과 화면고정을 함께 사용하면 에러가 나는 경우에 대한 예외 사항
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        int orientation = this.getResources().getConfiguration().orientation;

        ImageView charImage = (ImageView)findViewById(R.id.char_image);
        charImage.setVisibility(View.GONE);

        ImageView interfaceImage = (ImageView)findViewById(R.id.interface_image);
        interfaceImage.setVisibility(View.VISIBLE);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            Utility.loadEventData();

            Intent intent = getIntent();

            name = intent.getStringExtra("etNameValue");
            scriptFilename = intent.getStringExtra("etScriptValue");
            scriptIndex = intent.getStringExtra("etScriptIndexValue");

            TextView lovePointText = (TextView)findViewById(R.id.love_point);
            TextView lovePointValueText = (TextView)findViewById(R.id.love_point_value);
            int lovePoint = intent.getIntExtra("love", -1);
            if (lovePoint != -1) {
                lovePointText.setVisibility(View.VISIBLE);
                lovePointValueText.setVisibility(View.VISIBLE);
                lovePointValueText.setText(lovePoint + "");
            } else {
                lovePointText.setVisibility(View.GONE);
                lovePointValueText.setVisibility(View.GONE);
            }

            Button skipButton = (Button)findViewById(R.id.skip_button);
            //skipButton.setVisibility(View.GONE);//릴리즈모드에서는 활성화(디버깅용 버튼)
            skipButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (bEvent)
                    {
                        performEndScript();
                    }
                }
            });

            if (preferences.getBoolean("BGM", true)) {
                if (scriptFilename.equals("bad_ending") || scriptIndex.equals("[HAPPYEND]")) {
                    mediaPlayer = MediaPlayer.create(this, R.raw.bgm05_rise_and_shine);
                } else if (scriptFilename.equals("intro") || scriptFilename.equals("common")) {
                    mediaPlayer = MediaPlayer.create(this, R.raw.bgm06_serene_starlight);
                } else if (scriptFilename.equals("char1")) {
                    mediaPlayer = MediaPlayer.create(this, R.raw.bgm02_whispered_grace);
                } else if (scriptFilename.equals("char2")) {
                    mediaPlayer = MediaPlayer.create(this, R.raw.bgm03_radiant_dynamo);
                } else if (scriptFilename.equals("char3")) {
                    mediaPlayer = MediaPlayer.create(this, R.raw.bgm04_radiant_heart);
                }

                if (mediaPlayer != null) {
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                }
            }

            performETCScript();
        }
    }

    public void performEndScript() {

        ImageView bgImage = (ImageView)findViewById(R.id.bg);
        bgImage.setImageResource(R.drawable.camera_blank);

        ImageView charImage = (ImageView)findViewById(R.id.char_image);
        charImage.setVisibility(View.GONE);

        ImageView interfaceImage = (ImageView)findViewById(R.id.interface_image);
        interfaceImage.setVisibility(View.GONE);

        TextView talkText = (TextView)findViewById(R.id.talk_text);
        talkText.setVisibility(View.GONE);

        bWait = false;
        bEvent = false;

        scriptCharList.clear();
        scriptBgList.clear();
        scriptSpeakerList.clear();
        scriptTalkList.clear();

        Utility.saveEventData();

        Intent intent = new Intent();
        setResult(RESULT_OK, intent);

        finish();
    }

    public void setTimeData() {
        if (bEvent && !bWait) {
            if (scriptCharList.size() > currentScriptNum) {
                updateUI();
            } else {
                performEndScript();
            }
        }
    }

    private void updateUI() {
        ImageView interfaceImage = findViewById(R.id.interface_image);
        ImageView bgImage = findViewById(R.id.bg);
        ImageView charImage = findViewById(R.id.char_image);
        TextView talkText = findViewById(R.id.talk_text);

        interfaceImage.setVisibility(View.GONE);
        boolean nothing = true;

        String charString = scriptCharList.get(currentScriptNum);
        String bgString = scriptBgList.get(currentScriptNum);
        String speakerString = scriptSpeakerList.get(currentScriptNum);
        String talkString = scriptTalkList.get(currentScriptNum);

        String talkMessage = "[" + speakerString + "] " + talkString;

        if (!bgString.isEmpty()) {
            nothing = handleBackground(bgString, bgImage, charImage);
        }

        if (!charString.isEmpty()) {
            nothing = handleCharacter(charString, charImage) && nothing;
        }

        if (talkMessage.length() > 3) {
            nothing = false;
            talkMessage = talkMessage.replace("[]", name);
            interfaceImage.setVisibility(View.VISIBLE);
            talkText.setVisibility(View.VISIBLE);
            talkText.setText(talkMessage);
        } else {
            interfaceImage.setVisibility(View.GONE);
            talkText.setVisibility(View.GONE);
        }

        currentScriptNum++;
        if (!nothing) {
            bWait = true;
        }
    }

    private boolean handleBackground(String bgString, ImageView bgImage, ImageView charImage) {
        boolean nothing = false;
        if (bgString.charAt(0) == 'e') {
            bgString = bgString.substring(1);
            setImageFromAssets("event/event" + bgString + ".jpg", bgImage);
            charImage.setVisibility(View.GONE);
            Utility.checkEventData(bgString);
        } else if (bgString.charAt(0) == 'b') {
            if (bgString.charAt(1) == 's') {
                bgString = bgString.substring(2);
            } else {
                bgString = bgString.substring(1);
            }
            setImageFromAssets("bg/bg" + bgString + ".jpg", bgImage);
            charImage.setVisibility(View.GONE);
        }
        return nothing;
    }

    private boolean handleCharacter(String charString, ImageView charImage) {
        boolean nothing = false;
        if (charString.charAt(0) == 'c' || charString.charAt(0) == 'e') {
            charString = charString.substring(1);
            setImageFromAssets("char/char" + charString + ".jpg", charImage);
            charImage.setVisibility(View.VISIBLE);
        } else if (charString.charAt(0) == '*') {
            charImage.setVisibility(View.GONE);
        } else if (charString.charAt(0) == '[') {
            performEndScript();
        }
        return nothing;
    }

    private void setImageFromAssets(String assetPath, ImageView imageView) {
        try {
            Drawable image = Drawable.createFromStream(getAssets().open(assetPath), null);
            imageView.setImageDrawable(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTime() {
        if (!bWait)
        {
            setTimeData();
        }

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setTime();
            }
        }, 100);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                handleActionDown();
                break;
            case MotionEvent.ACTION_MOVE:
                // handleTouchMove(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
                // handleTouchUp();
                break;
        }

        return super.onTouchEvent(event);
    }

    private void handleActionDown() {
        if (bEvent && bWait) {
            bWait = false;
        }
    }

    void performETCScript() {
        try {
            Cursor cursor;
            String outFileName = getFilesDir().getAbsolutePath()+"/"+Constant.DB_NAME;
            SQLiteDatabase db = SQLiteDatabase.openDatabase(outFileName, null, SQLiteDatabase.OPEN_READONLY);

            String[] FROM = {	"*"  	};
            cursor = db.query(scriptFilename, FROM, null, null, null, null, null);

            scriptCharList.clear();
            scriptBgList.clear();
            scriptSpeakerList.clear();
            scriptTalkList.clear();

            boolean startScript = false;
            while(cursor.moveToNext()) {
                String character = cursor.getString(0);
                String background = cursor.getString(1);
                String speaker = cursor.getString(2);
                String talk = cursor.getString(3);

                if (!startScript) {
                    if (character != null && character.equals(scriptIndex)) {
                        startScript = true;
                    }
                } else {
                    if (character != null && character.charAt(0) == '[' && countOccurrences(character, ':') == 3) {
                        break;
                    }

                    scriptCharList.add(character != null ? character : "");
                    scriptBgList.add(background != null ? background : "");
                    scriptSpeakerList.add(speaker != null ? speaker : "");
                    scriptTalkList.add(talk != null ? talk : "");
                }
            }

            db.close();

        } catch (Exception e) {
            Toast.makeText(this, "ERROR IN CODE:"+e.toString(), Toast.LENGTH_LONG).show();
        }

        bWait = false;
        bEvent = true;

        currentScriptNum = 0;

        setTime();
    }

    private int countOccurrences(String str, char ch) {
        int count = 0;
        for (char c : str.toCharArray()) {
            if (c == ch) count++;
        }
        return count;
    }

    @Override
    public void onBackPressed(){

        AlertDialog.Builder builder = new AlertDialog.Builder(GameScriptActivity.this);
        builder.setTitle("종료");
        builder.setMessage("게임을 종료하시겠습니까?")
                .setCancelable(true).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (preferences.getBoolean("BGM", true)) {
                            if (mediaPlayer != null) {
                                mediaPlayer.stop();
                            }
                        }

                        setResult(RESULT_CANCELED);

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

    @Override
    protected void onUserLeaveHint() {
        if (preferences.getBoolean("BGM", true)) {
            if (mediaPlayer != null) {
                mediaPlayer.pause();
            }
        }
        super.onUserLeaveHint();
    }

    @Override
    protected void onPostResume() {
        if (preferences.getBoolean("BGM", true)) {
            if (mediaPlayer != null) {
                mediaPlayer.start();
            }
        }

        super.onPostResume();
    }

    @Override
    protected void onDestroy() {
        if (preferences.getBoolean("BGM", true)) {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }
        }

        super.onDestroy();
    }
}