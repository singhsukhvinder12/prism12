package com.e.seasianoticeboard.view.core.newsfeed.camera;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.opengl.GLException;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.daasuu.camerarecorder.CameraRecordListener;
import com.daasuu.camerarecorder.CameraRecorder;
import com.daasuu.camerarecorder.CameraRecorderBuilder;
import com.daasuu.camerarecorder.LensFacing;
import com.e.seasianoticeboard.R;
import com.e.seasianoticeboard.camera.VideoTrimActivity;
import com.e.seasianoticeboard.camera.audio.UtilKotlin;
import com.e.seasianoticeboard.views.institute.newsfeed.AddPostActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.IntBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by sudamasayuki2 on 2018/07/02.
 */

public class BaseCameraActivity extends AppCompatActivity {

    private SampleGLView sampleGLView;
    protected CameraRecorder cameraRecorder;
    private String filepath;
    private TextView recordBtn;
    private ImageView cameraRecord;
    protected LensFacing lensFacing = LensFacing.BACK;
    protected int cameraWidth = 1280;
    protected int cameraHeight = 720;
    protected int videoWidth = 720;
    protected int videoHeight = 720;
    private AlertDialog filterDialog;
    private boolean toggleClick = false;
    private TextView mTIme;
    private CountDownTimer countDownTimer;
    private long milisecont;
    private ImageView mGallary;
    private final int REQUEST_TAKE_GALLERY_VIDEO = 1000;

    private String onBackPress = "0";
    private ImageView front_camera;


    protected void onCreateActivity() {
        //getSupportActionBar().hide();
        recordBtn = findViewById(R.id.btn_record);
        front_camera = findViewById(R.id.front_camera);
        cameraRecord = findViewById(R.id.camera_record);
        mGallary = findViewById(R.id.ic_gallary);
        cameraRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTIme = findViewById(R.id.time);

                if (recordBtn.getText().equals(getString(R.string.app_record))) {
                    try {
                        filepath = getVideoFilePath();
                        cameraRecorder.start(filepath);
                        recordBtn.setText("Stop");
                        mGallary.setVisibility(View.GONE);
                        front_camera.setVisibility(View.GONE);
                        cameraRecord.setImageResource(R.drawable.ic_camera_open_red);
                        countDownTimer = new CountDownTimer(60000, 1000) {
                            public void onTick(long millisUntilFinished) {
                                milisecont = millisUntilFinished / 1000;
                                mTIme.setText("00:" + millisUntilFinished / 1000);
                            }

                            public void onFinish() {
                                try {
                                    onBackPress="1";
                                    mTIme.setText("00:" + milisecont);

                                    if (cameraRecorder!=null){

                                        cameraRecorder.stop();
                                    }
//                                    Intent intent = getIntent();
//                                    intent.putExtra("filePath", filepath);
//                                    intent.putExtra("onBackPress",onBackPress);
//                                    setResult(RESULT_OK, intent);
                                    onBackPressed();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    try {
                        cameraRecorder.stop();
                        countDownTimer.cancel();
                        countDownTimer.onFinish();
                        recordBtn.setText(getString(R.string.app_record));

                        Intent intent = getIntent();
                        intent.putExtra("filePath", filepath);
                        intent.putExtra("onBackPress",onBackPress);
                        setResult(RESULT_OK, intent);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        findViewById(R.id.btn_flash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cameraRecorder != null && cameraRecorder.isFlashSupport()) {
                    cameraRecorder.switchFlashMode();
                    cameraRecorder.changeAutoFocus();
                }
            }
        });

        findViewById(R.id.front_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                releaseCamera();
                if (lensFacing == LensFacing.BACK) {
                    lensFacing = LensFacing.FRONT;
                } else {
                    lensFacing = LensFacing.BACK;
                }
                toggleClick = true;
            }
        });

        findViewById(R.id.btn_filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filterDialog == null) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("Choose a filter");
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            filterDialog = null;
                        }
                    });

                    final Filters[] filters = Filters.values();
                    CharSequence[] charList = new CharSequence[filters.length];
                    for (int i = 0, n = filters.length; i < n; i++) {
                        charList[i] = filters[i].name();
                    }
                    builder.setItems(charList, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            BaseCameraActivity.this.changeFilter(filters[item]);
                        }
                    });
                    filterDialog = builder.show();
                } else {
                    filterDialog.dismiss();
                }

            }
        });

        findViewById(R.id.btn_image_capture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseCameraActivity.this.captureBitmap(new BitmapReadyCallbacks() {
                    @Override
                    public void onBitmapReady(final Bitmap bitmap) {
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                String imagePath = getImageFilePath();
                                BaseCameraActivity.this.saveAsPngImage(bitmap, imagePath);
                                exportPngToGallery(BaseCameraActivity.this.getApplicationContext(), imagePath);
                            }
                        });
                    }
                });
            }
        });


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        mGallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_PICK);
                BaseCameraActivity.this.startActivityForResult(Intent.createChooser(intent, "Select Video"), REQUEST_TAKE_GALLERY_VIDEO);

//            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
//            startActivityForResult(i, SELECT_VIDEO);


            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpCamera();
//        try {
//            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
//
//        } catch (Exception e ) {
//
//        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        releaseCamera();
    }

    private void releaseCamera() {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (sampleGLView != null) {
                        sampleGLView.onPause();
                    }

                    if (cameraRecorder != null) {
                        cameraRecorder.stop();
                        cameraRecorder.release();
                        cameraRecorder = null;
                    }

                    if (sampleGLView != null) {
                        ((FrameLayout) findViewById(R.id.wrap_view)).removeView(sampleGLView);
                        sampleGLView = null;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void setUpCameraView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                FrameLayout frameLayout = BaseCameraActivity.this.findViewById(R.id.wrap_view);
                frameLayout.removeAllViews();
                sampleGLView = null;
                sampleGLView = new SampleGLView(BaseCameraActivity.this.getApplicationContext());
                sampleGLView.setTouchListener(new SampleGLView.TouchListener() {
                    @Override
                    public void onTouch(MotionEvent event, int width, int height) {
                        if (cameraRecorder == null) return;
                        cameraRecorder.changeManualFocusPoint(event.getX(), event.getY(), width, height);
                    }
                });
                frameLayout.addView(sampleGLView);
            }
        });
    }


    private void setUpCamera() {
        setUpCameraView();

        cameraRecorder = new CameraRecorderBuilder(this, sampleGLView)
                //.recordNoFilter(true)
                .cameraRecordListener(new CameraRecordListener() {
                    @Override
                    public void onGetFlashSupport(final boolean flashSupport) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                findViewById(R.id.btn_flash).setEnabled(flashSupport);
                            }
                        });
                    }

                    @Override
                    public void onRecordComplete() {
                        exportMp4ToGallery(getApplicationContext(), filepath);
                    }

                    @Override
                    public void onRecordStart() {

                    }

                    @Override
                    public void onError(Exception exception) {
                        Log.e("CameraRecorder", exception.toString());
                    }

                    @Override
                    public void onCameraThreadFinish() {
                        if (toggleClick) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    setUpCamera();
                                }
                            });
                        }
                        toggleClick = false;
                    }
                })
                .videoSize(videoWidth, videoHeight)
                .cameraSize(cameraWidth, cameraHeight)
                .lensFacing(lensFacing)
                .build();

    }



    private void changeFilter(Filters filters) {
        cameraRecorder.setFilter(Filters.getFilterInstance(filters, getApplicationContext()));
    }

    private interface BitmapReadyCallbacks {
        void onBitmapReady(Bitmap bitmap);
    }

    private void captureBitmap(final BitmapReadyCallbacks bitmapReadyCallbacks) {
        sampleGLView.queueEvent(new Runnable() {
            @Override
            public void run() {
                EGL10 egl = (EGL10) EGLContext.getEGL();
                GL10 gl = (GL10) egl.eglGetCurrentContext().getGL();
                final Bitmap snapshotBitmap = BaseCameraActivity.this.createBitmapFromGLSurface(sampleGLView.getMeasuredWidth(), sampleGLView.getMeasuredHeight(), gl);

                BaseCameraActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bitmapReadyCallbacks.onBitmapReady(snapshotBitmap);
                    }
                });
            }
        });
    }

    private Bitmap createBitmapFromGLSurface(int w, int h, GL10 gl) {

        int bitmapBuffer[] = new int[w * h];
        int bitmapSource[] = new int[w * h];
        IntBuffer intBuffer = IntBuffer.wrap(bitmapBuffer);
        intBuffer.position(0);

        try {
            gl.glReadPixels(0, 0, w, h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, intBuffer);
            int offset1, offset2, texturePixel, blue, red, pixel;
            for (int i = 0; i < h; i++) {
                offset1 = i * w;
                offset2 = (h - i - 1) * w;
                for (int j = 0; j < w; j++) {
                    texturePixel = bitmapBuffer[offset1 + j];
                    blue = (texturePixel >> 16) & 0xff;
                    red = (texturePixel << 16) & 0x00ff0000;
                    pixel = (texturePixel & 0xff00ff00) | red | blue;
                    bitmapSource[offset2 + j] = pixel;
                }
            }
        } catch (GLException e) {
            Log.e("CreateBitmap", "createBitmapFromGLSurface: " + e.getMessage(), e);
            return null;
        }

        return Bitmap.createBitmap(bitmapSource, w, h, Bitmap.Config.ARGB_8888);
    }

    public void saveAsPngImage(Bitmap bitmap, String filePath) {
        try {
            File file = new File(filePath);
            FileOutputStream outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void exportMp4ToGallery(Context context, String filePath) {
        final ContentValues values = new ContentValues(2);
        values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
        values.put(MediaStore.Video.Media.DATA, filePath);
        context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.parse("file://" + filePath)));
    }

    public static String getVideoFilePath() {
        return getAndroidMoviesFolder().getAbsolutePath() + "/" + new SimpleDateFormat("yyyyMM_dd-HHmmss").format(new Date()) + "cameraRecorder1.mp4";
    }

    public static File getAndroidMoviesFolder() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    }

    private static void exportPngToGallery(Context context, String filePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(filePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    public static String getImageFilePath() {

        return getAndroidImageFolder().getAbsolutePath() + "/" + new SimpleDateFormat("yyyyMM_dd-HHmmss").format(new Date()) + "cameraRecorder.png";
    }

    public static File getAndroidImageFolder() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
                Uri selectedImageUri = data.getData();
                String selectedVideoPath = getAbsolutePath(this, data.getData());
                if (selectedVideoPath != null) {



                    try{
                        if (cameraRecorder != null) {
                            cameraRecorder.stop();
                            cameraRecorder.release();
                            cameraRecorder = null;
                        }

                        Intent intent = new Intent(this, VideoTrimActivity.class);
                        intent.putExtra("path", selectedVideoPath);
                        startActivityForResult(intent, 2);
                    }catch(Exception e){

                    }

                    //  finish();

//                    Intent intent = new Intent(this,
//                            VideoTrimActivity.class);
//                    intent.putExtra("path", selectedVideoPath);
//                    startActivity(intent);
                }
            } else if (requestCode == 2) {
                try {
                    if (data.getStringExtra("filePath") != null) {
                        //Toast.makeText(this,""+data.getStringExtra("filePath"),Toast.LENGTH_LONG).show();
                        filepath = data.getStringExtra("filePath");
                        onBackPress="1";
                        onBackPressed();

                    }
                } catch (Exception e) {

                }
            }
        }
    }

    @Override
    public void onBackPressed() {

        Intent intent = getIntent();
        intent.putExtra("filePath", filepath);
        intent.putExtra("onBackPress",onBackPress);
        setResult(RESULT_OK, intent);
        finish();


    }

    public static String getAbsolutePath(Context activity, Uri uri) {

        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;
            try {
                cursor = activity.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
                e.printStackTrace();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

}
