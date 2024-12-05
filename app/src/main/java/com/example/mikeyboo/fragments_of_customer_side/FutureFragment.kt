package com.example.mikeyboo.fragments_of_customer_side

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mikeyboo.R
import com.example.mikeyboo.adapter.fAdapter
import com.example.mikeyboo.databinding.FragmentFutureBinding
import com.example.mikeyboo.sharedData.SharedViewModel
import com.example.mikeyboo.models.Turn
import com.example.mikeyboo.models.User
import com.example.mikeyboo.sharedData.DataManager
import com.google.android.material.textview.MaterialTextView
import org.jetbrains.annotations.Nullable

class FutureFragment : Fragment() {

    private lateinit var high_score_text: MaterialTextView

    private lateinit var main_LST_scores: RecyclerView

    private var list_of_turns = ArrayList<Turn>()

    private var allTurns = ArrayList<Turn>()

    private var adapter: fAdapter = fAdapter()

    private val dataManager = DataManager()

    var user: User? = null

    private var _binding: FragmentFutureBinding? = null
    private val binding get() = _binding!! // Use View Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFutureBinding.inflate(inflater, container, false)
        findViews(_binding!!.root)
        main_LST_scores.adapter = adapter
        main_LST_scores.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        allTurns = dataManager.getListOfTurns()
        list_of_turns = dataManager.getFutureListOfTurns()
        list_of_turns.sortWith(compareBy<Turn> { it.year }.thenBy { it.month }.thenBy { it.day }.thenBy { it.hours }.thenBy { it.minutes })

        initViews()
    }

    private fun initViews() {
        if (dataManager.getFutureListOfUsersTurns(list_of_turns).isEmpty()) {
            if (high_score_text != null)
                high_score_text.visibility = View.VISIBLE
            if (main_LST_scores != null)
                main_LST_scores.visibility = View.GONE
        } else {
            if (high_score_text != null) {
                high_score_text.setVisibility(View.GONE)
            }
            if (main_LST_scores != null) {
                main_LST_scores.setVisibility(View.VISIBLE);
            }

            adapter = fAdapter()
            adapter.FutureAdapter(context, list_of_turns,allTurns,dataManager.getFutureListOfUsersTurns(list_of_turns))

            var linearManager = LinearLayoutManager(this.getContext())
            linearManager.setOrientation(LinearLayoutManager.VERTICAL)
            main_LST_scores.setLayoutManager(linearManager)
            main_LST_scores.setAdapter(adapter)
        }
    }

    private fun findViews(view: View) {
        high_score_text = view.findViewById(R.id.high_score_text)
        main_LST_scores = view.findViewById(R.id.main_LST_scores)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}