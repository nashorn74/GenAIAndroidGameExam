package com.omworldgame.genaiandroidgameexam;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.omworldgame.genaiandroidgameexam.common.Constant;
import com.omworldgame.genaiandroidgameexam.common.Utility;

import androidx.appcompat.app.AppCompatActivity;

public class ViewCgActivity extends AppCompatActivity {

    int char_num = 0;
    int viewCGNum = 0;
    private static MediaPlayer mediaPlayer = null;
    SharedPreferences preferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cg);

        try {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } catch (IllegalStateException ignore) {
            //nothing
            //안드로이드 8.0에서 투명배경과 화면고정을 함께 사용하면 에러가 나는 경우에 대한 예외 사항
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Intent intent = getIntent();
        char_num = intent.getIntExtra("charNum", -1);
        TextView bgmTitleView = (TextView)findViewById(R.id.bgm_title);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String[] bgmTitles = {
                getResources().getString(R.string.bgm_1_name),
                getResources().getString(R.string.bgm_2_name),
                getResources().getString(R.string.bgm_3_name),
                getResources().getString(R.string.bgm_4_name),
        };
        bgmTitleView.setText("BGM: "+bgmTitles[char_num]);
        viewCGNum = Constant.EventData.START_POINT[char_num]+1;

        boolean exist = false;
        for (int i = viewCGNum; i < Constant.EventData.END_POINT[char_num]; i++) {
            if (Utility.isViewEvent(i) == true) {
                exist = true;
                viewCGNum = i;
                break;
            }
        }
        if (exist == true) {
            //Utility.playWaveFromDAT(R.raw.previous);
            displayEventCG();
        }

        if (preferences.getBoolean("BGM", true)) {
            if (char_num == 0) {
                mediaPlayer = MediaPlayer.create(this, R.raw.bgm02_whispered_grace);
            } else if (char_num == 1) {
                mediaPlayer = MediaPlayer.create(this, R.raw.bgm03_radiant_dynamo);
            } else if (char_num == 2) {
                mediaPlayer = MediaPlayer.create(this, R.raw.bgm04_radiant_heart);
            } else if (char_num == 3) {
                mediaPlayer = MediaPlayer.create(this, R.raw.bgm05_rise_and_shine);
            }
        }

        if (mediaPlayer != null) {
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
    }

    void displayEventCG() {
        TextView countText = (TextView)findViewById(R.id.count);
        countText.setText((viewCGNum+1)+"/"+Constant.EventData.getEventDataSize());

        ImageView bgImage = (ImageView)findViewById(R.id.event_cg);
        try {
            try {
                ((BitmapDrawable)bgImage.getDrawable()).getBitmap().recycle();
            } catch(Exception e) {
                Log.e("", e.toString());
            }
        } catch(Exception e) {
            Log.e("", e.toString());
        }
        Drawable image = null;
        try {
            String filename = Constant.EventData.getEventData(viewCGNum);
            image = Drawable.createFromStream(getAssets().open("event/event" + filename + ".jpg"), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        bgImage.setImageDrawable(image);
    }

    public void onPrev(View view) {
        //Utility.playWaveFromDAT(R.raw.click);
        boolean exist = false;
        for (int i = viewCGNum - 1; i > Constant.EventData.START_POINT[char_num]; i--) {
            if (Utility.isViewEvent(i)) {
                exist = true;
                viewCGNum = i;
                break;
            }
        }
        if (exist) {
            //Utility.playWaveFromDAT(R.raw.previous);
            displayEventCG();
        }
    }

    public void onNext(View view) {
        boolean exist = false;
        for (int i = viewCGNum+1; i < Constant.EventData.END_POINT[char_num]; i++) {
            if (Utility.isViewEvent(i)) {
                exist = true;
                viewCGNum = i;
                break;
            }
        }
        if (exist) {
            //Utility.playWaveFromDAT(R.raw.previous);
            displayEventCG();
        }
    }

    public void onBack(View view) {
        //Utility.playWaveFromDAT(R.raw.cancel);
        finish();
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