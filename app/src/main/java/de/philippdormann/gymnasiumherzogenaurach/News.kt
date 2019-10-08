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


class News : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.news_activity, container, false)

        //network request
        val queue = Volley.newRequestQueue(context)
        val stringRequest = StringRequest(Request.Method.GET, "https://gymh.philippdormann.de/news/json.php?out",
                Response.Listener { response ->
                    //setup recyclerview
                    val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
                    recyclerView.hasFixedSize()
                    recyclerView.layoutManager = LinearLayoutManager(context)

                    //parse json to ArrayList<NewsItem>
                    val newsItems = ArrayList<NewsItem>()
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)

                        val newsItem = NewsItem()
                        newsItem.title = jsonObject.getString("title")
                        newsItem.contentArticle = jsonObject.getString("contentArticle")
                        newsItem.postDate = jsonObject.getString("postDate")
                        newsItem.imageMain = "https://gymh.philippdormann.de/news/compressed/" + jsonObject.getString("imageMain")
                        newsItems.add(newsItem)
                    }

                    //set data for Adapter
                    val newsItemAdapter = NewsItemAdapter(newsItems)
                    recyclerView.adapter = newsItemAdapter
                },
                Response.ErrorListener {}
        )
        queue.add(stringRequest)
        return view
    }
}