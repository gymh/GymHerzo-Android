package de.philippdormann.gymnasiumherzogenaurach

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment

class Notizen : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.notiz, container, false)

        val sharedPref = activity!!.getSharedPreferences("GYMH", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val editTextNotiz = view.findViewById<EditText>(R.id.editText_notiz)
        editTextNotiz.setText(sharedPref.getString("NOTIZ", ""))
        editTextNotiz.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                editor.putString("NOTIZ", editTextNotiz.text.toString())
                editor.apply()
            }

            override fun afterTextChanged(s: Editable) {

            }
        })
        return view
    }
}