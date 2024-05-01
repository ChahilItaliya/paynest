package com.example.payment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;


public class StatusActivity extends AppCompatActivity {
    private LottieAnimationView successAnimationView;
    private LottieAnimationView failureAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        successAnimationView =  findViewById(R.id.successAnimationView);
        failureAnimationView = findViewById(R.id.failureAnimationView);

        Intent in = getIntent();
        String status = in.getStringExtra("status");
        if("Successful".equals(status))
        {
            failureAnimationView.setVisibility(View.INVISIBLE);
            successAnimationView.setVisibility(View.VISIBLE);
            successAnimationView.playAnimation();
        }
        else
        {
            successAnimationView.setVisibility(View.INVISIBLE);

            failureAnimationView.setVisibility(View.VISIBLE);
            failureAnimationView.playAnimation();
        }
        successAnimationView.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // Animation has finished playing
                Intent intent = new Intent(StatusActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        failureAnimationView.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // Animation has finished playing
                Intent intent = new Intent(StatusActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}