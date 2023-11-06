package com.example.foodorderapp.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.foodorderapp.R;

public class GlideUtils {
    public static void loadUrl(String url, ImageView imageView) {
        if (StringUtil.isEmpty(url)) {
            imageView.setImageResource(R.drawable.image_no_available);
            return;
        }
        Glide.with(imageView.getContext())
                .load(url)
                .error(R.drawable.image_no_available)
                .dontAnimate()
                .into(imageView);
    }
}