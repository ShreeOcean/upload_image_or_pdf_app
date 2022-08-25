package com.ocean.uploadpdfimageapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import com.ocean.uploadpdfimageapp.databinding.ActivityMainBinding;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private static final int REQUEST_GALLERY_CODE = 201;
    Uri pickerInitialUri;
    String imageFilePath, pdfFilePath;
    Bitmap bitmap;
    int SELECT_IMAGE = 101;
    int SELECT_PDF = 103;
    int SELECT_PDF_FOR_ARRAY = 105;
    int SELECT_IMAGE_FOR_ARRAY= 107;

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

        if (resultCode == Activity.RESULT_OK && requestCode == SELECT_IMAGE) {

            //TODO code to get selected image for single time from storage
            Log.d("MainActivity", "onActivityResult: SELECT_FILE_IMAGE -->" + data.toString());

            pickerInitialUri = data.getData();
            convertToString(pickerInitialUri);
            binding.tvImageName1.setText(pickerInitialUri.getLastPathSegment());

        }
        if (resultCode == Activity.RESULT_OK && requestCode == SELECT_PDF) {

            //TODO code to get selected pdf for single time from storage
            Log.d("MainActivity", "onActivityResult: SELECT_FILE_IMAGE -->" + data.toString());

        }

        if (resultCode == Activity.RESULT_OK && requestCode == SELECT_IMAGE_FOR_ARRAY){
            //TODO code to get selected image for multiple selection or list of selection  from storage
            onSelectImageForExpensesInfo(data);

            Log.d("MainActivity", "onActivityResult: SELECT_FILE_IMAGE --> " + data.toString());

        }
        if (resultCode == Activity.RESULT_OK && requestCode == SELECT_PDF_FOR_ARRAY){
            //TODO code to get selected pdf for multiple selection or list of selection  from storage
            onSelectedPdfForExpensesInfo(data);

            Log.d("MainActivity", "onActivityResult: SELECT_FILE_IMAGE --> " + data.toString());
        }
    }

    private void convertToString(Uri pickerInitialUri) {


    }

    private void onSelectedPdfForExpensesInfo(@NonNull Intent data) {

        Log.d("MainActivity", "onSelectedPdfForExpensesInfo: " + data.getData());
        System.out.println("onSelectedPdfForExpensesInfo: " + data.getData().getPath());

        Uri uri = data.getData();
        String uriString = uri.toString();
        File file = new File(uriString);
        pdfFilePath = file.getAbsolutePath();
        //TODO: code for coverting file to base64
        binding.tvImageName1.setText(file.getName());
        Log.d("MainActivity", "onSelectedPdfForExpensesInfo: " + pdfFilePath);

    }

    private void onSelectImageForExpensesInfo(@NonNull Intent data) {


    }


}