package com.studiob4.fox_social_mdl

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class WebViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        val webView: WebView = findViewById(R.id.webView)

        // WebView-Einstellungen optimieren
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true // Aktiviert JavaScript
        webSettings.domStorageEnabled = true // Aktiviert DOM-Speicher (nützlich für komplexe Webseiten)
        webSettings.useWideViewPort = true // Ermöglicht die Unterstützung des Viewports
        webSettings.loadWithOverviewMode = true // Passt die Webseite an den Bildschirm an
        webSettings.builtInZoomControls = true // Aktiviert Zoom-Steuerungen
        webSettings.displayZoomControls = true // Blendet die Standard-Zoom-Steuerungen aus
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT // Verwendet den Cache, wenn verfügbar

        // Verhindert das Öffnen von Links außerhalb der WebView
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                view?.loadUrl(request?.url.toString())
                return true
            }
        }

        // Unterstützt Fortschrittsanzeigen und andere Web-Features
        webView.webChromeClient = WebChromeClient()

        // Lade die übergebene URL
        val url = intent.getStringExtra("url") ?: "https://fox-socialmdl.firebaseapp.com/"
        webView.loadUrl(url)
    }

    override fun onBackPressed() {
        val webView: WebView = findViewById(R.id.webView)
        if (webView.canGoBack()) {
            webView.goBack() // Wenn es einen Rückverlauf gibt, gehe zurück
        } else {
            super.onBackPressed() // Andernfalls verlasse die Aktivität
        }
    }

    override fun onDestroy() {
        // Vermeidet mögliche Speicherlecks
        val webView: WebView = findViewById(R.id.webView)
        webView.destroy()
        super.onDestroy()
    }
}
