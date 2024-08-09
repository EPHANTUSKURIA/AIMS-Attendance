package com.example.aimsoftattendance

import android.content.Context
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.aimsoftattendance.databinding.FragmentLeaveReportBinding
import com.example.aimsoftattendance.ui.leave.viewmodelleave.LeaveRequestReport
import com.example.aimsoftattendance.ui.leave.viewmodelleave.ReportViewModel

class LeaveReportFragment : Fragment() {

    private lateinit var binding: FragmentLeaveReportBinding
    private val viewModel: ReportViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout and initialize the binding
        binding = FragmentLeaveReportBinding.inflate(inflater, container, false)

        // Load report data
        viewModel.loadReportData()

        // Observe report data
        viewModel.reportData.observe(viewLifecycleOwner, Observer { reportData ->
            val reportHtml = generateReportHtml(reportData)
            binding.webView.settings.javaScriptEnabled = true
            binding.webView.loadDataWithBaseURL(null, reportHtml, "text/html", "UTF-8", null)
        })

        // Set up print button
        binding.printButton.setOnClickListener {
            printWebView(binding.webView)
        }

        return binding.root
    }

    private fun generateReportHtml(reportData: List<LeaveRequestReport>): String {
        // Generate HTML content using reportData
        val tableRows = reportData.joinToString(separator = "") {
            """
            <tr>
                <td>${it.leaveType}</td>
                <td>${it.count}</td>
            </tr>
            """.trimIndent()
        }

        val chartLabels = reportData.joinToString(separator = ",") { "\"${it.leaveType}\"" }
        val chartData = reportData.joinToString(separator = ",") { it.count.toString() }

        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Leave Report</title>
                <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
                <style>
                    body { font-family: Arial, sans-serif; margin: 20px; }
                    table { width: 100%; border-collapse: collapse; margin-bottom: 20px; }
                    th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
                    th { background-color: #f2f2f2; }
                    canvas { max-width: 100%; height: auto; }
                </style>
            </head>
            <body>
                <h1>Leave Report</h1>
                <h2>Leave Requests Summary</h2>
                <table id="leaveTable">
                    <thead>
                        <tr>
                            <th>Leave Type</th>
                            <th>Count</th>
                        </tr>
                    </thead>
                    <tbody>
                        $tableRows
                    </tbody>
                </table>
                <h2>Leave Requests Chart</h2>
                <canvas id="leaveChart"></canvas>
                <script>
                    const chartLabels = [$chartLabels];
                    const chartData = [$chartData];

                    const ctx = document.getElementById('leaveChart').getContext('2d');
                    new Chart(ctx, {
                        type: 'pie',
                        data: {
                            labels: chartLabels,
                            datasets: [{
                                label: 'Leave Requests',
                                data: chartData,
                                backgroundColor: [
                                    'rgba(255, 99, 132, 0.2)',
                                    'rgba(54, 162, 235, 0.2)',
                                    'rgba(255, 206, 86, 0.2)'
                                ],
                                borderColor: [
                                    'rgba(255, 99, 132, 1)',
                                    'rgba(54, 162, 235, 1)',
                                    'rgba(255, 206, 86, 1)'
                                ],
                                borderWidth: 1
                            }]
                        },
                        options: {
                            responsive: true,
                            plugins: {
                                legend: { position: 'top' },
                                tooltip: {
                                    callbacks: {
                                        label: function(context) {
                                            let label = context.label || '';
                                            if (context.parsed) {
                                                label += ': ' + context.parsed + ' requests';
                                            }
                                            return label;
                                        }
                                    }
                                }
                            }
                        }
                    });
                </script>
            </body>
            </html>
        """.trimIndent()
    }

    private fun printWebView(webView: WebView) {
        val printManager = requireContext().getSystemService(Context.PRINT_SERVICE) as PrintManager
        val jobName = "${getString(R.string.app_name)} Document"
        val printAdapter = webView.createPrintDocumentAdapter(jobName)
        val printJob = printManager.print(jobName, printAdapter, PrintAttributes.Builder().build())

        if (printJob.isCompleted) {
            // Notify user that the print job is complete
        } else if (printJob.isFailed) {
            // Notify user that the print job failed
        }
    }
}

