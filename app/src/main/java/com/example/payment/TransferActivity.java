package com.example.payment;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.util.HashMap;
import java.util.Map;

public class TransferActivity extends AppCompatActivity {
    private LottieAnimationView loadingAnimationView;


    TextView resiver;
    EditText amount;
    Button sendbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        Intent in = getIntent();
        String resid = in.getStringExtra("uid");

        amount = findViewById(R.id.amount);
        resiver = findViewById(R.id.resiverid);
        sendbtn = findViewById(R.id.btnpay);
        loadingAnimationView = findViewById(R.id.loadingAnimationView);

        fetchUserName(resid);
        Suid suid = com.example.payment.Suid.getInstance();
        resiver.setText(resid);
        String senderid = suid.getData();
        String resiverid = resid;


        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingAnimationView.setVisibility(View.VISIBLE);
                loadingAnimationView.playAnimation();

                String amt = amount.getText().toString();
                int Amount = Integer.parseInt(amt);
               performTransaction(senderid,resiverid,Amount);
            }
        });

    }
    private void performTransaction(String senderUid, String receiverUid, int amount) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.runTransaction(new Transaction.Function<Void>() {
                    @Override
                    public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                        DocumentReference senderRef = db.collection("users").document(senderUid);
                        DocumentReference receiverRef = db.collection("users").document(receiverUid);

                        DocumentSnapshot senderSnapshot = transaction.get(senderRef);
                        DocumentSnapshot receiverSnapshot = transaction.get(receiverRef);

                        int senderBalance = senderSnapshot.getLong("balance").intValue();
                        int receiverBalance = receiverSnapshot.getLong("balance").intValue();

                        // Check if the sender has sufficient balance
                        if (senderBalance < amount) {
                            Intent in = new Intent(TransferActivity.this,StatusActivity.class);
                            in.putExtra("status","Failed");
                            startActivity(in);
                            finish();
                            throw new FirebaseFirestoreException("Insufficient balance.", FirebaseFirestoreException.Code.ABORTED);

                        }

                        // Update sender's balance (subtract the amount)
                        int newSenderBalance = senderBalance - amount;
                        transaction.update(senderRef, "balance", newSenderBalance);

                        // Update receiver's balance (add the amount)
                        int newReceiverBalance = receiverBalance + amount;
                        transaction.update(receiverRef, "balance", newReceiverBalance);

                        // Record the transaction in Firestore
                        CollectionReference transactionsRef = db.collection("transactions");
                        Map<String, Object> transactionData = new HashMap<>();
                        transactionData.put("senderUid", senderUid);
                        transactionData.put("receiverUid", receiverUid);
                        transactionData.put("amount", amount);
                        transactionData.put("timestamp", FieldValue.serverTimestamp());
                        transaction.set(transactionsRef.document(), transactionData);

                        return null;
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Transaction completed successfully

                        Toast.makeText(TransferActivity.this, "Transaction Successful", Toast.LENGTH_SHORT).show();
                        Intent in = new Intent(TransferActivity.this, StatusActivity.class);
                        in.putExtra("status","Successful");
                        startActivity(in);
                        finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle transaction failure

                        Toast.makeText(TransferActivity.this, "Transaction Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Transaction Failed: " + e.getMessage());
                        Intent in = new Intent(TransferActivity.this, StatusActivity.class);
                        in.putExtra("status","Failed");
                        startActivity(in);
                        finish();
                    }
                });


    }
    private void fetchUserName(String uid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(uid);

        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String userName = documentSnapshot.getString("name");
                if (userName != null) {
                    // Handle the fetched user name
                    // You can set it to a TextView or use it as needed
                    // For example, updating a TextView named 'userNameTextView':
                    resiver.setText(userName);
                } else {
                    // Handle the case where the 'name' field is null
                }
            } else {
                // Handle the case where the user document does not exist
            }
        }).addOnFailureListener(e -> {
            // Handle errors while fetching user data
        });
    }



   /* private void updateBalance(String uid, int amount) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(uid);

        db.runTransaction(new Transaction.Function<Void>() {
                    @Override
                    public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                        DocumentSnapshot snapshot = transaction.get(userRef);
                        int currentBalance = snapshot.getLong("balance").intValue();
                        int newBalance = currentBalance + amount;

                        if (newBalance >= 0) { // Ensure the balance doesn't go negative
                            transaction.update(userRef, "balance", newBalance);
                        } else {
                            // Insufficient balance
                            throw new FirebaseFirestoreException("Insufficient balance.", FirebaseFirestoreException.Code.ABORTED);
                        }

                        return null;
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Balance updated successfully
                        Toast.makeText(TransferActivity.this, "Transaction Successful", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle balance update failure
                        Toast.makeText(TransferActivity.this, "Transaction Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Transaction Faild: " + e.getMessage());
                    }
                });
    }



    private void recordTransaction(String senderUid, String receiverUid, int amount) {
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

    */
}