package com.example.codevision2.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraHelper {

    private Activity activity;
    private Context context;
    public static final int REQUEST_CODE_CAPTURE = 101;
    public static final int REQUEST_PERMISSION_CAMERA = 102;
    public static String currentPhotoPath;
    public Uri imageUri;
    public File imageFile;

    public CameraHelper(Activity activity){
        this.activity = activity;
        this.context = (Context) activity;
    }

    public void captureRequest() {
        if(ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity, new String[] {android.Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA);
        }else{
            takeImageRequest();
        }
    }

    public void takeImageRequest(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(activity.getPackageManager()) != null){
            File photoFile = null;
            try {
                photoFile = createImageFile();
            }catch (IOException ex){
                Log.e("myTag", ex.getMessage());
            }

            if(photoFile != null){
                Uri photoUri = FileProvider.getUriForFile(context,
                        activity.getApplicationContext().getPackageName() + ".fileprovider", // Use application ID
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                activity.startActivityForResult(takePictureIntent, REQUEST_CODE_CAPTURE);
            }
        }else{
        }
    }

    private File createImageFile() throws IOException {
        //Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        imageFile = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        currentPhotoPath = imageFile.getAbsolutePath();
        imageUri = Uri.fromFile(imageFile);

        return imageFile;
    }

}
