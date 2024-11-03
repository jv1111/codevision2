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
    private boolean isScanner = false;

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
        setInputView(false);
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
        //TODO GENERATE A LOADING FUNCTION FOR BTN ENTER AND ENABLE AND DISABLE
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

    private void setInputView(boolean isEnabled){
        if(isEnabled){
            binding.btnSendInput.setBackgroundResource(R.drawable.round_button_20);
        }else{
            binding.btnSendInput.setBackgroundResource(R.drawable.round_button_disabled_20);
        }
        isScanner = isEnabled;
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
            if(isScanner){
                String input = String.valueOf(binding.etInput.getText());
                binding.etInput.setText("");
                Log.i("myTag btn: ", input);
                outputStr = outputStr + input;
                binding.tvOutput.setText(outputStr);
                WebSocketCompiler.sendMessage(input);
            }
        });

        anim.scaleDownRelativeLayoutOnTouchListener(binding.btnHelp, new AnimationUI.Callback() {
            @Override
            public void onRelease() {
                setLoadingProgress(0,0, "Loading", false);
                repo.analyzeCode(compiledCode, outputStr,Constant.AI_EXPLAIN_CODE, new Repository.RepoCallback<String>() {
                    @Override
                    public void onSuccess(String data) {
                        codeExplanation = data;
                        Log.i("myTag explanation", data);
                        binding.tvExplanation.setText(codeExplanation);
                        binding.tvExplanationTitle.setText(R.string.explanation_title);
                        binding.btnApplyChanges.setVisibility(View.GONE);
                        Log.i("myTag btnApply: ", "set to gone");
                        infoViewHandler(true);
                        setLoadingProgress(100,0, "Loading", false);
                    }

                    @Override
                    public void onFailed(String errorMessage) {
                        Log.e("myTag analyze error: ", errorMessage);
                        Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        setLoadingProgress(100,0, "Loading", false);
                    }
                });
            }
        });

        anim.scaleDownRelativeLayoutOnTouchListener(binding.btnAnalyze, new AnimationUI.Callback() {
            @Override
            public void onRelease() {
                setLoadingProgress(0, 0, "Loading", false);
                if(isAnalyzeEnabled){
                    repo.analyzeCode(binding.etCode.getText().toString(), null, Constant.AI_ANALYZE, new Repository.RepoCallback<String>() {
                        @Override
                        public void onSuccess(String data) {
                            Log.i("myTag", data);
                            codeExplanation = StringFormatter.formatSampleCode(data);
                            binding.tvExplanation.setText(codeExplanation);
                            binding.tvExplanationTitle.setText(R.string.explanation_suggestions);
                            binding.btnApplyChanges.setVisibility(View.VISIBLE);
                            Log.i("myTag btnApply: ", "set to visible");
                            infoViewHandler(true);
                            setLoadingProgress(100, 0, "Loading", false);
                        }

                        @Override
                        public void onFailed(String errorMessage) {
                            Log.e("myTag", errorMessage);
                            Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                            setLoadingProgress(100, 0, "Loading", false);
                        }
                    });
                }
            }
        });

        anim.scaleDownRelativeLayoutOnTouchListener(binding.btnApplyChanges, new AnimationUI.Callback() {
            @Override
            public void onRelease() {
                binding.etCode.setText(codeExplanation);
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

    private void setLoadingProgress(int uploadProgress, int orcPartProgress, String message, boolean isProgressVisible){
        int orcTotalProgress = uploadProgress - orcPartProgress;
        binding.tvLoadingMessage.setText(message);
        String strProgress = "0%";
        if(orcTotalProgress != 100){
            if(binding.layoutLoading.getVisibility() != View.VISIBLE) binding.layoutLoading.setVisibility(View.VISIBLE);
            if(orcTotalProgress > 0) strProgress = orcTotalProgress + "%";
            if(isProgressVisible) binding.tvLoadingPercentage.setText(strProgress);
            else binding.tvLoadingPercentage.setText("");
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
                setLoadingProgress(100, ORC_PART_PROGRESS, "Converting the image to text", true);
                repo.getTextFromImage(url, new Repository.RepoCallback<String>() {
                    @Override
                    public void onSuccess(String data) {
                        setLoadingProgress(100,0, "Finished", true);
                        binding.etCode.setText(data);
                    }

                    @Override
                    public void onFailed(String errorMessage) {
                        Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        setLoadingProgress(100,0, "Finished", true);
                    }
                });
            }
            @Override
            public void onProgressCallback(int progress) {
                setLoadingProgress(progress, ORC_PART_PROGRESS, "Preparing the image.", true);
            }
        });
    }

    @Override
    public void onCodeRunOutput(String output, Boolean isEnded) {
        outputStr = outputStr + "\n" + output;
        binding.tvOutput.setText(outputStr);
        Log.i("myTag output: ", outputStr);
        if(isEnded){
            btnHelpViewHandler(true);
            setInputView(false);
        }else{
            btnHelpViewHandler(false);
            setInputView(true);
        }
    }
}