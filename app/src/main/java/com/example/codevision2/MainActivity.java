package com.example.codevision2;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.codevision2.api.Repository;
import com.example.codevision2.databinding.ActivityMainBinding;
import com.example.codevision2.helper.CameraHelper;
import com.example.codevision2.helper.StorageHelper;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private StorageReference storageReference;
    private StorageHelper storageHelper;

    private CameraHelper cam;

    private Repository repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        setStatusbar();
        storageReference = FirebaseStorage.getInstance().getReference();
        storageHelper = new StorageHelper(this, storageReference);
        cam = new CameraHelper(this);
        repo = new Repository();

        binding.btnCapture.setOnClickListener( v -> {
            //capture();
            cam.captureRequest();
        });
        binding.btnCompile.setOnClickListener( v -> {
            String code = binding.etCode.getText().toString();
            repo.submitCode(code, data -> {
                try {
                    binding.tvOutput.setText(data.getOutput());
                }catch (Exception ex){
                    binding.tvOutput.setText("\"Request exceeds for the day..\"");
                }
            });
        });
    }

    private void setStatusbar(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.primary_button)); // Set the status bar color to black
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CameraHelper.REQUEST_PERMISSION_CAMERA){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                cam.takeImageRequest();
            }else {
                Toast.makeText(this, "Camera permission is required", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CameraHelper.REQUEST_CODE_CAPTURE){
            if (resultCode == Activity.RESULT_OK) {
                File f = new File(cam.currentPhotoPath);
                Log.i("myTag", "absolute file: " + Uri.fromFile(f));
                //binding.iv.setImageURI(Uri.fromFile(f));

                storageHelper.addPicToGallery(f);
                storageHelper.uploadImageToFirebase(f.getName(), Uri.fromFile(f), new StorageHelper.Callback() {
                    @Override
                    public void onUploadSuccess(String url) {
                        Log.i("myTag", "i got the uri: "+ url);
                        repo.getTextFromImage(url, new Repository.RepoCallback<String>() {
                            @Override
                            public void onSuccess(String data) {
                                binding.etCode.setText(data);
                            }
                        });
                    }
                });

            } else {
                // Handle the case where the result was not OK
                Toast.makeText(this, "Failed to take picture", Toast.LENGTH_SHORT).show();
            }
        }
    }
}