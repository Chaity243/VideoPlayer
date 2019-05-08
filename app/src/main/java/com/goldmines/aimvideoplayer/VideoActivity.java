package com.goldmines.aimvideoplayer;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.webkit.URLUtil;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class VideoActivity  extends AppCompatActivity implements MediaPlayer.OnCompletionListener {


    private static final String VIDEO_SAMPLE = "tacoma_narrows";//name of local kept video file
    private VideoView mVideoView;  // video view refrence object
    private int mCurrentPosition = 0;
    private static final String PLAYBACK_TIME = "play_time";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        if(savedInstanceState!=null)
        {
            mCurrentPosition=savedInstanceState.getInt(PLAYBACK_TIME);

        }

        initViews();

    }



    private void initViews() {
        mVideoView = findViewById(R.id.videoView); //Taking refrence of Video View
        MediaController controller = new MediaController(this);
        controller.setMediaPlayer(mVideoView);
        mVideoView.setOnCompletionListener(this);


        /*Use setMediaController() to do the reverse connection, that is, to tell the VideoView that the MediaController will be used to control it:*/
        mVideoView.setMediaController(controller);

    }


    @Override
    protected void onStart() {
        super.onStart();

        initializePlayer();
    }


/*
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState!=null)
        {
            mCurrentPosition=savedInstanceState.getInt(PLAYBACK_TIME);
        }
    }
*/

    //    This method takes a String file name  and returns the URI of the  videp file stored in raw folder.
    private Uri getMedia(String mediaName) {

        if(URLUtil.isValidUrl(mediaName))
        {
            // media name is an external URL
            return Uri.parse(mediaName);
        }
        else {
            return Uri.parse("android.resource://" + getPackageName() +
                    "/raw/" + mediaName);
        }
    }


    private void initializePlayer() {

        Uri videoUri = getMedia(VIDEO_SAMPLE);
        mVideoView.setVideoURI(videoUri);


        // set Video frame where it was left
        startVideoFrom();
        mVideoView.start();

    }

    private void startVideoFrom() {
        if (mCurrentPosition > 0) {
            mVideoView.seekTo(mCurrentPosition);
        } else {
            // Skipping to 1 shows the first frame of the video.
            mVideoView.seekTo(1);
        }
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


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PLAYBACK_TIME,mVideoView.getCurrentPosition());

    }


    @Override
    public void onCompletion(MediaPlayer mp) {

        mVideoView.seekTo(1);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
