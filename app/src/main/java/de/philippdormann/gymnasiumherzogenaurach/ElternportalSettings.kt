package de.philippdormann.gymnasiumherzogenaurach

import android.R
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment


class ElternportalSettings : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_elternportal_settings, container, false)

        /*
        val btnSave = view.findViewById<Button>(R.id.button_save_data_activate)
        btnSave.setOnClickListener {
            val handler = Handler()
            handler.postDelayed({
                applicationContext.startActivity(Intent(this, MainActivity::class.java))
            }, 2000)
        }

        val btnDeactivate = findViewById<Button>(R.id.button_deactivate)
        btnDeactivate.setOnClickListener {
            Toast.makeText(applicationContext, "Integration deaktiviert!", Toast.LENGTH_SHORT).show()
            val handler = Handler()
            handler.postDelayed({
                applicationContext.startActivity(Intent(this, MainActivity::class.java))
            }, 2000)
        }
        */

        val btnCancel = view.findViewById<Button>(R.id.button_cancel)
        btnCancel.setOnClickListener {
            val handler = Handler()
            handler.postDelayed({

            }, 2000)
        }
    }
}