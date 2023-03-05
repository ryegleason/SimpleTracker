package com.example.simpletracker.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.example.simpletracker.databinding.FragmentSurveyBinding
import com.example.simpletracker.db.SurveyEvent
import com.example.simpletracker.db.getDatabaseSingleton
import java.util.*
import kotlin.collections.ArrayList

class SurveyFragment : Fragment() {

    private var _binding: FragmentSurveyBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val surveyViewModel = ViewModelProvider(this).get(SurveyViewModel::class.java)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity!!)

        _binding = FragmentSurveyBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val eventDatabase = getDatabaseSingleton(context)

        val submitButton: Button = binding.submitButton

        val seekBar: SeekBar = binding.seekBar
        seekBar.max = sharedPreferences.getString("scale_value", null)?.toIntOrNull() ?: 6
        seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                submitButton.isEnabled = true
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                // nothing
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                // nothing
            }

        })

        val maxText = binding.maxText
        maxText.text = seekBar.max.toString()


        val tagLayout: LinearLayout = binding.tagLayout
        val tags : List<String> = sharedPreferences.getString("tags", null)?.split(",") ?: kotlin.collections.ArrayList()
        for (tag in tags) {
            val checkBox = CheckBox(activity)
            checkBox.text = tag.trim()
            checkBox.setOnClickListener {
                submitButton.isEnabled = true
            }

            tagLayout.addView(checkBox)
        }
        tagLayout.invalidate()


        submitButton.setOnClickListener {
            val activeTags : MutableList<String> = kotlin.collections.ArrayList()
            for (tagCheckbox in tagLayout.children) {
                if (tagCheckbox is CheckBox && tagCheckbox.isChecked) {
                    activeTags.add(tagCheckbox.text.toString())
                }
            }
            eventDatabase!!.surveyEventDao().insert(SurveyEvent(Date(), seekBar.progress, activeTags))
            submitButton.isEnabled = false
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}