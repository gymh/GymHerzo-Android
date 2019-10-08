package de.philippdormann.gymnasiumherzogenaurach

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import androidx.recyclerview.widget.DividerItemDecoration
import android.net.ConnectivityManager
import android.widget.Toast


class Termine : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.termine, container, false)

        val sharedPref = context!!.getSharedPreferences("GYMH", Context.MODE_PRIVATE)
        if (isOnline(context)) {
            val queue = Volley.newRequestQueue(context)
            val stringRequest = StringRequest(Request.Method.GET, "https://gymh.philippdormann.de/termine/api.php",
                    Response.Listener { response ->
                        //save response for offline mode
                        val editor = sharedPref.edit()
                        editor.putString("GYMH_OFFLINE_TERMINE", response)
                        editor.apply()

                        displayData(response, view)
                    },
                    Response.ErrorListener {}
            )
            queue.add(stringRequest)
        } else {
            val offlineData = sharedPref.getString("GYMH_OFFLINE_TERMINE", "")
            if (offlineData === "") {
                Toast.makeText(context, "keine Daten verfügbar", Toast.LENGTH_LONG).show()
            } else {
                //load from offline data
                displayData(offlineData, view)
            }
        }
        return view
    }

    private fun displayData(response: String?, view: View) {
        //setup recyclerview
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.hasFixedSize()
        recyclerView.layoutManager = LinearLayoutManager(context)

        //parse json to ArrayList<TerminItem>
        val termineItems = ArrayList<TerminItem>()
        val jsonArray = JSONArray(response)
        for (i in 0 until jsonArray.length()) {
            val terminData = JSONArray(jsonArray[i].toString())

            val termineItem = TerminItem()
            termineItem.title = terminData[0].toString()
            termineItem.date = terminData[1].toString()
            termineItem.description = terminData[2].toString()
            termineItems.add(termineItem)
        }

        //set data for Adapter
        val terminItemAdapter = TerminItemAdapter(termineItems)
        recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, LinearLayoutManager(context).orientation))
        recyclerView.adapter = terminItemAdapter
    }

    fun isOnline(context: Context?): Boolean {
        val connectivityManager = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}