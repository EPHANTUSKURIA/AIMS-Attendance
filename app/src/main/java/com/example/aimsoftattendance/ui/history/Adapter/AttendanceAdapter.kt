// AttendanceAdapter.kt
package com.example.aimsoftattendance.ui.history.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aimsoftattendance.AttendanceRecord
import com.example.aimsoftattendance.databinding.ItemAttendanceRecordBinding

class AttendanceAdapter : RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder>() {

    private var attendanceList: List<AttendanceRecord> = listOf()

    fun submitList(list: List<AttendanceRecord>) {
        attendanceList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        val binding = ItemAttendanceRecordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AttendanceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
        holder.bind(attendanceList[position])
    }

    override fun getItemCount(): Int = attendanceList.size

    class AttendanceViewHolder(private val binding: ItemAttendanceRecordBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(record: AttendanceRecord) {
            binding.record = record
            binding.executePendingBindings()
        }
    }
}
