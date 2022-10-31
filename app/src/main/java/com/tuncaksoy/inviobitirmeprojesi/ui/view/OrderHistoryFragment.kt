package com.tuncaksoy.inviobitirmeprojesi.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.tuncaksoy.inviobitirmeprojesi.R
import com.tuncaksoy.inviobitirmeprojesi.databinding.FragmentOrderHistoryBinding
import com.tuncaksoy.inviobitirmeprojesi.ui.adapter.OrderAdapter
import com.tuncaksoy.inviobitirmeprojesi.ui.viewmodel.OrderHistoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderHistoryFragment : Fragment() {
    private lateinit var binding: FragmentOrderHistoryBinding
    private lateinit var viewModel: OrderHistoryViewModel
    private lateinit var adapter: OrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[OrderHistoryViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_order_history, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = OrderAdapter(viewModel)
        binding.adapter = adapter
        viewModel.getOrder()
        observeLiveData()
    }

    fun observeLiveData() {
        viewModel.orderList.observe(viewLifecycleOwner) {
            adapter.submitList(it.reversed())
        }
    }
}