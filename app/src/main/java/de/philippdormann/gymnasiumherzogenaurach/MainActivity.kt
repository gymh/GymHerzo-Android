package de.philippdormann.gymnasiumherzogenaurach

import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.webkit.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import tcking.github.com.giraffeplayer2.GiraffePlayer
import tcking.github.com.giraffeplayer2.VideoInfo

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var context: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPref = getSharedPreferences("GYMH", Context.MODE_PRIVATE)
        setTheme(sharedPref.getInt("THEME", R.style.Standard))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        context = this

        val startseite = sharedPref.getInt("STARTSEITE", 0)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        navigationView.menu.getItem(startseite).isChecked = true
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        when (startseite) {
            0 -> fragmentTransaction.replace(R.id.contentFrame, News())
            1 -> fragmentTransaction.replace(R.id.contentFrame, Vertretungsplan())
            2 -> fragmentTransaction.replace(R.id.contentFrame, Notizen())
            3 -> fragmentTransaction.replace(R.id.contentFrame, Todo())
            4 -> fragmentTransaction.replace(R.id.contentFrame, Termine())
            5 -> fragmentTransaction.replace(R.id.contentFrame, Speiseplan())
            else -> fragmentTransaction.replace(R.id.contentFrame, News())
        }
        fragmentTransaction.commit()

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        }
        if (webView!!.canGoBack()) {
            webView!!.goBack()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        var fragmentToPlace: Fragment = News()
        when (id) {
            R.id.nav_news -> fragmentToPlace = News()
            R.id.nav_vertretungsplan -> fragmentToPlace = Vertretungsplan()
            R.id.nav_notizen -> fragmentToPlace = Notizen()
            R.id.nav_todo -> fragmentToPlace = Todo()
            R.id.nav_termine -> fragmentToPlace = Termine()
            R.id.nav_speiseplan -> fragmentToPlace = Speiseplan()
            R.id.nav_stundenplan -> fragmentToPlace = Stundenplan()
            R.id.nav_settings -> fragmentToPlace = Settings()
            R.id.nav_about -> fragmentToPlace = About()
            R.id.nav_gymag-> GiraffePlayer.play(applicationContext, VideoInfo("http://www.quirksmode.org/html5/videos/big_buck_bunny.mp4"))
        }
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.contentFrame, fragmentToPlace)
        fragmentTransaction.commit()
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    companion object {
        var webView: WebView? = null
        fun showInFragmentWebView(webView: WebView, url: String, context: Context?) {
            var url = url
            webView.webViewClient = WebViewClient()
            if (!offline(context!!)) {
                webView.clearCache(true)
            }
            if (offline(context)) {
                webView.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
            }
            webView.webViewClient = WebViewClient()
            webView.settings.javaScriptEnabled = true
            webView.settings.domStorageEnabled = true
            webView.settings.loadsImagesAutomatically = true
            webView.settings.javaScriptCanOpenWindowsAutomatically = true
            webView.settings.allowFileAccess = true
            webView.settings.setAppCacheEnabled(true)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                webView.settings.allowFileAccessFromFileURLs = true
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
            webView.webViewClient = object : WebViewClient() {
                override fun onReceivedError(view: WebView, webResourceRequest: WebResourceRequest, webResourceError: WebResourceError) {
                    webView.loadUrl("file:///android_asset/error.html")
                }
            }
            val progressDialog = ProgressDialog(context)
            progressDialog.setMessage("Wird geladen...")
            progressDialog.show()

            val sharedPref = context.getSharedPreferences("GYMH", Context.MODE_PRIVATE)
            url += if (url.toLowerCase().contains("?")) {
                "&theme="
            } else {
                "?theme="
            }
            url += sharedPref.getString("THEME-NAME", "Standard")
            webView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(webView: WebView, webResourceRequest: WebResourceRequest): Boolean {
                    when {
                        url.toLowerCase().contains(".pdf".toLowerCase()) -> {
                            val builder = CustomTabsIntent.Builder()
                            val customTabsIntent = builder.build()
                            customTabsIntent.launchUrl(webView.context, Uri.parse(url))
                        }
                        url.toLowerCase().contains("play.google".toLowerCase()) -> {
                            val builder = CustomTabsIntent.Builder()
                            val customTabsIntent = builder.build()
                            customTabsIntent.launchUrl(webView.context, Uri.parse(url))
                        }
                        else -> webView.loadUrl(url)
                    }
                    return true
                }

                override fun onPageFinished(view: WebView, url: String) {
                    if (progressDialog.isShowing) {
                        progressDialog.dismiss()
                    }
                }

                override fun onReceivedError(webView: WebView, webResourceRequest: WebResourceRequest, webResourceError: WebResourceError) {}
            }
            Log.d("URL", url)
            webView.loadUrl(url)
        }

        private fun offline(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo == null || !activeNetworkInfo.isConnected
        }
    }
}