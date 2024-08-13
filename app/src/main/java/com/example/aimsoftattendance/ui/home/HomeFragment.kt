package com.example.aimsoftattendance.ui.home

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import android.Manifest.permission.ACCESS_FINE_LOCATION
import androidx.fragment.app.FragmentManager
import com.example.aimsoftattendance.R
import com.example.aimsoftattendance.CustomCircularProgressBar
import com.example.aimsoftattendance.ui.history.AttendanceHistoryFragment
import com.example.aimsoftattendance.ui.profile.ProfileFragment
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var signInTimeTextView: TextView
    private lateinit var signOutTimeTextView: TextView
    private lateinit var totalHoursTextView: TextView
    private lateinit var overtimeTextView: TextView
    private lateinit var seekBar: SeekBar
    private lateinit var circularProgressBar: CustomCircularProgressBar
    private lateinit var profilePic: ShapeableImageView
    private lateinit var attendanceSummaryTextView: TextView

    private var signInTime: Long = 0
    private var signOutTime: Long = 0
    private var isSignedIn = false

    private val handler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        override fun run() {
            if (isSignedIn) {
                updateCircularProgressBar()
            }
            handler.postDelayed(this, 1000) // Update every second
        }
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // Geofence location (latitude, longitude) and radius in meters
    private val geofenceLat = -1.3031722060127642
    private val geofenceLng = 36.8101425299103
    private val geofenceRadius = 50 // 50 meters radius

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize UI elements
        signInTimeTextView = view.findViewById(R.id.signInTimeTextViewLabel)
        signOutTimeTextView = view.findViewById(R.id.signOutTimeTextViewLabel)
        totalHoursTextView = view.findViewById(R.id.totalHoursTextViewLabel)
        overtimeTextView = view.findViewById(R.id.overtimeTextViewLabel)
        seekBar = view.findViewById(R.id.slider)
        circularProgressBar = view.findViewById(R.id.circularProgressBar)
        profilePic = view.findViewById(R.id.profilePic)
        attendanceSummaryTextView = view.findViewById(R.id.attendanceSummaryTitle)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        setupSeekBar()
        setupProfilePicClick()
        setupAttendanceSummaryClick()
        return view
    }

    private fun setupSeekBar() {
        seekBar.max = 1
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                circularProgressBar.showMarker(progress == 1)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                when (seekBar?.progress) {
                    1 -> if (!isSignedIn) checkLocationServices()
                    0 -> if (isSignedIn) signOut()
                }
            }
        })
    }

    private fun setupProfilePicClick() {
        profilePic.setOnClickListener {
            // Navigate to ProfileFragment when profilePic is clicked
            val fragment = ProfileFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setupAttendanceSummaryClick() {
        attendanceSummaryTextView.setOnClickListener {
            // Navigate to AttendanceHistoryFragment when attendanceSummaryTextView is clicked
            val fragment = AttendanceHistoryFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun checkLocationServices() {
        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(requireContext(), "Please turn on your location services", Toast.LENGTH_LONG).show()
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        } else {
            getCurrentLocation()
        }
    }

    private fun getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    checkGeofence(location)
                } else {
                    Toast.makeText(requireContext(), "Unable to get your location. Try again later.", Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Failed to get location: ${exception.message}", Toast.LENGTH_LONG).show()
            }
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            } else {
                Toast.makeText(requireContext(), "Location permission is required to use this feature.", Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private fun checkGeofence(location: Location) {
        val targetLocation = Location("").apply {
            latitude = geofenceLat
            longitude = geofenceLng
        }
        val distance = location.distanceTo(targetLocation)

        if (distance <= geofenceRadius) {
            signIn()
            seekBar.progress = 1
        } else {
            Toast.makeText(requireContext(), "You are not within the allowed area to sign in.", Toast.LENGTH_LONG).show()
            seekBar.progress = 0
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
        val progress = ((elapsedTime % 43200) * 100 / 43200).toFloat() // 12 hours = 43200 seconds

        circularProgressBar.setProgress(progress)
        val progressColor = if (elapsedTime / 3600 >= 9) Color.BLUE else Color.GREEN
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



