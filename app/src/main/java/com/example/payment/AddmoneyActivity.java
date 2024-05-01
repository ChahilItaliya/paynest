package com.example.payment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddmoneyActivity extends AppCompatActivity {

    EditText amount;
    Button add;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmoney);
        db = FirebaseFirestore.getInstance();

        amount = findViewById(R.id.addtxt);
        add = findViewById(R.id.btnadd);
        Suid suid = com.example.payment.Suid.getInstance();
        String uid = suid.getData();


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int amt = Integer.parseInt(amount.getText().toString());
                Toast.makeText(AddmoneyActivity.this, "Money:" + amt , Toast.LENGTH_SHORT).show();
                addMoneyToBalance(uid,amt);
            }
        });

    }
    private void addMoneyToBalance(String uid, long amountToAdd) {
        // Reference to the user's document in Firestore
        DocumentReference userRef = db.collection("users").document(uid);

        userRef.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Get the current balance from the user's document
                            Long currentBalance = document.getLong("balance");

                            if (currentBalance != null) {
                                // Calculate the new balance by adding the amountToAdd
                                long newBalance = currentBalance + amountToAdd;

                                // Update the 'balance' field in Firestore with the new value
                                userRef.update("balance", newBalance)
                                        .addOnCompleteListener(updateTask -> {
                                            if (updateTask.isSuccessful()) {
                                                // Update was successful
                                                //balance.setText("₹" + newBalance); // Update the UI
                                                // You can also display a success message or perform other actions
                                                Toast.makeText(this, "Money Add success ", Toast.LENGTH_SHORT).show();
                                            } else {
                                                // Handle the case where the update failed
                                                Exception exception = updateTask.getException();
                                                if (exception != null) {
                                                    // Handle exceptions here
                                                }
                                            }
                                        });
                            } else {
                                // Handle the case where 'balance' is not present or is null
                            }
                        } else {
                            // Handle the case where the user's document does not exist
                        }
                    } else {
                        // Handle exceptions while trying to get the document
                        Exception exception = task.getException();
                        if (exception != null) {
                            // Handle exceptions here
                        }
                    }
                });
    }

}