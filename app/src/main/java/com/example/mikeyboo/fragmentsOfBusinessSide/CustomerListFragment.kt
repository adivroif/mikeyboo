package com.example.mikeyboo.fragmentsOfBusinessSide

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mikeyboo.R
import com.example.mikeyboo.adapter.CustomersAdapter
import com.example.mikeyboo.databinding.FragmentCustomerListBinding
import com.example.mikeyboo.models.Turn
import com.example.mikeyboo.models.User
import com.example.mikeyboo.sharedData.DataManager
import com.example.mikeyboo.sharedData.SharedViewModel
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import java.lang.ref.WeakReference

class CustomerListFragment : Fragment() {

    private lateinit var main_LST_scores: RecyclerView

    private var list_of_users : ArrayList<User>? = ArrayList<User>()

    private var list_of_turns : ArrayList<Turn>? = ArrayList<Turn>()

    private var list_of_users_to_present : ArrayList<User>? = ArrayList()

    private var adapter: CustomersAdapter = CustomersAdapter()

    private val dataManager = DataManager()

    var user: User? = null
    private var _binding: FragmentCustomerListBinding? = null
    private val binding get() = _binding!! // Use View Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCustomerListBinding.inflate(inflater, container, false)
        findViews(_binding!!.root)
        main_LST_scores.adapter = adapter
        main_LST_scores.layoutManager = LinearLayoutManager(context)
        list_of_turns = dataManager.getListOfTurns()
        getUsers()
        initViews()
        return binding.root
    }

    private fun getUsers() {
        list_of_users = dataManager.getListOfUsers()
        Log.d("Users in DataManager value", list_of_users?.size.toString())
    }

    private fun initViews() {
        Log.d("Users in DataManager after value", list_of_users?.size.toString())

        binding.btnSearch.setOnClickListener {searchAndPresentTurns(binding.userNameSearch.text.toString())}
        adapter = CustomersAdapter()
        adapter.UserAdapter(context, list_of_users, list_of_turns)

        val linearManager = LinearLayoutManager(context)
        linearManager.setOrientation(LinearLayoutManager.VERTICAL)
        main_LST_scores.setLayoutManager(linearManager)
        main_LST_scores.setAdapter(adapter)
    }

    private fun searchAndPresentTurns(name: String) {
        list_of_users_to_present = ArrayList()
        for (user in list_of_users!!) {
            if (user.name == name && user.userName != "michaellevi") {
                presentTurns(user.name)
                list_of_users_to_present!!.add(user)
            }
        }
        if(name == "")
            list_of_users_to_present = list_of_users

        adapter = CustomersAdapter()
        adapter.UserAdapter(context, list_of_users_to_present,list_of_turns)

        val linearManager = LinearLayoutManager(context)
        linearManager.setOrientation(LinearLayoutManager.VERTICAL)
        main_LST_scores.setLayoutManager(linearManager)
        main_LST_scores.setAdapter(adapter)
    }

    private fun presentTurns(name: String) {


    }

    private fun findViews(view: View) {
        main_LST_scores = view.findViewById(R.id.main_LST_scores)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}