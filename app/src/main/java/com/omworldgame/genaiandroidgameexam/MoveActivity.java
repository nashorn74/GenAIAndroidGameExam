package com.omworldgame.genaiandroidgameexam;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.omworldgame.genaiandroidgameexam.common.Utility;
import com.omworldgame.genaiandroidgameexam.entity.PlaceData;

import java.util.ArrayList;
import java.util.List;

public class MoveActivity extends AppCompatActivity {

    List<PlaceData> placeDataArrayList = new ArrayList<PlaceData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move);

        try {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } catch (IllegalStateException ignore) {
            //nothing
            //안드로이드 8.0에서 투명배경과 화면고정을 함께 사용하면 에러가 나는 경우에 대한 예외 사항
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Intent intent = getIntent();
        int curMonth = intent.getIntExtra("cur_month", 0);
        int curDay = intent.getIntExtra("cur_day", 0);
        if (intent.getParcelableArrayListExtra("place_list") != null) {
            for (int i = 0; i < intent.getParcelableArrayListExtra("place_list").size(); i++) {
                placeDataArrayList.add((PlaceData)intent.getParcelableArrayListExtra("place_list").get(i));
            }
        }

        TextView dateTextView = findViewById(R.id.date);
        @SuppressLint("DefaultLocale") String dateText = String.format("%d월 %d일", curMonth, curDay);
        dateTextView.setText(dateText);

        displayEventLocation();
    }

    void displayEventLocation() {
        int houseResourceCharId[] = {
                R.id.place1_char, R.id.place2_char, R.id.place3_char, R.id.place4_char,
                R.id.place5_char, R.id.place6_char, R.id.place7_char, R.id.place8_char,
                R.id.place9_char, R.id.place10_char,
        };

        int charResourceId[] = {
                R.drawable.char_icon1, R.drawable.char_icon2, R.drawable.char_icon3, R.drawable.char_icon4,

        };

        for (int i = 0; i < houseResourceCharId.length; i++) {
            PlaceData place = placeDataArrayList.get(i);
            ImageView charImage = (ImageView)findViewById(houseResourceCharId[i]);
            if (place.getEventCharNum() > -1) {
                charImage.setImageResource(charResourceId[place.getEventCharNum()]);
            } else {
                charImage.setImageResource(R.drawable.camera_blank);
            }
        }
    }

    public void onPlace1(View view) {
        if (placeDataArrayList.size() > 0) {
            PlaceData place = placeDataArrayList.get(0);
            Intent intent = new Intent();
            intent.putExtra("place_num", place.getPlaceNumber());
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    public void onPlace2(View view) {
        if (placeDataArrayList.size() > 1) {
            PlaceData place = placeDataArrayList.get(1);
            Intent intent = new Intent();
            intent.putExtra("place_num", place.getPlaceNumber());
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    public void onPlace3(View view) {
        if (placeDataArrayList.size() > 2) {
            PlaceData place = placeDataArrayList.get(2);
            Intent intent = new Intent();
            intent.putExtra("place_num", place.getPlaceNumber());
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    public void onPlace4(View view) {
        if (placeDataArrayList.size() > 3) {
            PlaceData place = placeDataArrayList.get(3);
            Intent intent = new Intent();
            intent.putExtra("place_num", place.getPlaceNumber());
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    public void onPlace5(View view) {
        if (placeDataArrayList.size() > 4) {
            PlaceData place = placeDataArrayList.get(4);
            Intent intent = new Intent();
            intent.putExtra("place_num", place.getPlaceNumber());
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    public void onPlace6(View view) {
        if (placeDataArrayList.size() > 5) {
            PlaceData place = placeDataArrayList.get(5);
            Intent intent = new Intent();
            intent.putExtra("place_num", place.getPlaceNumber());
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    public void onPlace7(View view) {
        if (placeDataArrayList.size() > 6) {
            PlaceData place = placeDataArrayList.get(6);
            Intent intent = new Intent();
            intent.putExtra("place_num", place.getPlaceNumber());
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    public void onPlace8(View view) {
        if (placeDataArrayList.size() > 7) {
            PlaceData place = placeDataArrayList.get(7);
            Intent intent = new Intent();
            intent.putExtra("place_num", place.getPlaceNumber());
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    public void onPlace9(View view) {
        if (placeDataArrayList.size() > 8) {
            PlaceData place = placeDataArrayList.get(8);
            Intent intent = new Intent();
            intent.putExtra("place_num", place.getPlaceNumber());
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    public void onPlace10(View view) {
        if (placeDataArrayList.size() > 9) {
            PlaceData place = placeDataArrayList.get(9);
            Intent intent = new Intent();
            intent.putExtra("place_num", place.getPlaceNumber());
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    public void onComplete(View view) {
        Utility.playWaveFromDAT(R.raw.click);

        Intent intent = new Intent();
        intent.putExtra("place_num", 9998);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}