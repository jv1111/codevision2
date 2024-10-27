package com.example.codevision2.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.codevision2.R;

public class AnimationUI {
    private Animation scaleUp;
    private Animation scaleDown;
    private Animation popup;
    private Animation appear_from_bottom;
    private Animation close_move_to_bottom;

    public AnimationUI(Context context){
        scaleUp = AnimationUtils.loadAnimation(context, R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(context, R.anim.scale_down);
        popup = AnimationUtils.loadAnimation(context, R.anim.popup);
        appear_from_bottom = AnimationUtils.loadAnimation(context, R.anim.move_from_bottom);
        close_move_to_bottom = AnimationUtils.loadAnimation(context, R.anim.move_to_bottom);
    }

    public interface Callback{
        void onRelease();
    }

    public void scale_down(ViewGroup layout, Runnable onAnimEnd){
        scaleDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                onAnimEnd.run();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }

    public void setAppearFromBottom(ViewGroup layout){
        layout.startAnimation(appear_from_bottom);
    }

    public void setMoveToBottom(ViewGroup layout, Runnable onAnimEnd){
        close_move_to_bottom.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                onAnimEnd.run();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        layout.startAnimation(close_move_to_bottom);
    }

    public void setPopup(ViewGroup layout){
        layout.startAnimation(popup);
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
    public void scaleDownRelativeLayoutOnTouchListener(ViewGroup layout, Callback cb){
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    layout.startAnimation(scaleUp);
                    layout.setScaleX(1f);
                    layout.setScaleY(1f);
                    cb.onRelease();
                } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    layout.startAnimation(scaleDown);
                    layout.setScaleX(.9f);
                    layout.setScaleY(.9f);
                }
                return true;
            }
        });
    }
}
