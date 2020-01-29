package com.liad.droptask.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.liad.droptask.R

class SplashActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }, 1000)
    }
}
