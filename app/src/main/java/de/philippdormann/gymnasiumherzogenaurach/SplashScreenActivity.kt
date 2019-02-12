package de.philippdormann.gymnasiumherzogenaurach

import android.content.Intent
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        startActivity(Intent(applicationContext, Login::class.java))
        finish()
    }
}
