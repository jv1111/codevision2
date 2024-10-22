package com.example.codevision2.View;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import com.example.codevision2.Constant;
import com.example.codevision2.R;
import com.example.codevision2.api.Repository;
import com.example.codevision2.databinding.ActivityMainBinding;
import com.example.codevision2.helper.AnimationUI;
import com.example.codevision2.helper.CameraHelper;
import com.example.codevision2.helper.StorageHelper;
import com.example.codevision2.helper.StringFormatter;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private StorageReference storageReference;
    private StorageHelper storageHelper;
    private CameraHelper cam;
    private Repository repo;
    private AnimationUI anim;
    private static final int ORC_PART_PROGRESS = 10;
    private Boolean isFirstCompileWithAI = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        setStatusbar();
        storageReference = FirebaseStorage.getInstance().getReference();
        storageHelper = new StorageHelper(this, storageReference);
        anim = new AnimationUI(this);
        cam = new CameraHelper(this);
        repo = new Repository();

        anim.scaleDownRelativeLayoutOnTouchListener(binding.btnCapture, new AnimationUI.Callback() {
            @Override
            public void onRelease() {
                cam.captureRequest();
            }
        });

        anim.scaleDownRelativeLayoutOnTouchListener(binding.btnCompile, new AnimationUI.Callback() {
            @Override
            public void onRelease() {
                binding.etCode.setText(Constant.codeSample);
                String code = Constant.AI_SCRIPT + binding.etCode.getText() + Constant.AI_CODE_TAIL;
                //Log.i("myTag my code", code);
                String sampleResult = Constant.resultSample;
                String responseResult = sampleResult;
                responseResult = StringFormatter.removeBackticks(responseResult);
                responseResult = StringFormatter.removeSCANNERTag(responseResult);
                Log.i("myTag sample result: ", responseResult);
                inputForScannerHandler(StringFormatter.containsScanner(sampleResult), responseResult);
                //compileWithAI(code);
                //TODO PREPARE FOR THE ACTUAL CONVO
            }
        });
    }

    private void inputForScannerHandler(Boolean isVisible, String title){
        if(isVisible){
            binding.tvScannerTitle.setText(title);
            binding.layoutScanner.setVisibility(View.VISIBLE);
        }else{
            binding.layoutScanner.setVisibility(View.GONE);
        }
    }

    private void compileWithAI(String code){
        anim.setLoadingRelativeLayout(true, binding.tvCompile, binding.pbCompile);
        binding.tvOutput.setText("Compiling");
        //TODO Detect a scanner
        repo.submitCodeWithAI(code, new Repository.RepoCallback<String>() {
            @Override
            public void onSuccess(String data) {
                Log.i("myTag response from ai", data);
                binding.tvOutput.setText(data);
                anim.setLoadingRelativeLayout(false, binding.tvCompile, binding.pbCompile);
            }

            @Override
            public void onFailed(String errorMessage) {
                Log.e("myTag error: ",errorMessage);
                binding.tvOutput.setText("Failed");
                anim.setLoadingRelativeLayout(false, binding.tvCompile, binding.pbCompile);
            }
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

    private void setConversionProgress(int uploadProgress, int orcPartProgress, String message){
        int orcTotalProgress = uploadProgress - orcPartProgress;
        binding.tvLoadingMessage.setText(message);
        String strProgress = "0%";
        if(orcTotalProgress != 100){
            if(binding.layoutLoading.getVisibility() != View.VISIBLE) binding.layoutLoading.setVisibility(View.VISIBLE);
            if(orcTotalProgress > 0) strProgress = orcTotalProgress + "%";
            binding.tvLoadingPercentage.setText(strProgress);
        }else {
            binding.layoutLoading.setVisibility(View.GONE);
        }
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
                storageHelper.addPicToGallery(f);
                storageHelper.uploadImageToFirebase(f.getName(), Uri.fromFile(f), new StorageHelper.Callback() {
                    @Override
                    public void onUploadSuccess(String url) {
                        setConversionProgress(100, ORC_PART_PROGRESS, "Converting the image to text");
                        repo.getTextFromImage(url, new Repository.RepoCallback<String>() {
                            @Override
                            public void onSuccess(String data) {
                                setConversionProgress(100,0, "Finished");
                                binding.etCode.setText(data);
                            }

                            @Override
                            public void onFailed(String errorMessage) {
                                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                                setConversionProgress(100,0, "Finished");
                            }
                        });
                    }
                    @Override
                    public void onProgressCallback(int progress) {
                        setConversionProgress(progress, ORC_PART_PROGRESS, "Preparing the image.");
                    }
                });
            }
            else {
                Toast.makeText(this, "Failed to take a picture", Toast.LENGTH_SHORT).show();
            }
        }
    }
}