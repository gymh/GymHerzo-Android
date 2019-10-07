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
            Toast.makeText(context, gymagCode, Toast.LENGTH_LONG).show()

            //TODO: network request for active code + active link response
            //TODO: fullscreen support
            if (gymagCode == "2019#104!8974&91074") {
                val youTubePlayerView = view.findViewById<YouTubePlayerView>(R.id.youtube_player_view)
                getLifecycle().addObserver(youTubePlayerView)
                /*
                val player = ExoPlayerFactory.newSimpleInstance(context)
                val playerView = view.findViewById<PlayerView>(R.id.playerView)
                playerView.setPlayer(player)

                val dataSourceFactory = DefaultDataSourceFactory(context, Util.getUserAgent(context, "yourApplicationName"))
                val videoSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mp4VideoUri)
                player.prepare(videoSource)
                */
            }
        }

        return view
    }
}