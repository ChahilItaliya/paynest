package com.example.payment;

import android.os.Bundle;
import android.widget.ViewAnimator;

import androidx.appcompat.app.AppCompatActivity;

public class AlltransactionActivity extends AppCompatActivity {

    private ViewAnimator viewAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alltransaction);

        viewAnimator = findViewById(R.id.viewAnimator);
        // Set a slide-in and slide-out animation
        viewAnimator.setInAnimation(this, R.anim.slide_in_left);
        viewAnimator.setOutAnimation(this, R.anim.slide_out_right);


    }
}