package de.philippdormann.gymnasiumherzogenaurach

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class Vertretungsplan : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.vertretungsplan, container, false)
        Log.d("LOGGER", "loadVP")
        MainActivity.webView = view.findViewById(R.id.webView)
        val sharedPref = activity!!.getSharedPreferences("GYMH", Context.MODE_PRIVATE)
        val filter = sharedPref.getString("FILTER", "")
        val shouldShowLehrerFullname = sharedPref.getBoolean("LEHRER-FULLNAME", false)
        var vpURL = "https://gymh.philippdormann.de/vertretungsplan/?f=$filter"
        if (shouldShowLehrerFullname) {
            vpURL += "&display-lehrer-full"
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
            MainActivity.showInFragmentWebView(MainActivity.webView!!, vpWeekURL, activity)
        } else {
            val finalVpURL = vpURL
            montag.setOnClickListener { MainActivity.showInFragmentWebView(MainActivity.webView!!, "$finalVpURL&d=mo", activity) }
            dienstag.setOnClickListener { MainActivity.showInFragmentWebView(MainActivity.webView!!, "$finalVpURL&d=di", activity) }
            mittwoch.setOnClickListener { MainActivity.showInFragmentWebView(MainActivity.webView!!, "$finalVpURL&d=mi", activity) }
            donnerstag.setOnClickListener { MainActivity.showInFragmentWebView(MainActivity.webView!!, "$finalVpURL&d=do", activity) }
            freitag.setOnClickListener { MainActivity.showInFragmentWebView(MainActivity.webView!!, "$finalVpURL&d=fr", activity) }
            MainActivity.showInFragmentWebView(MainActivity.webView!!, vpURL, activity)
        }
        return view
    }
}