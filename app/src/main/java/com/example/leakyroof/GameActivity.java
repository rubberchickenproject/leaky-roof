package com.example.leakyroof;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.leakyroof.ui.login.LoginActivity;

import java.util.HashMap;
import java.util.Map;

public class GameActivity extends MainActivity {

    private int LEVEL;
    private static final int MAX_RAINDROP_DIAMETER = 100;
    private static final float RAINDROP_AVG_SPEED = 2.0f; // 1.0f for testing, 2.0f for users
    private static final int MAX_DELAY = 1000;
    private static final float ACCELERATION_COEFF = 2.0f;

    private static float DIM_Y;
    private static float DIM_X;
    private Map<ImageButton, ObjectAnimator> raindropsToAnimators = new HashMap<>();
    private static ConstraintLayout LAYOUT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        LOGIN_FILE_NAME = getFilesDir() + "/roster";

        LEVEL = getIntent().getIntExtra("level", 1);

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
                catchRaindrop(raindropsToAnimators.get(v));
            }
        };

        for (int i = 0; i < LEVEL; i++) {
            raindropButton = createRaindrop(LAYOUT, (int) (Math.random() * DIM_X));
            raindropButton.setOnClickListener(raindropListener);
            raindropButton.setBackgroundColor(Color.TRANSPARENT);
            raindropAnimator = ObjectAnimator.ofFloat(
                    raindropButton, "translationY",
                    DIM_Y - raindropButton.getTranslationY());
            raindropAnimator.setInterpolator(
                    new AccelerateInterpolator(ACCELERATION_COEFF / 2));
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
        int randomDelay;
        for (ImageButton raindrop : raindropsToAnimators.keySet()) {
            moveRaindrop(raindropsToAnimators.get(raindrop), startDelay);
            randomDelay = (int) (Math.random() * MAX_DELAY);
            startDelay += randomDelay;
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
        /** Initiate a falling raindrop */
        long duration = (long) (10 * DIM_Y / RAINDROP_AVG_SPEED);
        animator.setStartDelay(startDelay);
        animator.setDuration(duration);
        animator.start();
    }

    /** Layout level methods (must be public)... */

    public void catchRaindrop(ObjectAnimator animator) {
        animator.end();
        createRaindrop(LAYOUT, (int) (Math.random() * DIM_X));
        int randomDelay = (int) (Math.random() * MAX_DELAY);
        animator.setStartDelay(randomDelay);
        animator.start();
    }

    public void logout(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
