package com.example.android.datafrominternet.HaveFun;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.android.datafrominternet.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ucla on 2018/3/18.
 */

class BitmapUtils {

    private static final String FILE_PROVIDER_AUTHORITY = "com.example.android.datafrominternet";

    //获取屏幕的像素 截取图片
    static Bitmap resamplePic(Context context, String imagePath) {

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(metrics);

        int targetH = metrics.heightPixels;
        int targetW = metrics.widthPixels;

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeFile(imagePath);
    }

    //在缓存目录中创建临时图片文件
    static File createTempImageFile(Context context) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalCacheDir();

        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    //分享图片
    static void shareImage(Context context, String imagePath) {
        File imageFile = new File(imagePath);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        Uri photoURI = FileProvider.getUriForFile(context, FILE_PROVIDER_AUTHORITY, imageFile);
        shareIntent.putExtra(Intent.EXTRA_STREAM, photoURI);
        context.startActivity(shareIntent);
    }

}
