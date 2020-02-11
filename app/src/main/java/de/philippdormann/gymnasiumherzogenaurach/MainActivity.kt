package de.philippdormann.gymnasiumherzogenaurach

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.net.http.SslError
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
import kotlinx.android.synthetic.main.webview.*

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
            R.id.nav_gymag -> fragmentToPlace = Gymag()
            R.id.nav_account -> fragmentToPlace = Account()
            R.id.nav_settings -> fragmentToPlace = Settings()
            R.id.nav_about -> fragmentToPlace = About()
        }
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.contentFrame, fragmentToPlace)
        fragmentTransaction.commit()
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    fun replaceFragment(frag: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.contentFrame, frag)
        fragmentTransaction.commit()
    }

    companion object {
        lateinit var webView: WebView

        @SuppressLint("SetJavaScriptEnabled", "DefaultLocale")
        fun showInFragmentWebView(webView: WebView, url: String, context: Context?) {
            var url = url
            webView.clearCache(true)
            webView.settings.javaScriptEnabled = true
            webView.settings.domStorageEnabled = true
            webView.settings.loadsImagesAutomatically = true
            webView.settings.javaScriptCanOpenWindowsAutomatically = true
            webView.settings.allowFileAccess = true
            webView.settings.databaseEnabled = true
            webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
            webView.settings.allowContentAccess = true
            webView.settings.blockNetworkLoads = false
            webView.settings.setAppCacheEnabled(false)
            webView.settings.allowFileAccessFromFileURLs = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }

            try {
                val pInfo = context!!.packageManager.getPackageInfo(context.packageName, 0)
                val version = pInfo.versionName
                url += if (url.toLowerCase().contains("?")) {
                    "&android_app_version="
                } else {
                    "?android_app_version="
                }
                url += version
            } catch (e: PackageManager.NameNotFoundException) {
            }

            val sharedPref = context!!.getSharedPreferences("GYMH", Context.MODE_PRIVATE)
            url += if (url.toLowerCase().contains("?")) {
                "&theme="
            } else {
                "?theme="
            }
            var theme = sharedPref.getString("THEME-NAME", "Standard")

            /*
            if (theme == "Standard") {
                */
            val nightModeFlags = context.getResources().getConfiguration().uiMode and Configuration.UI_MODE_NIGHT_MASK
            when (nightModeFlags) {
                Configuration.UI_MODE_NIGHT_YES -> theme = "Night"

                Configuration.UI_MODE_NIGHT_NO -> theme = "Standard"

                Configuration.UI_MODE_NIGHT_UNDEFINED -> theme = "Standard"
            }
            /*
            }
            */
            url += theme

            if (!sharedPref.getBoolean("VP_GENERAL", true)) {
                url += "&hideGeneral=true"
            }
            if (sharedPref.getBoolean("VP_READABLE", true)) {
                url += "&formatReadable=true"
            }

            webView.webViewClient = object : WebViewClient() {
                override fun onReceivedError(view: WebView, webResourceRequest: WebResourceRequest, webResourceError: WebResourceError) {
                    webView.loadUrl("file:///android_asset/error.html")
                }

                override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                    handler?.proceed()
                }

                override fun shouldOverrideUrlLoading(webView: WebView, webResourceRequest: WebResourceRequest): Boolean {
                    if (!url.contains("https://gymh.philippdormann.de")) {
                        val builder = CustomTabsIntent.Builder()
                        val customTabsIntent = builder.build()
                        customTabsIntent.launchUrl(webView.context, Uri.parse(url))
                    }
                    return true
                }

                override fun onPageFinished(view: WebView, url: String) {
                    Log.d("GYMH: URL LOADED", url)
                }
            }
            webView.loadUrl(url)
        }
    }
}