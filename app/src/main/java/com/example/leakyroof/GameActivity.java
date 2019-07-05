package com.example.leakyroof;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.leakyroof.ui.login.LoginActivity;

public class GameActivity extends MainActivity {

    private static float DIM_Y;
    private static float DIM_X;
    private ObjectAnimator raindropAnimator;
    private static final int RAINDROP_SPEED = 40; // TODO: come up with reasonable range here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        LOGIN_FILE_NAME = getFilesDir() + "/roster";

        // populate window dimensions
        DisplayMetrics appMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(appMetrics);
        DIM_Y = appMetrics.heightPixels;
        DIM_X = appMetrics.widthPixels;

        ImageButton raindropButton = findViewById(R.id.raindrop);
        TextView scoreTextView = findViewById(R.id.scoreTextView);
        raindropAnimator = ObjectAnimator.ofFloat(
                raindropButton, "translationY", DIM_Y - raindropButton.getTranslationY());


        RaindropAnimationListener raindropAnimationListener =
                new RaindropAnimationListener(scoreTextView);
        raindropAnimator.addListener(raindropAnimationListener);

        moveRaindrop();
    }

    private void moveRaindrop() {
        /** Executes actual animation using animator/whatever else I can manage to get working */
        raindropAnimator.setDuration(5000);
        raindropAnimator.start();
    }

    /** Layout level methods (must be public)... **/

    public void catchRaindrop(View view) {
        /* TODO: extend animator for multiple raindrops */
        raindropAnimator.cancel();
        view.setVisibility(View.GONE);
    }

    public void logout(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
