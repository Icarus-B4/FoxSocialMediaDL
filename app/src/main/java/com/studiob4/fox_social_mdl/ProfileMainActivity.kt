package com.studiob4.fox_social_mdl

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ProfileMainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_main)

        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        if (currentUser == null) {
            // Benutzer ist nicht eingeloggt, leite zur SignInActivity weiter
            val intent = Intent(this, SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
            return
        } else {
            // Nur beim ersten Login (nicht bei jedem Start der Aktivität)
            if (isFirstLogin()) {
                saveLoginTimestamp()
            }

            displayLoginTimestamp()
        }

        // Logout-Button einrichten
        val logoutButton: Button = findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener {
            auth.signOut()
            clearLoginTimestamp()
            val intent = Intent(this, SplashActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun isFirstLogin(): Boolean {
        val sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        return !sharedPreferences.contains("loginTimestamp")
    }

    private fun saveLoginTimestamp() {
        val sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("EEEE, HH:mm:ss")
        val formattedDateTime = currentDateTime.format(formatter)
        editor.putString("loginTimestamp", formattedDateTime)
        editor.apply()
    }

    private fun displayLoginTimestamp() {
        val sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val savedTimestamp = sharedPreferences.getString("loginTimestamp", "")
        val textViewTime = findViewById<TextView>(R.id.textViewTime)
        textViewTime.text = savedTimestamp
    }

    private fun clearLoginTimestamp() {
        val sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("loginTimestamp")
        editor.apply()
    }
}