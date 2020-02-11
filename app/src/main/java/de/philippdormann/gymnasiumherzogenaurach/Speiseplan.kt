package de.philippdormann.gymnasiumherzogenaurach

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class Speiseplan : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.webview, container, false)
        MainActivity.webView = view.findViewById(R.id.webView)
        MainActivity.showInFragmentWebView(MainActivity.webView, "https://gymh.philippdormann.de/mensaplan", activity)
        return view
    }
}