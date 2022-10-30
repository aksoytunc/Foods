package com.tuncaksoy.inviobitirmeprojesi.ui.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.tuncaksoy.inviobitirmeprojesi.R
import com.tuncaksoy.inviobitirmeprojesi.databinding.FragmentUserBinding
import com.tuncaksoy.inviobitirmeprojesi.listener.UserClickListener
import com.tuncaksoy.inviobitirmeprojesi.ui.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserFragment : Fragment(), UserClickListener {
    private lateinit var binding: FragmentUserBinding
    private lateinit var viewModel: UserViewModel
    private lateinit var storage: FirebaseStorage
    private lateinit var fireBase: FirebaseFirestore
    var selectedImage: Uri? = null
    var selectedBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        viewModel.getLiveUser()
        viewModel.getModePrefences()
        storage = FirebaseStorage.getInstance()
        fireBase = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.listener = this
        binding.displayData = viewModel.getModePrefences()
        observeLiveData()
        modeListener()
    }

    fun observeLiveData() {
        viewModel.userLive.observe(viewLifecycleOwner) {
            binding.user = it
        }
    }

    fun modeListener() {
        binding.displayMode.setOnCheckedChangeListener { _, b ->
            viewModel.loadModePreferences(binding.languageMode.isChecked, b)
            if (b) AppCompatDelegate.setDefaultNightMode (AppCompatDelegate.MODE_NIGHT_YES)
            else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        binding.languageMode.setOnCheckedChangeListener { _, b ->
            viewModel.loadModePreferences(b, binding.displayMode.isChecked)
        }
    }

    fun saveImageFirebase(image: Uri) {
        val reference = storage.reference
        val imageRef =
            reference.child("profilePhotos")
                .child(viewModel.firebaseAuth.currentUser?.email.toString())

        imageRef.putFile(image).addOnSuccessListener {
            FirebaseStorage.getInstance().reference.child("profilePhotos")
                .child(viewModel.firebaseAuth.currentUser?.email.toString()).downloadUrl.addOnSuccessListener {
                    viewModel.updateImage(it.toString())
                }
        }
    }

    override fun layoutClick() {
        binding.imageLayout.visibility = View.GONE
    }

    override fun loadBalanceClick(balance: String?, loadBalance: String?) {
        balance?.let { nBalance ->
            loadBalance?.let { nLoadBalance ->
                if (nBalance != "" && nLoadBalance != "") {
                    viewModel.updateBalance(nBalance.toInt(), nLoadBalance.toInt())
                }
            }
        }
    }

    override fun logoutClick() {
        (activity as MainActivity).logOut()
    }


    override fun profileImageClick() {
        binding.imageLayout.visibility = View.VISIBLE
    }

    override fun loadProfileImageClick() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                (activity as MainActivity),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1
            )
        } else {
            val storageInten =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(storageInten, 2)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val storageInten =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(storageInten, 2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            selectedImage = data.data
            selectedImage?.let {
                if (Build.VERSION.SDK_INT >= 28) {
                    val source =
                        ImageDecoder.createSource((activity as MainActivity).contentResolver, it)
                    selectedBitmap = ImageDecoder.decodeBitmap(source)
                    binding.imageViewUser.setImageBitmap(selectedBitmap)
                    binding.imageViewLarge.setImageBitmap(selectedBitmap)
                    saveImageFirebase(it)
                } else {
                    selectedBitmap =
                        MediaStore.Images.Media.getBitmap(
                            (activity as MainActivity).contentResolver,
                            it
                        )
                    binding.imageViewUser.setImageBitmap(selectedBitmap)
                    binding.imageViewLarge.setImageBitmap(selectedBitmap)
                    saveImageFirebase(it)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


}