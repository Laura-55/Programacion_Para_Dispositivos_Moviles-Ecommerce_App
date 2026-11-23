package es.upm.etsiinf.gib.proyecto;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etUser, etPassword;
    private CheckBox cbRemember;
    private Button btnLogin;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences("login_prefs", MODE_PRIVATE);

        // Si ya está logado, ir directamente al MainActivity
        if (prefs.getBoolean("logged", false)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

        etUser = findViewById(R.id.etUser);
        etPassword = findViewById(R.id.etPassword);
        cbRemember = findViewById(R.id.cbRemember);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> attemptLogin());
    }

    private void attemptLogin() {

        String username = etUser.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this,
                    "Introduce usuario y contraseña",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Lanzar el hilo en segundo plano
        new Thread(new LoginThread(
                this,
                username,
                password
        )).start();
    }

    // Llamado desde LoginThread en el hilo principal
    public void onLoginSuccess(String token) {

        if (cbRemember.isChecked()) {
            prefs.edit()
                    .putBoolean("logged", true)
                    .putString("token", token)
                    .apply();
        }

        Toast.makeText(this,
                "Login correcto",
                Toast.LENGTH_SHORT).show();

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    // Llamado desde LoginThread en el hilo principal
    public void onLoginError() {
        Toast.makeText(this,
                "Usuario o contraseña incorrectos",
                Toast.LENGTH_SHORT).show();
    }
}
