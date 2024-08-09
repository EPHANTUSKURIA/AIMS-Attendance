package com.example.myapp.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.example.aimsoftattendance.R


class DashboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        val webView: WebView = view.findViewById(R.id.webview_dashboard)
        webView.webViewClient = WebViewClient() // Ensures links are opened within the WebView
        webView.settings.javaScriptEnabled = true // Enable JavaScript

        // Load the HTML file from the assets folder
        webView.loadUrl("file:///android_asset/dashboard.html")

        return view
    }
}
