package com.example.leakyroof;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class RaindropAnimationListener implements AnimatorListener {

    private TextView textView;
    private static final int BOOST_AMOUNT = 1;

    public RaindropAnimationListener(TextView textView) {
        this.textView = textView;
    }

    @Override
    public void onAnimationEnd(Animator animation, boolean isReverse) {

    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        final int currentScore = Integer.parseInt(textView.getText().toString());
        textView.setText(currentScore + BOOST_AMOUNT);
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        String currentTextSequence = String.valueOf(textView.getText());
        List<String> parsedText = Arrays.asList(currentTextSequence.split("\\s"));
        final int currentScore = Integer.parseInt(parsedText.get(parsedText.size() - 1));
        textView.setText("Score: " + String.valueOf(currentScore + BOOST_AMOUNT));
    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
