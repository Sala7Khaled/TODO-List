package com.sala7khaled.todo_auth;

import android.content.Intent;
import android.os.Bundle;

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

public class LoginActivity extends AppCompatActivity {

    TextView signup;
    EditText username, password_login;
    Button login;
    FirebaseAuth myAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (!null)
        FirebaseUser currentUser = myAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        myAuth = FirebaseAuth.getInstance();

        signup = (TextView) findViewById(R.id.Signup);
        username = (EditText) findViewById(R.id.username_ET);
        password_login = (EditText) findViewById(R.id.password_login_ET);
        login = (Button) findViewById(R.id.login_BTN);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user, pass;
                user = username.getText().toString();
                pass = password_login.getText().toString();


                if (user.length() != 0 && pass.length() != 0) {
                    signIn(user, pass);
                } else if (user.length() == 0 && pass.length() == 0) {
                    Toast.makeText(LoginActivity.this, "Please Enter Username/Password.", Toast.LENGTH_SHORT).show();
                } else if (user.length() == 0 && pass.length() != 0) {
                    Toast.makeText(LoginActivity.this, "Please Enter Username.", Toast.LENGTH_SHORT).show();
                } else if (user.length() != 0 && pass.length() == 0) {
                    Toast.makeText(LoginActivity.this, "Please Enter Password.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupIntent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(signupIntent);
                finish();
            }
        });

    }
    void signIn(String user, String pass) {
        myAuth.signInWithEmailAndPassword(user, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = myAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Welcome " + user.getEmail(), Toast.LENGTH_SHORT).show();
                            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(mainIntent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Please check your Username or Password", Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }
}
