package com.seasia.prism.newsfeed.displaynewsfeed.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.seasia.prism.R;
import com.seasia.prism.newsfeed.displaynewsfeed.view.ViewImageActivity;

import java.util.ArrayList;


public class SlidingImage_Adapter extends PagerAdapter {

    private ArrayList<String> IMAGES;
    private LayoutInflater inflater;
    private Context context;

    public SlidingImage_Adapter(Context context, ArrayList<String> IMAGES) {
        this.context = context;
        this.IMAGES = IMAGES;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);
        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
        final TextView mCount = imageLayout.findViewById(R.id.count);
        Glide.with(context)
                .load(IMAGES.get(position))
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                }).into(imageView);
        view.addView(imageLayout, 0);
        int count = position + 1;
        if (IMAGES.size() != 1) {
            mCount.setText("" + count + "/" + IMAGES.size());
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewImageActivity.class);
                intent.putStringArrayListExtra("ImageList", IMAGES);
                intent.putExtra("position", "" + position);
                context.startActivity(intent);
            }
        });

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

}