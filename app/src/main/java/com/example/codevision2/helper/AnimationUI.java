package com.example.codevision2.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.codevision2.R;

public class AnimationUI {
    private Animation scaleUp;
    private Animation scaleDown;

    public AnimationUI(Context context){
        scaleUp = AnimationUtils.loadAnimation(context, R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(context, R.anim.scale_down);
    }

    public interface Callback{
        void onRelease();
    }

    public void setLoadingRelativeLayout(Boolean isLoading, TextView tv, ProgressBar pb){
        if(isLoading){
            tv.setVisibility(View.INVISIBLE);
            pb.setVisibility(View.VISIBLE);
        }else{
            tv.setVisibility(View.VISIBLE);
            pb.setVisibility(View.INVISIBLE);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void scaleDownRelativeLayoutOnTouchListener(RelativeLayout relativeLayout, Callback cb){
        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    relativeLayout.startAnimation(scaleUp);
                    relativeLayout.setScaleX(1f);
                    relativeLayout.setScaleY(1f);
                    cb.onRelease();
                } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    relativeLayout.startAnimation(scaleDown);
                    relativeLayout.setScaleX(.9f);
                    relativeLayout.setScaleY(.9f);
                }
                return true;
            }
        });
    }
}
