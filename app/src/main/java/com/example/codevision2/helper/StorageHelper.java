package com.example.codevision2.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.codevision2.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class StorageHelper {

    private final Activity activity;
    private final Context context;
    private final StorageReference storageReference;

    public StorageHelper(Activity activity, StorageReference storageReference){
        this.activity = activity;
        this.storageReference = storageReference;
        context = activity;
    }

    public void addPicToGallery(File f){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        activity.sendBroadcast(mediaScanIntent);
    }

    public void uploadImageToFirebase(String fileName, Uri uri){
        Log.i("myTag", "uploading image");
        StorageReference image = storageReference.child("images/" + fileName);
        image.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.i("myTag", "image url: " + uri);
                    }
                });
                Toast.makeText(context, "uploaded successfully",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("myTag", "upload failed");
            }
        });
    }
}
