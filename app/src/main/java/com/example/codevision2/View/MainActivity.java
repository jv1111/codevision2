package com.example.codevision2.View;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.codevision2.ENV;
import com.example.codevision2.R;
import com.example.codevision2.api.Repository;
import com.example.codevision2.api.WebSocketCompiler;
import com.example.codevision2.api.model.CompilerModel;
import com.example.codevision2.databinding.ActivityMainBinding;
import com.example.codevision2.helper.AnimationUI;
import com.example.codevision2.helper.CameraHelper;
import com.example.codevision2.helper.StorageHelper;
import com.example.codevision2.helper.StringFormatter;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class MainActivity extends AppCompatActivity implements WebSocketCompiler.ICompiler {

    private ActivityMainBinding binding;
    private StorageReference storageReference;
    private StorageHelper storageHelper;
    private CameraHelper cam;
    private Repository repo;
    private AnimationUI anim;
    private static final int ORC_PART_PROGRESS = 10;
    private String outputStr;
    private String compiledCode;
    private String codeExplanation;
    private Boolean isInfoActive = false;
    private boolean isAnalyzeEnabled = false;

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
        WebSocketCompiler.connectWebSocket(this, this);
        buttonsFunction();
        analyzeViewHandler();
    }

    @Override
    public void onBackPressed() {
        if(isInfoActive) infoViewHandler(false);
        else super.onBackPressed();
    }

    private void analyzeViewHandler(){
        if(StringFormatter.hasValue(binding.etCode.getText().toString())) {
            isAnalyzeEnabled = true;
            binding.btnAnalyze.setBackgroundResource(R.drawable.round_button_20);
        }
        else {
            binding.btnAnalyze.setBackgroundResource(R.drawable.round_button_disabled_20);
            isAnalyzeEnabled = false;
        }
       binding.etCode.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(StringFormatter.hasValue(binding.etCode.getText().toString())) {
                    isAnalyzeEnabled = true;
                    binding.btnAnalyze.setBackgroundResource(R.drawable.round_button_20);
                }
                else {
                    binding.btnAnalyze.setBackgroundResource(R.drawable.round_button_disabled_20);
                    isAnalyzeEnabled = false;
                }
           }

           @Override
           public void afterTextChanged(Editable s) {

           }
       });
    }

    private void buttonsFunction(){
        anim.scaleDownRelativeLayoutOnTouchListener(binding.btnCapture, new AnimationUI.Callback() {
            @Override
            public void onRelease() {
                cam.captureRequest();
            }
        });

        anim.scaleDownRelativeLayoutOnTouchListener(binding.btnCompile, new AnimationUI.Callback() {
            @Override
            public void onRelease() {
                Log.i("myTag code: ", binding.etCode.getText().toString());
                outputStr = "";
                binding.tvOutput.setText("Compiling");
                anim.setLoadingRelativeLayout(true, binding.tvCompile, binding.pbCompile);
                repo.runAndCompile(binding.etCode.getText().toString(), new Repository.RepoCallback<String>() {
                    @Override
                    public void onSuccess(String data) {
                        compiledCode = data;
                        anim.setLoadingRelativeLayout(false, binding.tvCompile, binding.pbCompile);
                    }

                    @Override
                    public void onFailed(String errorMessage) {
                        Log.e("myTag error: ", errorMessage);
                        binding.tvOutput.setText(errorMessage);
                        anim.setLoadingRelativeLayout(false, binding.tvCompile, binding.pbCompile);
                    }
                });
            }
        });

        binding.btnSendInput.setOnClickListener(v -> {
            String input = String.valueOf(binding.etInput.getText());
            binding.etInput.setText("");
            Log.i("myTag btn: ", input);
            outputStr = outputStr + input;
            binding.tvOutput.setText(outputStr);
            WebSocketCompiler.sendMessage(input);
        });

        anim.scaleDownRelativeLayoutOnTouchListener(binding.btnHelp, new AnimationUI.Callback() {
            @Override
            public void onRelease() {
                binding.btnApplyChanges.setVisibility(View.GONE);
                repo.analyzeCode(compiledCode, Constant.AI_EXPLAIN_CODE, new Repository.RepoCallback<String>() {
                    @Override
                    public void onSuccess(String data) {
                        codeExplanation = data;
                        Log.i("myTag explanation", data);
                        binding.tvExplanation.setText(codeExplanation);
                        infoViewHandler(true);
                    }

                    @Override
                    public void onFailed(String errorMessage) {
                        Log.e("myTag analyze error: ", errorMessage);
                    }
                });
            }
        });

        anim.scaleDownRelativeLayoutOnTouchListener(binding.btnAnalyze, new AnimationUI.Callback() {
            @Override
            public void onRelease() {
                if(isAnalyzeEnabled){
                    repo.analyzeCode(binding.etCode.getText().toString(), Constant.AI_ANALYZE, new Repository.RepoCallback<String>() {
                        @Override
                        public void onSuccess(String data) {
                            Log.i("myTag", data);
                            codeExplanation = data;
                            binding.tvExplanation.setText(codeExplanation);
                            infoViewHandler(true);
                            binding.btnApplyChanges.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onFailed(String errorMessage) {
                            Log.e("myTag", errorMessage);
                        }
                    });
                }
            }
        });

        anim.scaleDownRelativeLayoutOnTouchListener(binding.btnApplyChanges, new AnimationUI.Callback() {
            @Override
            public void onRelease() {
                String extractedCode = StringFormatter.getTheCodeFromString(codeExplanation);
                binding.etCode.setText(extractedCode);
                infoViewHandler(false);
            }
        });
    }

    private void btnHelpViewHandler(boolean isShown){
        if(isShown){
            anim.setPopup(binding.btnHelp);
            binding.btnHelp.setVisibility(View.VISIBLE);
        }else{
            anim.scale_down(binding.btnHelp, () -> {
                binding.btnHelp.setVisibility(View.GONE);
            });
        }
    }

    private void infoViewHandler(Boolean isShown){
        isInfoActive = isShown;
        if(isShown) {
            anim.setAppearFromBottom(binding.layoutInfoExplanation);
            binding.layoutInfo.setVisibility(View.VISIBLE);
        }else{
            Log.i("myTag", "hidding");
            anim.setMoveToBottom(binding.layoutInfoExplanation, () -> {
                binding.layoutInfo.setVisibility(View.GONE);
            });
        }
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

    //TODO SET LOADING PROGRESS FOR EVERY BUTTON

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
                onCaptureHandler();
            }
            else {
                Toast.makeText(this, "Failed to take a picture", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void onCaptureHandler(){
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

    @Override
    public void onCodeRunOutput(String output, Boolean isEnded) {
        outputStr = outputStr + "\n" + output;
        binding.tvOutput.setText(outputStr);
        Log.i("myTag output: ", String.valueOf(isEnded));
        if(isEnded){
            btnHelpViewHandler(true);
        }else{
            btnHelpViewHandler(false);
        }
    }
}