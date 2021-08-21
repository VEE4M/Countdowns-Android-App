package com.gmail.appverstas.countdownapp.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gmail.appverstas.countdownapp.CountdownViewModel
import com.gmail.appverstas.countdownapp.R
import com.gmail.appverstas.countdownapp.adapters.CountdownAdapter
import com.gmail.appverstas.countdownapp.databinding.FragmentCountdownListBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_countdown_list.*

@AndroidEntryPoint
class CountdownListFragment : Fragment(R.layout.fragment_countdown_list) {

    private val viewModel: CountdownViewModel by viewModels()
    private lateinit var binding: FragmentCountdownListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_countdown_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCountdownListBinding.bind(view)

        val adapter = CountdownAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.getAllItems().observe(viewLifecycleOwner, Observer { updatedList ->
            adapter.updateList(updatedList)
        })

        adapter.setOnItemClickListener {
            findNavController().navigate(CountdownListFragmentDirections.actionCountdownListFragmentToAddEditCountdownItemFragment(it.id.toString()))
        }

        binding.fabAddCountdownItem.setOnClickListener {
            findNavController().navigate(CountdownListFragmentDirections.actionCountdownListFragmentToAddEditCountdownItemFragment(""))
        }


        val itemTouchHelperCallback = object: ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT)
        {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val currentItem = adapter.getCurrentList()[position]
                viewModel.deleteCountdownItem(currentItem)
                Snackbar.make(requireView(), "Item deleted!", Snackbar.LENGTH_LONG)
                    .setAction("Undo"){
                        viewModel.insertCountdownItem(currentItem)
                    }
                    .show()
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView)
    }

}