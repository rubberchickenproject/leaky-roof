package com.example.leakyroof;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.leakyroof.ui.login.LoginActivity;

import java.util.HashMap;
import java.util.Map;

import static android.view.View.VISIBLE;

public class GameActivity extends MainActivity {

    private static float DIM_Y;
    private static float DIM_X;
    private int LEVEL = 3; // TODO: make this adjustable
    private static final int MAX_RAINDROP_DIAMETER = 100;
    private Map<ImageButton, ObjectAnimator> raindropsToAnimators = new HashMap<>();
    private static final float RAINDROP_SPEED = 1; // keep less than 2
    private static ConstraintLayout LAYOUT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        LOGIN_FILE_NAME = getFilesDir() + "/roster";

        // get window dimensions
        DisplayMetrics appMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(appMetrics);
        DIM_Y = appMetrics.heightPixels;
        DIM_X = appMetrics.widthPixels;

        LAYOUT = findViewById(R.id.gameLayout);

        ImageButton raindropButton;
        ObjectAnimator raindropAnimator;
        TextView scoreTextView = findViewById(R.id.scoreTextView);
        View.OnClickListener raindropListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catchRaindrop(v, raindropsToAnimators.get(v));
            }
        };

        for (int i = 0; i < LEVEL; i++) {
            // TODO: recycle raindrops
            raindropButton = createRaindrop(LAYOUT, DIM_X * i / LEVEL);
            raindropButton.setOnClickListener(raindropListener);
            raindropAnimator = ObjectAnimator.ofFloat(
                    raindropButton, "translationY",
                    DIM_Y - raindropButton.getTranslationY());
            raindropAnimator.setRepeatCount(ValueAnimator.INFINITE);
            raindropAnimator.addListener(new RaindropAnimationListener(scoreTextView));
            final ImageButton finalRaindropButton = raindropButton;
            raindropAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    finalRaindropButton.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {}

                @Override
                public void onAnimationCancel(Animator animation) {
                    finalRaindropButton.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animator animation) {}
            });
            raindropsToAnimators.put(raindropButton, raindropAnimator);
            LAYOUT.addView(raindropButton);
        }

        // main loop
        int startDelay = 0;
        for (ImageButton raindrop : raindropsToAnimators.keySet()) {
            moveRaindrop(raindropsToAnimators.get(raindrop), startDelay);
            startDelay += 1000; // TODO: vary this
        }
    }

    private ImageButton createRaindrop(ConstraintLayout layout, float xTranslation) {
        /** Returns a new stationary raindrop button, not yet added to layout */
        ImageButton raindropButton = new ImageButton(this);
        raindropButton.setImageDrawable(getDrawable(R.drawable.raindrop));
        raindropButton.setAdjustViewBounds(true);
        raindropButton.setMaxHeight(MAX_RAINDROP_DIAMETER);
        raindropButton.setMaxWidth(MAX_RAINDROP_DIAMETER);
        raindropButton.setTranslationX(xTranslation); // space evenly
        raindropButton.setTranslationY(layout.getTop());
        raindropButton.setVisibility(View.GONE);
        return raindropButton;
    }

    private void moveRaindrop(ObjectAnimator animator, int startDelay) {
        /** Animate the falling raindrop */
        long duration = (long) (10 * DIM_Y / RAINDROP_SPEED);
        animator.setStartDelay(startDelay);
        animator.setDuration(duration);
        animator.start();
    }

    /** Layout level methods (must be public)... */

    public void catchRaindrop(View view, ObjectAnimator animator) {
        animator.end();
        createRaindrop(LAYOUT, ((ImageButton) animator.getTarget()).getTranslationX());
        animator.setStartDelay(1000); // TODO: make this random
        animator.start();
    }

    public void logout(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
