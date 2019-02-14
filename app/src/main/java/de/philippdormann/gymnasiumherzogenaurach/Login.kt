package de.philippdormann.gymnasiumherzogenaurach

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import java.math.BigInteger
import java.security.MessageDigest

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val textView = findViewById<TextView>(R.id.textViewWrongLogin)

        val sharedPref = getSharedPreferences("GYMH", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        if (sharedPref.getBoolean("loggedIN", false)) {
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                textView.visibility = View.GONE
            }

            override fun afterTextChanged(s: Editable) {

            }
        }

        val editTextUsername = findViewById<EditText>(R.id.editText_username)
        editTextUsername.addTextChangedListener(textWatcher)
        val editTextPassword = findViewById<EditText>(R.id.editText_password)
        editTextPassword.addTextChangedListener(textWatcher)
        val button = findViewById<Button>(R.id.button_login)
        button.setOnClickListener {
            val username = md5(editTextUsername.text.toString().toLowerCase())
            val password = md5(editTextPassword.text.toString().toLowerCase())
            if ((username == "13d90cae300cf5afb8eb6c659e852df6" || username == "104c5d6cf12cd8ca8716e361b92151aa") && password == "43ea10765c0d86ee737dc8afc7b726f6") {
                editor.putBoolean("loggedIN", true)
                editor.apply()
                startActivity(Intent(applicationContext, MainActivity::class.java))
            } else {
                textView.visibility = View.VISIBLE
            }
        }
    }

    private fun md5(string: String): Any {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(string.toByteArray())).toString(16).padStart(32, '0')
    }
}