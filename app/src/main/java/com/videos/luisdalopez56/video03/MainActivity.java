package com.videos.luisdalopez56.video03;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private MediaRecorder mediaRecorder = null;
    private MediaPlayer mediaPlayer = null;
    private String fileName = null;
    private Button btnRec,btnStop, btnPlay;
    private boolean recording = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnRec = findViewById(R.id.btn_grabar);
        btnStop = findViewById(R.id.btn_parar);
        btnPlay = findViewById(R.id.btn_play);

        fileName = Environment.getExternalStorageDirectory() + "/test.mp4";

        SurfaceView surface = (SurfaceView)findViewById(R.id.videoView);
        SurfaceHolder holder = surface.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        btnRec.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                btnRec.setEnabled(false);
                btnStop.setEnabled(true);
                btnPlay.setEnabled(false);

                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);

                mediaRecorder.setOutputFile(fileName);
                try {
                    mediaRecorder.prepare();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaRecorder.start();
                recording = true;
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnRec.setEnabled(true);
                btnStop.setEnabled(false);
                btnPlay.setEnabled(true);

                if (recording) {
                    recording = false;
                    mediaRecorder.stop();
                    mediaRecorder.reset();

                } else if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                }
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnRec.setEnabled(false);
                btnStop.setEnabled(true);
                btnPlay.setEnabled(false);

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {

                        btnRec.setEnabled(true);
                        btnStop.setEnabled(false);
                        btnPlay.setEnabled(true);
                    }
                });

                try {
                    mediaPlayer.setDataSource(fileName);
                    mediaPlayer.prepare();
                } catch (IllegalStateException e) {
                } catch (IOException e) {
                }

                mediaPlayer.start();

            }
        });
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mediaRecorder == null) {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setPreviewDisplay(holder.getSurface());
        }

        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDisplay(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        mediaRecorder.release();
        mediaPlayer.release();
    }



}