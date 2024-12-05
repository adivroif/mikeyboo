package com.example.mikeyboo.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mikeyboo.callbacks.CallbackTurn
import com.example.mikeyboo.callbacks.CallbackType
import com.example.mikeyboo.databinding.ForcalendarBinding
import com.example.mikeyboo.databinding.ForhistoryBinding
import com.example.mikeyboo.models.Turn
import com.example.mikeyboo.databinding.HorizontalBinding
import com.example.mikeyboo.databinding.ReviewsListBinding

class TurnsAdapter : RecyclerView.Adapter<TurnsAdapter.PlayerViewHolder>() {
    private var context: Context? = null
    private var listOfTurns: List<Turn>? = null
    var callbackTurn: CallbackTurn? = null



    fun turnsAdapter(context: Context?, turnsList: List<Turn>?) {
        this.context = context
        this.listOfTurns = turnsList
    }

    fun setPlayerCallback(callbackTurn: CallbackTurn?): TurnsAdapter {
        this.callbackTurn = callbackTurn
        return this
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val binding = ForcalendarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlayerViewHolder(binding)
    }

    override fun getItemCount(): Int {
        if (listOfTurns != null)
            return listOfTurns!!.size
        return 0
    }

    @SuppressLint("SetTextI18n", "SuspiciousIndentation")
    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        with(holder) {
            with(listOfTurns!![position]) {
                if(listOfTurns!![position].minutes == 0)
                    binding.time.text = listOfTurns!![position].hours.toString() + ":" + listOfTurns!![position].minutes.toString() + "0"
                else
                    binding.time.text = listOfTurns!![position].hours.toString() + ":" + listOfTurns!![position].minutes.toString()
                binding.date.text = listOfTurns!![position].day.toString() + "/" + listOfTurns!![position].month.toString() + "/" + listOfTurns!![position].year.toString()
                binding.customerName.text = listOfTurns!![position].user?.name
                binding.type.text = listOfTurns!![position].type.split("-")[0].substring(1,listOfTurns!![position].type.split("-")[0].length-1)

            }

        }
    }

    fun getItem(position: Int): Turn {
        return listOfTurns!![position]
    }

    fun updatePlayerList(newReviews: List<Turn>?) {
        this.listOfTurns = newReviews
        notifyDataSetChanged()
    }

    inner class PlayerViewHolder(val binding: ForcalendarBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.playerCARDData.setOnClickListener {
                if (callbackTurn != null)
                    callbackTurn!!.turnClicked(
                        getItem(adapterPosition),
                        adapterPosition
                    )
            }
        }
    }
}