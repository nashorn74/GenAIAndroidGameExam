package com.omworldgame.genaiandroidgameexam;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.omworldgame.genaiandroidgameexam.common.Utility;

public class ConfigActivity extends AppCompatActivity {

    SharedPreferences preferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        try {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } catch (IllegalStateException ignore) {
            //nothing
            //안드로이드 8.0에서 투명배경과 화면고정을 함께 사용하면 에러가 나는 경우에 대한 예외 사항
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        ToggleButton bgmToggleButton = (ToggleButton)findViewById(R.id.bgm_toggle);
        ToggleButton soundToggleButton = (ToggleButton)findViewById(R.id.sound_toggle);
        if (preferences.getBoolean("BGM", true)) {
            bgmToggleButton.setChecked(true);
        } else {
            bgmToggleButton.setChecked(false);
        }
        if (preferences.getBoolean("SOUND", true)) {
            soundToggleButton.setChecked(true);
        } else {
            soundToggleButton.setChecked(false);
        }
    }

    public void onBGMToggleClicked(View view) {
        Utility.playWaveFromDAT(R.raw.select);
    }

    public void onSoundToggleClicked(View view) {
        Utility.playWaveFromDAT(R.raw.select);
    }
    public void onBack(View view) {
        Utility.playWaveFromDAT(R.raw.click);

        ToggleButton bgmToggleButton = (ToggleButton)findViewById(R.id.bgm_toggle);
        ToggleButton soundToggleButton = (ToggleButton)findViewById(R.id.sound_toggle);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("BGM", bgmToggleButton.isChecked());
        editor.putBoolean("SOUND", soundToggleButton.isChecked());
        editor.apply();

        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}