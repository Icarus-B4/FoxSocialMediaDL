package com.studiob4.fox_social_mdl

import android.Manifest.permission
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.messaging
import com.studiob4.fox_social_mdl.R.drawable
import com.studiob4.fox_social_mdl.R.id
import com.studiob4.fox_social_mdl.R.layout
import com.studiob4.fox_social_mdl.R.mipmap
import com.studiob4.fox_social_mdl.R.string


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var auth: FirebaseAuth
    private lateinit var navView: NavigationView
    private val CHANNEL_ID = "install_notification_channel"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)

        val imageView = findViewById<ImageView>(id.imageView)

        val toolbar: Toolbar = findViewById(id.toolbar)

        val txtMarquee: TextView? = findViewById(id.marqueeText)

        val isLoggedIn = checkIfUserIsLoggedIn()

        Firebase.messaging.isAutoInitEnabled = true

        // Now we will call setSelected() method
        // and pass boolean value as true
        txtMarquee!!.setSelected(true);

        val webView: WebView = findViewById(id.webmain)

        // WebView-Einstellungen optimieren
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true // Aktiviert JavaScript
        webSettings.domStorageEnabled = true // Aktiviert DOM-Speicher (nützlich für komplexe Webseiten)
        webSettings.useWideViewPort = true // Ermöglicht die Unterstützung des Viewports
        webSettings.loadWithOverviewMode = false // Passt die Webseite an den Bildschirm an
        webSettings.builtInZoomControls = false // Aktiviert Zoom-Steuerungen
        webSettings.displayZoomControls = true // Blendet die Standard-Zoom-Steuerungen aus
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT // Verwendet den Cache, wenn verfügbar


        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                view?.loadUrl(request?.url.toString())
                return true
            }
        }
        // Unterstützt Fortschrittsanzeigen und andere Web-Features
        webView.webChromeClient = WebChromeClient()

        // Lade die übergebene URL
        val url = intent.getStringExtra("url") ?: "https://chatgpt.com"
        webView.loadUrl(url)

        // Benachrichtigungskanal erstellen
        createNotificationChannel()

        // Überprüfen, ob es der erste Start nach der Installation ist
        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val firstRun = sharedPreferences.getBoolean("firstRun", true)

        if (firstRun) {
            sendInstallationNotification()
            // Setze firstRun auf false, damit die Benachrichtigung nur einmal angezeigt wird
            sharedPreferences.edit().putBoolean("firstRun", false).apply()
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener { regTokenTask ->
            if (regTokenTask.isSuccessful) {
                Log.d(TAG, "FCM registration token: ${regTokenTask.result}")
            } else {
                Log.e(TAG, "Unable to retrieve registration token",
                    regTokenTask.exception)
            }
        }
        FirebaseInstallations.getInstance().id.addOnCompleteListener { installationIdTask ->
            if (installationIdTask.isSuccessful) {
                Log.d(TAG, "Firebase Installations ID: ${installationIdTask.result}")
            } else {
                Log.e(TAG, "Unable to retrieve installations ID",
                    installationIdTask.exception)
            }
        }

        // Set the toolbar as the app bar for the activity
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(id.drawer_layout)
        navView = findViewById(id.nav_view)

        navView.setNavigationItemSelectedListener(this)

        if (isLoggedIn) {
            // Falls nicht eingeloggt, weiterleiten
            val intent = Intent(this, SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // Glide verwenden, um das GIF zu laden
        Glide.with(this)
            .asGif()
            .load(drawable.watsup2) // Stelle sicher, dass das GIF im Drawable-Ordner liegt
            .into(imageView)
        drawerLayout = findViewById(id.drawer_layout)

        // Menü öffnen und schliessen, um das GIF zu laden
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, string.navigation_drawer_open, string.navigation_drawer_close
        )
        val navView: NavigationView = findViewById(id.nav_view)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        val tabLayout = findViewById<TabLayout>(id.tabLayout)

        navView.setNavigationItemSelectedListener(this)

        auth = FirebaseAuth.getInstance()

    }

    private fun TextView(): Any {
        TODO("Not yet implemented")
    }

    private fun sendInstallationNotification() {
        val context = this // Falls du in einer Activity bist. Wenn du in einem Fragment bist, nutze requireContext()
        val REQUEST_CODE = 1001
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(mipmap.ic_stat_ic_notification) // Hier musst du dein eigenes Icon hinzufügen
            .setContentTitle(context.getString(string.notification_title))
            .setContentText(context.getString(string.notification_text))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Erlaubnis für Benachrichtigungen fehlt, hier die Erlaubnis anfragen
                ActivityCompat.requestPermissions(
                    this@MainActivity, // Oder nutze requireActivity() in einem Fragment
                    arrayOf(permission.POST_NOTIFICATIONS),
                    REQUEST_CODE
                )
                return
            }
            notify(1001, builder.build())
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
        // Beispiel: Abfragen des Login-Status (dies muss entsprechend deinem Setup angepasst werden)
        val isLoggedIn = checkIfUserIsLoggedIn()

        // Bestimmen der richtigen Activity basierend auf dem Login-Status
        val targetActivity = if (isLoggedIn)MainActivity::class.java else ProfileMainActivity::class.java

        // Intent erstellen und zur entsprechenden Activity navigieren
        val intent = Intent(this, targetActivity)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    // Methode zur Überprüfung des Login-Status
    private fun checkIfUserIsLoggedIn(): Boolean {
        // Beispiel: Überprüfe den Login-Status über SharedPreferences oder eine andere Methode
        val sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }

    private fun createNotificationChannel() {
        // Nur für Android O und höher erforderlich
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(string.notification_channel_name)
            val descriptionText = getString(string.notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Registrierung des Kanals beim System
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendInstallationNotification(context: Context) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(mipmap.ic_stat_ic_notification)
            .setContentTitle(context.getString(string.notification_title))
            .setContentText(context.getString(string.notification_text))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling ActivityCompat#requestPermissions
                return
            }
            notify(1001, builder.build())
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            id.nav_home -> {
                // Handle home navigation
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            id.nav_mdlnews -> {
                // Handle Mdl-News gallery navigation
                val intent = Intent(this, MDLNewsActivity::class.java)
                startActivity(intent)
            }
            id.nav_aliexpress -> {
                // Start AliExproActivity
                val intent = Intent(this, AliexpressActivity::class.java)
                startActivity(intent)
            }
            id.nav_login -> {
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
            }
            id.nav_logout -> {
                auth.signOut()
                Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
                // Update UI after logout if necessary
                val intent = Intent(this, SplashActivity::class.java)
                startActivity(intent)
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


    override fun onDestroy() {
        // Vermeidet mögliche Speicherlecks
        val webView: WebView = findViewById(id.webmain)
        webView.destroy()
        super.onDestroy()
    }

}