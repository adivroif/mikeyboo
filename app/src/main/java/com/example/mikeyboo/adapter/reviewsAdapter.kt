package com.example.mikeyboo.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mikeyboo.callbacks.CallbackTurn
import com.example.mikeyboo.callbacks.CallbackType
import com.example.mikeyboo.databinding.ForhistoryBinding
import com.example.mikeyboo.models.Turn
import com.example.mikeyboo.databinding.HorizontalBinding
import com.example.mikeyboo.databinding.ReviewsListBinding

class reviewsAdapter : RecyclerView.Adapter<reviewsAdapter.PlayerViewHolder>() {
    private var context: Context? = null
    private var reviewsList: List<String>? = null
    var callbackType: CallbackType? = null



    fun reviewAdapter(context: Context?, reviewsList: List<String>?) {
        this.context = context
        this.reviewsList = reviewsList
    }

    fun setPlayerCallback(callbackType: CallbackType?): reviewsAdapter {
        this.callbackType = callbackType
        return this
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val binding = ReviewsListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlayerViewHolder(binding)
    }

    override fun getItemCount(): Int {
        if (reviewsList != null)
            return reviewsList!!.size
        return 0
    }

    @SuppressLint("SetTextI18n", "SuspiciousIndentation")
    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        with(holder) {
            with(reviewsList!![position]) {
                binding.review.text = reviewsList!![position]
            }

        }
    }

    fun getItem(position: Int): String {
        return reviewsList!![position]
    }

    fun updatePlayerList(newReviews: List<String>?) {
        this.reviewsList = newReviews
        notifyDataSetChanged()
    }

    inner class PlayerViewHolder(val binding: ReviewsListBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.playerCARDData.setOnClickListener {
                if (callbackType != null)
                    callbackType!!.typeClicked(
                        getItem(adapterPosition),
                        adapterPosition
                    )
            }
        }
    }
}