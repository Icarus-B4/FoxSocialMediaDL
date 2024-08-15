package com.studiob4.fox_social_mdl

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Timer
import java.util.TimerTask

class AliexpressActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout2: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var auth: FirebaseAuth
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: ImageSliderAdapter

    private val handler = Handler(Looper.getMainLooper())
    private val delay: Long = 3000 // Zeitintervall zwischen den Scrolls (in Millisekunden)

    private var timer: Timer? = null
    private var autoScrollTask: TimerTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aliexpress)

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

        // Glide verwenden, um das GIF zu laden
        val imageView = findViewById<ImageView>(R.id.imageView)
        Glide.with(this)
            .asGif()
            .load(R.drawable.aliexpress_watsup)
            .into(imageView)

        // ViewPager2 und Adapter initialisieren
        viewPager = findViewById(R.id.viewPager)

        // Button für den Link öffnen
        val buttonOpenLink: Button = findViewById(R.id.buttonOpenLink)
        buttonOpenLink.setOnClickListener {
            val link = "https://s.click.aliexpress.com/e/_DmU7I0j" // Ersetze dies durch die gewünschte URL
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            startActivity(intent)
        }

        // Rufe die Methode zum Abrufen von Bildern auf
        fetchImagesFromFirestore()

        // Toolbar und Navigation Setup
        val toolbar: Toolbar = findViewById(R.id.toolbar2)
        setSupportActionBar(toolbar)

        drawerLayout2 = findViewById(R.id.drawer_layout2)
        navView = findViewById(R.id.alixpress_nav_view)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout2, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout2.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)
    }

    private fun fetchImagesFromFirestore() {
        val db = FirebaseFirestore.getInstance()
        val imageCollection = db.collection("images")

        imageCollection.get()
            .addOnSuccessListener { result ->
                val imageUrls = mutableListOf<String>()
                val links = mutableListOf<String>()

                for (document in result) {
                    val imageUrl = document.getString("imageUrl") ?: ""
                    val link = document.getString("link") ?: ""
                    imageUrls.add(imageUrl)
                    links.add(link)
                }

                if (imageUrls.isNotEmpty()) {
                    // Initialisiere den Adapter nur, wenn es Bilder gibt
                    adapter = ImageSliderAdapter(this, imageUrls, links)
                    viewPager.adapter = adapter

                    // Sicherstellen, dass der Timer nicht bereits läuft
                    timer?.cancel()
                    timer = Timer()
                    autoScrollTask = object : TimerTask() {
                        override fun run() {
                            runOnUiThread {
                                val currentItem = viewPager.currentItem
                                val nextItem = if (currentItem + 1 < adapter.itemCount) currentItem + 1 else 0
                                viewPager.setCurrentItem(nextItem, true)
                            }
                        }
                    }
                    timer?.scheduleAtFixedRate(autoScrollTask, delay, delay)
                } else {
                    Log.e("Firestore", "Keine Bilder gefunden")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error getting documents: ", exception)
            }
    }

    override fun onResume() {
        super.onResume()
        // Starten des Auto-Scrolls, wenn die Aktivität sichtbar ist
        timer?.cancel() // Stopp vorheriger Timer
        timer = Timer()
        autoScrollTask = object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    val currentItem = viewPager.currentItem
                    val nextItem = if (currentItem + 1 < adapter.itemCount) currentItem + 1 else 0
                    viewPager.setCurrentItem(nextItem, true)
                }
            }
        }
        timer?.scheduleAtFixedRate(autoScrollTask, delay, delay)
    }

    override fun onPause() {
        super.onPause()
        // Stoppen des Auto-Scrolls, wenn die Aktivität nicht sichtbar ist
        autoScrollTask?.cancel()
        timer?.cancel()
        timer = null
    }

    // Navigation Sitepannel/Menu
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                // Handle home navigation
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_mdlnews -> {
                // Handle Mdl-News gallery navigation
                val intent = Intent(this, MDLNewsActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_aliexpress -> {
                // Start AliexpressActivity
                val intent = Intent(this, AliexpressActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_login -> {
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_logout -> {
                auth.signOut()
                Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
                // Update UI after logout if necessary
                val intent = Intent(this, SplashActivity::class.java)
                startActivity(intent)
            }
        }
        drawerLayout2.closeDrawer(GravityCompat.START)
        return true
    }
}
