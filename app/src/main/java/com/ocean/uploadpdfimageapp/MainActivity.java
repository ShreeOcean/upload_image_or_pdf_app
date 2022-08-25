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
    private static final int PICK_IMAGE_FILE = 1;
    private static final int PICK_PDF_FILE = 2;
    Uri pickerInitialUri;
    String uploaded_image_name, imageFilePath, pdfFilePath;
    Bitmap bitmap;
    int SELECT_FILE_IMAGE = 101;
    int SELECT_PDF_FILE = 103;
    int SELECT_PDF_FOR_EXPENSES_INFO = 105;
    int SELECT_IMAGE_FOR_EXPENSES_INFO= 107;

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
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("application/pdf");
                    /** Optionally, specify a URI for the file that should appear in the system file picker when it loads. */
                    intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, PICK_PDF_FILE);
                }else if (items[which].equals("Choose Image")){
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_APP_GALLERY);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                    startActivityForResult(intent, PICK_IMAGE_FILE);
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

        if (resultCode == Activity.RESULT_OK && requestCode == SELECT_FILE_IMAGE) {
//            onSelectFromGalleryResult(data);
            Log.d("MainActivity", "onActivityResult: SELECT_FILE_IMAGE -->" + data.toString());

        }
        if (resultCode == Activity.RESULT_OK && requestCode == SELECT_PDF_FILE) {
//            selectPdfFromGallery(data);

            Log.d("MainActivity", "onActivityResult: SELECT_FILE_IMAGE -->" + data.toString());

        }
        //TODO: update field condition code for attachment in expenses-info
        if (resultCode == Activity.RESULT_OK && requestCode == SELECT_IMAGE_FOR_EXPENSES_INFO){
            //TODO code to get selected image from storage
            onSelectImageForExpensesInfo(data);

            Log.d("MainActivity", "onActivityResult: SELECT_FILE_IMAGE --> " + data.toString());

        }
        if (resultCode == Activity.RESULT_OK && requestCode == SELECT_PDF_FOR_EXPENSES_INFO){
            //TODO code to get selected pdf from storage
            onSelectedPdfForExpensesInfo(data);

            Log.d("MainActivity", "onActivityResult: SELECT_FILE_IMAGE --> " + data.toString());
        }
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