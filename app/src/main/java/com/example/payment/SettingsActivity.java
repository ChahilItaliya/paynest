package com.example.payment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class SettingsActivity extends AppCompatActivity {
    ImageView img;
    TextView name,email;
    Button transaction,logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        img = findViewById(R.id.imageView2);
        name = findViewById(R.id.textView);
        email = findViewById(R.id.textView2);
        transaction = findViewById(R.id.button4);
        logout = findViewById(R.id.btn1);
        Suid suid = com.example.payment.Suid.getInstance();
        name.setText(suid.getName());
        email.setText(suid.getEmail());
        loadProfileImage(suid.getImgurl());

        transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(SettingsActivity.this,MainActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(in);
                finish();
            }
        });

    }
    private void loadProfileImage(String imageURL) {
        if (imageURL != null && !imageURL.isEmpty()) {
            // Use an image-loading library like Glide to load and display the image
            // Replace R.drawable.default_profile with your default placeholder image
            Glide.with(this)
                    .load(imageURL)
                    .placeholder(R.drawable.profile)
                    .error(R.drawable.profile)
                    .into(img);
        } else {
            // If no image URL is available, you can set a default placeholder image here
            img.setImageResource(R.drawable.profile);
        }


    }
}