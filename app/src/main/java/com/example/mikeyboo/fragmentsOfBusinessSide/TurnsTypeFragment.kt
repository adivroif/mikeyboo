package com.example.mikeyboo.fragmentsOfBusinessSide

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mikeyboo.R
import com.example.mikeyboo.adapter.TurnsTypeAdapter
import com.example.mikeyboo.databinding.FragmentTurnsTypeBinding
import com.example.mikeyboo.firebaseConnections.firebaseConnections
import com.example.mikeyboo.models.Turn
import com.example.mikeyboo.models.Type
import com.example.mikeyboo.models.User
import com.example.mikeyboo.sharedData.DataManager
import com.example.mikeyboo.sharedData.SharedViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import org.jetbrains.annotations.Nullable

class TurnsTypeFragment : Fragment() {

    private lateinit var btn_add : MaterialButton

    private lateinit var userInputType : TextInputEditText

    private lateinit var high_score_text: MaterialTextView

    private lateinit var main_LST_scores: RecyclerView

    private var list_of_types : ArrayList<Type>? = ArrayList()

    private var firebaseConnections = firebaseConnections()

    private var typesRef = firebaseConnections.initDBConnection("type")

    private var adapter: TurnsTypeAdapter = TurnsTypeAdapter()

    private val dataManager = DataManager()

    private val viewModel: SharedViewModel by viewModels()

    var user: User? = null
    private var _binding: FragmentTurnsTypeBinding? = null
    private val binding get() = _binding!! // Use View Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTurnsTypeBinding.inflate(inflater, container, false)
        findViews(_binding!!.root)
        main_LST_scores.adapter = adapter
        main_LST_scores.layoutManager = LinearLayoutManager(context)
        list_of_types = dataManager.getListOfTypes()
        return binding.root
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.btnAdd.setOnClickListener{addType(Type(binding.userInputType.text.toString(),binding.userInputPrice.text.toString()))}
        if (list_of_types?.isEmpty() == true) {
            if (high_score_text != null)
                high_score_text.visibility = View.VISIBLE
            if (main_LST_scores != null)
                main_LST_scores.visibility = View.GONE
        } else {
            if (high_score_text != null) {
                high_score_text.setVisibility(View.GONE);
            }
            if (main_LST_scores != null) {
                main_LST_scores.setVisibility(View.VISIBLE);
            }

            adapter = TurnsTypeAdapter()
            adapter.TypeAdapter(context, list_of_types)

            var linearManager = LinearLayoutManager(this.getContext())
            linearManager.setOrientation(LinearLayoutManager.VERTICAL)
            main_LST_scores.setLayoutManager(linearManager)
            main_LST_scores.setAdapter(adapter)
        }
    }

    private fun addType(input: Type) {
        list_of_types?.add(input)
        list_of_types?.let { viewModel.setListOfType(it) }
        typesRef.setValue(list_of_types)
        binding.userInputType.setText("")
        binding.userInputPrice.setText("")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("הוספת שירות!")
        builder.setMessage("הוספת השירות בוצע בהצלחה")
        builder.setPositiveButton("אישור") { dialog, which ->
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun findViews(view: View) {
        high_score_text = view.findViewById(R.id.high_score_text)
        main_LST_scores = view.findViewById(R.id.main_LST_scores)
        userInputType = view.findViewById(R.id.userInputType)
        btn_add = view.findViewById(R.id.btn_add)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}