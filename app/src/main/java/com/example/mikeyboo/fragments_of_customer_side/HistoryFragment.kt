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
import com.example.mikeyboo.adapter.hAdapter
import com.example.mikeyboo.sharedData.SharedViewModel
import com.example.mikeyboo.models.Turn
import com.example.mikeyboo.models.User
import com.example.mikeyboo.databinding.FragmentHistoryBinding
import com.example.mikeyboo.sharedData.DataManager
import com.google.android.material.textview.MaterialTextView
import org.jetbrains.annotations.Nullable

class HistoryFragment : Fragment() {

    private lateinit var high_score_text: MaterialTextView

    private lateinit var main_LST_scores: RecyclerView

    private var list_of_turns = ArrayList<Turn>()

    private var adapter: hAdapter = hAdapter()

    private val viewModel: SharedViewModel by viewModels()

    private var dataManager = DataManager()

    var user: User? = null

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!! // Use View Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        findViews(_binding!!.root)
        main_LST_scores.adapter = adapter
        main_LST_scores.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list_of_turns = dataManager.getHistoryListOfUsersTurns()

        initViews()
    }

    private fun initViews() {
        if (dataManager.getHistoryListOfUsersTurns().isEmpty()) {
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

            adapter = hAdapter()
            adapter.HistoryAdapter(context,list_of_turns)

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

    fun setPlayerList(playerList: List<Turn>) {
        this.list_of_turns = playerList as ArrayList<Turn>
        if (this.adapter != null) {
            adapter.updatePlayerList(this.list_of_turns)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}