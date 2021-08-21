package com.gmail.appverstas.countdownapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gmail.appverstas.countdownapp.data.entities.CountdownItem
import com.gmail.appverstas.countdownapp.databinding.ListRowBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*

/**
 *Veli-Matti Tikkanen, 11.8.2021
 */
class CountdownAdapter: RecyclerView.Adapter<CountdownAdapter.MyViewHolder>() {

    private var itemList = emptyList<CountdownItem>()
    private var onItemClickListener: ((CountdownItem) -> Unit)? = null
    private lateinit var binding: ListRowBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = ListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val dateFormat = SimpleDateFormat("dd.MM.yy, HH:mm", Locale.getDefault())
        val dateString = dateFormat.format(itemList[position].date)
        val dateDifferenceMap = getDifference(dateString)
        binding.itemTitle.text = itemList[position].title
        binding.itemDateTime.text = dateString
        binding.layoutYears.visibility = View.INVISIBLE
        binding.layoutMonths.visibility = View.INVISIBLE

        if(dateDifferenceMap["YEARS"]!! > 0){
            binding.layoutYears.visibility = View.VISIBLE
            binding.layoutMonths.visibility = View.VISIBLE
            binding.tvYears.text = dateDifferenceMap["YEARS"].toString()
            binding.tvMonths.text = dateDifferenceMap["MONTHS"].toString()
        }else if (dateDifferenceMap["MONTHS"]!! > 0) {
            binding.layoutMonths.visibility = View.VISIBLE
            binding.tvMonths.text = dateDifferenceMap["MONTHS"].toString()
        }
        binding.tvDays.text = dateDifferenceMap["DAYS"].toString()

        holder.itemView.setOnClickListener {
            onItemClickListener?.let { click ->
                click(itemList[position])
            }
        }

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun updateList(updatedList: List<CountdownItem>){
        itemList = updatedList
        notifyDataSetChanged()
    }

    fun getCurrentList(): List<CountdownItem>{
        return itemList
    }

    fun setOnItemClickListener(onItemClick: (CountdownItem) -> Unit){
        this.onItemClickListener = onItemClick
    }

    private fun getDifference(date: String): Map<String, Int>{
        val from = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.yy, HH:mm"))
        val today = LocalDate.now()
        val period = Period.between(from, today)
        return mapOf("DAYS" to period.days,"MONTHS" to period.months,"YEARS" to period.years)
    }

    inner class MyViewHolder(binding: ListRowBinding): RecyclerView.ViewHolder(binding.root)

}