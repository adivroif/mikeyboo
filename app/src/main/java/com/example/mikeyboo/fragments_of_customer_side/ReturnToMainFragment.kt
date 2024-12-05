package com.example.mikeyboo.fragments_of_customer_side

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mikeyboo.R
import com.example.mikeyboo.sharedData.SharedViewModel
import com.example.mikeyboo.models.User

class ReturnToMainFragment : Fragment() {

    private val viewModel: SharedViewModel by viewModels()
    var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findNavController().popBackStack()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_return_to_main, container, false)
    }
}