package com.studiob4.fox_social_mdl

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in) // Setze das Layout

        // Load GIF into ImageView
        val imageView: ImageView = findViewById(R.id.foxSocmdl)
        Glide.with(this).asGif().load(R.drawable.foxsocmdl).into(imageView)

        firebaseAuth = FirebaseAuth.getInstance()

        // Überprüfe, ob der Benutzer bereits eingeloggt ist
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            // Benutzer ist bereits eingeloggt, überspringe den Login und gehe zur Hauptaktivität
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Beende die aktuelle Aktivität, damit der Benutzer nicht zurückgehen kann
        }

        val emailEt = findViewById<EditText>(R.id.emailEt)
        val passEt = findViewById<EditText>(R.id.passET)
        val signInButton = findViewById<Button>(R.id.button1)
        val signUpTextView = findViewById<TextView>(R.id.TextView)
        val forgotPasswordTextView = findViewById<TextView>(R.id.forgotPasswordTextView)

        signUpTextView.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        signInButton.setOnClickListener {
            val email = emailEt.text.toString()
            val pass = passEt.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {

                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        // Zeige eine Toast-Nachricht, wenn das Erfolgreich Eingeloggt ist
                        Toast.makeText(this, "Your logged in successfully!", Toast.LENGTH_SHORT).show()
                        startActivity(intent)
                    } else {
                        // Zeige eine Toast-Nachricht, wenn das Passwort falsch ist
                        Toast.makeText(this, "Login fehlgeschlagen: " + it.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()
            }
        }

        forgotPasswordTextView.setOnClickListener {
            val email = emailEt.text.toString()
            if (email.isNotEmpty()) {
                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Password reset email sent.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Error: " + task.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please enter your email address.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

