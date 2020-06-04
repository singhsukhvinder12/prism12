package com.seasia.prism.util;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.seasia.prism.R;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.AlbumLoader;

import java.io.File;

public class MediaLoader implements AlbumLoader {

    @Override
    public void load(ImageView imageView, AlbumFile albumFile) {
        load(imageView, albumFile.getPath());
    }

    @Override
    public void load(ImageView imageView, String url) {

            Glide.with(imageView.getContext())
                    .load(url)
                    .error(R.drawable.image_placeholder)
                    .placeholder(R.drawable.image_placeholder)
                    .into(imageView);
            imageView.setVisibility(View.VISIBLE);

    }

//    public void deleteFiles(String  path) {
//
//        try {
//            File file =new  File(path);
//
//            if (file.exists()) {
//                String deleteCmd = "rm -r " + path;
//                Runtime runtime = Runtime.getRuntime();
//                runtime.exec(deleteCmd);
//            }
//        } catch (Exception e) {
//        }
//    }

}
