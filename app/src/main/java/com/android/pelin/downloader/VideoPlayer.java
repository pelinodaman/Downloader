package com.android.pelin.downloader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoPlayer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        Intent intent = getIntent();

        Bundle b = intent.getExtras();

        String path = "";

        if(b!=null)
        {
           path = (String) b.get("videoPath");
        }


        VideoView video=(VideoView) findViewById(R.id.videoView);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(video);
        video.setMediaController(mediaController);
        video.setKeepScreenOn(true);
        video.setVideoPath(path);
        video.start();
        video.requestFocus();

    }
}
