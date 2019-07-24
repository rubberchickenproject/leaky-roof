package com.example.leakyroof.data;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.leakyroof.GameActivity;
import com.example.leakyroof.R;

import java.util.List;

public class LevelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.game_title);
        toolbar.setSubtitle(R.string.level_selection); // TODO: make this show up
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void onClick(View view) {
        // TODO: direct to different activities according to level
        String[] text = ((Button) view).getText().toString().split(" ");
        int level = Integer.valueOf(text[text.length - 1]);
        Intent intent = new Intent(this, GameActivity.class)
                .putExtra("level", level);
        startActivity(intent);
    }
}
