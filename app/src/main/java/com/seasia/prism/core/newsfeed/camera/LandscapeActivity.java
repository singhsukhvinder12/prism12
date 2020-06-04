package com.seasia.prism.core.newsfeed.camera;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.seasia.prism.R;
import com.seasia.prism.core.newsfeed.camera.BaseCameraActivity;


public class LandscapeActivity extends BaseCameraActivity {

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, LandscapeActivity.class);
        activity.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landscape);
        onCreateActivity();
        videoWidth = 1280;
        videoHeight = 720;
        cameraWidth = 1280;
        cameraHeight = 720;
    }
}
