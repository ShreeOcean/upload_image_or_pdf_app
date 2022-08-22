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
    String uploaded_image_name, imageFilePath;
    Bitmap bitmap;
    int SELECT_FILE = 1;
    int SELECT_PDF_FILE = 3;
    int SELECT_PDF_FOR_EXPENSES_INFO = 5;
    int SELECT_IMAGE_FOR_EXPENSES_INFO= 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =  ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnChooseFileReportExpense.setVisibility(View.VISIBLE);
        binding.btnChooseFileReportExpense.setOnClickListener(v -> {
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
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == SELECT_FILE) {
//            onSelectFromGalleryResult(data);

        } else if (resultCode == Activity.RESULT_OK && requestCode == SELECT_PDF_FILE) {
//            selectPdfFromGallery(data);
        } //TODO: update field condition code for attachment in expenses-info
        else if (resultCode == Activity.RESULT_OK && requestCode == SELECT_IMAGE_FOR_EXPENSES_INFO){
            //TODO code to get selected image from storage
            onSelectImageForExpensesInfo(data);
        }else if (resultCode == Activity.RESULT_OK && requestCode == SELECT_PDF_FOR_EXPENSES_INFO){
            //TODO code to get selected pdf from storage
            onSelectedPdfForExpensesInfo(data);
        }
    }

    private void onSelectedPdfForExpensesInfo(@NonNull Intent data) {
        uri = data.getData();
        String uriString = uri.toString();
        File myFile = new File(uriString);
        String paths = myFile.getAbsolutePath();
        String displayName = null;

        if (uriString.startsWith("content://")) {
            Cursor cursor = null;
            try {

                cursor = getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    String fgd = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

                    /** as per update on 11aug22 showing chosen pdf or photo to show on textview */
                    tvImgNameReportExpense.setText(fgd);

                    String pdfoimg = fgd.substring(fgd.length() - 4);
                    imageFilePath = fgd;
                    try {
                        InputStream iStream = getContentResolver().openInputStream(uri);
                        byte[] inputData = getBytes(iStream);
                        DataPart dp = new DataPart(imageFilePath, inputData, "application/pdf");
                        dataPart.add(dp);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            } finally {
                cursor.close();
            }
        } else if (uriString.startsWith("file://")) {
            imageFilePath = myFile.getName();
            try {
                InputStream iStream = getContentResolver().openInputStream(uri);
                byte[] inputData = getBytes(iStream);
                DataPart dp = new DataPart(imageFilePath, inputData, "application/pdf");
                dataPart.add(dp);

            } catch (IOException e) {
                e.printStackTrace();
            }
            /** as per update on 11aug22 showing choosen pdf or photo to show on textview */
            tvImgNameReportExpense.setText(imageFilePath);


        }
    }

    private void onSelectImageForExpensesInfo(@NonNull Intent data) {
        uri = data.getData();
        uries.add(uri); //TODO: list of uri
        String uriString = uri.toString();
        File myFile = new File(uriString);
        try {
            /** checking the build version of device must be greater than equal to android jellybean */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                if (data.getClipData() != null) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    /** evaluate the count before the for loop --- otherwise, the count is evaluated every loop. */
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) { //TODO: why ?

                        Uri selectedImageUri = data.getClipData().getItemAt(i).getUri();

                        BitmapFactory.decodeStream(RepostExpensesActivity.this.getContentResolver().openInputStream(selectedImageUri), null, options);

                        options.inSampleSize = calculateInSampleSize(options, 1000, 1000);
                        options.inJustDecodeBounds = false;

                        bmp = BitmapFactory.decodeStream(RepostExpensesActivity.this.getContentResolver().openInputStream(selectedImageUri), null, options);


                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        imageBytes = baos.toByteArray();


                        Uri furi = getImageUri(getApplicationContext(), bmp);
                        //File finalFile = new File(getRealPathFromUri(furi));
                        File finalFile = FileUtils.getFile(getApplicationContext(), furi);
                        imageFilePath = finalFile.toString();
                        Log.d("imageFilePathForEI1", imageFilePath); //ExpensesInfo
                        /** as per update on 11aug22 showing choosen pdf or photo to show on textview */
                        tvImgNameReportExpense.setText(imageFilePath);

                        try {
                            InputStream iStream = getContentResolver().openInputStream(uri);//TODO: uri is used here
                            byte[] inputData = getBytes(iStream);
                            DataPart dp = new DataPart(imageFilePath, inputData, "image/jpeg");
                            dataPart.add(dp);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (data.getData() != null) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.

                    Uri selectedImageUri = data.getData();

                    BitmapFactory.decodeStream(RepostExpensesActivity.this.getContentResolver().openInputStream(selectedImageUri), null, options);

                    options.inSampleSize = calculateInSampleSize(options, 1000, 1000);
                    options.inJustDecodeBounds = false;

                    bmp = BitmapFactory.decodeStream(RepostExpensesActivity.this.getContentResolver().openInputStream(selectedImageUri), null, options);


                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    imageBytes = baos.toByteArray();


                    Uri furi = getImageUri(getApplicationContext(), bmp);
                    //File finalFile = new File(getRealPathFromUri(furi));
                    File finalFile = FileUtils.getFile(getApplicationContext(), furi);

                    imageFilePath = finalFile.toString();
                    /** as per update on 11aug22 showing choosen pdf or photo to show on textview */
                    tvImgNameReportExpense.setText(imageFilePath);
                    Log.d("imageFilePathForEI2", imageFilePath);
                    try {
                        InputStream iStream = getContentResolver().openInputStream(uri);
                        byte[] inputData = getBytes(iStream);
                        DataPart dp = new DataPart(imageFilePath, inputData, "image/jpeg");
                        dataPart.add(dp);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.v("tostring", e.toString());
        }
    }

    /** String method to covert bitmap to base64 */
    public static String convertBitmapToBase64(@NonNull Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    /** method to covert base64 to bitmap */
    public static Bitmap convertBase64ToBitmap(String base64){
        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return bitmap;
    }
}