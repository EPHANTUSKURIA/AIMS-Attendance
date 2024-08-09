package com.example.aimsoftattendance

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.aimsoftattendance.ui.auth.LoginFragment
import com.example.aimsoftattendance.ui.home.HomeFragment

class LandingActivity : AppCompatActivity(),

    LoginFragment.OnLoginFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        val logoImage: ImageView = findViewById(R.id.logoImage)
        val fadeIn: Animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        logoImage.startAnimation(fadeIn)

        fadeIn.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                if (supportFragmentManager.findFragmentById(R.id.fragment_container) == null) {
                    val loginFragment = LoginFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, loginFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit()
                }
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
    }

    override fun onLoginSuccess() {

        val homeFragment = HomeFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, homeFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }

    override fun onForgotPassword() {
    }
}
