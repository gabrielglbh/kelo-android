package com.gabr.gabc.kelo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.gabr.gabc.kelo.main.MainActivity
import com.gabr.gabc.kelo.utils.SharedPreferences
import com.gabr.gabc.kelo.welcome.WelcomeActivity

/** Activity that presents the user a Splash Screen to load either MainActivity or WelcomeActivity */
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        Handler(Looper.getMainLooper()).postDelayed({
            SharedPreferences.getIfFirstLaunched(this)
            if (SharedPreferences.isFirstLaunched && SharedPreferences.userId != null &&
                SharedPreferences.groupId != null)
                startActivity(Intent(this, MainActivity::class.java))
            else startActivity(Intent(this, WelcomeActivity::class.java))
        }, 1000)
    }
}