package com.tuncaksoy.inviobitirmeprojesi.ui.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.tuncaksoy.inviobitirmeprojesi.R
import com.tuncaksoy.inviobitirmeprojesi.data.repository.FoodRepository
import com.tuncaksoy.inviobitirmeprojesi.databinding.FragmentHomePageBinding
import com.tuncaksoy.inviobitirmeprojesi.listener.HomePageClickListener
import com.tuncaksoy.inviobitirmeprojesi.retrofit.NetworkConnection
import com.tuncaksoy.inviobitirmeprojesi.ui.adapter.AllFoodAdapter
import com.tuncaksoy.inviobitirmeprojesi.ui.viewmodel.HomePageViewModel
import com.tuncaksoy.inviobitirmeprojesi.utils.makeToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomePageFragment : Fragment(),
    HomePageClickListener, SearchView.OnQueryTextListener {
    private lateinit var binding: FragmentHomePageBinding
    private lateinit var viewModel: HomePageViewModel
    private lateinit var adapter: AllFoodAdapter

    override fun onResume() {
        super.onResume()
        binding.progress.visibility = View.VISIBLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomePageViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_page, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.listener = this
        adapter = AllFoodAdapter(viewModel)
        binding.adapter = adapter
        binding.searchView.setOnQueryTextListener(this@HomePageFragment)
        observeLiveData()
    }

    fun observeLiveData() {
        viewModel.networkConnection.observe(viewLifecycleOwner) { isConnected ->
            if (isConnected) {
                viewModel.getAllFood()
                sortlist()
                filterList()
                filter()
                sort()
            } else makeToast(requireContext(), "control Internet")
        }
        viewModel.lastFoodList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.progress.visibility = View.GONE
        }
        viewModel.answer.observe(viewLifecycleOwner) {
            viewModel.filter(
                binding.filterSpinner.selectedItemPosition,
                null,
                binding.sortSpinner.selectedItemPosition
            )
        }
    }

    fun sortlist() {
        val sortList = ArrayList<String>()
        sortList.add("Name A-Z")
        sortList.add("Name Z-A")
        sortList.add("Price 0-∞")
        sortList.add("Price ∞-0")
        val adapter =
            ArrayAdapter(
                this.requireContext(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                sortList
            )
        binding.sortSpinner.adapter = adapter
    }

    fun filterList() {
        val filterList = ArrayList<String>()
        filterList.add("All")
        filterList.add("Food")
        filterList.add("Drink")
        filterList.add("Sweet")
        val adapter = ArrayAdapter(
            this.requireContext(),
            android.R.layout.simple_list_item_1,
            android.R.id.text1,
            filterList
        )
        binding.filterSpinner.adapter = adapter
    }

    fun filter() {
        binding.filterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                viewModel.filter(
                    p2,
                    null,
                    binding.sortSpinner.selectedItemPosition
                )
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    fun sort() {
        binding.sortSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                viewModel.filter(
                    binding.filterSpinner.selectedItemPosition, null, p2
                )
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        p0?.let {
            viewModel.filter(
                binding.filterSpinner.selectedItemPosition,
                p0,
                binding.sortSpinner.selectedItemPosition
            )
        }
        return true
    }
}