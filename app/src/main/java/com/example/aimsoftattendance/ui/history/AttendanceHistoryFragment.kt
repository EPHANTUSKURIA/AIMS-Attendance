package com.example.aimsoftattendance.ui.history

import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.aimsoftattendance.databinding.FragmentAttendanceHistoryBinding
import java.io.File
import java.io.IOException

class AttendanceHistoryFragment : Fragment() {

    private var _binding: FragmentAttendanceHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAttendanceHistoryBinding.inflate(inflater, container, false)

        // Initialize WebView for displaying HTML content
        val webView: WebView = binding.pieChartWebView
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()

        // Load the HTML content into the WebView
        loadHtmlIntoWebView(webView)

        // Setup Print button
        binding.printButton.setOnClickListener {
            printReportToPDF()
        }

        return binding.root
    }

    private fun loadHtmlIntoWebView(webView: WebView) {
        // Generate HTML content with Chart.js
        val htmlContent = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Dashboard</title>
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        background-color: #f5f5f5;
                        margin: 0;
                        padding: 0;
                    }
                    .container {
                        padding: 16px;
                    }
                    .card {
                        background: white;
                        border-radius: 12px;
                        box-shadow: 0 4px 8px rgba(0,0,0,0.2);
                        margin-bottom: 16px;
                        padding: 16px;
                    }
                    .card h2 {
                        margin: 0 0 16px;
                        font-size: 18px;
                        color: #333;
                    }
                    .chart-container {
                        height: 300px;
                        background: #fff;
                        border: 1px solid #ddd;
                        border-radius: 8px;
                    }
                    .footer {
                        margin-top: 32px;
                        text-align: center;
                        color: #666;
                    }
                </style>
               
            </head>
            <body>
            <div class="container">
                <h1>Dashboard</h1>
                <div class="card">
                    <h2>Attendance Statistics</h2>
                    <p>Total Attendance Today: 10</p>
                    <p>Total Hours Today: 8</p>
                    <p>Average Hours Worked: 7</p>
                </div>
                <div class="card chart-container">
                    <canvas id="weeklyAttendanceChart"></canvas>
                </div>
            </div>
            <div class="footer">
                &copy; Aimsoft Limited Attendance
            </div>
            <script>
                const ctx = document.getElementById('weeklyAttendanceChart').getContext('2d');
                const weeklyAttendanceChart = new Chart(ctx, {
                    type: 'bar', // Change to 'line', 'pie', etc. for different chart types
                    data: {
                        labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'], // X-axis labels
                        datasets: [{
                            label: 'Attendance Hours',
                            data: [8, 7, 6, 5, 8, 7, 6], // Example data
                            backgroundColor: 'rgba(75, 192, 192, 0.2)',
                            borderColor: 'rgba(75, 192, 192, 1)',
                            borderWidth: 1
                        }]
                    },
                    options: {
                        scales: {
                            y: {
                                beginAtZero: true
                            }
                        }
                    }
                });
            </script>
            </body>
            </html>
        """.trimIndent()

        webView.loadData(htmlContent, "text/html", "UTF-8")
    }

    private fun printReportToPDF() {
        // Create a new PdfDocument
        val pdfDocument = PdfDocument()

        // Setup the page info
        val pageInfo = PdfDocument.PageInfo.Builder(612, 792, 1).create()
        val page = pdfDocument.startPage(pageInfo)

        // Render content from WebView to the PDF page
        binding.pieChartWebView.draw(page.canvas)

        pdfDocument.finishPage(page)

        // Save the document to file
        val filePath = "${context?.getExternalFilesDir(null)?.absolutePath}/AttendanceSummary.pdf"
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


