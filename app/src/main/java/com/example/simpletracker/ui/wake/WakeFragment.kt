package com.example.simpletracker.ui.wake

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.simpletracker.databinding.FragmentWakeBinding
import com.example.simpletracker.db.EventDatabase
import com.example.simpletracker.db.SleepEvent
import com.example.simpletracker.db.getDatabaseSingleton
import java.util.Date

class WakeFragment : Fragment() {

    private var _binding: FragmentWakeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val wakeViewModel = ViewModelProvider(this).get(WakeViewModel::class.java)

        _binding = FragmentWakeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val eventDatabase = getDatabaseSingleton(context)
        val mostRecentSleepEventList : List<SleepEvent> = eventDatabase!!.sleepEventDao().getMostRecentSleepEvent()
        wakeViewModel.setSleepStatus(!(mostRecentSleepEventList.isEmpty() || !mostRecentSleepEventList[0].isWakeup))

        val textView: TextView = binding.wakeSleepText
        wakeViewModel.displayText.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val button: Button = binding.wakeSleepButton
        wakeViewModel.buttonText.observe(viewLifecycleOwner) {
            button.text = it
        }
        button.setOnClickListener {
            eventDatabase.sleepEventDao().insert(SleepEvent(Date(), !wakeViewModel.isAwake))
            wakeViewModel.setSleepStatus(!wakeViewModel.isAwake)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}