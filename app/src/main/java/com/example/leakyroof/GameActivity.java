package com.example.leakyroof;

import android.animation.ObjectAnimator;
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

public class GameActivity extends MainActivity {

    private static float DIM_Y;
    private static float DIM_X;
    private int LEVEL = 2;
    private static final int MAX_RAINDROP_DIAMETER = 200;
    private Map<View, ObjectAnimator> raindropsToAnimators = new HashMap<>();
    private static final float RAINDROP_SPEED = 1; // keep less than 2

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

        ConstraintLayout layout = findViewById(R.id.gameLayout);

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
            raindropButton = createRaindrop(layout, DIM_X * i / LEVEL);
            raindropButton.setOnClickListener(raindropListener);
            raindropAnimator = ObjectAnimator.ofFloat(
                    raindropButton, "translationY",
                    DIM_Y - raindropButton.getTranslationY());
            raindropAnimator.addListener(new RaindropAnimationListener(scoreTextView));
            raindropsToAnimators.put(raindropButton, raindropAnimator);
            layout.addView(raindropButton);
        }

        // main loop
        for (ObjectAnimator animator : raindropsToAnimators.values()) {
            moveRaindrop(animator);
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
        return raindropButton;
    }

    private void moveRaindrop(ObjectAnimator animator) {
        /** whatever else I can manage to get working */
        long duration = (long) (10 * DIM_Y / RAINDROP_SPEED);
        animator.setDuration(duration);
        animator.start();
    }

    /** Layout level methods (must be public)... */

    public void catchRaindrop(View view, ObjectAnimator animator) {
        /** TODO: extend animator for multiple raindrops */
        animator.cancel();
        view.setVisibility(View.GONE);
    }

    public void logout(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
