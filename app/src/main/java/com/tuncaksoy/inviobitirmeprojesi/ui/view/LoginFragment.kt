package com.tuncaksoy.inviobitirmeprojesi.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.tuncaksoy.inviobitirmeprojesi.R
import com.tuncaksoy.inviobitirmeprojesi.databinding.FragmentLoginBinding
import com.tuncaksoy.inviobitirmeprojesi.listener.LoginClickListener
import com.tuncaksoy.inviobitirmeprojesi.ui.viewmodel.LoginViewModel
import com.tuncaksoy.inviobitirmeprojesi.utils.makeToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(), LoginClickListener {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.listener = this
    }

    override fun btnLoginClick(view: View, email: String?, password: String?) {
        email?.let {
            password?.let { password ->
                if (it != "" && password != "") {
                    binding.progressBar.visibility = View.VISIBLE
                    viewModel.firebaseAuth.signInWithEmailAndPassword(it, password).addOnCompleteListener { task ->
                        if (task.isSuccessful) (activity as LogoutActivity).login()
                    }.addOnFailureListener {
                        makeToast(requireContext(), it.message.toString())
                        binding.progressBar.visibility = View.GONE
                    }
                }else makeToast(requireContext(),getString(R.string.blankEmailPass))
            }
        }
    }

    override fun txtRegisterClick(view: View) {
        val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment2()
        Navigation.findNavController(view).navigate(action)
    }
}