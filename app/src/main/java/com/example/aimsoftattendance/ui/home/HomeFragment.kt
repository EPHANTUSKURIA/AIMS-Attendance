package com.example.aimsoftattendance.ui.home

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.aimsoftattendance.R
import com.example.aimsoftattendance.CustomCircularProgressBar // Import your custom progress bar class
import com.google.android.material.imageview.ShapeableImageView
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var signInTimeTextView: TextView
    private lateinit var signOutTimeTextView: TextView
    private lateinit var totalHoursTextView: TextView
    private lateinit var overtimeTextView: TextView
    private lateinit var seekBar: SeekBar
    private lateinit var circularProgressBar: CustomCircularProgressBar // Use your custom type
    private lateinit var profilePic: ShapeableImageView

    private var signInTime: Long = 0
    private var signOutTime: Long = 0
    private var isSignedIn = false

    private val handler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        override fun run() {
            updateCircularProgressBar()
            handler.postDelayed(this, 1000) // Update every second
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize UI elements
        signInTimeTextView = view.findViewById(R.id.signInTimeTextViewLabel)
        signOutTimeTextView = view.findViewById(R.id.signOutTimeTextViewLabel)
        totalHoursTextView = view.findViewById(R.id.jobHoursTextViewLabel)
        overtimeTextView = view.findViewById(R.id.overtimeHoursTextViewLabel)
        seekBar = view.findViewById(R.id.slider)
        circularProgressBar = view.findViewById(R.id.circularProgressBar) // Initialize as CustomCircularProgressBar
        profilePic = view.findViewById(R.id.profilePic)

        setupSeekBar()
        setupProfilePicClick()
        return view
    }

    private fun setupSeekBar() {
        seekBar.max = 1
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (progress == 1 && !isSignedIn) {
                    signIn()
                    seekBar?.progress = 1 // Keep SeekBar at the right position
                    circularProgressBar.showMarker(true)
                } else if (progress == 0 && isSignedIn) {
                    signOut()
                    seekBar?.progress = 0 // Move SeekBar back to the left position
                    circularProgressBar.showMarker(false)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun setupProfilePicClick() {
        profilePic.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }
    }

    private fun signIn() {
        isSignedIn = true
        signInTime = System.currentTimeMillis()
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        signInTimeTextView.text = sdf.format(Date(signInTime))
        startTimer()
    }

    private fun signOut() {
        isSignedIn = false
        signOutTime = System.currentTimeMillis()
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        signOutTimeTextView.text = sdf.format(Date(signOutTime))
        stopTimer()
        calculateWorkingHours()
    }

    private fun startTimer() {
        handler.post(runnable)
    }

    private fun stopTimer() {
        handler.removeCallbacks(runnable)
    }

    private fun updateCircularProgressBar() {
        val currentTime = System.currentTimeMillis()
        val elapsedTime = (currentTime - signInTime) / 1000 // Elapsed time in seconds
        val elapsedHours = elapsedTime / 3600
        val progress = ((elapsedTime % 43200) * 100 / 43200).toFloat() // 12 hours = 43200 seconds

        // Set the progress on the CustomCircularProgressBar
        circularProgressBar.setProgress(progress)

        // Determine which color to use
        val progressColor = if (elapsedHours >= 9) Color.BLUE else Color.GREEN

        // Set the color of the progress
        circularProgressBar.setProgressColor(progressColor)
    }

    private fun calculateWorkingHours() {
        val elapsedTime = (signOutTime - signInTime) / 1000 // Elapsed time in seconds
        val hours = elapsedTime / 3600
        val minutes = (elapsedTime % 3600) / 60
        val seconds = elapsedTime % 60

        totalHoursTextView.text = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds)

        val overtime = if (hours > 9) hours - 9 else 0
        overtimeTextView.text = String.format(Locale.getDefault(), "%d hr", overtime)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(runnable)
    }
}

