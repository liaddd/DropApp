package com.liad.droptask.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.liad.droptask.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            moveToMain()
        }, 1000)
    }

    private fun moveToMain() {
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        finish()
    }
}
