package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class WelcomeActivity extends AppCompatActivity {
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        // instancia
        auth = FirebaseAuth.getInstance();

        Button logoutButton = findViewById(R.id.loginButton);

        logoutButton.setOnClickListener(v -> {
            auth.signOut();

            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}