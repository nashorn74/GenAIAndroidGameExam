package com.omworldgame.genaiandroidgameexam;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.omworldgame.genaiandroidgameexam.common.Utility;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static MediaPlayer mediaPlayer = null;
    private SoundPool soundPool;
    SharedPreferences preferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } catch (IllegalStateException ignore) {
            //nothing
            //안드로이드 8.0에서 투명배경과 화면고정을 함께 사용하면 에러가 나는 경우에 대한 예외 사항
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Utility.setActivity(this);
        Utility.copyDatabase(this);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getBoolean("BGM", true)) {
            mediaPlayer = MediaPlayer.create(this, R.raw.bgm01_dreaming_together);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
    }

    public void displayMenuImage() {
        int imageIds[] = {
                R.drawable.main01, R.drawable.main02, R.drawable.main03,
        };
        Random random = new Random();
        int randomNumber = random.nextInt(3);

        ImageView mainImageView = (ImageView)findViewById(R.id.main_image);
        mainImageView.setImageResource(imageIds[randomNumber]);
    }

    @Override
    protected void onPostResume() {
        displayMenuImage();

        if (preferences.getBoolean("BGM", true)) {
            mediaPlayer.start();
        }

        super.onPostResume();
    }

    public void onClick(View view) {
        displayMenuImage();
    }

    public void onStartGame(View view) {
        /*Utility.playWaveFromDAT(R.raw.select);*/

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.input_name);

        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.name_layout, null);
        final EditText editText = customLayout.findViewById(R.id.editText);
        editText.setText(R.string.default_name);
        builder.setView(customLayout);

        // add a button
        builder.setPositiveButton(R.string.start_game, (dialog, which) -> {
            // send data from the AlertDialog to the Activity
            sendDialogDataToActivity(editText.getText().toString());
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void sendDialogDataToActivity(String data) {
        Intent intent = new Intent(MainActivity.this, GameMainActivity.class);
        intent.putExtra("load_num", -1);
        intent.putExtra("name", data);
        startActivity(intent);
    }

    public void onLoadGame(View view) {
        /*Utility.playWaveFromDAT(R.raw.select);

        Intent intent = new Intent(MainActivity.this, LoadGameActivity.class);
        startActivityForResult(intent, Constant.GM_LOAD);*/
    }

    public void onViewCG(View view) {
        //Utility.playWaveFromDAT(R.raw.select);

        Intent intent = new Intent(MainActivity.this, ViewCgMenuActivity.class);
        startActivity(intent);
    }

    public void onSettings(View view) {
        /*Utility.playWaveFromDAT(R.raw.select);

        //Intent intent = new Intent(MainActivity.this, ViewScriptMenuActivity.class);
        //startActivity(intent);
        Intent intent = new Intent(MainActivity.this, ConfigActivity.class);
        startActivityForResult(intent, Constant.GM_CONFIG);*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if (requestCode == Constant.GM_LOAD) {
            if (resultCode == RESULT_OK) {
                int loadNum = data.getIntExtra("load_num", 0);

                //TODO 저장된 파일이 있는지 체크

                Intent intent = new Intent(MainActivity.this, GameMainActivity.class);
                intent.putExtra("load_num", loadNum);
                startActivity(intent);
            }
        } else if (requestCode == Constant.GM_CONFIG) {
            if (preferences.getBoolean("BGM", true)) {
                mediaPlayer = MediaPlayer.create(this, R.raw.bgm00_casanova_whispers);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
            } else {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                }
            }
        }*/
    }

    @Override
    protected void onUserLeaveHint() {
        if (preferences.getBoolean("BGM", true)) {
            mediaPlayer.pause();
        }
        super.onUserLeaveHint();
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
        if (preferences.getBoolean("BGM", true)) {
            mediaPlayer.pause();
        }
        super.onBackPressed();
    }
}