package com.studiob4.fox_social_mdl

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        val splashImage = findViewById<ImageView>(R.id.splash_image)
        val fadeInAnimation: Animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        splashImage.startAnimation(fadeInAnimation)

        Handler(Looper.getMainLooper()).postDelayed({
            // Starte die Hauptaktivität nach einer Verzögerung
            startActivity(Intent(this, MainActivity::class.java))
            finish()  // Schließe den Splash-Screen
        }, 2000)  // 3000 ms Verzögerung für den Splash-Screen
    }
}