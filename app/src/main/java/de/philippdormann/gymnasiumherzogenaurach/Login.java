package de.philippdormann.gymnasiumherzogenaurach;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final TextView textView = findViewById(R.id.textViewWrongLogin);

        SharedPreferences sharedPref = getSharedPreferences("GYMH", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();

        if (sharedPref.getBoolean("loggedIN", false)) {
            startActivity(new Intent(getApplicationContext(), Main.class));
        }

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textView.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        final EditText editText_username = findViewById(R.id.editText_username);
        editText_username.addTextChangedListener(textWatcher);
        final EditText editText_password = findViewById(R.id.editText_password);
        editText_password.addTextChangedListener(textWatcher);
        Button button = findViewById(R.id.button_login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = md5(editText_username.getText().toString().toLowerCase());
                String password = md5(editText_password.getText().toString().toLowerCase());
                if ((username.equals("13d90cae300cf5afb8eb6c659e852df6") || username.equals("104c5d6cf12cd8ca8716e361b92151aa")) && password.equals("43ea10765c0d86ee737dc8afc7b726f6")) {
                    editor.putBoolean("loggedIN", true);
                    editor.apply();
                    startActivity(new Intent(getApplicationContext(), Main.class));
                } else {
                    textView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private String md5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte anArray : array) {
                sb.append(Integer.toHexString((anArray & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException ignored) {
        }
        return "";
    }
}
