package com.zohdy.maxburger.activities;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

import com.bumptech.glide.Glide;
import com.zohdy.maxburger.R;
import com.zohdy.maxburger.common.Common;

import java.io.IOException;

public class OrderReadyActivity extends AppCompatActivity {

    Button buttonOrderReady;
    MediaPlayer mediaPlayer;
    TextView textViewOrderReady;
    ImageView imageViewBurgerGif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_ready);

        mediaPlayer = new MediaPlayer();
        imageViewBurgerGif = findViewById(R.id.iv_burger_gif);
        buttonOrderReady = findViewById(R.id.btn_order_ready);
        textViewOrderReady = findViewById(R.id.tv_order_ready);
        textViewOrderReady.setText("Din mad er klar " + Common.currenUser.getName());

        playAlarmSound();
        handleOrderReadyButton();
        displayGifImage();
    }

    private void displayGifImage() {
        Glide.with(getApplicationContext())
                .asGif()
                .load(R.drawable.burger_gif)
                .into(imageViewBurgerGif);
    }

    private void playAlarmSound() {
        Uri myUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(getApplicationContext(), myUri);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
    }


    private void handleOrderReadyButton() {
        buttonOrderReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPlaying();
                Intent backToHomeActivityIntent = new Intent(OrderReadyActivity.this, HomeActivity.class);
                startActivity(backToHomeActivityIntent);
            }
        });
    }

    private void stopPlaying() {
        if(mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
