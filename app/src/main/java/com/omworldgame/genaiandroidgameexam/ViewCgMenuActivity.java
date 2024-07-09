package com.omworldgame.genaiandroidgameexam;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.omworldgame.genaiandroidgameexam.common.Constant;
import com.omworldgame.genaiandroidgameexam.common.Utility;

import androidx.appcompat.app.AppCompatActivity;

public class ViewCgMenuActivity extends AppCompatActivity {

    int[] counts = {
            0, 0, 0, 0,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cg_menu);

        try {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } catch (IllegalStateException ignore) {
            //nothing
            //안드로이드 8.0에서 투명배경과 화면고정을 함께 사용하면 에러가 나는 경우에 대한 예외 사항
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        String[] names = {
                "유아라", "박지원", "김유진", "공통",
        };
        int nameIds[] = {
                R.id.char1_name, R.id.char2_name, R.id.char3_name, R.id.char4_name,
        };

        Utility.loadEventData();
        for (int i = 0; i < Constant.EventData.START_POINT.length; i++) {
            int total = Constant.EventData.END_POINT[i] - (Constant.EventData.START_POINT[i]+1);
            int count = 0;
            for (int j = Constant.EventData.START_POINT[i]+1; j < Constant.EventData.END_POINT[i]; j++) {
                if (Utility.isViewEvent(j)) {
                    count = count + 1;
                }
            }
            counts[i] = count;
            TextView nameTextView = (TextView)findViewById(nameIds[i]);
            nameTextView.setText(names[i]+"\n"+count+"/"+total);
            //Log.e("view_cg", names[i]+"\n"+count+"/"+total);
        }
    }

    public void onChar1(View view) {
        //Utility.playWaveFromDAT(R.raw.click);
        if (counts[0] > 0) {
            Intent intent = new Intent(ViewCgMenuActivity.this, ViewCgActivity.class);
            intent.putExtra("charNum", 0);
            startActivity(intent);
        }
    }

    public void onChar2(View view) {
        //Utility.playWaveFromDAT(R.raw.click);
        if (counts[1] > 0) {
            Intent intent = new Intent(ViewCgMenuActivity.this, ViewCgActivity.class);
            intent.putExtra("charNum", 1);
            startActivity(intent);
        }
    }

    public void onChar3(View view) {
        //Utility.playWaveFromDAT(R.raw.click);
        if (counts[2] > 0) {
            Intent intent = new Intent(ViewCgMenuActivity.this, ViewCgActivity.class);
            intent.putExtra("charNum", 2);
            startActivity(intent);
        }
    }

    public void onChar4(View view) {
        //Utility.playWaveFromDAT(R.raw.click);
        if (counts[3] > 0) {
            Intent intent = new Intent(ViewCgMenuActivity.this, ViewCgActivity.class);
            intent.putExtra("charNum", 3);
            startActivity(intent);
        }
    }

    public void onBack(View view) {
        //Utility.playWaveFromDAT(R.raw.cancel);
        finish();
    }
}