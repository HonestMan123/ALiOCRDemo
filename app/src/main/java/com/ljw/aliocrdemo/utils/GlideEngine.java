package com.ljw.aliocrdemo.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.zhihu.matisse.engine.ImageEngine;

/**
 * @author Android(JiaWei)
 * @date 2017/10/20.
 */

public class GlideEngine implements ImageEngine {

    @Override
    public void loadThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView, Uri uri) {
        // 加载图片
        Glide.with(context)
                .asBitmap()
                .load(uri)
                .apply(new RequestOptions().placeholder(placeholder).override(resize, resize).centerCrop())
                .transition(new BitmapTransitionOptions().crossFade(300))
                .into(imageView);
    }

    @Override
    public void loadAnimatedGifThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView, Uri uri) {
        Glide.with(context)
                .asBitmap()
                .load(uri)
                .apply(new RequestOptions().placeholder(placeholder).override(resize, resize).centerCrop())
                .transition(new BitmapTransitionOptions().crossFade(300))
                .into(imageView);
    }

    @Override
    public void loadImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {
        Glide.with(context)
                .load(uri)
                .apply(new RequestOptions().priority(Priority.HIGH).override(resizeX, resizeY).centerCrop())
                .transition(new DrawableTransitionOptions().crossFade(300))
                .into(imageView);
    }

    @Override
    public void loadAnimatedGifImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {
        Glide.with(context)
                .asGif()
                .load(uri)
                .apply(new RequestOptions().priority(Priority.HIGH).override(resizeX, resizeY).centerCrop())
                .into(imageView);
    }

    @Override
    public boolean supportAnimatedGif() {
        return true;
    }
}