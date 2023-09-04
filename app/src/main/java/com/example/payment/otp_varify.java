package com.example.payment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class otp_varify extends AppCompatActivity {

    private PinView pinView;
    private Button otpvrify;

    private FirebaseAuth mAuth;
    private String verificationId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_varify);

        mAuth = FirebaseAuth.getInstance();
        pinView = findViewById(R.id.pinview);
        otpvrify = findViewById(R.id.otpvrify);

        Intent intent = getIntent();
        String phone = intent.getStringExtra("number");
        sendVerificationCode(phone);

        otpvrify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(pinView.getText().toString())) {
                    // if the OTP text field is empty display
                    // a message to user to enter OTP
                    Toast.makeText(otp_varify.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
                } else {
                    // if OTP field is not empty calling
                    // method to verify the OTP.
                    Toast.makeText(otp_varify.this, "pinview"+pinView.getText().toString(), Toast.LENGTH_SHORT).show();
                    verifyCode(pinView.getText().toString());
                }
            }
        });
    }

    private void verifyCode(String code) {
        // below line is used for getting
        // credentials from our verification id and code.
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        // after getting credential we are
        // calling sign in method.
        signInWithCredential(credential);
    }
    private void signInWithCredential(PhoneAuthCredential credential) {
        // inside this method we are checking if
        // the code entered is correct or not.



        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // if the code is correct and the task is successful
                            // we are sending our user to new activity.
                            Toast.makeText(otp_varify.this, "otp Match", Toast.LENGTH_SHORT).show();
                            registerUser();

                        } else {
                            // if the code is not correct then we are
                            // displaying an error message to the user.
                            Toast.makeText(otp_varify.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    private void sendVerificationCode(String number) {
        // this method is used for getting
        // OTP on user phone number.
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)         // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)         // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks

            // initializing our callbacks for on
            // verification callback method.
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        // below method is used when
        // OTP is sent from Firebase
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            // when we receive the OTP it
            // contains a unique id which
            // we are storing in our string
            // which we have already created.
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            // below line is used for getting OTP code
            // which is sent in phone auth credentials.
            final String code = phoneAuthCredential.getSmsCode();

            // checking if the code
            // is null or not.
            if (code != null) {
                // if the code is not null then
                // we are setting that code to
                // our OTP edittext field.
                pinView.setText(code);

                // after setting this code
                // to OTP edittext field we
                // are calling our verifycode method.
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(otp_varify.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    };
   /* private void registerUser()
    {
        Intent intent = getIntent();
        String phone = intent.getStringExtra("number");
        String email = intent.getStringExtra("email");
        String password = intent.getStringExtra("password");
        sendVerificationCode(phone);
        // Register new user
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(otp_varify.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // User registered successfully
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {
                                // Save additional user details to the Realtime Database
                                String userId = user.getUid();
                                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
                                userRef.child("email").setValue(email);
                                userRef.child("phoneNumber").setValue(phone);
                                Intent in = new Intent(otp_varify.this,MainActivity.class);
                                startActivity(in);
                                finish();
                            }
                        } else {
                            // Handle registration failure
                            Toast.makeText(otp_varify.this, "Registration failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }*/
   private void registerUser() {
       Intent intent = getIntent();
       String phone = intent.getStringExtra("number");
       String email = intent.getStringExtra("email");
       String password = intent.getStringExtra("password");

       FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
               .addOnCompleteListener(otp_varify.this, new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if (task.isSuccessful()) {
                           // User registered successfully
                           FirebaseAuth auth = FirebaseAuth.getInstance();
                           FirebaseFirestore db = FirebaseFirestore.getInstance();
                           // Get the current user's UID
                           String uid = auth.getCurrentUser().getUid();
                           //creat new phone number class
                           Map<String, Object> numMap = new HashMap<>();
                           numMap.put("uid", uid);
                           db.collection("phoneNumbers")
                                   .document(phone)
                                   .set(numMap);

                           // Create a new user document with the UID as the document name
                           Map<String, Object> userMap = new HashMap<>();
                           userMap.put("email", email);
                           userMap.put("phoneNumber", phone);

                           db.collection("users")
                                   .document(uid)
                                   .set(userMap)
                                   .addOnSuccessListener(new OnSuccessListener<Void>() {
                                       @Override
                                       public void onSuccess(Void aVoid) {
                                           // User data successfully stored in Firestore
                                           Intent in = new Intent(otp_varify.this, MainActivity.class);
                                           startActivity(in);
                                           finish();
                                       }
                                   })
                                   .addOnFailureListener(new OnFailureListener() {
                                       @Override
                                       public void onFailure(@NonNull Exception e) {
                                           // Handle Firestore data storage failure
                                           Toast.makeText(otp_varify.this, "Failed to store user data.", Toast.LENGTH_SHORT).show();
                                       }
                                   });
                       }
                       else {
                           // Handle registration failure
                           Toast.makeText(otp_varify.this, "Registration failed.", Toast.LENGTH_SHORT).show();
                       }
                   }

               });

       //
   }
}
