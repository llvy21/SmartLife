package com.example.android.datafrominternet.HaveFun;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.datafrominternet.R;

import java.io.File;
import java.io.IOException;

/**
 * Created by ucla on 2018/3/18.
 */

public class MainInterface extends AppCompatActivity{

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_STORAGE_PERMISSION = 1;
    private static final String FILE_PROVIDER_AUTHORITY = "com.example.android.fileprovider";

    private ImageView imageView;
    private Button camera, emojify;

    private String mTempPhotoPath;

    private Bitmap mResultsBitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interface);

        imageView = (ImageView) findViewById(R.id.interface_imageView);
        camera = (Button) findViewById(R.id.interface_btn_camera);
        emojify = (Button) findViewById(R.id.interface_btn_emojify);


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_STORAGE_PERMISSION);
        } else launchCamera();

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCamera();
            }
        });

        emojify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mResultsBitmap = BitmapUtils.resamplePic(MainInterface.this, mTempPhotoPath);

                mResultsBitmap = Emojify.detectFaces(MainInterface.this, mResultsBitmap);

                imageView.setImageBitmap(mResultsBitmap);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // Called when you request permission to read and write to external storage
        switch (requestCode) {
            case REQUEST_STORAGE_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // If you get permission, launch the camera
                    launchCamera();
                } else {
                    // If you do not get permission, show a Toast
                    Toast.makeText(this, "获取权限失败", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            processAndSetImage();
        }
    }



    private void launchCamera(){

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = BitmapUtils.createTempImageFile(this);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {

                mTempPhotoPath = photoFile.getAbsolutePath();

                Uri photoURI = FileProvider.getUriForFile(this, FILE_PROVIDER_AUTHORITY, photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

            }
        }

    }

    private void processAndSetImage() {

        mResultsBitmap = BitmapUtils.resamplePic(this, mTempPhotoPath);

        Emojify.detectFaces(this, mResultsBitmap);

        imageView.setImageBitmap(mResultsBitmap);
    }



}
