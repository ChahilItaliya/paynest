package com.example.payment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private EditText email,number,password;

    private TextView login;
    private Button regbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email=findViewById(R.id.reg_email);
        number=findViewById(R.id.reg_number);
        password=findViewById(R.id.reg_password);
        regbtn=findViewById(R.id.reg_button);
        login=findViewById(R.id.login);

        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = email.getText().toString().trim();
                String userPassword = password.getText().toString().trim();
                String Number = number.getText().toString().trim();
                String userNumber = "+91" + Number;
                // Perform form validation
                if (TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(userPassword) || TextUtils.isEmpty(userNumber)) {
                    Toast.makeText(RegisterActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Intent data to next page
                Intent in = new Intent(RegisterActivity.this, otp_varify.class);
                in.putExtra("number",userNumber);
                in.putExtra("email",userEmail);
                in.putExtra("password",userPassword);
                startActivity(in);
                finish();
            /*    // Register new user
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(userEmail, userPassword)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // User registered successfully
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    if (user != null) {
                                        // Save additional user details to the Realtime Database
                                        String userId = user.getUid();
                                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
                                        userRef.child("email").setValue(userEmail);
                                        userRef.child("phoneNumber").setValue(userNumber);
                                        Intent in = new Intent(RegisterActivity.this,MainActivity.class);
                                        startActivity(in);
                                        finish();
                                    }
                                } else {
                                    // Handle registration failure
                                    Toast.makeText(RegisterActivity.this, "Registration failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

               */
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in= new Intent(RegisterActivity.this,MainActivity.class);
                startActivity(in);
                finish();
            }
        });



    }
}