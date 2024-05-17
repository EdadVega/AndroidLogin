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

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText emailEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Iniciar instancia de Firebase
        auth = FirebaseAuth.getInstance();

        // Buscar usuarios por id
        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
        Button loginButton = findViewById(R.id.loginButton);

        // Listener de botón de inicio de sesión
        loginButton.setOnClickListener(v -> signIn());
    }

    private void signIn() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Inicio de sesión exitoso
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            // Crear un nuevo documento para el usuario en Cloud Firestore
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("email", user.getEmail());
                            // Agrega más datos del usuario si los tienes

                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("users").document(user.getUid())
                                    .set(userData)
                                    .addOnSuccessListener(aVoid -> {
                                        // Datos del usuario guardados en Firestore
                                        // Redirigir a la vista de bienvenida
                                        Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                                        startActivity(intent);
                                    })
                                    .addOnFailureListener(e -> {
                                        // Error al guardar datos del usuario
                                        Toast.makeText(LoginActivity.this, "Error al guardar datos del usuario: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        // Error al iniciar sesión
                        Toast.makeText(LoginActivity.this, "Error al iniciar sesión: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
