package de.philippdormann.gymnasiumherzogenaurach

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment

class Settings : Fragment() {
    @SuppressLint("DefaultLocale")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.settings, container, false)

        val sharedPref = activity!!.getSharedPreferences("GYMH", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val savedFilter = sharedPref.getString("FILTER", "")!!.toUpperCase()

        var isFilterDetailVisible = false
        val buttonSpracheReligionSport = view.findViewById<Button>(R.id.buttonSpracheReligionSport)
        buttonSpracheReligionSport.setOnClickListener {
            val filterContent = view.findViewById<ScrollView>(R.id.filter_content)
            if (!savedFilter.toUpperCase().contains("Q1")) {
                if (isFilterDetailVisible) {
                    filterContent.visibility = View.GONE
                    isFilterDetailVisible = false
                } else {
                    filterContent.visibility = View.VISIBLE
                    isFilterDetailVisible = true
                }
            } else {
                buttonSpracheReligionSport.visibility = View.GONE
                filterContent.visibility = View.GONE
                isFilterDetailVisible = false
            }
        }

        Handler().postDelayed({
            kotlin.run {
                if (isFilterDetailVisible) {
                    val radioGroupSport = view.findViewById<RadioGroup>(R.id.radioGroup_sport)
                    radioGroupSport.setOnCheckedChangeListener { _, _ ->
                        val checked = radioGroupSport.checkedRadioButtonId
                        Log.d("LOGGER", checked.toString())
                    }
                }
            }
        }, 500);

        val button = view.findViewById<Button>(R.id.button_spenden)
        button.setOnClickListener {
            val builder = CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(activity!!, Uri.parse("http://paypal.me/philippdormann"))
        }

        val buttonClearCache = view.findViewById<Button>(R.id.button_clear_cache)
        buttonClearCache.setOnClickListener {
            activity!!.cacheDir.deleteRecursively()
            Toast.makeText(context, "Zwischenspeicher geleert...", Toast.LENGTH_LONG).show()
            restart()
        }

        val btn_config_elternportal = view.findViewById<Button>(R.id.btn_config_elternportal)
        btn_config_elternportal.setOnClickListener {
            //val intent = Intent(getActivity(), ElternportalSettings::class.java)
            //activity!!.startActivity(intent)
            //TODO: bottom sheet (https://github.com/michaelbel/BottomSheet)
            activity!!.startActivity(Intent(activity, ElternportalSettings::class.java))
        }

        val editTextFilter = view.findViewById<EditText>(R.id.editText_filter)
        editTextFilter.setText(savedFilter)
        if (savedFilter != "" && !savedFilter.toUpperCase().contains("Q1")) {
            buttonSpracheReligionSport.visibility = View.VISIBLE
        }
        editTextFilter.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            @SuppressLint("DefaultLocale")
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                editor.putString("FILTER", editTextFilter.text.toString().toUpperCase())
                editor.apply()

                val filterContent = view.findViewById<ScrollView>(R.id.filter_content)
                if (editTextFilter.text.toString().toUpperCase() != "" && !editTextFilter.text.toString().toUpperCase().contains("Q")) {
                    buttonSpracheReligionSport.visibility = View.VISIBLE
                } else {
                    buttonSpracheReligionSport.visibility = View.GONE
                    filterContent.visibility = View.GONE
                    isFilterDetailVisible = false
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })
        val spinner = view.findViewById<Spinner>(R.id.spinner)
        spinner.setSelection(sharedPref.getInt("STARTSEITE", 0))
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                editor.putInt("STARTSEITE", position)
                editor.apply()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        val switchWeek = view.findViewById<Switch>(R.id.switchWeek)
        switchWeek.isChecked = sharedPref.getBoolean("WOCHENANSICHT", false)
        switchWeek.setOnCheckedChangeListener { _, isChecked ->
            editor.putBoolean("WOCHENANSICHT", isChecked)
            editor.apply()
        }

        val switchVPReadable = view.findViewById<Switch>(R.id.switch_vp_readable)
        switchVPReadable.isChecked = sharedPref.getBoolean("VP_READABLE", false)
        switchVPReadable.setOnCheckedChangeListener { _, isChecked ->
            editor.putBoolean("VP_READABLE", isChecked)
            editor.apply()
        }

        val switchVPGeneral = view.findViewById<Switch>(R.id.switch_vp_general)
        switchVPGeneral.isChecked = sharedPref.getBoolean("VP_GENERAL", true)
        switchVPGeneral.setOnCheckedChangeListener { _, isChecked ->
            editor.putBoolean("VP_GENERAL", isChecked)
            editor.apply()
        }

        val switchLehrerDisplayFull = view.findViewById<Switch>(R.id.switchLehrerDisplayFull)
        switchLehrerDisplayFull.isChecked = sharedPref.getBoolean("LEHRER-FULLNAME", false)
        switchLehrerDisplayFull.setOnCheckedChangeListener { _, isChecked ->
            editor.putBoolean("LEHRER-FULLNAME", isChecked)
            editor.apply()
        }

        val themeRed = view.findViewById<Button>(R.id.theme_red)
        themeRed.setOnClickListener { saveTheme(R.style.Red, "Red") }
        val themePink = view.findViewById<Button>(R.id.theme_pink)
        themePink.setOnClickListener { saveTheme(R.style.Pink, "Pink") }
        val themeNight = view.findViewById<Button>(R.id.theme_night)
        themeNight.setOnClickListener { saveTheme(R.style.Night, "Night") }
        val themeBlue = view.findViewById<Button>(R.id.theme_blue)
        themeBlue.setOnClickListener { saveTheme(R.style.Blue, "Blue") }
        val themeGreen = view.findViewById<Button>(R.id.theme_green)
        themeGreen.setOnClickListener { saveTheme(R.style.Green, "Green") }
        val themeBluegrey = view.findViewById<Button>(R.id.theme_bluegrey)
        themeBluegrey.setOnClickListener { saveTheme(R.style.BlueGrey, "BlueGrey") }
        val themeLightgreen = view.findViewById<Button>(R.id.theme_lightgreen)
        themeLightgreen.setOnClickListener { saveTheme(R.style.LightGreen, "LightGreen") }
        val themePurple = view.findViewById<Button>(R.id.theme_purple)
        themePurple.setOnClickListener { saveTheme(R.style.Purple, "Purple") }
        val themeDeeppurple = view.findViewById<Button>(R.id.theme_deeppurple)
        themeDeeppurple.setOnClickListener { saveTheme(R.style.DeepPurple, "DeepPurple") }
        val themeIndigo = view.findViewById<Button>(R.id.theme_indigo)
        themeIndigo.setOnClickListener { saveTheme(R.style.Indigo, "Indigo") }
        val themeCyan = view.findViewById<Button>(R.id.theme_cyan)
        themeCyan.setOnClickListener { saveTheme(R.style.Cyan, "Cyan") }
        val themeStandard = view.findViewById<Button>(R.id.theme_standard)
        themeStandard.setOnClickListener { saveTheme(R.style.Standard, "Standard") }
        val themeRosegold = view.findViewById<Button>(R.id.theme_rosegold)
        themeRosegold.setOnClickListener { saveTheme(R.style.Rosegold, "Rosegold") }
        val themeOrange = view.findViewById<Button>(R.id.theme_orange)
        themeOrange.setOnClickListener { saveTheme(R.style.Orange, "Orange") }
        val themeAmber = view.findViewById<Button>(R.id.theme_amber)
        themeAmber.setOnClickListener { saveTheme(R.style.Amber, "Amber") }
        val themeTeal = view.findViewById<Button>(R.id.theme_teal)
        themeTeal.setOnClickListener { saveTheme(R.style.Teal, "Teal") }
        val themeSand = view.findViewById<Button>(R.id.theme_sand)
        themeSand.setOnClickListener { saveTheme(R.style.Sand, "Sand") }
        val themeRainbow = view.findViewById<Button>(R.id.theme_rainbow)
        themeRainbow.setOnClickListener { saveTheme(R.style.Rainbow, "Rainbow") }

        return view
    }

    private fun saveTheme(theme: Int, themeName: String) {
        val sharedPref = activity!!.getSharedPreferences("GYMH", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putInt("THEME", theme)
        editor.putString("THEME-NAME", themeName)
        editor.apply()
        if (sharedPref.getInt("THEME", 1) == theme) {
            restart()
        }
    }

    private fun restart() {
        activity!!.startActivity(Intent(activity, MainActivity::class.java))
        activity!!.finishAffinity()
    }
}