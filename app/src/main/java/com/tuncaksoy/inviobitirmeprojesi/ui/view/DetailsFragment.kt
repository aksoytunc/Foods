package com.tuncaksoy.inviobitirmeprojesi.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.tuncaksoy.inviobitirmeprojesi.R
import com.tuncaksoy.inviobitirmeprojesi.data.model.Food
import com.tuncaksoy.inviobitirmeprojesi.databinding.FragmentDetailsBinding
import com.tuncaksoy.inviobitirmeprojesi.listener.DetailsClickListener
import com.tuncaksoy.inviobitirmeprojesi.ui.viewmodel.DetalsViewModel
import com.tuncaksoy.inviobitirmeprojesi.utils.makeAlert
import com.tuncaksoy.inviobitirmeprojesi.utils.makeToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment(), DetailsClickListener {
    private lateinit var binding: FragmentDetailsBinding
    private lateinit var viewModel: DetalsViewModel
    lateinit var product: Food
    lateinit var newProduct: Food
    var foodNumber = 0
    var newFoodNumber = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[DetalsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        arguments?.let {
            product = DetailsFragmentArgs.fromBundle(it).product
            binding.food = product
            viewModel.networkConnection.observe(viewLifecycleOwner) { isConnected ->
                if (isConnected) {
                    viewModel.newProduct(product)
                } else {
                    makeToast(requireContext(), getString(R.string.controlInternet))
                }
            }
        }
        return binding.root
    }

    fun observeLiveData() {
        viewModel.newProduct.observe(viewLifecycleOwner) {
            newProduct = it
            binding.food = it
        }

        viewModel.deleteAnswer.observe(viewLifecycleOwner) {
            viewModel.addBasket(newProduct, newFoodNumber)
        }

        viewModel.addAnswer.observe(viewLifecycleOwner) {
            viewModel.newProduct(newProduct)
        }

        viewModel.favoriteAnswer.observe(viewLifecycleOwner){
            viewModel.newProduct(newProduct)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.listener = this
        observeLiveData()
    }

    override fun addToBasketClick(foodNumber: String?) {
        viewModel.networkConnection.observe(viewLifecycleOwner) { isConnected ->
            if (isConnected) {
                foodNumber?.let {
                    newFoodNumber = it.toInt()
                }
                viewModel.deleteToBasket(newProduct, newFoodNumber)
                (activity as MainActivity).viewModel.getBasket()
                makeAlert(requireContext(),"${newProduct.yemek_adi}","$foodNumber ${getString(R.string.addBasket)}")
            } else {
                makeToast(requireContext(), getString(R.string.controlInternet))
            }
        }
    }

    override fun saveFavoriteClick(food: Food) {
        if (food.yemek_favori == true) viewModel.deleteFavoritesFood(food)
        else viewModel.saveFavoritesFood(food)
    }

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
}