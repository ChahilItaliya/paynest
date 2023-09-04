package com.example.payment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class MainActivity extends AppCompatActivity {

    private EditText email,password;
    private TextView register;
    private Button btnlogin;
    private FirebaseAuth mAuth;
    private static final String TAG = "MainActivity";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.login_email);
        password=findViewById(R.id.login_password);
        btnlogin = findViewById(R.id.login_button);
        register=findViewById(R.id.register);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = email.getText().toString().trim();
                String userPassword = password.getText().toString().trim();
                if (TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(userPassword)) {
                    Toast.makeText(MainActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

               /* // Login user
                FirebaseAuth.getInstance().signInWithEmailAndPassword(userEmail, userPassword)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // User login successful
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    if (user != null) {
                                        // Now you can access the user's data from the Realtime Database
                                        String userId = user.getUid();
                                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
                                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                // Example: Get user details
                                                String userEmail = snapshot.child("email").getValue(String.class);
                                                String userPhoneNumber = snapshot.child("phoneNumber").getValue(String.class);
                                                Log.d(TAG, "User email: " + userEmail + ", Phone number: " + userPhoneNumber);
                                                Intent in = new Intent(MainActivity.this,HomeActivity.class);
                                                in.putExtra("number", userPhoneNumber);
                                                startActivity(in);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                Log.e(TAG, "Error getting user data", error.toException());
                                            }
                                        });
                                    }
                                } else {
                                    // Handle login failure
                                    Toast.makeText(MainActivity.this, "Login failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                */
                // Login user
                mAuth.signInWithEmailAndPassword(userEmail, userPassword)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        // Fetch user's phone number from Firestore using their UID
                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                        db.collection("users").document(user.getUid())
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful() && task.getResult() != null) {
                                                            DocumentSnapshot document = task.getResult();
                                                            if (document.exists()) {
                                                                String userPhoneNumber = document.getString("phoneNumber");
                                                                Log.d(TAG, "User's phone number: " + userPhoneNumber);
                                                                // Start the HomeActivity with the user's phone number
                                                                Intent in = new Intent(MainActivity.this, HomeActivity.class);
                                                                in.putExtra("number", userPhoneNumber);
                                                                in.putExtra("uid",user.getUid());
                                                                startActivity(in);
                                                            }
                                                        }
                                                    }
                                                });
                                    }
                                } else {
                                    // Handle login failure
                                    Toast.makeText(MainActivity.this, "Login failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(in);
                finish();
            }
        });


    }


}