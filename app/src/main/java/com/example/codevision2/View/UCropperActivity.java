package com.example.codevision2.View;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.codevision2.Constant;
import com.example.codevision2.R;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.UUID;

public class UCropperActivity extends AppCompatActivity {

    String sourceUri, destinationUri;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ucropper);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        if(intent.getExtras()!=null){
            sourceUri = intent.getStringExtra("SendImageData");
            uri = Uri.parse(sourceUri);
        }

        destinationUri = new StringBuilder(UUID.randomUUID().toString()).append(".jpg").toString();
        UCrop.Options options = new UCrop.Options();
        UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationUri)))
                .withOptions(options)
                //.withAspectRatio(16, 9)
                .withMaxResultSize(2000, 2000)
                .start(UCropperActivity.this);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);

            Intent intent = new Intent();
            intent.putExtra("CROP", resultUri+ "");
            setResult(101, intent);
            finish();

        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

    public static void startUCrop(Uri uri, Activity activity) {
        // Start uCrop Activity
        UCrop.of(uri, Uri.fromFile(new File(activity.getCacheDir(), "cropped_image.jpg")))
                .withAspectRatio(1, 1)  // Aspect ratio 1:1 (Square crop)
                .withMaxResultSize(500, 500)  // Max crop size (500x500)
                .start(activity, Constant.UCROP_REQUEST_CODE);
    }

}