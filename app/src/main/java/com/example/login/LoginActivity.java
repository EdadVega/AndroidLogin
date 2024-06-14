package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

import android.util.Log;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText emailEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // instancia Firebase
        auth = FirebaseAuth.getInstance();

        //Busqueda id
        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
        Button loginButton = findViewById(R.id.loginButton);
        Button registerButton = findViewById(R.id.registerButton); // Agregado el botón de registro

        // login
        loginButton.setOnClickListener(v -> signIn());

        // registro
        registerButton.setOnClickListener(v -> {
            // intent para registro
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
        Log.d("LoginActivity", "La actividad LoginActivity se ha creado correctamente.");
    }

    private void signIn() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Inicio exitoso
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            // Map de usuario Firestore
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("email", user.getEmail());

                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("users").document(user.getUid())
                                    .set(userData)
                                    .addOnSuccessListener(aVoid -> {
                                        // Datos en Firestore y redirección a welcome
                                        Intent intent = new Intent(LoginActivity.this, WeatherActivity.class);
                                        startActivity(intent);
                                    })
                                    .addOnFailureListener(e -> {
                                        // Error al guardar registro
                                        Toast.makeText(LoginActivity.this, "Error al guardar datos del usuario: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        // Error de inicio sesión
                        Toast.makeText(LoginActivity.this, "Error al iniciar sesión, credenciales invalidas.", Toast.LENGTH_SHORT).show();
                    }
                });
        Log.d("LoginActivity", "Iniciando sesión con email: " + email);
    }
}
