package de.philippdormann.gymnasiumherzogenaurach

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.utsman.recycling.setupAdapter
import kotlinx.android.synthetic.main.template_termine_row.view.*
import org.json.JSONArray

class Termine : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.termine, container, false)

        val queue = Volley.newRequestQueue(context)
        val stringRequest = StringRequest(com.android.volley.Request.Method.GET, "https://gymh.philippdormann.de/termine/api.php",
                com.android.volley.Response.Listener { response ->
                    Log.d("LOGGER", response)

                    val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)

                    val jsonArray = JSONArray(response);
                    for (i in 0..(jsonArray.length() - 1)) {
                        Log.d("LOGGER", jsonArray[i].toString())
                        val jsonSubArray = JSONArray(jsonArray[i].toString());

                        val listData = arrayOf(jsonSubArray[0].toString(), jsonSubArray[1].toString(), jsonSubArray[2].toString())

                        recyclerView.setupAdapter<Array<String>>(R.layout.template_termine_row) { adapter, context, list ->
                            bind { itemView, position, item ->
                                itemView.name_item.text = item!![0]
                                itemView.date_item.text = item!![1]
                                itemView.description_item.text = item!![2]
                                itemView.setOnClickListener {
                                    Toast.makeText(context, "asdf", Toast.LENGTH_LONG).show()
                                }
                            }

                            submitItem(listData)
                        }
                    }
                },
                Response.ErrorListener {}
        )
        queue.add(stringRequest)
        return view
    }
}