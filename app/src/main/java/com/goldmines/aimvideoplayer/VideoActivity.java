package com.goldmines.aimvideoplayer;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class VideoActivity  extends AppCompatActivity {


    private static final String VIDEO_SAMPLE = "tacoma_narrows";//name of local kept video file
    private VideoView mVideoView;  // video view refrence object

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        initViews();
    }



    private void initViews() {
        mVideoView = findViewById(R.id.videoView); //Taking refrence of Video View
        initializePlayer();

    }


    @Override
    protected void onStart() {
        super.onStart();

        initializePlayer();
    }

    //    This method takes a String file name  and returns the URI of the  videp file stored in raw folder.
    private Uri getMedia(String mediaName) {
        return Uri.parse("android.resource://" + getPackageName() +
                "/raw/" + mediaName);
    }


    private void initializePlayer() {

        Uri videoUri = getMedia(VIDEO_SAMPLE);
        mVideoView.setVideoURI(videoUri);
        mVideoView.start();

    }

    private void releasePlayer() {
        mVideoView.stopPlayback();
    }



    /* This test is required because the behavior of onPause() and onStop() changed in Android N (7.0, API 24). In older versions of Android, onPause() was the end of the visual lifecycle of your app, and you could start releasing resources when the app was paused.

     In newer versions of Android, your app may be paused but still visible on the screen, as with multi-window or picture-in-picture (PIP) mode. In those cases the user likely wants the video to continue playing in the background. If the video is being played in multi-window or PIP mode, then it is onStop() that indicates the end of the visible life cycle of the app, and your video playback should indeed stop at that time.

     If you only stop playing your video in onStop(), as in the previous step, then on older devices there may be a few seconds where even though the app is no longer visible on screen, the video's audio track continues to play while onStop() catches up. This test for older versions of Android pauses the actual playback in onPause() to prevent the sound from playing after the app has disappeared from the screen.
 */
    @Override
    protected void onPause() {
        super.onPause();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            mVideoView.pause();
        }

    }


    @Override
    protected void onStop() {
        super.onStop();

        releasePlayer();
    }
}
