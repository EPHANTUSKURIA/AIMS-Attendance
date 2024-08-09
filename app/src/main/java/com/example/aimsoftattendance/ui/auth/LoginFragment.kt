package com.example.aimsoftattendance.ui.auth

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.aimsoftattendance.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var listener: OnLoginFragmentInteractionListener

    interface OnLoginFragmentInteractionListener {
        fun onLoginSuccess()
        fun onForgotPassword()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnLoginFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnLoginFragmentInteractionListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.setOnClickListener {
            Log.d("LoginFragment", "Login button clicked")
            val email = binding.emailInput.text.toString()
            val employeeNumber = binding.employeeNumberInput.text.toString()

            if (validateCredentials(email, employeeNumber)) {
                Log.d("LoginFragment", "Credentials valid, notifying listener")
                listener.onLoginSuccess()
            } else {
                Log.d("LoginFragment", "Invalid credentials")
                Toast.makeText(context, "Invalid email or employee number", Toast.LENGTH_SHORT).show()
            }
        }

        binding.forgotpassword.setOnClickListener {
            Log.d("LoginFragment", "Forgot password clicked")
            listener.onForgotPassword()
        }
    }

    private fun validateCredentials(email: String, employeeNumber: String): Boolean {
        // Add more detailed logging here if needed
        Log.d("LoginFragment", "Validating credentials - Email: $email, Employee Number: $employeeNumber")
        return email == "test@example.com" && employeeNumber == "12345"
    }

}
