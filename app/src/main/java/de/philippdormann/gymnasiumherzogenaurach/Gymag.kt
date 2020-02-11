package de.philippdormann.gymnasiumherzogenaurach

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


class Gymag : Fragment() {
    @SuppressLint("DefaultLocale")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.gymag, container, false)

        val button = view.findViewById<Button>(R.id.button_gymag_code)
        button.setOnClickListener {
            val editText = view.findViewById<EditText>(R.id.gymag_code)
            val gymagCode = editText.text.toString()


            //TODO: network request for active code + active link response
            //TODO: fullscreen support
            if (gymagCode == "2019#104!8974&91074") {
                Toast.makeText(context, "success! u did it", Toast.LENGTH_LONG).show()
            }
        }

        return view
    }
}