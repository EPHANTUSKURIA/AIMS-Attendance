package com.example.aimsoftattendance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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

        // Set click listeners for the buttons using the corresponding methods
        binding.buttonEditProfile.setOnClickListener {
            onEditProfile()
        }

        binding.buttonCancel.setOnClickListener {
            onCancel()
        }
    }

    // Function to handle the edit profile action
    private fun onEditProfile() {
        // Navigate to the Edit Profile screen
        findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
    }

    // Function to handle the cancel action
    private fun onCancel() {
        // Navigate back or perform another action
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

