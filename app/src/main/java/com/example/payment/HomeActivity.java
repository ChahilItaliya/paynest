package com.example.payment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class HomeActivity extends AppCompatActivity {

    ImageView scanner,receive;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        scanner = findViewById(R.id.Scanner);
        receive = findViewById(R.id.receive);
        Intent intent = getIntent();
        String number = intent.getStringExtra("number");


        scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(HomeActivity.this,ScannerActivity.class);
                startActivity(in);
            }
        });
        receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(HomeActivity.this, QrcodeActivity.class);
                in.putExtra("number", number);
                startActivity(in);
            }
        });
    }

}