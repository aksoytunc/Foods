package com.tuncaksoy.inviobitirmeprojesi.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.tuncaksoy.inviobitirmeprojesi.R
import com.tuncaksoy.inviobitirmeprojesi.databinding.FragmentFavoritesBinding
import com.tuncaksoy.inviobitirmeprojesi.ui.adapter.FavoriteAdapter
import com.tuncaksoy.inviobitirmeprojesi.ui.viewmodel.FavoritesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var viewModel: FavoritesViewModel
    private lateinit var adapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[FavoritesViewModel::class.java]
        viewModel.getAllFavorites()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorites, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = FavoriteAdapter(viewModel)
        binding.adapter = adapter
        observeLiveData()
    }

    fun observeLiveData() {
        viewModel.favoritesList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        viewModel.answer.observe(viewLifecycleOwner) {
            viewModel.getAllFavorites()
        }
    }
}