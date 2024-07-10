package com.omworldgame.genaiandroidgameexam;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.omworldgame.genaiandroidgameexam.common.Utility;

import java.util.ArrayList;

public class SaveGameActivity extends AppCompatActivity {

    ArrayList<String> fileList = null;
    int selectedFile = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_game);

        try {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } catch (IllegalStateException ignore) {
            //nothing
            //안드로이드 8.0에서 투명배경과 화면고정을 함께 사용하면 에러가 나는 경우에 대한 예외 사항
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        displayFileList();
    }

    void displayFileList() {
        fileList = Utility.getSaveFileList();

        final int saveButtonResourceId[] = {
                R.id.save_01, R.id.save_02,	R.id.save_03, R.id.save_04, R.id.save_05,
                R.id.save_06, R.id.save_07,	R.id.save_08, R.id.save_09, R.id.save_10
        };
        for (int i = 0; i < saveButtonResourceId.length; i++) {
            TextView filenameText = (TextView)findViewById(saveButtonResourceId[i]);
            filenameText.setText(fileList.get(i));
            filenameText.setTextColor(Color.BLACK);
            if (selectedFile == i) {
                filenameText.setBackgroundColor(Color.WHITE);
            } else {
                filenameText.setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }

    public void onSave1(View view) {
        selectedFile = 0;
        displayFileList();
    }
    public void onSave2(View view) {
        selectedFile = 1;
        displayFileList();
    }
    public void onSave3(View view) {
        selectedFile = 2;
        displayFileList();
    }
    public void onSave4(View view) {
        selectedFile = 3;
        displayFileList();
    }
    public void onSave5(View view) {
        selectedFile = 4;
        displayFileList();
    }
    public void onSave6(View view) {
        selectedFile = 5;
        displayFileList();
    }
    public void onSave7(View view) {
        selectedFile = 6;
        displayFileList();
    }
    public void onSave8(View view) {
        selectedFile = 7;
        displayFileList();
    }
    public void onSave9(View view) {
        selectedFile = 8;
        displayFileList();
    }
    public void onSave10(View view) {
        selectedFile = 9;
        displayFileList();
    }

    public void onSave(View view) {
        Utility.playWaveFromDAT(R.raw.select);

        Intent intent = new Intent();
        intent.putExtra("save_num", selectedFile);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void onBack(View view) {
        Utility.playWaveFromDAT(R.raw.cancel);
        finish();
    }
}