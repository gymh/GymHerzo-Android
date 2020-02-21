package de.philippdormann.gymnasiumherzogenaurach

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import java.net.URLEncoder


class ElternportalSettings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_elternportal_settings)

        //val sharedPref = applicationContext.getSharedPreferences("GYMH", Context.MODE_PRIVATE)

        val button_save_data_activate = findViewById<Button>(R.id.button_save_data_activate)
        button_save_data_activate.setOnClickListener(View.OnClickListener {
            if (isOnline(applicationContext)) {
                val username = URLEncoder.encode(findViewById<EditText>(R.id.editTextTextEmailAddress).text.toString(), "utf-8")
                val password = URLEncoder.encode(findViewById<EditText>(R.id.editTextPassword).text.toString(), "utf-8")
                val url = "https://elternportal-api.now.sh/?username=$username&password=$password&action=check_auth&school=heraugy"
                Log.d("URL", url)
                val myRequest = JsonObjectRequest(Request.Method.GET, url, null,
                        Response.Listener { response ->
                            Log.d("LOG", response.toString())
                        },
                        Response.ErrorListener {
                            Log.d("LOG", "error")
                        }
                )
                myRequest.retryPolicy = DefaultRetryPolicy(
                        15000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
            } else {

            }
        })
    }

    private fun isOnline(context: Context?): Boolean {
        val connectivityManager = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}