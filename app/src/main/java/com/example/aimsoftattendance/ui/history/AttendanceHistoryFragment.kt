import android.content.Context
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aimsoftattendance.ui.history.Adapter.AttendanceAdapter
import com.example.aimsoftattendance.AttendanceHistoryViewModel
import com.example.aimsoftattendance.AttendanceRecord
import com.example.aimsoftattendance.databinding.FragmentAttendanceHistoryBinding

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

        val webView: WebView = binding.pieChartWebView
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val adapter = AttendanceAdapter()
        recyclerView.adapter = adapter

        viewModel.attendanceData.observe(viewLifecycleOwner, Observer { data ->
            adapter.submitList(data)
            setupPieChart(webView, data)
        })

        binding.printButton.setOnClickListener {
            printChart(webView)
        }

        return binding.root
    }

    private fun setupPieChart(webView: WebView, data: List<AttendanceRecord>) {
        val totalWorkedHours = data.fold(0f) { acc, record -> acc + record.workedHours }
        val totalMissedHours = data.fold(0f) { acc, record -> acc + record.missedHours }
        val totalOvertimeHours = data.fold(0f) { acc, record -> acc + record.overtimeHours }

        val htmlContent = """
            <html>
<head>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
        google.charts.load('current', {'packages':['corechart']});
        google.charts.setOnLoadCallback(drawChart);
        function drawChart() {
            var data = google.visualization.arrayToDataTable([
                ['Category', 'Hours'],
                ['Worked Hours', $totalWorkedHours],
                ['Missed Hours', $totalMissedHours],
                ['Overtime', $totalOvertimeHours]
            ]);
            var options = {
                title: 'Attendance Summary'
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

    private fun printChart(webView: WebView) {
        val printManager = requireContext().getSystemService(Context.PRINT_SERVICE) as PrintManager
        val printAdapter = webView.createPrintDocumentAdapter("PieChart")
        printManager.print("PieChart", printAdapter, PrintAttributes.Builder().build())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
