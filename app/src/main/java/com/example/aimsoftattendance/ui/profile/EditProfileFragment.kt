package com.example.aimsoftattendance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.aimsoftattendance.databinding.FragmentEditProfileBinding

class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        binding.handler = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Any additional setup if required
    }

    fun onSaveProfile(view: View) {
        // Handle save action
        findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment)
    }

    fun onCancel(view: View) {
        // Handle cancel action
        findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
