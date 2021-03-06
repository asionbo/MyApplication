package com.example.asionbo.myapplication.utils;

import android.app.Activity;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;

import com.example.asionbo.myapplication.R;
import com.lzy.imagepicker.loader.ImageLoader;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by asionbo on 2018/4/19.
 */

public class PicassoImgaeLoader implements ImageLoader{
    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        if (TextUtils.isEmpty(path)){//默认图片
            Picasso.get()
                    .load(R.drawable.ic_add_white_24dp)
                    .placeholder(R.drawable.ic_add_white_24dp)
                    .error(R.drawable.ic_add_white_24dp)
//                .resize(width,height)
//                .centerInside()
                    .fit()
                    .centerInside()
                    .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                    .into(imageView);
        }else {
            Picasso.get()
                    .load(Uri.fromFile(new File(path)))
                    .placeholder(R.drawable.ic_add_white_24dp)
                    .error(R.drawable.ic_add_white_24dp)
//                .resize(width,height)
//                .centerInside()
                    .fit()
                    .centerInside()
                    .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                    .into(imageView);
        }
    }

    @Override
    public void displayImagePreview(Activity activity, String path, ImageView imageView, int width, int height) {
        Picasso.get()
                .load(Uri.fromFile(new File(path)))
                .resize(width,height)
                .centerInside()
                .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                .into(imageView);
    }

    @Override
    public void clearMemoryCache() {

    }

    public static void displayImageDefault(){

    }
}
