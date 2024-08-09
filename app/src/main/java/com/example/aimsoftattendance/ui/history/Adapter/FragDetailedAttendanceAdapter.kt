package com.example.aimsoftattendance

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aimsoftattendance.databinding.ItemAttendanceRecordBinding

class FragDetailedAttendanceAdapter : RecyclerView.Adapter<FragDetailedAttendanceAdapter.ViewHolder>() {

    private var records: List<AttendanceRecord> = emptyList()

    class ViewHolder(val binding: ItemAttendanceRecordBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(record: AttendanceRecord) {
            binding.record = record
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAttendanceRecordBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val record = records[position]
        holder.bind(record)
    }

    override fun getItemCount(): Int = records.size

    fun submitList(newRecords: List<AttendanceRecord>) {
        records = newRecords
        notifyDataSetChanged()
    }
}
