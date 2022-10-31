package com.tuncaksoy.inviobitirmeprojesi.ui.view

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.tuncaksoy.inviobitirmeprojesi.R
import com.tuncaksoy.inviobitirmeprojesi.data.model.Food
import com.tuncaksoy.inviobitirmeprojesi.databinding.FragmentDetailsBinding
import com.tuncaksoy.inviobitirmeprojesi.listener.DetalsClickListener
import com.tuncaksoy.inviobitirmeprojesi.ui.viewmodel.DetalsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment(), DetalsClickListener {
    private lateinit var binding: FragmentDetailsBinding
    private lateinit var viewModel: DetalsViewModel
    lateinit var product: Food
    lateinit var newProduct: Food
    var foodNumber = 0
    var newFoodNumber = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[DetalsViewModel::class.java]
        arguments?.let {
            product = DetailsFragmentArgs.fromBundle(it).product
            viewModel.newProduct(product)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.listener = this
        observeLiveData()
    }

    override fun addToBasketClick(foodNumber: String?) {
        foodNumber?.let {
            newFoodNumber = it.toInt()
        }
        viewModel.deleteToBasket(newProduct, newFoodNumber)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun plusClick() {
        foodNumber = binding.foodNumberText.text.toString().toInt()
        foodNumber++
        binding.foodNumberText.text = foodNumber.toString()
    }

    override fun sourClick() {
        foodNumber = binding.foodNumberText.text.toString().toInt()
        if (foodNumber > 1) {
            foodNumber--
            binding.foodNumberText.text = foodNumber.toString()
        }
    }

    fun observeLiveData() {
        viewModel.newProduct.observe(viewLifecycleOwner) {
            newProduct = it
            newProduct.yemek_favori = product.yemek_favori
            binding.food = newProduct
        }

        viewModel.deleteAnswer.observe(viewLifecycleOwner) {
            viewModel.addBasket(newProduct, newFoodNumber)
        }

        viewModel.addAnswer.observe(viewLifecycleOwner) {
            viewModel.newProduct(newProduct)
        }

        viewModel.newProduct.observe(viewLifecycleOwner) {
            newProduct = it
            binding.food = it
        }
    }
}