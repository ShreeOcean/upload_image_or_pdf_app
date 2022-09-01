package com.ocean.uploadpdfimageapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.ocean.uploadpdfimageapp.databinding.ActivityMainBinding;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private static final int REQUEST_GALLERY_CODE = 201;
    Uri uri_single, uri_multi;
    String imageFilePath_single = null,
            imageFilePath_multi = null,
            uriString;
    Bitmap bitmap_single_file, bitmap_multi_file;
    byte[] bytes_single_file, bytes_multi_file;
    int SELECT_IMAGE = 101;
    int SELECT_PDF = 103;
    int SELECT_PDF_FOR_ARRAY = 105;
    int SELECT_IMAGE_FOR_ARRAY= 107;
    boolean status_single_file = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =  ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnChooseSingleFile.setVisibility(View.VISIBLE);
        binding.btnChooseSingleFile.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                                Manifest.permission
                                        .READ_EXTERNAL_STORAGE},
                        1);
            }else {
                status_single_file = true;
                startDialog();
            }
        });
    }

    private void startDialog() {
        final CharSequence[] items = {"Choose PDF", "Choose Image", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Add PDF/IMAGE!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals("Choose PDF")){

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("application/pdf");
                    startActivityForResult(intent, SELECT_PDF);

                }else if (items[which].equals("Choose Image")){

                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE);

                }else if (items[which].equals("Cancel")){
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (status_single_file == true) {
            if (resultCode == Activity.RESULT_OK && requestCode == SELECT_IMAGE) {
                //TODO code to get selected image for single time from storage
                Log.d("MainActivity", "onActivityResult: SELECT_FILE_IMAGE -->" + data.toString());
            } else if (resultCode == Activity.RESULT_OK && requestCode == SELECT_PDF) {
                //TODO code to get selected pdf for single time from storage
                Log.d("MainActivity", "onActivityResult: SELECT_FILE_IMAGE -->" + data.toString());
            }
            status_single_file = false;
        } else {
            if (resultCode == Activity.RESULT_OK && requestCode == SELECT_IMAGE_FOR_ARRAY){
                //TODO code to get selected image for multiple selection or list of selection  from storage
                Log.d("MainActivity", "onActivityResult: SELECT_FILE_IMAGE --> " + data.toString());
            } else if (resultCode == Activity.RESULT_OK && requestCode == SELECT_PDF_FOR_ARRAY){
                //TODO code to get selected pdf for multiple selection or list of selection  from storage
                Log.d("MainActivity", "onActivityResult: SELECT_FILE_IMAGE --> " + data.toString());
            }
            status_single_file = true;
        }
    }

    @NonNull
    private byte[] getBytes(@NonNull InputStream inputStream) throws IOException {

        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
}