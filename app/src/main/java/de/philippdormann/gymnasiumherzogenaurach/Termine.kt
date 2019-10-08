package de.philippdormann.gymnasiumherzogenaurach

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

class Termine : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.termine, container, false)

        val queue = Volley.newRequestQueue(context)
        val stringRequest = StringRequest(Request.Method.GET, "https://gymh.philippdormann.de/termine/api.php",
                Response.Listener { response ->
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
                },
                Response.ErrorListener {}
        )
        queue.add(stringRequest)
        return view
    }
}