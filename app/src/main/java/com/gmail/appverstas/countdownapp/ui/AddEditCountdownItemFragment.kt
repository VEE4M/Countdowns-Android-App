package com.gmail.appverstas.countdownapp.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.gmail.appverstas.countdownapp.CountdownViewModel
import com.gmail.appverstas.countdownapp.R
import com.gmail.appverstas.countdownapp.data.entities.CountdownItem
import com.gmail.appverstas.countdownapp.databinding.FragmentAddEditCountdownItemBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AddEditCountdownItemFragment : Fragment(R.layout.fragment_add_edit_countdown_item), DatePicker.OnDateChangedListener, TimePicker.OnTimeChangedListener {


    var currentItem: CountdownItem? = null
    private val viewModel: CountdownViewModel by viewModels()
    private val args: AddEditCountdownItemFragmentArgs by navArgs()
    private val calendar = Calendar.getInstance()
    private lateinit var binding: FragmentAddEditCountdownItemBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_edit_countdown_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddEditCountdownItemBinding.bind(view)

        setHasOptionsMenu(true)
        setCurrentDateTime(calendar)

        if(args.id.isNotEmpty()){
            viewModel.observeCountdownItemByID(args.id).observe(viewLifecycleOwner, androidx.lifecycle.Observer { countdownItem ->
                currentItem = countdownItem
                binding.etItemTitle.setText(countdownItem.title)
                calendar.timeInMillis = countdownItem.date
                setCurrentDateTime(calendar)
            })
        }

        binding.ivCalendar.setOnClickListener {
            setDateFromDatePickerDialog(calendar)
        }

        binding.ivClock.setOnClickListener {
            setTimeFromTimePickerDialog(calendar)
        }

        binding.btnSave.setOnClickListener {
            saveToDatabase()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.add_edit_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_save -> saveToDatabase()
            R.id.menu_delete -> deleteFromDatabase()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDateChanged(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTimeChanged(p0: TimePicker?, p1: Int, p2: Int) {
        setCurrentDateTime(calendar)
    }

    private fun deleteFromDatabase() {
        currentItem?.let { item ->
            viewModel.deleteCountdownItem(item)
            Snackbar.make(requireView(), getString(R.string.snackbar_deleted), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.snackbar_undo)){
                    viewModel.insertCountdownItem(item)
                }
                .show()
        }
        findNavController().navigate(R.id.action_addEditCountdownItemFragment_to_countdownListFragment)
    }

    private fun setDateFromDatePickerDialog(calendar: Calendar) {
        val datePicker = DatePickerDialog(requireContext())
        datePicker.setOnDateSetListener { _, year, month, day ->
            calendar.set(year, month, day)
            setCurrentDateTime(calendar)
        }
        datePicker.show()
    }



   private fun setTimeFromTimePickerDialog(calendar: Calendar){
        TimePickerDialog(requireContext(),
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), hourOfDay, minute)
                setCurrentDateTime(calendar)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }




    private fun setCurrentDateTime(calendar: Calendar){
        val date = "${calendar.get(Calendar.DAY_OF_MONTH)}.${calendar.get(Calendar.MONTH)+1}.${calendar.get(Calendar.YEAR)}"
        val time = "${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}"
        binding.tvCountdownFromDate.text = "Countdown from: $date , $time"
    }

    private fun saveToDatabase(){
        if(validInput()){
            val newItem = CountdownItem(
                currentItem?.id ?: 0,
                binding.etItemTitle.text.toString(),
                calendar.timeInMillis
            )
            viewModel.insertCountdownItem(newItem)
            Snackbar.make(requireView(), getString(R.string.snackbar_saved), Snackbar.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addEditCountdownItemFragment_to_countdownListFragment)
        }else{
            Snackbar.make(requireView(), getString(R.string.title_missing), Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun validInput(): Boolean{
        return binding.etItemTitle.text.isNotEmpty()
    }

}