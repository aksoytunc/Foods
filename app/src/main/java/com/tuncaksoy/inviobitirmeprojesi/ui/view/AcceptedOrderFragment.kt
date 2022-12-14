package com.tuncaksoy.inviobitirmeprojesi.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.tuncaksoy.inviobitirmeprojesi.R
import com.tuncaksoy.inviobitirmeprojesi.data.model.Order
import com.tuncaksoy.inviobitirmeprojesi.databinding.FragmentAcceptedOrderBinding
import com.tuncaksoy.inviobitirmeprojesi.ui.viewmodel.AcceptedOrderViewModel
import com.tuncaksoy.inviobitirmeprojesi.utils.makeToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AcceptedOrderFragment : Fragment() {
    private lateinit var viewModel: AcceptedOrderViewModel
    private lateinit var binding: FragmentAcceptedOrderBinding
    lateinit var order: Order

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[AcceptedOrderViewModel::class.java]
        arguments?.let {
            order = AcceptedOrderFragmentArgs.fromBundle(it).order
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_accepted_order, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.order = order
        observeLiveData()
    }

    private fun observeLiveData(){
        viewModel.networkConnection.observe(viewLifecycleOwner){
            if (it) (activity as MainActivity).viewModel.getBasket()
            else makeToast(requireContext(), getString(R.string.controlInternet))
        }
    }
}