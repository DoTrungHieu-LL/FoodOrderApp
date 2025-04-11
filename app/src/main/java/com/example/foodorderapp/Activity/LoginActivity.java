package com.example.foodorderapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.foodorderapp.R;
import com.example.foodorderapp.databinding.ActivityLoginBinding;

public class LoginActivity extends BaseActivity {

    ActivityLoginBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setVariable();

    }
    private void setVariable() {

        binding.signupBtn.setOnClickListener(v -> {
            // Chuyển sang SignupActivity
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        });

        binding.loginBtn.setOnClickListener(v ->{
            String email = binding.userEdit.getText().toString();
            String password = binding.passEdit.getText().toString();
            if(!email.isEmpty() && !password.isEmpty()){
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(LoginActivity.this, task ->{
                   if (task.isSuccessful()){
                       startActivity(new Intent(LoginActivity.this, MainActivity.class));
                   }else {
                       Toast.makeText(LoginActivity.this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                   }
                });
            }else{
                Toast.makeText(LoginActivity.this, "Hãy nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            }
        });

    }
}