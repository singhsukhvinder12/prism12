package com.e.seasianoticeboard.view.core.newsfeed.audio;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.e.seasianoticeboard.R;
import com.e.seasianoticeboard.views.institute.newsfeed.AddPostActivity;

import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class RecordAudioActivity extends AppCompatActivity {

    String AudioSavePathInDevice = null;
    CountDownTimer countDownTimer;
    MediaRecorder mediaRecorder;
    TextView tvTime;
    ImageView recordAudio, btnClose, btnRestart, btnPlay, ivCheck, ivClear, gifImage;
    Random random;
    boolean startRecording = false;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    public static final int RequestPermissionCode = 1;
    private long milisecont;
    private boolean timerStart = false;
    MediaPlayer mp;
    String playStatus="0",pause="0";
    boolean ispause=false;
    long audioTime=60000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_audio);
        btnPlay = findViewById(R.id.play);
        recordAudio = findViewById(R.id.recordAudio);
        gifImage = findViewById(R.id.gif_image_view);

        ivCheck = findViewById(R.id.ivCheck);
        ivClear = findViewById(R.id.ivClear);
        btnRestart = findViewById(R.id.restart);
        tvTime = (TextView) findViewById(R.id.timer);
        random = new Random();
        Glide.with(this)
                .load(getDrawable(R.drawable.audio_player_gif_image))
                .into(gifImage);

        recordAudio.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                if (timerStart == true) {
                    if (mp != null) {
                        mp.stop();
                    }
                }

                startResording();


            }
        });
//
//
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              if(playStatus.equals("0")) {
                  playStatus="1";
                  btnPlay.setBackgroundResource(R.drawable.ic_pause);

                  mp = new MediaPlayer();
                  try {
                      mp.setDataSource(AudioSavePathInDevice);//Write your location here
                      mp.prepare();
                      // mp.start();
                      playSound(mp);

                  } catch (Exception e) {
                      e.printStackTrace();
                  }
              }else {
                  if(pause.equals("0")){
                      mp.pause();
                      pause="1";
                      btnPlay.setBackgroundResource(R.drawable.aar_ic_play);
                  } else {
                      playSound(mp);
                      pause="0";
                      btnPlay.setBackgroundResource(R.drawable.ic_pause);
                  }
              }
                }
        });

        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timerStart == true) {
                    if (mp != null) {
                        mp.stop();
                    }
                }
                playStatus="0";
                pause="0";
                AudioSavePathInDevice = "";
                tvTime.setText("00:00:00");
                startRecording = false;
                startResording();
            }
        });

        ivClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mp != null) {
                    mp.stop();
                }
                finish();
            }
        });

        ivCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mp != null) {
                    mp.stop();
                }
                setResult(RESULT_OK, getIntent().putExtra("data", AudioSavePathInDevice));
                finish();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mp != null) {
            mp.pause();
            pause="1";
            btnPlay.setBackgroundResource(R.drawable.aar_ic_play);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mp != null) {
            mp.stop();
        }
    }

    private void playSound(MediaPlayer player) {
        if (player != null) {
            if (timerStart == false) {
                player.start();
                timerStart = true;
                Timer timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (player != null && player.isPlaying()) {
                                    tvTime.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            tvTime.setText("00:00:" + player.getCurrentPosition() / 1000);
                                        }
                                    });
                                } else {
                                    btnPlay.setBackgroundResource(R.drawable.aar_ic_play);
                                    timer.cancel();
                                    timer.purge();
                                    timerStart = false;
//                                    if (mp != null) {
//                                        mp.stop();
//                                    }
                                 //   pause="0";
                                }
                            }
                        });
                    }
                }, 0, 1000);
            }
        }
    }

    private void startResording() {


        if (startRecording == false) {
            if (checkPermission()) {

                AudioSavePathInDevice =
                        Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                                CreateRandomAudioFileName(5) + "AudioRecording.3gp";

                MediaRecorderReady();
                try {
                    recordAudio.setBackground(getDrawable(R.drawable.ic_pause));
                    //     recordAudio.setBackground(getDrawable(R.drawable.ic_stop_audio));
                    startRecording = true;
                    playStatus="0";
                    pause="0";
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                    ispause=true;
                    btnRestart.setVisibility(View.GONE);
                    btnPlay.setVisibility(View.GONE);
                    ivCheck.setVisibility(View.GONE);
                    ivClear.setVisibility(View.GONE);
                    //   btnDone.setVisibility(View.GONE);
                    countDownTimer = new CountDownTimer(60900, 1000) {
                        public void onTick(long millisUntilFinished) {
                            milisecont = millisUntilFinished / 1000;
                            tvTime.setText("00:00:" + millisUntilFinished / 1000);
                        }

                        public void onFinish() {
                            startRecording = false;
                            tvTime.setText("00:00:" + milisecont);
                            mediaRecorder.stop();
                            //   btnDone.setVisibility(View.VISIBLE);
                            recordAudio.setBackground(getDrawable(R.drawable.aar_ic_rec));
                            btnRestart.setVisibility(View.VISIBLE);
                            btnPlay.setVisibility(View.VISIBLE);
                            ivCheck.setVisibility(View.VISIBLE);
                            ivClear.setVisibility(View.VISIBLE);
                            //  recordAudio.setBackground(getDrawable(R.drawable.ic_pause));
                        }
                    }.start();

                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                requestPermission();
            }

        } else {
            recordAudio.setBackground(getDrawable(R.drawable.aar_ic_rec));
            countDownTimer.cancel();
            countDownTimer.onFinish();
            startRecording = false;

        }
    }

    public void MediaRecorderReady() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }

    public String CreateRandomAudioFileName(int string) {
        StringBuilder stringBuilder = new StringBuilder(string);
        int i = 0;
        while (i < string) {
            stringBuilder.append(RandomAudioFileName.
                    charAt(random.nextInt(RandomAudioFileName.length())));

            i++;
        }
        return stringBuilder.toString();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(RecordAudioActivity.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        } catch (Exception e) {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(RecordAudioActivity.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(RecordAudioActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }
}