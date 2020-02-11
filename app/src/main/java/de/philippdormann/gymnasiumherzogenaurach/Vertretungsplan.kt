package de.philippdormann.gymnasiumherzogenaurach

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class Vertretungsplan : Fragment() {
    @SuppressLint("DefaultLocale")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.vertretungsplan, container, false)
        MainActivity.webView = view.findViewById(R.id.webView)
        val sharedPref = activity!!.getSharedPreferences("GYMH", Context.MODE_PRIVATE)
        val filter = sharedPref.getString("FILTER", "")
        val advancedFilter = sharedPref.getString("ADVANCED_FILTER", "")
        val shouldShowLehrerFullname = sharedPref.getBoolean("LEHRER-FULLNAME", false)
        val displayGeneral = sharedPref.getBoolean("VP_GENERAL", true)
        var vpURL = "https://gymh.philippdormann.de/vertretungsplan/?f=$filter"
        if (shouldShowLehrerFullname) {
            vpURL += "&display-lehrer-full"
        }
        if (!displayGeneral) {
            vpURL += "&hideGeneral"
        }
        if (advancedFilter != "") {
            vpURL += "&filter_combo=$advancedFilter"
        }
        try {
            val pInfo = activity!!.packageManager.getPackageInfo(activity!!.packageName, 0)
            val version = pInfo.versionName
            vpURL += if (vpURL.toLowerCase().contains("?")) {
                "&android_app_version="
            } else {
                "?android_app_version="
            }
            vpURL += version
        } catch (e: PackageManager.NameNotFoundException) {
        }

        val montag = view.findViewById<Button>(R.id.montag)
        val dienstag = view.findViewById<Button>(R.id.dienstag)
        val mittwoch = view.findViewById<Button>(R.id.mittwoch)
        val donnerstag = view.findViewById<Button>(R.id.donnerstag)
        val freitag = view.findViewById<Button>(R.id.freitag)
        if (sharedPref.getBoolean("WOCHENANSICHT", false)) {
            montag.visibility = View.GONE
            dienstag.visibility = View.GONE
            mittwoch.visibility = View.GONE
            donnerstag.visibility = View.GONE
            freitag.visibility = View.GONE
            var vpWeekURL = "https://gymh.philippdormann.de/vertretungsplan/week.php?f=$filter"
            if (shouldShowLehrerFullname) {
                vpWeekURL += "&display-lehrer-full"
            }
            MainActivity.showInFragmentWebView(MainActivity.webView, vpWeekURL, activity)
        } else {
            var finalVpURL = vpURL
            val vpReadable = sharedPref.getBoolean("VP_READABLE", false)
            finalVpURL += "&vpr=$vpReadable"
            montag.setOnClickListener { MainActivity.showInFragmentWebView(MainActivity.webView, "$finalVpURL&d=mo", activity) }
            dienstag.setOnClickListener { MainActivity.showInFragmentWebView(MainActivity.webView, "$finalVpURL&d=di", activity) }
            mittwoch.setOnClickListener { MainActivity.showInFragmentWebView(MainActivity.webView, "$finalVpURL&d=mi", activity) }
            donnerstag.setOnClickListener { MainActivity.showInFragmentWebView(MainActivity.webView, "$finalVpURL&d=do", activity) }
            freitag.setOnClickListener { MainActivity.showInFragmentWebView(MainActivity.webView, "$finalVpURL&d=fr", activity) }
            MainActivity.showInFragmentWebView(MainActivity.webView, finalVpURL, activity)
        }
        return view
    }
}