package de.philippdormann.gymnasiumherzogenaurach

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import ru.noties.markwon.Markwon

class About : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.about, container, false)

        val textView = view.findViewById<TextView>(R.id.about_text)
        Markwon.setMarkdown(textView, "Diese App wurde erstellt von Philipp Dormann.\n\n" +
                "Dies ist meine W-Seminar Arbeit:\n\n" +
                "Das Ziel dieser App ist es, möglichst alle Informationen des Gymnasium Herzogenaurach in einer App zu vereinen.\n\n" +
                "Der gesamte [Code dieser App ist öffentlich auf GitHub zugänglich](https://github.com/gymh/GymHerzo-Android).\n\n" +
                "Einbringungen, Feedback sowie selbst erstellte Erweiterungen oder neue Funktionen sind jederzeit willkommen.\n\n" +
                "Solltest du einen Fehler in der App finden, so melde diesen bitte [per E-Mail](mailto:philipp.dormann1@gmail.com) oder am besten [direkt auf Github](https://github.com/gymh/GymHerzo-Android/issues).\n\n" +
                "Um die Weiterentwicklung (sowie auch das Weiterbestehen) dieser App zu ermöglichen, ist in den Einstellungen ein Button zum Spenden zu finden.")

        return view
    }
}