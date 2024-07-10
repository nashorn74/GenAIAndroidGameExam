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

public class LoadGameActivity extends AppCompatActivity {
    ArrayList<String> fileList = null;
    int selectedFile = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_game);

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

        final int loadButtonResourceId[] = {
                R.id.load_01, R.id.load_02,	R.id.load_03, R.id.load_04, R.id.load_05,
                R.id.load_06, R.id.load_07,	R.id.load_08, R.id.load_09, R.id.load_10
        };
        for (int i = 0; i < loadButtonResourceId.length; i++) {
            TextView filenameText = (TextView)findViewById(loadButtonResourceId[i]);
            filenameText.setText(fileList.get(i));
            filenameText.setTextColor(Color.BLACK);
            if (selectedFile == i) {
                filenameText.setBackgroundColor(Color.WHITE);
            } else {
                filenameText.setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }

    public void onLoad1(View view) {
        selectedFile = 0;
        displayFileList();
    }
    public void onLoad2(View view) {
        selectedFile = 1;
        displayFileList();
    }
    public void onLoad3(View view) {
        selectedFile = 2;
        displayFileList();
    }
    public void onLoad4(View view) {
        selectedFile = 3;
        displayFileList();
    }
    public void onLoad5(View view) {
        selectedFile = 4;
        displayFileList();
    }
    public void onLoad6(View view) {
        selectedFile = 5;
        displayFileList();
    }
    public void onLoad7(View view) {
        selectedFile = 6;
        displayFileList();
    }
    public void onLoad8(View view) {
        selectedFile = 7;
        displayFileList();
    }
    public void onLoad9(View view) {
        selectedFile = 8;
        displayFileList();
    }
    public void onLoad10(View view) {
        selectedFile = 9;
        displayFileList();
    }

    public void onLoad(View view) {
        if (fileList.get(selectedFile).equals(getResources().getString(R.string.empty_filename))) {
            return;
        }
        Utility.playWaveFromDAT(R.raw.select);
        Intent intent = new Intent();
        intent.putExtra("load_num", selectedFile);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void onBack(View view) {
        Utility.playWaveFromDAT(R.raw.cancel);
        finish();
    }
}