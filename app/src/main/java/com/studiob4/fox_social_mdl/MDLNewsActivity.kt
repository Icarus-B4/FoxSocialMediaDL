package com.studiob4.fox_social_mdl

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MDLNewsActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mdlnews) // Ersetze durch dein Layout

        val viewPager: ViewPager = findViewById(R.id.pager3) // Ersetze durch die ID deines ViewPagers
        val adapter = MdlAdapter(supportFragmentManager)
        viewPager.adapter = adapter

        // Überprüfen, ob der Benutzer angemeldet ist
        auth = FirebaseAuth.getInstance()
        val currentUser: FirebaseUser? = auth.currentUser
        if (currentUser == null) {
            // Benutzer ist nicht angemeldet, leite zur Anmeldeseite weiter
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish() // Beende die aktuelle Aktivität
            return
        }

        // Füge den PageChangeListener hinzu, um den letzten Swipe zu erkennen
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                // Keine Aktion nötig
            }

            override fun onPageSelected(position: Int) {
                // Prüfe ob der Benutzer auf die dritte Seite gekommen ist (Index 2, da indexbasiert ab 0 gezählt wird)
                if (position == 2) {
                    // Leite den Benutzer zur Webseite weiter
                    val url = "https://fox-socialmdl.firebaseapp.com/"
                    val intent = Intent(this@MDLNewsActivity, WebViewActivity::class.java)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                // Keine Aktion nötig
            }
        })
    }
}
