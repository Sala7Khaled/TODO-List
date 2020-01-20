package com.sala7khaled.todo_auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import models.Users;

public class SignupActivity extends AppCompatActivity {

    EditText fName, lName, email, password, phone;
    Button signUp;

    DatabaseReference myDatabase;
    private FirebaseAuth myAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        myAuth = FirebaseAuth.getInstance();

        fName = findViewById(R.id.fName_ET);
        lName = findViewById(R.id.lName_ET);
        email = findViewById(R.id.email_ET);
        phone = findViewById(R.id.phone_ET);
        password = findViewById(R.id.password_ET);
        signUp = findViewById(R.id.signup_BTN);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fNameSTR = fName.getText().toString().trim();
                String lNameSTR = lName.getText().toString().trim();
                String emailSTR = email.getText().toString().trim();
                String passSTR = password.getText().toString().trim();
                String phoneSTR = phone.getText().toString().trim();

                if (fNameSTR.length() > 0 && lNameSTR.length() > 0 && emailSTR.length() > 0
                        && passSTR.length() > 0 && phoneSTR.length() > 0) {

                    writeNewUser(fNameSTR, lNameSTR, emailSTR, passSTR, phoneSTR);
                }
                if (passSTR.length() < 6) {

                    password.setError("Password should be more than 6 characters");
                }
                if (fNameSTR.isEmpty()) {
                    fName.setError("Required");
                }
                if (lNameSTR.isEmpty()) {
                    lName.setError("Required");
                }
                if (emailSTR.isEmpty()) {
                    email.setError("Required");
                }
                if (passSTR.isEmpty()) {
                    password.setError("Required");
                }
                if (phoneSTR.isEmpty()) {
                    phone.setError("Required");
                }

            }
        });

    }

    private void writeNewUser(final String fNameSTR, final String lNameSTR, final String emailSTR, final String passSTR, final String phoneSTR) {

        myAuth.createUserWithEmailAndPassword(emailSTR, passSTR)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Auth
                            FirebaseUser firebaseUser = myAuth.getCurrentUser();
                            String userUid = firebaseUser.getUid();
                            // another way
                            // String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                            // Realtime DB
                            myDatabase = FirebaseDatabase.getInstance().getReference();
                            Users newUser = new Users(fNameSTR, lNameSTR, emailSTR, passSTR, phoneSTR);
                            myDatabase.child("Users").child(userUid).setValue(newUser);
                            //myDatabase.push().setValue(newUser);

                            Toast.makeText(SignupActivity.this, "Signup Success!", Toast.LENGTH_SHORT).show();
                            Intent mainIntent = new Intent(SignupActivity.this, MainActivity.class);
                            startActivity(mainIntent);
                            finish();

                        } else {
                            Toast.makeText(SignupActivity.this, "Error! Please Try again later.", Toast.LENGTH_LONG).show();
                        }

                    }

                });

    }
}