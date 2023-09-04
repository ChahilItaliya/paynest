package com.example.payment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.util.HashMap;
import java.util.Map;

public class TransActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText amt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans);
        amt = findViewById(R.id.txtamt);
        Intent intent = getIntent();
        String uid = intent.getStringExtra("uid");
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        String amount = amt.getText().toString();

        // Replace these with actual user IDs and transaction amount
        FirebaseUser user = mAuth.getCurrentUser();
        String senderUserId = user.getUid();
        String receiverUserId = uid;
        double transactionAmount = Double.parseDouble(amount);;

        performTransaction(senderUserId, receiverUserId, transactionAmount);
    }

    private void performTransaction(String senderUid, String receiverUid, double amount) {
        // Update sender's balance (subtract the amount)
        updateBalance(senderUid, -amount);

        // Update receiver's balance (add the amount)
        updateBalance(receiverUid, amount);

        // Record the transaction in Firestore
        recordTransaction(senderUid, receiverUid, amount);
    }

    private void updateBalance(String uid, double amount) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(uid);

        userRef.update("balance", FieldValue.increment(amount))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Balance updated successfully
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle balance update failure
                    }
                });
    }

    private void recordTransaction(String senderUid, String receiverUid, double amount) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference transactionsRef = db.collection("transactions");

        Map<String, Object> transactionData = new HashMap<>();
        transactionData.put("senderUid", senderUid);
        transactionData.put("receiverUid", receiverUid);
        transactionData.put("amount", amount);
        transactionData.put("timestamp", FieldValue.serverTimestamp());

        transactionsRef.add(transactionData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // Transaction recorded successfully
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle transaction recording failure
                    }
                });
    }

}