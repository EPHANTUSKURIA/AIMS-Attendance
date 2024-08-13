package com.example.aimsoftattendance.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.aimsoftattendance.R
import com.example.aimsoftattendance.databinding.ProfileFragmentBinding

class ProfileFragment : Fragment() {

    private var _binding: ProfileFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ProfileFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Bind the fragment instance to the layout
        binding.handler = this

        // Set click listeners for the buttons using the corresponding methods
        binding.buttonEditProfile.setOnClickListener {
            onEditProfile()
        }

        binding.buttonCancel.setOnClickListener {
            onCancel()
        }
    }

    // Function to handle the edit profile action
    fun onEditProfile() {
        // Navigate to the Edit Profile screen using FragmentManager
        val fragment = EditProfileFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    // Function to handle the cancel action
    fun onCancel() {
        // Navigate back or perform another action
        parentFragmentManager.popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


