package com.example.aimsoftattendance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.aimsoftattendance.ui.auth.LoginFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Check if savedInstanceState is null to avoid overlapping fragments
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LoginFragment())
                .commit()
        }
    }
}
