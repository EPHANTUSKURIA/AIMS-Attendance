package com.example.aimsoftattendance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.aimsoftattendance.databinding.FragmentDetailedAttendanceBinding

class DetailedAttendanceFragment : Fragment() {

    private lateinit var binding: FragmentDetailedAttendanceBinding
    private val viewModel: DetailedAttendanceViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailedAttendanceBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val date = arguments?.getString("date") ?: ""
        viewModel.loadDetailedRecords(date)

        // Setup RecyclerView
        val adapter = FragDetailedAttendanceAdapter() // Ensure this adapter is implemented
        binding.recyclerViewDetailed.adapter = adapter

        viewModel.detailedRecords.observe(viewLifecycleOwner, Observer { data ->
            adapter.submitList(data)
        })

        // Setup WebView for displaying the pie chart
        setupPieChartWebView()

        // Handle print button click
        binding.printButton.setOnClickListener {
            // Implement your print functionality here
            printCharts()
        }

        return binding.root
    }

    private fun setupPieChartWebView() {
        val webView: WebView = binding.pieChartWebView
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()

        // Generate HTML content for the pie chart (update this with actual data as needed)
        val htmlContent = """
            <html>
<head>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
        google.charts.load('current', {'packages':['corechart']});
        google.charts.setOnLoadCallback(drawChart);
        function drawChart() {
            var data = google.visualization.arrayToDataTable([
                ['Client', 'Points Earned'],
                ['0-50', 25],
                ['51-100', 20],
                ['101-150', 15],
                ['151-200', 10],
                ['201+', 5]
            ]);
            var options = {
                title: 'Distribution of Clients by Points Earned',
            };
            var chart = new google.visualization.PieChart(document.getElementById('piechart'));
            chart.draw(data, options);
        }
    </script>
</head>
<body>
    <div id="piechart" style="width: 100%; height: 100%;"></div>
</body>
</html>
        """.trimIndent()

        webView.loadData(htmlContent, "text/html", null)
    }

    private fun printCharts() {
        // Here you will implement printing functionality
        // Example code might involve creating PrintDocumentAdapter for WebView
    }
}
