package com.tuncaksoy.inviobitirmeprojesi.ui.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.tuncaksoy.inviobitirmeprojesi.R
import com.tuncaksoy.inviobitirmeprojesi.data.model.Food
import com.tuncaksoy.inviobitirmeprojesi.databinding.FragmentShoppingBinding
import com.tuncaksoy.inviobitirmeprojesi.listener.ShoppingClickListener
import com.tuncaksoy.inviobitirmeprojesi.ui.adapter.BasketAdapter
import com.tuncaksoy.inviobitirmeprojesi.ui.viewmodel.ShoppingViewModel
import com.tuncaksoy.inviobitirmeprojesi.utils.makeToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShoppingFragment : Fragment(), ShoppingClickListener {
    private lateinit var binding: FragmentShoppingBinding
    private lateinit var viewModel: ShoppingViewModel
    private lateinit var adapter: BasketAdapter
    private lateinit var bottomSheetFragment: BasketCheckBottomSheetFragment
    var basketList = listOf<Food>()
    var total = 0

    override fun onResume() {
        super.onResume()
        binding.progress.visibility = View.VISIBLE
        observeLiveData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ShoppingViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shopping, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = BasketAdapter(parentFragmentManager, viewModel)
        binding.listener = this
        binding.adapter = adapter
        observeLiveData()
    }

    fun observeLiveData() {
        viewModel.networkConnection.observe(viewLifecycleOwner) { isConnected ->
            Log.e("conntected", isConnected.toString())
            if (isConnected) {
                viewModel.getBasket()
            } else makeToast(requireContext(), "control Internet")
        }
        viewModel.basketFoodList.observe(viewLifecycleOwner) {
            basketList = it
            adapter.submitList(it)
            total = 0
            for (food in it) {
                food.yemek_fiyat?.let { price ->
                    food.yemek_siparis_adet?.let { number ->
                        total += price * number.toInt()
                    }
                }
            }
            binding.total = total
            binding.allfoodNumber = it.size
            binding.progress.visibility = View.GONE
        }
        viewModel.answer.observe(viewLifecycleOwner) {
            viewModel.getBasket()
        }
    }

    override fun basketConfirmClick(total: Int, number: Int) {
        bottomSheetFragment =
            BasketCheckBottomSheetFragment(binding.textView10, total, number, viewModel, basketList)
        bottomSheetFragment.show(parentFragmentManager, "check")
    }
}