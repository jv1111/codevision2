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
import com.yalantis.ucrop.UCrop;

import java.io.File;

public class MainActivity extends AppCompatActivity implements WebSocketCompiler.ICompiler {

    private ActivityMainBinding binding;
    private StorageReference storageReference;
    private StorageHelper storageHelper;
    private CameraHelper cam;
    private Repository repo;
    private AnimationUI anim;
    private WebSocketCompiler webSocketCompiler;
    private static final int ORC_PART_PROGRESS = 10;
    private String outputStr;
    private String compiledCode;
    private String codeExplanation;
    private Boolean isInfoActive = false;
    private Boolean isSuggestionActive = false;
    private boolean isAnalyzeEnabled = false;
    private boolean isScanner = false;
    private boolean isBtnHelpEnabled = false;

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
        webSocketCompiler = new WebSocketCompiler(this, this);
        webSocketCompiler.connectWebSocket();
        buttonsFunction();
        analyzeViewHandler();
        setupInputListeners();
        setInputView(false);
        //test();
    }

    @Override
    public void onBackPressed() {
        if(isInfoActive) binding.layoutInfo.post(()->{ infoViewHandler(false); });
        else if(isSuggestionActive) binding.layoutAnalyze.post(()->{ suggestionsViewHandler(false); });
        else super.onBackPressed();
    }

    private void test(){
        binding.layoutInfo.post(()->{
            String testInput = "```java\n hi \n```";
            String testInput2 = Constant.SAMPLE_ANALYZE_RESPONSE;
            codeExplanation = Constant.SAMPLE_ANALYZE_RESPONSE;
            binding.tvExplanation.setText(StringFormatter.formatSampleCode(testInput2));
            infoViewHandler(true);
        });
    }

    private void suggestionsViewHandler(Boolean isShown){
        isSuggestionActive = isShown;
        if(isShown) {
            anim.setAppearFromBottom(binding.layoutAnalyzeExplanation);
            binding.layoutAnalyze.setVisibility(View.VISIBLE);
        }else{
            Log.i("myTag", "hidding");
            anim.setMoveToBottom(binding.layoutAnalyzeExplanation, () -> {
                binding.layoutAnalyze.setVisibility(View.GONE);
            });
        }
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
    }

    private void setupInputListeners(){
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

        anim.scaleDownRelativeLayoutOnTouchListener(binding.btnCropYes, new AnimationUI.Callback() {
            @Override
            public void onRelease() {
                binding.layoutCropPrompt.setVisibility(View.GONE);
                UCropperActivity.startUCrop(cam.imageUri, MainActivity.this);
            }
        });

        anim.scaleDownRelativeLayoutOnTouchListener(binding.btnCropNo, new AnimationUI.Callback() {
            @Override
            public void onRelease() {
                binding.layoutCropPrompt.setVisibility(View.GONE);
                uploadTheCapturedImage(cam.imageUri);
            }
        });

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
                btnHelpViewHandler(false);
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
                webSocketCompiler.sendMessage(input);
            }
        });
        anim.scaleDownRelativeLayoutOnTouchListener(binding.btnHelp, new AnimationUI.Callback() {
            @Override
            public void onRelease() {
                if(isBtnHelpEnabled) {
                    setLoadingProgress(0,0, "Loading", false);
                    repo.analyzeCode(compiledCode, outputStr,Constant.AI_EXPLAIN_CODE, new Repository.RepoCallback<String>() {
                        @Override
                        public void onSuccess(String data) {
                            codeExplanation = StringFormatter.formatSampleCode(data);
                            Log.i("myTag explanation", data);
                            binding.tvExplanation.setText(codeExplanation);
                            binding.tvExplanationTitle.setText(R.string.explanation_title);
                            binding.layoutInfo.post(() -> {
                                infoViewHandler(true);
                            });
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
            }
        });

        anim.scaleDownRelativeLayoutOnTouchListener(binding.btnAnalyze, new AnimationUI.Callback() {
            @Override
            public void onRelease() {
                if(isAnalyzeEnabled){
                    setLoadingProgress(0, 0, "Loading", false);
                    repo.analyzeCode(binding.etCode.getText().toString(), null, Constant.AI_ANALYZE, new Repository.RepoCallback<String>() {
                        @Override
                        public void onSuccess(String data) {
                            codeExplanation = data;
                            binding.tvAnalyzeExplanation.setText(StringFormatter.formatSampleCode(data));
                            binding.layoutAnalyze.post(()->{
                                suggestionsViewHandler(true);
                            });
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
                try{
                    binding.etCode.setText(StringFormatter.extractCodeExtended(codeExplanation));
                }catch (Exception ex){
                    Toast.makeText(MainActivity.this, "something went wrong try again.", Toast.LENGTH_LONG).show();
                } finally {
                    binding.layoutInfo.post(()->{
                        suggestionsViewHandler(false);
                    });
                }
            }
        });
    }

    private void btnHelpViewHandler(boolean isShown){
        if(isShown){
            isBtnHelpEnabled = true;
            binding.imgHelp.setImageResource(R.drawable.baseline_help_outline_24);
        }else{
            Log.i("myTag", "hidding help");
            isBtnHelpEnabled = false;
            binding.imgHelp.setImageResource(R.drawable.baseline_help_outline_grayed_out_24);
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
        if(requestCode == Constant.UCROP_REQUEST_CODE){
            Log.i("myTag", "imaged_cropped");
            try {
                Uri resultUri = UCrop.getOutput(data);
                uploadTheCapturedImage(resultUri);
            }catch (Exception ex){
                Toast.makeText(MainActivity.this, "Canceled " + ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void askToCrop(){
        binding.layoutCropPrompt.setVisibility(View.VISIBLE);
    }

    private void onCaptureHandler(){
        storageHelper.addPicToGallery(cam.imageFile);
        askToCrop();
    }

    private void convertImageToText(String imageUri){
        repo.getTextFromImage(imageUri, new Repository.RepoCallback<String>() {
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

    private void convertImageToText2(String imageUrl){
        repo.getTextFromImage2(imageUrl, new Repository.RepoCallback<String>() {
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

    private void uploadTheCapturedImage(Uri uri){
        storageHelper.uploadImageToFirebase(cam.imageFile.getName(), uri, new StorageHelper.Callback() {
            @Override
            public void onUploadSuccess(String url) {
                setLoadingProgress(100, ORC_PART_PROGRESS, "Converting the image to text", true);
                //convertImageToText(url);
                convertImageToText2(url);
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

    @Override
    public void onConnectionFailed(Throwable t) {
        webSocketCompiler.connectWebSocket();
        binding.tvOutput.post(()->{
           binding.tvOutput.setText("WebSocket Error: " + t.getMessage() + "reconnecting...");
        });
    }
}