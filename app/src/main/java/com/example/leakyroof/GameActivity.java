package com.example.leakyroof;

import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;

public class GameActivity extends MainActivity {

    private static float DIM_Y;
    private static float DIM_X;

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
        moveRaindrop(raindropButton);
        raindropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
            }
        });
    }

    private void moveRaindrop(View view) {
        ObjectAnimator animation = ObjectAnimator.ofFloat(
                view, "translationY", DIM_Y - view.getTranslationY());
        animation.setDuration(5000);
        animation.start();
    }
}
