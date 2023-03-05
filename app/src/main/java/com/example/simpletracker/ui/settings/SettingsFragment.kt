package com.example.simpletracker.ui.settings

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.simpletracker.R
import com.example.simpletracker.databinding.FragmentSettingsBinding
import com.example.simpletracker.db.DB_NAME
import com.example.simpletracker.db.getDatabaseSingleton
import java.io.File

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val scalePreference: EditTextPreference? = findPreference("scale_value")
        scalePreference?.setOnBindEditTextListener { editText ->
            editText.inputType = InputType.TYPE_CLASS_NUMBER
        }

        val askPeriodPreference: EditTextPreference? = findPreference("ask_period")
        askPeriodPreference?.setOnBindEditTextListener { editText ->
            editText.inputType = InputType.TYPE_CLASS_NUMBER
        }

        val savePreference: Preference? = findPreference("export")
        savePreference?.setOnPreferenceClickListener {
            getDatabaseSingleton(context)?.close()
            val db : File = context!!.getDatabasePath(DB_NAME)
            val externalTarget = File(context!!.getExternalFilesDir(null), "export.db")
            db.copyTo(externalTarget, overwrite = true)
            true
        }
    }
}