package com.example.aimsoftattendance

import android.content.Context
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aimsoftattendance.ui.history.Adapter.AttendanceAdapter
import com.example.aimsoftattendance.databinding.FragmentAttendanceHistoryBinding
import java.io.File
import java.io.IOException

class AttendanceHistoryFragment : Fragment() {

    private lateinit var viewModel: AttendanceHistoryViewModel
    private var _binding: FragmentAttendanceHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAttendanceHistoryBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(AttendanceHistoryViewModel::class.java)

        return inflater.inflate(R.layout.fragment_attendance_history, container, false)

        // Initialize WebView for displaying HTML content
        val webView: WebView = binding.pieChartWebView
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()

        // Setup RecyclerView
        setupRecyclerView()

        // Load the HTML file into the WebView
        loadHtmlIntoWebView(webView)

        // Setup Print button
        binding.printButton.setOnClickListener {
            printReportToPDF()
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        // Initialize RecyclerView with data from ViewModel
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val adapter = AttendanceAdapter()
        recyclerView.adapter = adapter

        viewModel.attendanceData.observe(viewLifecycleOwner, Observer { data ->
            adapter.submitList(data)
        })
    }

    private fun loadHtmlIntoWebView(webView: WebView) {
        // Load the HTML file from the assets folder
        webView.loadUrl("file:///android_asset/dashboard.html")
    }

    private fun printReportToPDF() {
        // Create a new PdfDocument
        val pdfDocument = PdfDocument()

        // Setup the page info
        val pageInfo = PdfDocument.PageInfo.Builder(612, 792, 1).create()
        val page = pdfDocument.startPage(pageInfo)

        // Render content from WebView and RecyclerView to the PDF page
        binding.pieChartWebView.draw(page.canvas)
        binding.recyclerView.draw(page.canvas)

        pdfDocument.finishPage(page)

        // Save the document to file
        val filePath = context?.getExternalFilesDir(null)?.absolutePath + "/AttendanceSummary.pdf"
        val file = File(filePath)
        try {
            file.outputStream().use { pdfDocument.writeTo(it) }
            Toast.makeText(context, "PDF saved to $filePath", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(context, "Error saving PDF", Toast.LENGTH_LONG).show()
        } finally {
            pdfDocument.close()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

