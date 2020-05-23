package com.e.seasianoticeboard.view.core.newsfeed.displaynewsfeed.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.e.seasianoticeboard.R;
import com.e.seasianoticeboard.view.core.newsfeed.displaynewsfeed.view.ViewImageActivity;

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
        Glide.with(context).load(IMAGES.get(position)).placeholder(R.drawable.image_placeholder).error(R.drawable.image_placeholder).into(imageView);
        view.addView(imageLayout, 0);
        int count = position + 1;
        if(IMAGES.size()!=1){
            mCount.setText("" + count + "/" + IMAGES.size());
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ViewImageActivity.class);
                intent.putStringArrayListExtra("ImageList",IMAGES);
                intent.putExtra("position",""+position);
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